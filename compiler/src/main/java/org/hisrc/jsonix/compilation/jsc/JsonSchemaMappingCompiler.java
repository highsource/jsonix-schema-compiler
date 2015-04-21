package org.hisrc.jsonix.compilation.jsc;

import javax.json.JsonBuilderFactory;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.definition.Mapping;
import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Modules;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;

public class JsonSchemaMappingCompiler<T, C extends T> {

	private final JsonBuilderFactory builderFactory;
	private final Modules<T, C> modules;
	private final Module<T, C> module;
	private final Mapping<T, C> mapping;

	public JsonSchemaMappingCompiler(JsonBuilderFactory builderFactory,
			Modules<T, C> modules, Module<T, C> module, Mapping<T, C> mapping) {
		Validate.notNull(builderFactory);
		Validate.notNull(modules);
		Validate.notNull(module);
		Validate.notNull(mapping);
		this.builderFactory = builderFactory;
		this.modules = modules;
		this.module = module;
		this.mapping = mapping;
	}

	public Modules<T, C> getModules() {
		return modules;
	}

	public Module<T, C> getModule() {
		return module;
	}

	public Mapping<T, C> getMapping() {
		return mapping;
	}

	public JsonSchemaBuilder compile() {
		throw new UnsupportedOperationException();
	}

	public JsonSchemaBuilder createTypeInfoSchemaRef(MTypeInfo<T, C> typeInfo) {
		throw new UnsupportedOperationException();
	}
}
