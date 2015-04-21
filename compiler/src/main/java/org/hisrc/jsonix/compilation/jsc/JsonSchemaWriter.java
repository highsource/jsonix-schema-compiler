package org.hisrc.jsonix.compilation.jsc;

import javax.json.JsonStructure;

import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Output;

public interface JsonSchemaWriter<T, C extends T> {

	public void writeJsonSchema(Module<T, C> module, JsonStructure schema,
			Output output);

}
