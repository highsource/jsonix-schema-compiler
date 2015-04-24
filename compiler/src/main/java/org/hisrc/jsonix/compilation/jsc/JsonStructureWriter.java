package org.hisrc.jsonix.compilation.jsc;

import javax.json.JsonStructure;

import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Output;

public interface JsonStructureWriter<T, C extends T> {

	public void writeJsonStructure(Module<T, C> module, JsonStructure schema,
			Output output);

}
