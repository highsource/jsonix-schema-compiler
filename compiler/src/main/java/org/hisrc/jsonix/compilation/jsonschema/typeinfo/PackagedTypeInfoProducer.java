package org.hisrc.jsonix.compilation.jsonschema.typeinfo;

import javax.json.JsonValue;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.compilation.jsonschema.JsonSchemaMappingCompiler;
import org.hisrc.jsonix.compilation.mapping.MappingCompiler;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.hisrc.jsonix.jsonschema.JsonSchemaKeywords;
import org.jvnet.jaxb2_commons.xml.bind.model.MPackagedTypeInfo;

import com.sun.xml.xsom.XmlString;

public abstract class PackagedTypeInfoProducer<T, C extends T> implements TypeInfoProducer<T, C> {

	private final MPackagedTypeInfo<T, C> typeInfo;

	public PackagedTypeInfoProducer(MPackagedTypeInfo<T, C> typeInfo) {
		Validate.notNull(typeInfo);
		this.typeInfo = typeInfo;
	}

	@Override
	public JsonSchemaBuilder createTypeInfoSchemaRef(JsonSchemaMappingCompiler<T, C> mappingCompiler) {
		final String typeInfoSchemaId = mappingCompiler.getModules()
				.getSchemaId(typeInfo.getPackageInfo().getPackageName());
		final String schemaId = mappingCompiler.getMapping().getSchemaId();

		final String mappingSchemaId = typeInfoSchemaId.equals(schemaId) ? "#" : typeInfoSchemaId;
		final String typeInfoRef = mappingSchemaId + "/" + JsonSchemaKeywords.definitions + "/"
				+ typeInfo.getContainerLocalName(MappingCompiler.DEFAULT_SCOPED_NAME_DELIMITER);
		return new JsonSchemaBuilder().addRef(typeInfoRef);
	}
	
	@Override
	public JsonValue createValue(JsonSchemaMappingCompiler<T, C> mappingCompiler, XmlString item) {
		return createValue(mappingCompiler, item.value);
	}
	
	@Override
	public JsonValue createValue(JsonSchemaMappingCompiler<T, C> mappingCompiler, String item) {
		throw new UnsupportedOperationException();
	}
}
