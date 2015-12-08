package org.hisrc.jsonix.compilation.jsonschema;

import javax.xml.namespace.QName;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.JsonixConstants;
import org.hisrc.jsonix.definition.Mapping;
import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Modules;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.hisrc.jsonix.jsonschema.JsonSchemaConstants;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MEnumLeafInfo;
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
		final String schemaId = mapping.getSchemaId();
		schema.addId(schemaId);
		addElementInfos(schema);
		addClassInfoSchemas(schema);
		addElementLeafInfoSchemas(schema);
		return schema;
	}

	private void addElementInfos(final JsonSchemaBuilder schema) {
		for (MElementInfo<T, C> elementInfo : mapping.getElementInfos()) {
			final QName elementName = elementInfo.getElementName();
			final MTypeInfo<T, C> typeInfo = elementInfo.getTypeInfo();
			final MTypeInfo<T, C> scope = elementInfo.getScope();

			final JsonSchemaBuilder elementInfoSchema = new JsonSchemaBuilder();
			elementInfoSchema.addType(JsonSchemaConstants.OBJECT_TYPE);
			final JsonSchemaBuilder qNameRef = new JsonSchemaBuilder()
					.addRef(XmlSchemaJsonSchemaConstants.QNAME_TYPE_INFO_SCHEMA_REF);
			final JsonSchemaBuilder nameConstant = new JsonSchemaBuilder();
			nameConstant.addType(JsonSchemaConstants.OBJECT_TYPE);
			nameConstant
					.addProperty(
							JsonixJsonSchemaConstants.LOCAL_PART_PROPERTY_NAME,
							new JsonSchemaBuilder().addEnum(elementName
									.getLocalPart()));
			nameConstant.addProperty(
					JsonixJsonSchemaConstants.NAMESPACE_URI_PROPERTY_NAME,
					new JsonSchemaBuilder().addEnum(elementName
							.getNamespaceURI()));

			elementInfoSchema.addProperty(
					JsonixConstants.NAME_PROPERTY_NAME,
					new JsonSchemaBuilder().addAllOf(qNameRef).addAllOf(
							nameConstant));

			elementInfoSchema.addProperty(JsonixConstants.VALUE_PROPERTY_NAME,
					createTypeInfoSchemaRef(typeInfo));

			elementInfoSchema
					.add(JsonixJsonSchemaConstants.ELEMENT_NAME_PROPERTY_NAME,
							new JsonSchemaBuilder()
									.add(JsonixJsonSchemaConstants.LOCAL_PART_PROPERTY_NAME,
											elementName.getLocalPart())
									.add(JsonixJsonSchemaConstants.NAMESPACE_URI_PROPERTY_NAME,
											elementName.getNamespaceURI()));
			if (scope != null) {
				elementInfoSchema.add(
						JsonixJsonSchemaConstants.SCOPE_PROPERTY_NAME,
						createTypeInfoSchemaRef(scope));
			}
			schema.addAnyOf(elementInfoSchema);
		}
	}

	private void addElementLeafInfoSchemas(final JsonSchemaBuilder schema) {
		final JsonSchemaEnumLeafInfoProducer<T, C> enumLeafInfoCompiler = new JsonSchemaEnumLeafInfoProducer<T, C>(
				this);
		for (MEnumLeafInfo<T, C> enumLeafInfo : mapping.getEnumLeafInfos()) {
			final JsonSchemaBuilder enumLeafInfoSchema = enumLeafInfoCompiler
					.produce(enumLeafInfo);
			schema.addDefinition(
					enumLeafInfo
							.getContainerLocalName(JsonixConstants.DEFAULT_SCOPED_NAME_DELIMITER),
					enumLeafInfoSchema);
		}
	}

	private void addClassInfoSchemas(final JsonSchemaBuilder schema) {
		final JsonSchemaClassInfoProducer<T, C> classInfoCompiler = new JsonSchemaClassInfoProducer<T, C>(
				this);
		for (MClassInfo<T, C> classInfo : mapping.getClassInfos()) {
			final JsonSchemaBuilder classInfoSchema = classInfoCompiler
					.produce(classInfo);
			schema.addDefinition(
					classInfo
							.getContainerLocalName(JsonixConstants.DEFAULT_SCOPED_NAME_DELIMITER),
					classInfoSchema);
		}
	}

	public JsonSchemaBuilder createTypeInfoSchemaRef(MTypeInfo<T, C> typeInfo) {
		return typeInfo
				.acceptTypeInfoVisitor(new JsonSchemaRefTypeInfoProducerVisitor<T, C>(
						this));
	}
}
