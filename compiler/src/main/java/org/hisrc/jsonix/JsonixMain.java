package org.hisrc.jsonix;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.hisrc.jscm.codemodel.JSProgram;
import org.hisrc.jscm.codemodel.writer.CodeWriter;
import org.hisrc.jsonix.compilation.ModulesCompiler;
import org.hisrc.jsonix.compilation.ProgramWriter;
import org.hisrc.jsonix.configuration.ModulesConfiguration;
import org.hisrc.jsonix.configuration.ModulesConfigurationUnmarshaller;
import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Modules;
import org.hisrc.jsonix.definition.Output;
import org.hisrc.jsonix.log.Log;
import org.hisrc.jsonix.log.SystemLog;
import org.hisrc.jsonix.naming.CompactNaming;
import org.hisrc.jsonix.naming.StandardNaming;
import org.hisrc.jsonix.xjc.plugin.JsonixPlugin;
import org.jvnet.jaxb2_commons.xjc.model.concrete.XJCCMInfoFactory;
import org.jvnet.jaxb2_commons.xml.bind.model.MModelInfo;
import org.xml.sax.SAXParseException;

import com.sun.codemodel.JCodeModel;
import com.sun.tools.xjc.ConsoleErrorReporter;
import com.sun.tools.xjc.ErrorReceiver;
import com.sun.tools.xjc.ModelLoader;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.model.nav.NClass;
import com.sun.tools.xjc.model.nav.NType;

public class JsonixMain {

	private Log log = new SystemLog();

	public static void main(String[] args) throws Exception {

		final List<String> arguments = new ArrayList<String>(
				Arrays.asList(args));

		if (!arguments.contains("-extension")) {
			arguments.add("-extension");
		}

		if (!arguments.contains(JsonixPlugin.OPTION)) {
			arguments.add(JsonixPlugin.OPTION);
		}

		final String defaultNaming;
		if (arguments.contains(JsonixPlugin.OPTION_COMPACT)) {
			defaultNaming = CompactNaming.NAMING_NAME;
		} else {
			defaultNaming = StandardNaming.NAMING_NAME;
		}
		// Driver.main(arguments.toArray(new String[arguments.size()]));

		Options options = new Options();

		options.parseArguments(arguments.toArray(new String[arguments.size()]));

		final JsonixMain main = new JsonixMain(options, defaultNaming);
		main.execute();
	}

	private final Options options;
	private final String defaultNaming;

	public JsonixMain(Options options, String defaultNaming) {
		Validate.notNull(options);
		Validate.notNull(defaultNaming);
		this.options = options;
		this.defaultNaming = defaultNaming;
	}

	private void execute() {

		final ConsoleErrorReporter receiver = new ConsoleErrorReporter();
		final Model model = ModelLoader.load(options, new JCodeModel(),
				receiver);

		final ModulesConfigurationUnmarshaller customizationHandler = new ModulesConfigurationUnmarshaller(
				this.log);

		final ModulesConfiguration modulesConfiguration = customizationHandler
				.unmarshal(model, this.defaultNaming);

		final ErrorReceiver errorReceiver = new ConsoleErrorReporter();

		final MModelInfo<NType, NClass> mModel = new XJCCMInfoFactory(model)
				.createModel();

		final Modules modules = modulesConfiguration.build(log, mModel);

		final ModulesCompiler<NType, NClass> modulesCompiler = new ModulesCompiler<NType, NClass>(
				modules);

		modulesCompiler.compile(mModel, new ProgramWriter() {

			@Override
			public void writeProgram(Module module, JSProgram program,
					Output output) {
				try {
					writePrograms(options.targetDir, output.getDirectory(),
							output.getFileName(), program);
				} catch (IOException ioex) {
					errorReceiver.error(new SAXParseException(
							MessageFormat
									.format("Could not create the code for the module [{0}].",
											module.getName()), null, ioex));
				}
			}
		});
	}

	private File writePrograms(final File targetDir, final String directory,
			final String fileName, JSProgram... programs) throws IOException {
		Validate.notNull(fileName);

		final File dir = new File(targetDir, directory);
		dir.mkdirs();
		final File file = new File(dir, fileName);
		final FileWriter fileWriter = new FileWriter(file);
		try {
			// final StringWriter stringWriter = new StringWriter();
			final CodeWriter codeWriter = new CodeWriter(fileWriter);
			for (JSProgram program : programs) {
				codeWriter.program(program);
				codeWriter.lineTerminator();
			}
		} finally {
			try {
				fileWriter.close();
			} catch (IOException ignored) {
			}
		}
		return file;
	}
}
