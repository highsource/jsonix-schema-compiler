package org.hisrc.jsonix;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.args4j.PartialCmdLineParser;
import org.hisrc.jsonix.compilation.jsonschema.JsonStructureWriter;
import org.hisrc.jsonix.compilation.mapping.ProgramWriter;
import org.hisrc.jsonix.execution.JsonixInvoker;
import org.hisrc.jsonix.settings.Settings;
import org.hisrc.jsonix.xjc.plugin.JsonixPlugin;

import com.sun.codemodel.JCodeModel;
import com.sun.tools.xjc.ConsoleErrorReporter;
import com.sun.tools.xjc.ErrorReceiver;
import com.sun.tools.xjc.ModelLoader;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.model.nav.NClass;
import com.sun.tools.xjc.model.nav.NType;

public class JsonixMain {

	// private final Logger logger = LoggerFactory.getLogger(JsonixMain.class);

	public static void main(String[] args) throws Exception {

		final List<String> arguments = new ArrayList<String>(args.length);

		final Settings settings = new Settings();
		final PartialCmdLineParser parser = new PartialCmdLineParser(settings);
		int position = 0;

		while (position < args.length) {
			final int consumed = parser.parseArgument(args, position);
			if (consumed == 0) {
				arguments.add(args[position++]);
			} else {
				position += consumed;
			}
		}

		if (!arguments.contains(JsonixPlugin.OPTION)) {
			arguments.add(JsonixPlugin.OPTION);
		}

		if (!arguments.contains("-extension")) {
			arguments.add("-extension");
		}

		// TODO
		arguments.add("-disableXmlSecurity");
		// TODO
		System.setProperty("javax.xml.accessExternalSchema", "all");
		// TODO
		System.setProperty("javax.xml.accessExternalDTD", "all");

		File targetDirectory = settings.getTargetDirectory();

		if (targetDirectory == null) {
			targetDirectory = new File(".");
		}

		if (!targetDirectory.exists()) {
			targetDirectory.mkdirs();
		}

		arguments.add("-d");
		arguments.add(targetDirectory.getAbsolutePath());

		final Options options = new Options();

		options.parseArguments(arguments.toArray(new String[arguments.size()]));

		new JsonixMain(settings, options).execute();
	}

	private final Settings settings;
	private final Options options;

	public JsonixMain(Settings settings, Options options) {
		this.settings = Validate.notNull(settings);
		this.options = Validate.notNull(options);
	}

	public Options getOptions() {
		return options;
	}

	public Settings getSettings() {
		return settings;
	}

	private void execute() {

		final ErrorReceiver errorHandler = new ConsoleErrorReporter();
		final Model model = ModelLoader.load(getOptions(), new JCodeModel(),
				errorHandler);

		final File targetDirectory = getOptions().targetDir;

		final ProgramWriter<NType, NClass> programWriter = new TargetDirectoryProgramWriter(
				targetDirectory, errorHandler);

		final JsonStructureWriter<NType, NClass> jsonStructureWriter = new TargetDirectoryJsonStructureWriter(
				targetDirectory, errorHandler);

		new JsonixInvoker().execute(settings, model, programWriter,
				jsonStructureWriter);

	}

}
