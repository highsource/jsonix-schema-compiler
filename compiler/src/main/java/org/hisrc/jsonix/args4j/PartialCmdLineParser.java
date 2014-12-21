package org.hisrc.jsonix.args4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.NamedOptionDef;
import org.kohsuke.args4j.ParserProperties;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;

public class PartialCmdLineParser extends CmdLineParser {

	private OptionHandler<?> currentOptionHandler;

	public PartialCmdLineParser(Object bean) {
		super(bean);
	}

	public PartialCmdLineParser(Object bean, ParserProperties parserProperties) {
		super(bean, parserProperties);
	}

	public int parseArgument(final String[] args, final int position)
			throws CmdLineException {
		Validate.noNullElements(args);
		currentOptionHandler = null;

		CmdLineImpl cmdLine = new CmdLineImpl(args, position);

		Set<OptionHandler<?>> present = new HashSet<OptionHandler<?>>();
		int argIndex = position;
		int consumed = 0;

		while (cmdLine.hasMore()) {
			String arg = cmdLine.getCurrentToken();
			if (isOption(arg)) {
				// '=' is for historical compatibility fallback
				boolean isKeyValuePair = arg.contains(getProperties()
						.getOptionValueDelimiter()) || arg.indexOf('=') != -1;

				// parse this as an option.
				currentOptionHandler = isKeyValuePair ? findOptionHandler(arg)
						: findOptionByName(arg);

				if (currentOptionHandler == null) {
					return consumed;
				}

				// known option; skip its name
				if (isKeyValuePair) {
					cmdLine.splitToken();
				} else {
					cmdLine.proceed(1);
					consumed++;
				}
			} else {
				if (argIndex >= getArguments().size()) {
					return consumed;
				}

				// known argument
				currentOptionHandler = getArguments().get(argIndex);
				if (currentOptionHandler == null) // this is a programmer error.
													// arg index should be
													// continuous
					throw new IllegalStateException("@Argument with index="
							+ argIndex + " is undefined");

				if (!currentOptionHandler.option.isMultiValued())
					argIndex++;
			}
			int diff = currentOptionHandler.parseArguments(cmdLine);
			cmdLine.proceed(diff);
			consumed += diff;
			present.add(currentOptionHandler);
		}

		// check whether a help option is set
		boolean helpSet = false;
		for (OptionHandler<?> handler : getOptions()) {
			if (handler.option.help() && present.contains(handler)) {
				helpSet = true;
			}
		}

		if (!helpSet) {
			checkRequiredOptionsAndArguments(present);
		}

		return consumed;
	}

	private OptionHandler<?> findOptionHandler(String name) {
		// Look for key/value pair first.
		int pos = name.indexOf(getProperties().getOptionValueDelimiter());
		if (pos < 0) {
			pos = name.indexOf('='); // historical compatibility fallback
		}
		if (pos > 0) {
			name = name.substring(0, pos);
		}
		return findOptionByName(name);
	}

	private OptionHandler<?> findOptionByName(String name) {
		for (OptionHandler<?> h : getOptions()) {
			NamedOptionDef option = (NamedOptionDef) h.option;
			if (name.equals(option.name())) {
				return h;
			}
			for (String alias : option.aliases()) {
				if (name.equals(alias)) {
					return h;
				}
			}
		}
		return null;
	}

	private void checkRequiredOptionsAndArguments(Set<OptionHandler<?>> present)
			throws CmdLineException {
		// make sure that all mandatory options are present
		for (OptionHandler<?> handler : getOptions()) {
			if (handler.option.required() && !present.contains(handler)) {
				throw new CmdLineException(this,
						Messages.REQUIRED_OPTION_MISSING,
						handler.option.toString());
			}
		}

		// make sure that all mandatory arguments are present
		for (OptionHandler<?> handler : getOptions()) {
			if (handler.option.required() && !present.contains(handler)) {
				throw new CmdLineException(this,
						Messages.REQUIRED_ARGUMENT_MISSING,
						handler.option.toString());
			}
		}

		// make sure that all requires arguments are present
		for (OptionHandler<?> handler : present) {
			if (handler.option instanceof NamedOptionDef
					&& !isHandlerHasHisOptions((NamedOptionDef) handler.option,
							present)) {
				throw new CmdLineException(this,
						Messages.REQUIRES_OPTION_MISSING,
						handler.option.toString(),
						Arrays.toString(((NamedOptionDef) handler.option)
								.depends()));
			}
		}

		// make sure that all forbids arguments are not present
		for (OptionHandler<?> handler : present) {
			if (handler.option instanceof NamedOptionDef
					&& !isHandlerAllowOtherOptions(
							(NamedOptionDef) handler.option, present)) {
				throw new CmdLineException(this,
						Messages.FORBIDDEN_OPTION_PRESENT,
						handler.option.toString(),
						Arrays.toString(((NamedOptionDef) handler.option)
								.forbids()));
			}
		}
	}

	private boolean isHandlerHasHisOptions(NamedOptionDef option,
			Set<OptionHandler<?>> present) {
		for (String depend : option.depends()) {
			if (!present.contains(findOptionHandler(depend)))
				return false;
		}
		return true;
	}

	private boolean isHandlerAllowOtherOptions(NamedOptionDef option,
			Set<OptionHandler<?>> present) {
		for (String forbid : option.forbids()) {
			if (present.contains(findOptionHandler(forbid)))
				return false;
		}
		return true;
	}

	private class CmdLineImpl implements Parameters {
		private final String[] args;
		private int pos;

		CmdLineImpl(String[] args, int position) {
			this.args = args;
			pos = position;
		}

		protected boolean hasMore() {
			return pos < args.length;
		}

		protected String getCurrentToken() {
			return args[pos];
		}

		private void proceed(int n) {
			pos += n;
		}

		public String getParameter(int idx) throws CmdLineException {
			if (pos + idx >= args.length || pos + idx < 0)
				throw new CmdLineException(PartialCmdLineParser.this,
						Messages.MISSING_OPERAND, getOptionName());
			return args[pos + idx];
		}

		public int size() {
			return args.length - pos;
		}

		/**
		 * Used when the current token is of the form "-option=value", to
		 * replace the current token by "value", as if this was given as two
		 * tokens "-option value"
		 */
		void splitToken() {
			if (pos < args.length && pos >= 0) {
				int idx = args[pos].indexOf("=");
				if (idx > 0) {
					args[pos] = args[pos].substring(idx + 1);
				}
			}
		}
	}

	private String getOptionName() {
		return currentOptionHandler.option.toString();
	}
}
