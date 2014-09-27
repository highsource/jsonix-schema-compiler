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
import org.hisrc.jsonix.configuration.OutputConfiguration;
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
		
		// TODO
		arguments.add("-disableXmlSecurity");
		// TODO
		System.setProperty("javax.xml.accessExternalSchema", "all");
		// TODO
		System.setProperty("javax.xml.accessExternalDTD", "all");
		
		
		if (!arguments.contains(JsonixPlugin.OPTION)) {
			arguments.add(JsonixPlugin.OPTION);
		}

		final OutputConfiguration defaultOutputConfiguration;
		if (arguments.contains(JsonixPlugin.OPTION_COMPACT)) {
			defaultOutputConfiguration = new OutputConfiguration(CompactNaming.NAMING_NAME, OutputConfiguration.STANDARD_FILE_NAME_PATTERN);
		} else {
			defaultOutputConfiguration = new OutputConfiguration(StandardNaming.NAMING_NAME, OutputConfiguration.STANDARD_FILE_NAME_PATTERN);
		}
		// Driver.main(arguments.toArray(new String[arguments.size()]));

		Options options = new Options();

		options.parseArguments(arguments.toArray(new String[arguments.size()]));

		final JsonixMain main = new JsonixMain(options, defaultOutputConfiguration);
		main.execute();
	}

	private final Options options;
	private final OutputConfiguration defaultOutputConfiguration;

	public JsonixMain(Options options, OutputConfiguration defaultOutputConfiguration) {
		Validate.notNull(options);
		Validate.notNull(defaultOutputConfiguration);
		this.options = options;
		this.defaultOutputConfiguration = defaultOutputConfiguration;
	}

	private void execute() {

		final ConsoleErrorReporter receiver = new ConsoleErrorReporter();
		final Model model = ModelLoader.load(options, new JCodeModel(),
				receiver);

		final ModulesConfigurationUnmarshaller customizationHandler = new ModulesConfigurationUnmarshaller(
				this.log);

		final ModulesConfiguration modulesConfiguration = customizationHandler
				.unmarshal(model, this.defaultOutputConfiguration);

		final ErrorReceiver errorReceiver = new ConsoleErrorReporter();

		final MModelInfo<NType, NClass> modelInfo = new XJCCMInfoFactory(model)
				.createModel();

		final Modules<NType, NClass> modules = modulesConfiguration.build(log, modelInfo);

		final ModulesCompiler<NType, NClass> modulesCompiler = new ModulesCompiler<NType, NClass>(
				modules);

		modulesCompiler.compile(new ProgramWriter<NType, NClass>() {

			@Override
			public void writeProgram(Module<NType, NClass> module, JSProgram program,
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
