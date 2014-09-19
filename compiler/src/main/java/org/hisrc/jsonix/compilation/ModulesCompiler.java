package org.hisrc.jsonix.compilation;

import org.apache.commons.lang3.Validate;
import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.JSProgram;
import org.hisrc.jscm.codemodel.impl.CodeModelImpl;
import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Modules;
import org.hisrc.jsonix.definition.Output;

public class ModulesCompiler<T, C extends T> {

	private final Modules<T, C> modules;

	public ModulesCompiler(Modules<T, C> modules) {
		Validate.notNull(modules);
		this.modules = modules;
	}

	public void compile(ProgramWriter<T, C> programWriter) {
		final JSCodeModel codeModel = new CodeModelImpl();

		for (Module<T, C> module : this.modules.getModules()) {
			for (Output output : module.getOutputs()) {
				// TODO isEmpty
				final ModuleCompiler<T, C> moduleCompiler = new ModuleCompiler<T, C>(
						codeModel, modules, module, output);

				final JSProgram program = moduleCompiler.compile();
				programWriter.writeProgram(module, program, output);
			}
		}
	}

}
