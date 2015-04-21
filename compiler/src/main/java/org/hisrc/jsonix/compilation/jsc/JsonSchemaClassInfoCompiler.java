package org.hisrc.jsonix.compilation.jsc;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.definition.Mapping;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;

public class JsonSchemaClassInfoCompiler<T, C extends T> implements
		JsonSchemaTypeInfoCompiler<MClassInfo<T, C>, T, C> {

	// TODO move to constants
	public static final String DEFAULT_SCOPED_NAME_DELIMITER = ".";

	private final JsonSchemaMappingCompiler<T, C> mappingCompiler;
	private final Mapping<T, C> mapping;

	public JsonSchemaClassInfoCompiler(
			JsonSchemaMappingCompiler<T, C> mappingCompiler) {
		Validate.notNull(mappingCompiler);
		this.mappingCompiler = mappingCompiler;
		this.mapping = mappingCompiler.getMapping();
	}

	public JsonSchemaMappingCompiler<T, C> getMappingCompiler() {
		return mappingCompiler;
	}

	@Override
	public JsonSchemaBuilder compile(MClassInfo<T, C> classInfo) {
		final JsonSchemaBuilder classInfoSchemaBuilder = new JsonSchemaBuilder();
		classInfoSchemaBuilder.addType("object");
		final String localName = classInfo
				.getContainerLocalName(DEFAULT_SCOPED_NAME_DELIMITER);
		classInfoSchemaBuilder.addTitle(localName);
		// TODO addId ?
		// ...
		// TODO add type name ?
		final String targetNamespace = mapping.getTargetNamespaceURI();
		final QName defaultTypeName = new QName(targetNamespace, localName);
		final QName typeName = classInfo.getTypeName();

		final MClassTypeInfo<T, C> baseTypeInfo = classInfo.getBaseTypeInfo();
		final JsonSchemaBuilder typeInfoSchemaBuilder;
		if (baseTypeInfo != null) {
			final JsonSchemaBuilder baseTypeInfoSchemaBuilder = mappingCompiler
					.createTypeInfoSchemaRef(baseTypeInfo);
			typeInfoSchemaBuilder = new JsonSchemaBuilder();
			typeInfoSchemaBuilder.addAllOf(baseTypeInfoSchemaBuilder);
			typeInfoSchemaBuilder.addAllOf(classInfoSchemaBuilder);
		} else {
			typeInfoSchemaBuilder = classInfoSchemaBuilder;
		}

		final List<JsonSchemaBuilder> propertyInfoSchemaBuilders = compilePropertyInfos(classInfo);
		for (JsonSchemaBuilder propertyInfoSchemaBuilder : propertyInfoSchemaBuilders) {
			classInfoSchemaBuilder.addItem(propertyInfoSchemaBuilder);
		}

		return typeInfoSchemaBuilder;
	}

	private List<JsonSchemaBuilder> compilePropertyInfos(
			MClassInfo<T, C> classInfo) {
		final List<JsonSchemaBuilder> propertyInfoSchemaBuilders = new ArrayList<JsonSchemaBuilder>(
				classInfo.getProperties().size());
		for (MPropertyInfo<T, C> propertyInfo : classInfo.getProperties()) {
			if (mapping.getPropertyInfos().contains(propertyInfo)) {
				propertyInfoSchemaBuilders
						.add(propertyInfo
								.acceptPropertyInfoVisitor(new JsonSchemaPropertyInfoCompilerVisitor<T, C>(
										this)));
			}
		}
		return propertyInfoSchemaBuilders;
	}
}
