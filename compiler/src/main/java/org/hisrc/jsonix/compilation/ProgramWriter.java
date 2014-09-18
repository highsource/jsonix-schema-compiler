package org.hisrc.jsonix.compilation;

import org.hisrc.jscm.codemodel.JSProgram;
import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Output;

public interface ProgramWriter {

	public void writeProgram(Module module, JSProgram program, Output output);
}
