package org.hisrc.jsonix.compilation.jsc;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

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

		// TODO move to the builder
		final Map<String, JsonSchemaBuilder> propertyInfoSchemaBuilders = compilePropertyInfos(classInfo);
		for (Entry<String, JsonSchemaBuilder> entry : propertyInfoSchemaBuilders
				.entrySet()) {
			classInfoSchemaBuilder
					.addProperty(entry.getKey(), entry.getValue());
		}

		return typeInfoSchemaBuilder;
	}

	private Map<String, JsonSchemaBuilder> compilePropertyInfos(
			MClassInfo<T, C> classInfo) {
		final Map<String, JsonSchemaBuilder> propertyInfoSchemaBuilders = new LinkedHashMap<String, JsonSchemaBuilder>(
				classInfo.getProperties().size());
		for (MPropertyInfo<T, C> propertyInfo : classInfo.getProperties()) {
			if (mapping.getPropertyInfos().contains(propertyInfo)) {
				propertyInfoSchemaBuilders
						.put(propertyInfo.getPrivateName(),
								propertyInfo
										.acceptPropertyInfoVisitor(new JsonSchemaPropertyInfoCompilerVisitor<T, C>(
												this)));
			}
		}
		return propertyInfoSchemaBuilders;
	}
}
