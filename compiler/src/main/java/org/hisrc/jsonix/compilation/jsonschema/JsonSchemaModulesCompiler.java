package org.hisrc.jsonix.compilation.jsonschema;

import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.definition.JsonSchema;
import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Modules;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;

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
		final JsonProvider provider = JsonProvider.provider();
		final JsonBuilderFactory builderFactory = provider
				.createBuilderFactory(null);
		for (final Module<T, C> module : this.modules.getModules()) {
			if (!module.isEmpty()) {
				for (JsonSchema jsonSchema : module.getJsonSchemas()) {
					final JsonSchemaModuleCompiler<T, C> moduleCompiler = new JsonSchemaModuleCompiler<T, C>(
							this, module, jsonSchema);
					final JsonSchemaBuilder moduleSchema = moduleCompiler
							.compile();
					final JsonObject moduleSchemaJsonObject = moduleSchema
							.build(builderFactory);
					writer.writeJsonStructure(module, moduleSchemaJsonObject,
							jsonSchema.getFileName());
				}
			}
		}
	}
}
