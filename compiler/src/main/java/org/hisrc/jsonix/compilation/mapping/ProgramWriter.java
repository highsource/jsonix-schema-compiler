package org.hisrc.jsonix.compilation.mapping;

import org.hisrc.jscm.codemodel.JSProgram;
import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Output;

public interface ProgramWriter<T, C extends T> {

	public void writeProgram(Module<T, C> module, JSProgram program,
			Output output);
}
