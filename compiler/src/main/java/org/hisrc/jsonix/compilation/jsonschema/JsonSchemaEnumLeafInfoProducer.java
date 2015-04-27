package org.hisrc.jsonix.compilation.jsonschema;

import javax.xml.namespace.QName;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.JsonixConstants;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.hisrc.jsonix.naming.StandardNaming;
import org.jvnet.jaxb2_commons.xml.bind.model.MEnumConstantInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MEnumLeafInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;

public class JsonSchemaEnumLeafInfoProducer<T, C extends T> implements
		JsonSchemaTypeInfoProducer<MEnumLeafInfo<T, C>, T, C> {

	private final JsonSchemaMappingCompiler<T, C> mappingCompiler;

	public JsonSchemaEnumLeafInfoProducer(
			JsonSchemaMappingCompiler<T, C> mappingCompiler) {
		Validate.notNull(mappingCompiler);
		this.mappingCompiler = mappingCompiler;
	}

	public JsonSchemaMappingCompiler<T, C> getMappingCompiler() {
		return mappingCompiler;
	}

	@Override
	public JsonSchemaBuilder produce(MEnumLeafInfo<T, C> enumLeafInfo) {
		final JsonSchemaBuilder enumLeafInfoSchema = new JsonSchemaBuilder();
		final String localName = enumLeafInfo
				.getContainerLocalName(JsonixConstants.DEFAULT_SCOPED_NAME_DELIMITER);
		enumLeafInfoSchema.addTitle(localName);
		final MTypeInfo<T, C> baseTypeInfo = enumLeafInfo.getBaseTypeInfo();
		final JsonSchemaBuilder typeInfoSchema;
		final JsonSchemaBuilder baseTypeInfoSchema = mappingCompiler
				.createTypeInfoSchemaRef(baseTypeInfo);
		typeInfoSchema = new JsonSchemaBuilder();
		typeInfoSchema.addAllOf(baseTypeInfoSchema);

		for (MEnumConstantInfo<T, C> enumConstant : enumLeafInfo.getConstants()) {
			final JsonSchemaBuilder enumConstantSchema = createEnumConstant(
					enumLeafInfo, enumConstant);
			// TODO generate enums
			if (enumConstantSchema != null) {
				typeInfoSchema.addEnum(enumConstantSchema);
			}
		}

		typeInfoSchema.add(JsonixJsonSchemaConstants.TYPE_TYPE_PROPERTY_NAME,
				StandardNaming.ENUM_INFO);
		final QName typeName = enumLeafInfo.getTypeName();
		if (typeName != null) {
			typeInfoSchema
					.add(JsonixJsonSchemaConstants.TYPE_NAME_PROPERTY_NAME,
							new JsonSchemaBuilder()
									.add(JsonixJsonSchemaConstants.LOCAL_PART_PROPERTY_NAME,
											typeName.getLocalPart())
									.add(JsonixJsonSchemaConstants.NAMESPACE_URI_PROPERTY_NAME,
											typeName.getNamespaceURI()));
		}

		return typeInfoSchema;
	}

	private JsonSchemaBuilder createEnumConstant(
			MEnumLeafInfo<T, C> enumLeafInfo,
			MEnumConstantInfo<T, C> enumConstant) {
		return null;
	}
}
