package org.hisrc.jsonix.compilation.jsonschema.typeinfo;

import javax.json.JsonValue;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.compilation.jsonschema.JsonSchemaMappingCompiler;
import org.hisrc.jsonix.compilation.jsonschema.XmlSchemaJsonSchemaConstants;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.hisrc.jsonix.jsonschema.JsonSchemaKeywords;

import com.sun.xml.xsom.XmlString;

public class BuiltinLeafInfoProducer<T, C extends T, O> implements TypeInfoProducer<T, C> {

	private final String jsonSchemaId;
	private final QName qualifiedName;

	public BuiltinLeafInfoProducer(QName qualifiedName) {
		this(XmlSchemaJsonSchemaConstants.SCHEMA_ID, Validate.notNull(qualifiedName));
	}

	public BuiltinLeafInfoProducer(String jsonShemaId, QName qualifiedName) {
		Validate.notNull(qualifiedName);
		Validate.notNull(jsonShemaId);
		this.jsonSchemaId = jsonShemaId;
		this.qualifiedName = qualifiedName;
	}

	@Override
	public JsonSchemaBuilder compile(JsonSchemaMappingCompiler<T, C> mappingCompiler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public JsonSchemaBuilder createTypeInfoSchemaRef(JsonSchemaMappingCompiler<T, C> mappingCompiler) {
		return new JsonSchemaBuilder()
				.addRef(this.jsonSchemaId + "/" + JsonSchemaKeywords.definitions + "/" + qualifiedName.getLocalPart());
	}

	@Override
	public JsonValue createValue(JsonSchemaMappingCompiler<T, C> mappingCompiler, XmlString item) {
		return createValue(mappingCompiler, item.value);
	}
	
	@Override
	public JsonValue createValue(JsonSchemaMappingCompiler<T, C> mappingCompiler, String item) {
		return null;
	}

}
