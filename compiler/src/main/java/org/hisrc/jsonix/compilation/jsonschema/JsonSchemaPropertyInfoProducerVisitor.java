package org.hisrc.jsonix.compilation.jsonschema;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.JsonixConstants;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.hisrc.jsonix.jsonschema.JsonSchemaConstants;
import org.hisrc.jsonix.naming.StandardNaming;
import org.hisrc.jsonix.xml.xsom.ParticleMultiplicityCounter;
import org.hisrc.xml.xsom.XSFunctionApplier;
import org.jvnet.jaxb2_commons.xml.bind.model.MAnyAttributePropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MAnyElementPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MAttributePropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElement;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementRefPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementRefsPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementTypeInfos;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementTypeRef;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementsPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfoVisitor;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MValuePropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MWrappable;
import org.jvnet.jaxb2_commons.xml.bind.model.origin.MElementOrigin;
import org.jvnet.jaxb2_commons.xml.bind.model.origin.MElementTypeRefOrigin;
import org.jvnet.jaxb2_commons.xml.bind.model.origin.MOriginated;

import com.sun.tools.xjc.model.Multiplicity;

public class JsonSchemaPropertyInfoProducerVisitor<T, C extends T>
		implements MPropertyInfoVisitor<T, C, JsonSchemaBuilder> {

	private final XSFunctionApplier<Multiplicity> multiplicityCounter = new XSFunctionApplier<Multiplicity>(
			ParticleMultiplicityCounter.INSTANCE);

	private JsonSchemaMappingCompiler<T, C> mappingCompiler;

	public JsonSchemaPropertyInfoProducerVisitor(JsonSchemaMappingCompiler<T, C> mappingCompiler) {
		Validate.notNull(mappingCompiler);
		this.mappingCompiler = mappingCompiler;
	}

	@Override
	public JsonSchemaBuilder visitElementPropertyInfo(MElementPropertyInfo<T, C> info) {
		final JsonSchemaBuilder schema = new JsonSchemaBuilder();
		addPropertyInfoSchema(info, schema);
		addPropertyInfoTypeSchema(StandardNaming.ELEMENT, schema);
		addElementNameSchema(info.getElementName(), schema);
		addWrappableSchema(info, schema);

		final JsonSchemaBuilder itemTypeSchema = createTypeSchema(info, info.getTypeInfo());
		final JsonSchemaBuilder typeSchema = createPossiblyCollectionTypeSchema(info, itemTypeSchema);

		schema.addAllOf(typeSchema);
		return schema;
	}

	@Override
	public JsonSchemaBuilder visitElementsPropertyInfo(MElementsPropertyInfo<T, C> info) {
		final JsonSchemaBuilder schema = new JsonSchemaBuilder();
		addPropertyInfoSchema(info, schema);
		addPropertyInfoTypeSchema(StandardNaming.ELEMENTS, schema);
		addWrappableSchema(info, schema);

		final JsonSchemaBuilder itemTypeSchema = createElementTypeInfosSchema(info);
		final JsonSchemaBuilder typeSchema = createPossiblyCollectionTypeSchema(info, itemTypeSchema);
		schema.addAllOf(typeSchema);
		return schema;
	}

	@Override
	public JsonSchemaBuilder visitElementRefPropertyInfo(MElementRefPropertyInfo<T, C> info) {

		final JsonSchemaBuilder schema = new JsonSchemaBuilder();
		addPropertyInfoSchema(info, schema);
		addPropertyInfoTypeSchema(StandardNaming.ELEMENT_REF, schema);
		addElementNameSchema(info.getElementName(), schema);
		addWrappableSchema(info, schema);

		final List<JsonSchemaBuilder> itemTypeSchemas = new ArrayList<JsonSchemaBuilder>(3);
		if (info.isMixed()) {
			itemTypeSchemas
					.add(new JsonSchemaBuilder().addRef(XmlSchemaJsonSchemaConstants.STRING_TYPE_INFO_SCHEMA_REF));
		}
		if (info.isDomAllowed()) {
			itemTypeSchemas.add(new JsonSchemaBuilder().addRef(JsonixJsonSchemaConstants.DOM_TYPE_INFO_SCHEMA_REF));
		}
		if (info.isTypedObjectAllowed()) {
			itemTypeSchemas.add(createElementRefSchema(info));
		}
		final JsonSchemaBuilder typeSchema = createPossiblyCollectionTypeSchema(info,
				createPossiblyAnyOfTypeSchema(itemTypeSchemas));
		schema.addAllOf(typeSchema);
		return schema;
	}

	@Override
	public JsonSchemaBuilder visitElementRefsPropertyInfo(MElementRefsPropertyInfo<T, C> info) {
		final JsonSchemaBuilder schema = new JsonSchemaBuilder();
		addPropertyInfoSchema(info, schema);
		addPropertyInfoTypeSchema(StandardNaming.ELEMENT_REFS, schema);
		addWrappableSchema(info, schema);

		final List<JsonSchemaBuilder> itemTypeSchemas = new ArrayList<JsonSchemaBuilder>(
				2 + info.getElementTypeInfos().size());
		if (info.isMixed()) {
			itemTypeSchemas
					.add(new JsonSchemaBuilder().addRef(XmlSchemaJsonSchemaConstants.STRING_TYPE_INFO_SCHEMA_REF));
		}
		if (info.isDomAllowed()) {
			itemTypeSchemas.add(new JsonSchemaBuilder().addRef(JsonixJsonSchemaConstants.DOM_TYPE_INFO_SCHEMA_REF));
		}
		if (info.isTypedObjectAllowed()) {
			itemTypeSchemas.addAll(createElementRefsSchema(info));
		}
		final JsonSchemaBuilder typeSchema = createPossiblyCollectionTypeSchema(info,
				createPossiblyAnyOfTypeSchema(itemTypeSchemas));
		schema.addAllOf(typeSchema);
		return schema;
	}

	@Override
	public JsonSchemaBuilder visitValuePropertyInfo(MValuePropertyInfo<T, C> info) {
		final JsonSchemaBuilder schema = new JsonSchemaBuilder();
		addPropertyInfoSchema(info, schema);
		addPropertyInfoTypeSchema(StandardNaming.VALUE, schema);

		final JsonSchemaBuilder itemTypeSchema = createTypeSchema(info, info.getTypeInfo());
		final JsonSchemaBuilder typeSchema = createPossiblyCollectionTypeSchema(info, itemTypeSchema);
		schema.addAllOf(typeSchema);
		return schema;
	}

	@Override
	public JsonSchemaBuilder visitAnyElementPropertyInfo(MAnyElementPropertyInfo<T, C> info) {

		final JsonSchemaBuilder schema = new JsonSchemaBuilder();
		addPropertyInfoSchema(info, schema);
		addPropertyInfoTypeSchema(StandardNaming.ANY_ELEMENT, schema);

		final List<JsonSchemaBuilder> itemTypeSchemas = new ArrayList<JsonSchemaBuilder>(3);

		if (info.isMixed()) {
			itemTypeSchemas
					.add(new JsonSchemaBuilder().addRef(XmlSchemaJsonSchemaConstants.STRING_TYPE_INFO_SCHEMA_REF));
		}
		if (info.isDomAllowed()) {
			itemTypeSchemas.add(new JsonSchemaBuilder().addRef(JsonixJsonSchemaConstants.DOM_TYPE_INFO_SCHEMA_REF));
		}
		if (info.isTypedObjectAllowed()) {
			final JsonSchemaBuilder anyElementSchema;
            if (mappingCompiler.getMapping().getMappingStyle().equals("simplified")) {
				anyElementSchema = new JsonSchemaBuilder();
			} else {
				anyElementSchema = new JsonSchemaBuilder().addType(JsonSchemaConstants.OBJECT_TYPE)
						.addProperty(JsonixConstants.NAME_PROPERTY_NAME,
								new JsonSchemaBuilder().addRef(XmlSchemaJsonSchemaConstants.QNAME_TYPE_INFO_SCHEMA_REF))
						.addProperty(JsonixConstants.VALUE_PROPERTY_NAME, new JsonSchemaBuilder());
			}
			itemTypeSchemas.add(anyElementSchema);
		}
		final JsonSchemaBuilder typeSchema = createPossiblyCollectionTypeSchema(info,
				createPossiblyAnyOfTypeSchema(itemTypeSchemas));
		schema.addAllOf(typeSchema);
		return schema;
	}

	@Override
	public JsonSchemaBuilder visitAttributePropertyInfo(MAttributePropertyInfo<T, C> info) {
		final JsonSchemaBuilder schema = new JsonSchemaBuilder();
		addPropertyInfoSchema(info, schema);
		addPropertyInfoTypeSchema(StandardNaming.ATTRIBUTE, schema);
		addAttributeNameSchema(info.getAttributeName(), schema);
		final JsonSchemaBuilder itemTypeSchema = createTypeSchema(info, info.getTypeInfo());
		final JsonSchemaBuilder typeSchema = createPossiblyCollectionTypeSchema(info, itemTypeSchema);
		schema.addAllOf(typeSchema);
		return schema;
	}

	@Override
	public JsonSchemaBuilder visitAnyAttributePropertyInfo(MAnyAttributePropertyInfo<T, C> info) {
		final JsonSchemaBuilder schema = new JsonSchemaBuilder();
		addPropertyInfoSchema(info, schema);
		addPropertyInfoTypeSchema(StandardNaming.ANY_ATTRIBUTE, schema);
		final JsonSchemaBuilder typeSchema = new JsonSchemaBuilder().addType(JsonSchemaConstants.OBJECT_TYPE)
				.addAdditionalProperties(new JsonSchemaBuilder().addType(JsonSchemaConstants.STRING_TYPE));
		schema.addAllOf(typeSchema);
		return schema;
	}

	private void addPropertyInfoTypeSchema(String string, JsonSchemaBuilder schema) {
		schema.add(JsonixJsonSchemaConstants.PROPERTY_TYPE_PROPERTY_NAME, string);
	}

	private void addPropertyInfoSchema(MPropertyInfo<T, C> propertyInfo, JsonSchemaBuilder schema) {
		schema.addTitle(propertyInfo.getPrivateName());
	}

	private void addWrappableSchema(MWrappable info, JsonSchemaBuilder schema) {
		final QName wrapperElementName = info.getWrapperElementName();
		if (wrapperElementName != null) {
			addNameSchema(schema, JsonixJsonSchemaConstants.WRAPPER_ELEMENT_NAME_PROPERTY_NAME, wrapperElementName);
		}
	}

	private void addNameSchema(JsonSchemaBuilder schema, final String key, final QName name) {
		schema.add(key, createNameSchema(name));
	}

	private JsonSchemaBuilder createNameSchema(final QName elementName) {
		return new JsonSchemaBuilder()
				.add(JsonixJsonSchemaConstants.LOCAL_PART_PROPERTY_NAME, elementName.getLocalPart())
				.add(JsonixJsonSchemaConstants.NAMESPACE_URI_PROPERTY_NAME, elementName.getNamespaceURI());
	}

	private void addElementNameSchema(QName elementName, JsonSchemaBuilder schema) {
		addNameSchema(schema, JsonixJsonSchemaConstants.ELEMENT_NAME_PROPERTY_NAME, elementName);
	}

	private void addAttributeNameSchema(QName attributeName, JsonSchemaBuilder schema) {
		addNameSchema(schema, JsonixJsonSchemaConstants.ATTRIBUTE_NAME_PROPERTY_NAME, attributeName);
	}

	private JsonSchemaBuilder createElementTypeInfosSchema(
			MElementTypeInfos<T, C, MElementTypeRef<T, C>, MElementTypeRefOrigin> info) {

		final JsonSchemaBuilder schema = new JsonSchemaBuilder();

		if (!info.getElementTypeInfos().isEmpty()) {
			for (MElementTypeRef<T, C> elementTypeInfo : info.getElementTypeInfos()) {
				final JsonSchemaBuilder elementTypeInfoSchema = createElementTypeInfoSchema(elementTypeInfo);
				schema.addAnyOf(elementTypeInfoSchema);
			}
		}
		return schema;
	}

	private JsonSchemaBuilder createElementTypeInfoSchema(MElementTypeRef<T, C> elementTypeInfo) {
		final JsonSchemaBuilder elementTypeInfoSchema = new JsonSchemaBuilder();
		addElementNameSchema(elementTypeInfo.getElementName(), elementTypeInfoSchema);
		elementTypeInfoSchema.addAnyOf(createTypeSchema(elementTypeInfo, elementTypeInfo.getTypeInfo()));
		return elementTypeInfoSchema;
	}

	private List<JsonSchemaBuilder> createElementRefsSchema(
			MElementTypeInfos<T, C, MElement<T, C>, MElementOrigin> info) {

		final List<MElement<T, C>> elementTypeInfos = info.getElementTypeInfos();
		final List<JsonSchemaBuilder> schemas = new ArrayList<JsonSchemaBuilder>(elementTypeInfos.size());

		for (MElement<T, C> elementTypeInfo : elementTypeInfos) {
			final JsonSchemaBuilder elementTypeInfoSchema = createElementRefSchema(elementTypeInfo);
			schemas.add(elementTypeInfoSchema);
		}
		return schemas;

	}

	private <M extends MElementTypeInfo<T, C, O>, O> JsonSchemaBuilder createElementRefSchema(M elementTypeInfo) {
		final JsonSchemaBuilder schema;
		if (mappingCompiler.getMapping().getMappingStyle().equals("simplified")) {
			schema = createTypeSchema(elementTypeInfo, elementTypeInfo.getTypeInfo());
		} else {
			schema = new JsonSchemaBuilder();
			addElementNameSchema(elementTypeInfo.getElementName(), schema);
			schema.addType(JsonSchemaConstants.OBJECT_TYPE);
			schema.addProperty(JsonixConstants.NAME_PROPERTY_NAME,
					new JsonSchemaBuilder().addRef(XmlSchemaJsonSchemaConstants.QNAME_TYPE_INFO_SCHEMA_REF));
			schema.addProperty(JsonixConstants.VALUE_PROPERTY_NAME,
					createTypeSchema(elementTypeInfo, elementTypeInfo.getTypeInfo()));
		}
		return schema;
	}

	private JsonSchemaBuilder createPossiblyAnyOfTypeSchema(final List<JsonSchemaBuilder> schemas) {
		if (schemas.size() == 0) {
			return new JsonSchemaBuilder();
		} else if (schemas.size() == 1) {
			return schemas.get(0);
		} else {
			final JsonSchemaBuilder schema = new JsonSchemaBuilder();
			schema.addAnyOf(schemas);
			return schema;
		}
	}

	private JsonSchemaBuilder createPossiblyCollectionTypeSchema(MPropertyInfo<T, C> propertyInfo,
			final JsonSchemaBuilder itemTypeSchema) {
		final JsonSchemaBuilder typeSchema;
		if (propertyInfo.isCollection()) {
			typeSchema = new JsonSchemaBuilder();
			typeSchema.addType(JsonSchemaConstants.ARRAY_TYPE).addItem(itemTypeSchema);

			final Multiplicity multiplicity = multiplicityCounter.apply(propertyInfo.getOrigin());
			if (multiplicity != null) {
				if (multiplicity.min != null) {
					typeSchema.addMinItems(multiplicity.min);
				}
				if (multiplicity.max != null) {
					typeSchema.addMaxItems(multiplicity.max);
				}
			}

		} else {
			typeSchema = itemTypeSchema;
		}
		return typeSchema;
	}

	private <M extends MOriginated<O>, O> JsonSchemaBuilder createTypeSchema(M originated, MTypeInfo<T, C> typeInfo) {
		return typeInfo.acceptTypeInfoVisitor(new CreateTypeInfoSchema<T, C, MOriginated<O>, O>(mappingCompiler, originated));
	}
}
