package org.hisrc.jsonix.compilation.jsonschema;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.JsonixConstants;
import org.hisrc.jsonix.definition.Mapping;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.hisrc.jsonix.jsonschema.JsonSchemaConstants;
import org.hisrc.jsonix.naming.StandardNaming;
import org.hisrc.jsonix.xml.xsom.ParticleMultiplicityCounter;
import org.hisrc.xml.xsom.XSFunctionApplier;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;

import com.sun.tools.xjc.model.Multiplicity;

public class JsonSchemaClassInfoProducer<T, C extends T> implements
		JsonSchemaTypeInfoProducer<MClassInfo<T, C>, T, C> {

	private final JsonSchemaMappingCompiler<T, C> mappingCompiler;
	private final Mapping<T, C> mapping;
	private final XSFunctionApplier<Multiplicity> multiplicityCounter = new XSFunctionApplier<Multiplicity>(
			ParticleMultiplicityCounter.INSTANCE);

	public JsonSchemaClassInfoProducer(
			JsonSchemaMappingCompiler<T, C> mappingCompiler) {
		Validate.notNull(mappingCompiler);
		this.mappingCompiler = mappingCompiler;
		this.mapping = mappingCompiler.getMapping();
	}

	public JsonSchemaMappingCompiler<T, C> getMappingCompiler() {
		return mappingCompiler;
	}

	@Override
	public JsonSchemaBuilder produce(MClassInfo<T, C> classInfo) {
		final JsonSchemaBuilder classInfoSchema = new JsonSchemaBuilder();
		classInfoSchema.addType(JsonSchemaConstants.OBJECT_TYPE);
		final String localName = classInfo
				.getContainerLocalName(JsonixConstants.DEFAULT_SCOPED_NAME_DELIMITER);
		classInfoSchema.addTitle(localName);
		final MClassTypeInfo<T, C> baseTypeInfo = classInfo.getBaseTypeInfo();
		final JsonSchemaBuilder typeInfoSchema;
		if (baseTypeInfo != null) {
			final JsonSchemaBuilder baseTypeInfoSchema = mappingCompiler
					.createTypeInfoSchemaRef(baseTypeInfo);
			typeInfoSchema = new JsonSchemaBuilder();
			typeInfoSchema.addAllOf(baseTypeInfoSchema);
			typeInfoSchema.addAllOf(classInfoSchema);
		} else {
			typeInfoSchema = classInfoSchema;
		}

		final Map<String, JsonSchemaBuilder> propertyInfoSchemas = compilePropertyInfos(classInfo);
		final List<String> propertiesOrder = new ArrayList<String>(
				propertyInfoSchemas.size());
		propertiesOrder.addAll(propertyInfoSchemas.keySet());
		classInfoSchema.addProperties(propertyInfoSchemas);
		for (MPropertyInfo<T, C> propertyInfo : classInfo.getProperties()) {
			final Multiplicity multiplicity = multiplicityCounter
					.apply(propertyInfo.getOrigin());
			if (multiplicity != null && multiplicity.min != null
					&& multiplicity.min.compareTo(BigInteger.ZERO) > 0) {
				typeInfoSchema.addRequired(propertyInfo.getPrivateName());
			}
		}
		typeInfoSchema.add(JsonixJsonSchemaConstants.TYPE_TYPE_PROPERTY_NAME,
				StandardNaming.CLASS_INFO);
		final QName typeName = classInfo.getTypeName();
		if (typeName != null) {
			typeInfoSchema
					.add(JsonixJsonSchemaConstants.TYPE_NAME_PROPERTY_NAME,
							new JsonSchemaBuilder()
									.add(JsonixJsonSchemaConstants.LOCAL_PART_PROPERTY_NAME,
											typeName.getLocalPart())
									.add(JsonixJsonSchemaConstants.NAMESPACE_URI_PROPERTY_NAME,
											typeName.getNamespaceURI()));
		}
		if (!propertiesOrder.isEmpty()) {
			typeInfoSchema.add("propertiesOrder", propertiesOrder);
		}
		return typeInfoSchema;
	}

	private Map<String, JsonSchemaBuilder> compilePropertyInfos(
			MClassInfo<T, C> classInfo) {
		final Map<String, JsonSchemaBuilder> propertyInfoSchemas = new LinkedHashMap<String, JsonSchemaBuilder>(
				classInfo.getProperties().size());
		for (MPropertyInfo<T, C> propertyInfo : classInfo.getProperties()) {
			if (mapping.getPropertyInfos().contains(propertyInfo)) {
				propertyInfoSchemas
						.put(propertyInfo.getPrivateName(),
								propertyInfo
										.acceptPropertyInfoVisitor(new JsonSchemaPropertyInfoProducerVisitor<T, C>(
												this)));
			}
		}
		return propertyInfoSchemas;
	}
}
