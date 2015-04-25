package org.hisrc.jsonix.execution;

import org.hisrc.jsonix.compilation.ModulesCompiler;
import org.hisrc.jsonix.compilation.ProgramWriter;
import org.hisrc.jsonix.compilation.jsonschema.JsonSchemaModulesCompiler;
import org.hisrc.jsonix.compilation.jsonschema.JsonStructureWriter;
import org.hisrc.jsonix.configuration.JsonSchemaConfiguration;
import org.hisrc.jsonix.configuration.ModulesConfiguration;
import org.hisrc.jsonix.configuration.ModulesConfigurationUnmarshaller;
import org.hisrc.jsonix.configuration.OutputConfiguration;
import org.hisrc.jsonix.context.DefaultJsonixContext;
import org.hisrc.jsonix.definition.Modules;
import org.hisrc.jsonix.settings.Settings;
import org.jvnet.jaxb2_commons.xjc.model.concrete.XJCCMInfoFactory;
import org.jvnet.jaxb2_commons.xml.bind.model.MModelInfo;
import org.slf4j.Logger;

import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.model.nav.NClass;
import com.sun.tools.xjc.model.nav.NType;

public class JsonixInvoker {

	public void execute(Settings settings, Model model,
			ProgramWriter<NType, NClass> programWriter,
			JsonStructureWriter<NType, NClass> jsonStructureWriter) {

		final DefaultJsonixContext context = new DefaultJsonixContext();

		context.setLogLevel(settings.getLogLevel().asInt());

		final Logger logger = context.getLoggerFactory().getLogger(
				JsonixInvoker.class.getName());

		if (model == null) {
			logger.error("The model is null, there was probably a problem parsing schemas.");
			return;
		}

		final ModulesConfigurationUnmarshaller customizationHandler = new ModulesConfigurationUnmarshaller(
				context);

		final OutputConfiguration defaultOutputConfiguration = new OutputConfiguration(
				settings.getDefaultNaming().getName(),
				OutputConfiguration.STANDARD_FILE_NAME_PATTERN);

		final JsonSchemaConfiguration defaultJsonSchemaConfiguration = new JsonSchemaConfiguration(
				JsonSchemaConfiguration.STANDARD_FILE_NAME_PATTERN);

		final ModulesConfiguration modulesConfiguration = customizationHandler
				.unmarshal(model, defaultOutputConfiguration,
						defaultJsonSchemaConfiguration);

		final MModelInfo<NType, NClass> modelinfo = new XJCCMInfoFactory(model)
				.createModel();

		final Modules<NType, NClass> modules = modulesConfiguration.build(
				context, modelinfo);

		final ModulesCompiler<NType, NClass> modulesCompiler = new ModulesCompiler<NType, NClass>(
				modules);

		modulesCompiler.compile(programWriter);

		final JsonSchemaModulesCompiler<NType, NClass> jsonSchemaModulesCompiler = new JsonSchemaModulesCompiler<NType, NClass>(
				modules);
		jsonSchemaModulesCompiler.compile(jsonStructureWriter);
	}
}
