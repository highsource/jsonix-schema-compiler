package org.hisrc.jsonix.compilation.jsc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.definition.Mapping;
import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Modules;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;

public class JsonSchemaModuleCompiler<T, C extends T> {

	private final JsonSchemaModulesCompiler<T, C> modulesCompiler;
	private final Modules<T, C> modules;
	private final Module<T, C> module;

	public JsonSchemaModuleCompiler(
			JsonSchemaModulesCompiler<T, C> modulesCompiler, Module<T, C> module) {
		Validate.notNull(modulesCompiler);
		Validate.notNull(module);
		this.modulesCompiler = modulesCompiler;
		this.modules = modulesCompiler.getModules();
		this.module = module;
	}

	public JsonSchemaModulesCompiler<T, C> getModulesCompiler() {
		return modulesCompiler;
	}

	public Modules<T, C> getModules() {
		return modules;
	}

	public Module<T, C> getModule() {
		return module;
	}

	public JsonSchemaBuilder compile() {
		final List<JsonSchemaBuilder> mappingSchemas = new ArrayList<JsonSchemaBuilder>(
				this.module.getMappings().size());
		for (Mapping<T, C> mapping : this.module.getMappings()) {
			if (!mapping.isEmpty()) {
				final JsonSchemaMappingCompiler<T, C> mappingCompiler = new JsonSchemaMappingCompiler<T, C>(
						this, mapping);

				final JsonSchemaBuilder mappingSchema = mappingCompiler
						.compile();

				mappingSchemas.add(mappingSchema);
			}
		}

		if (mappingSchemas.size() == 1) {
			return mappingSchemas.get(0);
		} else {
			final JsonSchemaBuilder schema = new JsonSchemaBuilder();
			for (JsonSchemaBuilder mappingSchema : mappingSchemas) {
				schema.addAnyOf(mappingSchema);
			}
			return schema;
		}
	}

}
