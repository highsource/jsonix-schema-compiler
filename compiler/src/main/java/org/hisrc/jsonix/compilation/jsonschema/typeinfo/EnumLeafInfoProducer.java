package org.hisrc.jsonix.compilation.jsonschema.typeinfo;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonValue;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.JsonixConstants;
import org.hisrc.jsonix.compilation.jsonschema.JsonSchemaMappingCompiler;
import org.hisrc.jsonix.compilation.jsonschema.JsonixJsonSchemaConstants;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.hisrc.jsonix.naming.StandardNaming;
import org.jvnet.jaxb2_commons.xml.bind.model.MEnumConstantInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MEnumLeafInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;

import com.sun.xml.xsom.XmlString;

public class EnumLeafInfoProducer<T, C extends T> extends PackagedTypeInfoProducer<T, C> {

	private MEnumLeafInfo<T, C> enumLeafInfo;

	public EnumLeafInfoProducer(MEnumLeafInfo<T, C> enumLeafInfo) {
		super(Validate.notNull(enumLeafInfo));
		this.enumLeafInfo = enumLeafInfo;
	}

	@Override
	public JsonSchemaBuilder compile(JsonSchemaMappingCompiler<T, C> mappingCompiler) {
		final JsonSchemaBuilder enumLeafInfoSchema = new JsonSchemaBuilder();
		final String localName = enumLeafInfo.getContainerLocalName(JsonixConstants.DEFAULT_SCOPED_NAME_DELIMITER);
		enumLeafInfoSchema.addTitle(localName);
		final MTypeInfo<T, C> baseTypeInfo = enumLeafInfo.getBaseTypeInfo();
		final JsonSchemaBuilder typeInfoSchema;
		TypeInfoProducer<T, C> baseTypeInfoProducer = mappingCompiler.getTypeInfoProducer(enumLeafInfo, baseTypeInfo);
		final JsonSchemaBuilder baseTypeInfoSchema = baseTypeInfoProducer.createTypeInfoSchemaRef(mappingCompiler);
		typeInfoSchema = new JsonSchemaBuilder();
		typeInfoSchema.addAllOf(baseTypeInfoSchema);

		final JsonSchemaBuilder enumsTypeInfoSchema = new JsonSchemaBuilder();

		boolean valuesSupported = true;
		for (MEnumConstantInfo<T, C> enumConstantInfo : enumLeafInfo.getConstants()) {
			final JsonValue value = baseTypeInfoProducer.createValue(mappingCompiler,
					enumConstantInfo.getLexicalValue());
			if (value == null) {
				valuesSupported = false;
				break;
			} else {
				enumsTypeInfoSchema.addEnum(value);
			}
		}
		if (valuesSupported) {
			typeInfoSchema.addAllOf(enumsTypeInfoSchema);
		}

		typeInfoSchema.add(JsonixJsonSchemaConstants.TYPE_TYPE_PROPERTY_NAME, StandardNaming.ENUM_INFO);
		final QName typeName = enumLeafInfo.getTypeName();
		if (typeName != null) {
			typeInfoSchema.add(JsonixJsonSchemaConstants.TYPE_NAME_PROPERTY_NAME,
					new JsonSchemaBuilder()
							.add(JsonixJsonSchemaConstants.LOCAL_PART_PROPERTY_NAME, typeName.getLocalPart()).add(
									JsonixJsonSchemaConstants.NAMESPACE_URI_PROPERTY_NAME, typeName.getNamespaceURI()));
		}

		return typeInfoSchema;
	}

	@Override
	public JsonValue createValue(JsonSchemaMappingCompiler<T, C> mappingCompiler, String item) {
		final MTypeInfo<T, C> baseTypeInfo = enumLeafInfo.getBaseTypeInfo();
		final TypeInfoProducer<T, C> baseTypeInfoProducer = mappingCompiler.getTypeInfoProducer(enumLeafInfo,
				baseTypeInfo);
		return baseTypeInfoProducer.createValue(mappingCompiler, item);
	}

	@Override
	public JsonValue createValue(JsonSchemaMappingCompiler<T, C> mappingCompiler, XmlString item) {
		final MTypeInfo<T, C> baseTypeInfo = enumLeafInfo.getBaseTypeInfo();
		final TypeInfoProducer<T, C> baseTypeInfoProducer = mappingCompiler.getTypeInfoProducer(enumLeafInfo,
				baseTypeInfo);
		return baseTypeInfoProducer.createValue(mappingCompiler, item);
	}
}
