package org.hisrc.jsonix.execution;

import org.hisrc.jsonix.compilation.ModulesCompiler;
import org.hisrc.jsonix.compilation.ProgramWriter;
import org.hisrc.jsonix.configuration.ModulesConfiguration;
import org.hisrc.jsonix.configuration.ModulesConfigurationUnmarshaller;
import org.hisrc.jsonix.context.JsonixContext;
import org.hisrc.jsonix.definition.Modules;
import org.hisrc.jsonix.settings.Settings;
import org.jvnet.jaxb2_commons.xjc.model.concrete.XJCCMInfoFactory;
import org.jvnet.jaxb2_commons.xml.bind.model.MModelInfo;

import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.model.nav.NClass;
import com.sun.tools.xjc.model.nav.NType;

public class JsonixInvoker {

	public void execute(JsonixContext context, Settings settings, Model model, ProgramWriter<NType, NClass> programWriter)
	{
		final ModulesConfigurationUnmarshaller customizationHandler = new ModulesConfigurationUnmarshaller(
				context);

		final ModulesConfiguration modulesConfiguration = customizationHandler
				.unmarshal(model, this.defaultOutputConfiguration);

		final MModelInfo<NType, NClass> modelinfo = new XJCCMInfoFactory(model)
				.createModel();

		final Modules<NType, NClass> modules = modulesConfiguration.build(
				context, modelinfo);

		final ModulesCompiler<NType, NClass> modulesCompiler = new ModulesCompiler<NType, NClass>(
				modules);

		modulesCompiler.compile(programWriter);
	}
}
