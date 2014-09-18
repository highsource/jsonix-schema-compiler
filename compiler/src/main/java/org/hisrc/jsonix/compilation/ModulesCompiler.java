package org.hisrc.jsonix.compilation;

import org.apache.commons.lang3.Validate;
import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.JSProgram;
import org.hisrc.jscm.codemodel.impl.CodeModelImpl;
import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Modules;
import org.hisrc.jsonix.definition.Output;
import org.jvnet.jaxb2_commons.xml.bind.model.MModelInfo;

public class ModulesCompiler<T, C extends T> {

	private final Modules modules;

	public ModulesCompiler(Modules modules) {
		Validate.notNull(modules);
		this.modules = modules;
	}

	public void compile(MModelInfo<T, C> mModel, ProgramWriter programWriter) {
		final JSCodeModel codeModel = new CodeModelImpl();

		for (Module module : this.modules.getModules()) {
			for (Output output : module.getOutputs()) {
				// TODO isEmpty
				final ModuleCompiler<T, C> moduleCompiler = new ModuleCompiler<T, C>(
						codeModel, modules, module, output);

				final JSProgram program = moduleCompiler.compile(mModel);
				programWriter.writeProgram(module, program, output);
			}
		}
	}

}
