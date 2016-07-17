package org.hisrc.jsonix.compilation.jsonschema;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.json.JsonBuilderFactory;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.definition.JsonSchema;
import org.hisrc.jsonix.definition.Mapping;
import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Modules;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;

public class JsonSchemaModuleCompiler<T, C extends T> {

	private final Modules<T, C> modules;
	private final Module<T, C> module;
	private final JsonBuilderFactory jsonBuilderFactory;

	public JsonSchemaModuleCompiler(JsonBuilderFactory jsonBuilderFactory, Modules<T, C> modules, Module<T, C> module,
			JsonSchema jsonSchema) {
		Validate.notNull(jsonBuilderFactory);
		Validate.notNull(module);
		Validate.notNull(jsonSchema);
		this.jsonBuilderFactory = jsonBuilderFactory;
		this.modules = modules;
		this.module = module;
	}

	public JsonBuilderFactory getJsonBuilderFactory() {
		return jsonBuilderFactory;
	}

	public Modules<T, C> getModules() {
		return modules;
	}

	public Module<T, C> getModule() {
		return module;
	}

	public JsonSchemaBuilder compile() {
		final Map<Mapping<T, C>, JsonSchemaBuilder> mappingSchemas = new LinkedHashMap<Mapping<T, C>, JsonSchemaBuilder>(
				this.module.getMappings().size());
		for (Mapping<T, C> mapping : this.module.getMappings()) {
			if (!mapping.isEmpty()) {
				final JsonSchemaMappingCompiler<T, C> mappingCompiler = new JsonSchemaMappingCompiler<T, C>(
						jsonBuilderFactory, modules, module, mapping);
				mappingSchemas.put(mapping, mappingCompiler.compile());

			}
		}

		final JsonSchemaBuilder schema;
		if (mappingSchemas.size() == 1) {
			schema = mappingSchemas.values().iterator().next();
		} else {
			schema = new JsonSchemaBuilder();
			schema.addId(getModule().getSchemaId());
			for (Entry<Mapping<T, C>, JsonSchemaBuilder> entry : mappingSchemas.entrySet()) {
				final Mapping<T, C> mapping = entry.getKey();
				final JsonSchemaBuilder mappingSchema = entry.getValue();
				schema.addDefinition(mapping.getMappingName(), mappingSchema);
				schema.addAnyOf(new JsonSchemaBuilder().addRef(mapping.getSchemaId()));
			}
		}

		return schema;
	}
}
