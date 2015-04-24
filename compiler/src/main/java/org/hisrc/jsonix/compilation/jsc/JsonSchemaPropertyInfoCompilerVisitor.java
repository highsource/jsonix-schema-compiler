package org.hisrc.jsonix.compilation.jsc;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.JsonixConstants;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.hisrc.jsonix.jsonschema.JsonSchemaConstants;
import org.hisrc.jsonix.naming.StandardNaming;
import org.jvnet.jaxb2_commons.xml.bind.model.MAnyAttributePropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MAnyElementPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MAttributePropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementRefPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementRefsPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementTypeInfos;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementsPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfoVisitor;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MValuePropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MWrappable;

public class JsonSchemaPropertyInfoCompilerVisitor<T, C extends T> implements
		MPropertyInfoVisitor<T, C, JsonSchemaBuilder> {

	private final JsonSchemaClassInfoCompiler<T, C> classInfoCompiler;

	public JsonSchemaPropertyInfoCompilerVisitor(
			JsonSchemaClassInfoCompiler<T, C> classInfoCompiler) {
		Validate.notNull(classInfoCompiler);
		this.classInfoCompiler = classInfoCompiler;
	}

	public JsonSchemaClassInfoCompiler<T, C> getClassInfoCompiler() {
		return classInfoCompiler;
	}

	@Override
	public JsonSchemaBuilder visitElementPropertyInfo(
			MElementPropertyInfo<T, C> info) {
		final JsonSchemaBuilder schema = new JsonSchemaBuilder();
		addPropertyInfoSchema(info, schema);
		addPropertyInfoTypeSchema(StandardNaming.ELEMENT, schema);
		addWrappableSchema(info, schema);
		addElementNameSchema(info.getElementName(), schema);
		final JsonSchemaBuilder itemTypeSchema = createTypeSchema(info
				.getTypeInfo());
		final JsonSchemaBuilder typeSchema = createPossiblyCollectionTypeSchema(
				info.isCollection(), itemTypeSchema);
		schema.addAnyOf(typeSchema);
		return schema;
	}

	@Override
	public JsonSchemaBuilder visitElementsPropertyInfo(
			MElementsPropertyInfo<T, C> info) {
		final JsonSchemaBuilder schema = new JsonSchemaBuilder();
		addPropertyInfoSchema(info, schema);
		addPropertyInfoTypeSchema(StandardNaming.ELEMENTS, schema);
		addWrappableSchema(info, schema);
		final JsonSchemaBuilder itemTypeSchema = createElementTypeInfosSchema(info);
		final JsonSchemaBuilder typeSchema = createPossiblyCollectionTypeSchema(
				info.isCollection(), itemTypeSchema);
		schema.addAnyOf(typeSchema);
		return schema;
	}

	@Override
	public JsonSchemaBuilder visitElementRefPropertyInfo(
			MElementRefPropertyInfo<T, C> info) {
		final JsonSchemaBuilder schema = new JsonSchemaBuilder();
		addPropertyInfoSchema(info, schema);
		addPropertyInfoTypeSchema(StandardNaming.ELEMENT_REF, schema);
		addWrappableSchema(info, schema);
		addElementNameSchema(info.getElementName(), schema);

		final JsonSchemaBuilder itemTypeSchema = new JsonSchemaBuilder();
		if (info.isMixed()) {
			itemTypeSchema
					.addAnyOf(new JsonSchemaBuilder()
							.addType(XmlSchemaJsonSchemaConstants.STRING_TYPE_INFO_SCHEMA_REF));
		}
		if (info.isDomAllowed()) {
			itemTypeSchema
					.addAnyOf(new JsonSchemaBuilder()
							.addType(JsonixJsonSchemaConstants.DOM_TYPE_INFO_SCHEMA_REF));
		}
		if (info.isTypedObjectAllowed()) {
			itemTypeSchema.addAnyOf(createElementRefSchema(info));
		}
		final JsonSchemaBuilder typeSchema = createPossiblyCollectionTypeSchema(
				info.isCollection(), itemTypeSchema);
		schema.addAnyOf(typeSchema);
		return schema;
	}

	@Override
	public JsonSchemaBuilder visitElementRefsPropertyInfo(
			MElementRefsPropertyInfo<T, C> info) {
		final JsonSchemaBuilder schema = new JsonSchemaBuilder();
		addPropertyInfoSchema(info, schema);
		addPropertyInfoTypeSchema(StandardNaming.ELEMENT_REFS, schema);
		addWrappableSchema(info, schema);

		final JsonSchemaBuilder itemTypeSchema = new JsonSchemaBuilder();
		if (info.isMixed()) {
			itemTypeSchema
					.addAnyOf(new JsonSchemaBuilder()
							.addType(XmlSchemaJsonSchemaConstants.STRING_TYPE_INFO_SCHEMA_REF));
		}
		if (info.isDomAllowed()) {
			itemTypeSchema
					.addAnyOf(new JsonSchemaBuilder()
							.addType(JsonixJsonSchemaConstants.DOM_TYPE_INFO_SCHEMA_REF));
		}
		if (info.isTypedObjectAllowed()) {
			itemTypeSchema.addAnyOf(createElementRefsSchema(info));
		}
		final JsonSchemaBuilder typeSchema = createPossiblyCollectionTypeSchema(
				info.isCollection(), itemTypeSchema);
		schema.addAnyOf(typeSchema);
		return schema;
	}

	@Override
	public JsonSchemaBuilder visitValuePropertyInfo(
			MValuePropertyInfo<T, C> info) {
		final JsonSchemaBuilder schema = new JsonSchemaBuilder();
		addPropertyInfoSchema(info, schema);
		addPropertyInfoTypeSchema(StandardNaming.VALUE, schema);
		final JsonSchemaBuilder itemTypeSchema = createTypeSchema(info
				.getTypeInfo());
		final JsonSchemaBuilder typeSchema = createPossiblyCollectionTypeSchema(
				info.isCollection(), itemTypeSchema);
		schema.addAnyOf(typeSchema);
		return schema;
	}

	@Override
	public JsonSchemaBuilder visitAnyElementPropertyInfo(
			MAnyElementPropertyInfo<T, C> info) {
		final JsonSchemaBuilder schema = new JsonSchemaBuilder();
		addPropertyInfoSchema(info, schema);
		addPropertyInfoTypeSchema(StandardNaming.ANY_ELEMENT, schema);

		final JsonSchemaBuilder itemTypeSchema = new JsonSchemaBuilder();
		if (info.isMixed()) {
			itemTypeSchema
					.addAnyOf(new JsonSchemaBuilder()
							.addType(XmlSchemaJsonSchemaConstants.STRING_TYPE_INFO_SCHEMA_REF));
		}
		if (info.isDomAllowed()) {
			itemTypeSchema
					.addAnyOf(new JsonSchemaBuilder()
							.addType(JsonixJsonSchemaConstants.DOM_TYPE_INFO_SCHEMA_REF));
		}
		if (info.isTypedObjectAllowed()) {
			final JsonSchemaBuilder anyElementSchema = new JsonSchemaBuilder()
					.addType(JsonSchemaConstants.OBJECT_TYPE)
					.addProperty(
							JsonixConstants.NAME_PROPERTY_NAME,
							new JsonSchemaBuilder()
									.addRef(XmlSchemaJsonSchemaConstants.QNAME_TYPE_INFO_SCHEMA_REF))
					.addProperty(JsonixConstants.VALUE_PROPERTY_NAME,
							new JsonSchemaBuilder());
			itemTypeSchema.addAnyOf(anyElementSchema);
		}
		final JsonSchemaBuilder typeSchema = createPossiblyCollectionTypeSchema(
				info.isCollection(), itemTypeSchema);
		schema.addAnyOf(typeSchema);
		return schema;
	}

	@Override
	public JsonSchemaBuilder visitAttributePropertyInfo(
			MAttributePropertyInfo<T, C> info) {
		final JsonSchemaBuilder schema = new JsonSchemaBuilder();
		addPropertyInfoSchema(info, schema);
		addPropertyInfoTypeSchema(StandardNaming.ATTRIBUTE, schema);
		addAttributeNameSchema(info.getAttributeName(), schema);
		final JsonSchemaBuilder itemTypeSchema = createTypeSchema(info
				.getTypeInfo());
		final JsonSchemaBuilder typeSchema = createPossiblyCollectionTypeSchema(
				info.isCollection(), itemTypeSchema);
		schema.addAnyOf(typeSchema);
		return schema;
	}

	@Override
	public JsonSchemaBuilder visitAnyAttributePropertyInfo(
			MAnyAttributePropertyInfo<T, C> info) {
		final JsonSchemaBuilder schema = new JsonSchemaBuilder();
		addPropertyInfoSchema(info, schema);
		addPropertyInfoTypeSchema(StandardNaming.ANY_ATTRIBUTE, schema);
		final JsonSchemaBuilder typeSchema = new JsonSchemaBuilder().addType(
				JsonSchemaConstants.OBJECT_TYPE).addAdditionalProperties(
				new JsonSchemaBuilder()
						.addType(JsonSchemaConstants.STRING_TYPE));
		schema.addAnyOf(typeSchema);
		return schema;
	}

	private void addPropertyInfoTypeSchema(String string,
			JsonSchemaBuilder schema) {
		// TODO

	}

	private void addPropertyInfoSchema(MPropertyInfo<T, C> propertyInfo,
			JsonSchemaBuilder schemaBuilder) {
		schemaBuilder.addTitle(propertyInfo.getPrivateName());
		// TODO
		// if (propertyInfo.isCollection()) {
		// options.append(naming.collection(), this.codeModel._boolean(true));
		// }
	}

	private void addWrappableSchema(MWrappable info,
			JsonSchemaBuilder schemaBuilder) {
		final QName wrapperElementName = info.getWrapperElementName();
		if (wrapperElementName != null) {
			// TODO add wrapper element name
			// options.append(naming.wrapperElementName(), mappingCompiler
			// .createElementNameExpression(wrapperElementName));
		}
	}

	private void addElementNameSchema(QName elementName,
			JsonSchemaBuilder schemaBuilder) {
		// TODO add element name
	}

	private void addAttributeNameSchema(QName attributeName,
			JsonSchemaBuilder schema) {
		// TODO Auto-generated method stub

	}

	private JsonSchemaBuilder createElementTypeInfosSchema(
			MElementTypeInfos<T, C> info) {

		final JsonSchemaBuilder schema = new JsonSchemaBuilder();

		if (!info.getElementTypeInfos().isEmpty()) {
			for (MElementTypeInfo<T, C> elementTypeInfo : info
					.getElementTypeInfos()) {
				final JsonSchemaBuilder elementTypeInfoSchema = createElementTypeInfoSchema(elementTypeInfo);
				schema.addAnyOf(elementTypeInfoSchema);
			}
		}
		return schema;
	}

	private JsonSchemaBuilder createElementTypeInfoSchema(
			MElementTypeInfo<T, C> elementTypeInfo) {
		final JsonSchemaBuilder elementTypeInfoSchema = new JsonSchemaBuilder();
		addElementNameSchema(elementTypeInfo.getElementName(),
				elementTypeInfoSchema);
		elementTypeInfoSchema.addAnyOf(createTypeSchema(elementTypeInfo
				.getTypeInfo()));
		return elementTypeInfoSchema;
	}

	private JsonSchemaBuilder createElementRefsSchema(
			MElementTypeInfos<T, C> info) {

		final JsonSchemaBuilder schema = new JsonSchemaBuilder();

		final List<MElementTypeInfo<T, C>> elementTypeInfos = info
				.getElementTypeInfos();
		if (!elementTypeInfos.isEmpty()) {
			for (MElementTypeInfo<T, C> elementTypeInfo : elementTypeInfos) {
				final JsonSchemaBuilder elementTypeInfoSchema = createElementRefSchema(elementTypeInfo);
				schema.addAnyOf(elementTypeInfoSchema);
			}
		}
		return schema;

	}

	private JsonSchemaBuilder createElementRefSchema(
			MElementTypeInfo<T, C> elementTypeInfo) {
		final JsonSchemaBuilder schema = new JsonSchemaBuilder();
		addElementNameSchema(elementTypeInfo.getElementName(), schema);
		// TODO constant
		schema.addType(JsonSchemaConstants.OBJECT_TYPE);
		schema.addProperty(
				JsonixConstants.NAME_PROPERTY_NAME,
				new JsonSchemaBuilder()
						.addRef(XmlSchemaJsonSchemaConstants.QNAME_TYPE_INFO_SCHEMA_REF));
		schema.addProperty(JsonixConstants.VALUE_PROPERTY_NAME,
				createTypeSchema(elementTypeInfo.getTypeInfo()));
		return schema;
	}

	private JsonSchemaBuilder createPossiblyCollectionTypeSchema(
			boolean collection, final JsonSchemaBuilder itemTypeSchema) {
		final JsonSchemaBuilder typeSchemaBuilder;
		if (collection) {
			typeSchemaBuilder = new JsonSchemaBuilder();
			typeSchemaBuilder.addType(JsonSchemaConstants.ARRAY_TYPE).addItem(
					itemTypeSchema);
		} else {
			typeSchemaBuilder = itemTypeSchema;
		}
		return typeSchemaBuilder;
	}

	private JsonSchemaBuilder createTypeSchema(MTypeInfo<T, C> typeInfo) {
		return getClassInfoCompiler().getMappingCompiler()
				.createTypeInfoSchemaRef(typeInfo);
	}
}
