package org.hisrc.jsonix.compilation.jsc;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonBuilderFactory;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.definition.Mapping;
import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Modules;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;

public class JsonSchemaModuleCompiler<T, C extends T> {

	private final JsonBuilderFactory builderFactory;
	private final Modules<T, C> modules;
	private final Module<T, C> module;

	public JsonSchemaModuleCompiler(JsonBuilderFactory builderFactory,
			Modules<T, C> modules, Module<T, C> module) {
		Validate.notNull(builderFactory);
		Validate.notNull(modules);
		Validate.notNull(module);
		this.builderFactory = builderFactory;
		this.modules = modules;
		this.module = module;
	}

	public JsonSchemaBuilder compile() {
		final List<JsonSchemaBuilder> mappingSchemaBuilders = new ArrayList<JsonSchemaBuilder>(
				this.module.getMappings().size());
		for (Mapping<T, C> mapping : this.module.getMappings()) {
			if (!mapping.isEmpty()) {
				final JsonSchemaMappingCompiler<T, C> mappingCompiler = new JsonSchemaMappingCompiler<T, C>(
						builderFactory, modules, module, mapping);

				final JsonSchemaBuilder mappingSchemaBuilder = mappingCompiler
						.compile();

				mappingSchemaBuilders.add(mappingSchemaBuilder);
			}
		}

		if (mappingSchemaBuilders.size() == 1) {
			return mappingSchemaBuilders.get(0);
		} else {
			final JsonSchemaBuilder schemaBuilder = new JsonSchemaBuilder();
			for (JsonSchemaBuilder mappingSchemaBuilder : mappingSchemaBuilders) {
				schemaBuilder.addAnyOf(mappingSchemaBuilder);
			}
			return schemaBuilder;
		}
	}

}
