package org.hisrc.jsonix.compilation.jsonschema;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.definition.JsonSchema;
import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Modules;

public class JsonSchemaModulesCompiler<T, C extends T> {

	private final Modules<T, C> modules;

	public JsonSchemaModulesCompiler(Modules<T, C> modules) {
		Validate.notNull(modules);
		this.modules = modules;
	}

	public Modules<T, C> getModules() {
		return modules;
	}

	public void compile(JsonStructureWriter<T, C> writer) {
		for (final Module<T, C> module : this.modules.getModules()) {
			if (!module.isEmpty()) {
				for (JsonSchema jsonSchema : module.getJsonSchemas()) {
					final JsonSchemaModuleCompiler<T, C> moduleCompiler = new JsonSchemaModuleCompiler<T, C>(
							this, module, jsonSchema);
					moduleCompiler.compile(writer);
				}
			}
		}
	}
}
