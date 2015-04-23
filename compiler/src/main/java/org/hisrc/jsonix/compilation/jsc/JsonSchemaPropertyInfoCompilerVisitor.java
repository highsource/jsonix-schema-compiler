package org.hisrc.jsonix.compilation.jsc;

import javax.xml.namespace.QName;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
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
		// TODO append type
		// options.append(naming.type(),
		// this.codeModel.string(naming.element()));
		addPropertyInfoSchema(info, schema);
		addWrappableSchema(info, schema);
		addElementNameSchema(info.getElementName(), schema);
		final JsonSchemaBuilder itemTypeSchema = createItemTypeSchema(info
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
		// TODO
		// options.append(naming.type(),
		// this.codeModel.string(naming.elements()));
		addPropertyInfoSchema(info, schema);
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
		addWrappableSchema(info, schema);
		addElementNameSchema(info.getElementName(), schema);

		final JsonSchemaBuilder typeSchemaBuilder;
		if (info.isCollection()) {
			typeSchemaBuilder = new JsonSchemaBuilder();
			schema.addType("array").addItem(typeSchemaBuilder);
		} else {
			typeSchemaBuilder = schema;
		}
		final JsonSchemaBuilder itemTypeSchema = new JsonSchemaBuilder();
		// TODO
		// createWildcardSchema(info, schema);
		if (info.isMixed()) {
			itemTypeSchema.addAnyOf(new JsonSchemaBuilder().addType("string"));
		}
		// TODO
		// itemTypeSchema.addAnyOf(createElementTypeInfoSchema(info));
		final JsonSchemaBuilder typeSchema = createPossiblyCollectionTypeSchema(
				info.isCollection(), itemTypeSchema);
		// options.append(naming.type(),
		// this.codeModel.string(naming.elementRef()));
		return schema;
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
		elementTypeInfoSchema.addAnyOf(createItemTypeSchema(elementTypeInfo
				.getTypeInfo()));
		return elementTypeInfoSchema;
	}

	private void addElementNameSchema(QName elementName,
			JsonSchemaBuilder schemaBuilder) {
		// TODO add element name
	}

	private JsonSchemaBuilder createItemTypeSchema(MTypeInfo<T, C> typeInfo) {
		return getClassInfoCompiler().getMappingCompiler()
				.createTypeInfoSchemaRef(typeInfo);
	}

	private JsonSchemaBuilder createPossiblyCollectionTypeSchema(
			boolean collection, final JsonSchemaBuilder itemTypeSchema) {
		final JsonSchemaBuilder typeSchemaBuilder;
		if (collection) {
			typeSchemaBuilder = new JsonSchemaBuilder();
			typeSchemaBuilder.addType("array").addItem(itemTypeSchema);
		} else {
			typeSchemaBuilder = itemTypeSchema;
		}
		return typeSchemaBuilder;
	}

	@Override
	public JsonSchemaBuilder visitAnyElementPropertyInfo(
			MAnyElementPropertyInfo<T, C> info) {
		throw new UnsupportedOperationException();
	}

	@Override
	public JsonSchemaBuilder visitAttributePropertyInfo(
			MAttributePropertyInfo<T, C> info) {
		throw new UnsupportedOperationException();
	}

	@Override
	public JsonSchemaBuilder visitAnyAttributePropertyInfo(
			MAnyAttributePropertyInfo<T, C> info) {
		throw new UnsupportedOperationException();
	}

	@Override
	public JsonSchemaBuilder visitValuePropertyInfo(
			MValuePropertyInfo<T, C> info) {
		throw new UnsupportedOperationException();
	}

	@Override
	public JsonSchemaBuilder visitElementRefsPropertyInfo(
			MElementRefsPropertyInfo<T, C> info) {
		throw new UnsupportedOperationException();
	}
}
