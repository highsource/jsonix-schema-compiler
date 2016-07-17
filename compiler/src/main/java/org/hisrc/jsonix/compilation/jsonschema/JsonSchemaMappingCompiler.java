package org.hisrc.jsonix.compilation.jsonschema;

import javax.json.JsonBuilderFactory;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.JsonixConstants;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.ClassInfoProducer;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.CreateTypeInfoProducer;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.EnumLeafInfoProducer;
import org.hisrc.jsonix.definition.Mapping;
import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Modules;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.hisrc.jsonix.jsonschema.JsonSchemaConstants;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MEnumLeafInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.origin.MOriginated;

public class JsonSchemaMappingCompiler<T, C extends T> {

	private final Modules<T, C> modules;
	private final Module<T, C> module;
	private final Mapping<T, C> mapping;
	private final JsonBuilderFactory jsonBuilderFactory;

	public JsonSchemaMappingCompiler(JsonBuilderFactory jsonBuilderFactory, Modules<T, C> modules, Module<T, C> module,
			Mapping<T, C> mapping) {
		Validate.notNull(jsonBuilderFactory);
		Validate.notNull(modules);
		Validate.notNull(module);
		Validate.notNull(mapping);
		this.jsonBuilderFactory = jsonBuilderFactory;
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

	public JsonBuilderFactory getJsonBuilderFactory() {
		return jsonBuilderFactory;
	}

	public JsonSchemaBuilder compile() {
		final JsonSchemaBuilder schema = new JsonSchemaBuilder();
		final String schemaId = mapping.getSchemaId();
		schema.addId(schemaId);
		addElementInfos(schema);
		addClassInfoSchemas(schema);
		addEnumLeafInfoSchemas(schema);
		return schema;
	}

	private void addElementInfos(final JsonSchemaBuilder schema) {
		for (MElementInfo<T, C> elementInfo : mapping.getElementInfos()) {
			final QName elementName = elementInfo.getElementName();
			final MTypeInfo<T, C> typeInfo = elementInfo.getTypeInfo();
			final MClassInfo<T, C> scope = elementInfo.getScope();

			final JsonSchemaBuilder elementInfoSchema = new JsonSchemaBuilder();
			elementInfoSchema.addType(JsonSchemaConstants.OBJECT_TYPE);
			final JsonSchemaBuilder qNameRef = new JsonSchemaBuilder()
					.addRef(XmlSchemaJsonSchemaConstants.QNAME_TYPE_INFO_SCHEMA_REF);
			final JsonSchemaBuilder nameConstant = new JsonSchemaBuilder();
			nameConstant.addType(JsonSchemaConstants.OBJECT_TYPE);
			nameConstant.addProperty(JsonixJsonSchemaConstants.LOCAL_PART_PROPERTY_NAME,
					new JsonSchemaBuilder().addEnum(elementName.getLocalPart()));
			nameConstant.addProperty(JsonixJsonSchemaConstants.NAMESPACE_URI_PROPERTY_NAME,
					new JsonSchemaBuilder().addEnum(elementName.getNamespaceURI()));

			elementInfoSchema.addProperty(JsonixConstants.NAME_PROPERTY_NAME,
					new JsonSchemaBuilder().addAllOf(qNameRef).addAllOf(nameConstant));

			elementInfoSchema.addProperty(JsonixConstants.VALUE_PROPERTY_NAME,
					createTypeInfoSchemaRef(elementInfo, typeInfo));

			elementInfoSchema.add(JsonixJsonSchemaConstants.ELEMENT_NAME_PROPERTY_NAME,
					new JsonSchemaBuilder()
							.add(JsonixJsonSchemaConstants.LOCAL_PART_PROPERTY_NAME, elementName.getLocalPart()).add(
									JsonixJsonSchemaConstants.NAMESPACE_URI_PROPERTY_NAME,
									elementName.getNamespaceURI()));
			if (scope != null) {
				elementInfoSchema.add(JsonixJsonSchemaConstants.SCOPE_PROPERTY_NAME,
						createTypeInfoSchemaRef(scope, scope));
			}
			schema.addAnyOf(elementInfoSchema);
		}
	}

	private void addEnumLeafInfoSchemas(final JsonSchemaBuilder schema) {
		for (MEnumLeafInfo<T, C> enumLeafInfo : mapping.getEnumLeafInfos()) {
			final EnumLeafInfoProducer<T, C> enumLeafInfoCompiler = new EnumLeafInfoProducer<T, C>(enumLeafInfo);
			final JsonSchemaBuilder enumLeafInfoSchema = enumLeafInfoCompiler.compile(this);
			schema.addDefinition(enumLeafInfo.getContainerLocalName(JsonixConstants.DEFAULT_SCOPED_NAME_DELIMITER),
					enumLeafInfoSchema);
		}
	}

	private void addClassInfoSchemas(final JsonSchemaBuilder schema) {
		for (MClassInfo<T, C> classInfo : mapping.getClassInfos()) {
			final ClassInfoProducer<T, C> classInfoCompiler = new ClassInfoProducer<T, C>(classInfo);
			final JsonSchemaBuilder classInfoSchema = classInfoCompiler.compile(this);
			schema.addDefinition(classInfo.getContainerLocalName(JsonixConstants.DEFAULT_SCOPED_NAME_DELIMITER),
					classInfoSchema);
		}
	}

	public <M extends MOriginated<O>, O> JsonSchemaBuilder createTypeInfoSchemaRef(M originated,
			MTypeInfo<T, C> typeInfo) {
		return typeInfo.acceptTypeInfoVisitor(new CreateTypeInfoProducer<T, C, O>(originated))
				.createTypeInfoSchemaRef(this);
	}
}
