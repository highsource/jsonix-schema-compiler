package org.hisrc.jsonix.compilation.jsonschema;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.definition.Mapping;
import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Modules;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;

public class JsonSchemaMappingCompiler<T, C extends T> {

	private JsonSchemaModuleCompiler<T, C> moduleCompiler;
	private final Modules<T, C> modules;
	private final Module<T, C> module;
	private final Mapping<T, C> mapping;

	public JsonSchemaMappingCompiler(
			JsonSchemaModuleCompiler<T, C> moduleCompiler, Mapping<T, C> mapping) {
		Validate.notNull(moduleCompiler);
		Validate.notNull(mapping);
		this.moduleCompiler = moduleCompiler;
		this.modules = moduleCompiler.getModules();
		this.module = moduleCompiler.getModule();
		this.mapping = mapping;
	}

	public JsonSchemaModuleCompiler<T, C> getModuleCompiler() {
		return moduleCompiler;
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
		final JsonSchemaBuilder schema = new JsonSchemaBuilder();
		schema.addId(mapping.getSchemaId());
		final JsonSchemaClassInfoCompiler<T, C> classInfoCompiler = new JsonSchemaClassInfoCompiler<T, C>(
				this);
		for (MClassInfo<T, C> classInfo : mapping.getClassInfos()) {
			final JsonSchemaBuilder classInfoSchema = classInfoCompiler
					.compile(classInfo);
			// TODO constant
			schema.addDefinition(classInfo.getContainerLocalName("."),
					classInfoSchema);
		}
		return schema;
	}

	public JsonSchemaBuilder createTypeInfoSchemaRef(MTypeInfo<T, C> typeInfo) {
		return typeInfo
				.acceptTypeInfoVisitor(new JsonSchemaRefTypeInfoCompilerVisitor<T, C>(
						this));
	}
}
