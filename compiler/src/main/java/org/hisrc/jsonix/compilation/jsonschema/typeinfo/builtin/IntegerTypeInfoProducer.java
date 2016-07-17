package org.hisrc.jsonix.compilation.jsonschema.typeinfo.builtin;

import java.math.BigInteger;

import javax.json.JsonArrayBuilder;
import javax.json.JsonValue;
import javax.xml.namespace.QName;

import org.hisrc.jsonix.compilation.jsonschema.JsonSchemaMappingCompiler;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.BuiltinLeafInfoProducer;

public class IntegerTypeInfoProducer<T, C extends T, O> extends BuiltinLeafInfoProducer<T, C, O> {

	public IntegerTypeInfoProducer(QName qualifiedName) {
		super(qualifiedName);
	}
	
	@Override
	public JsonValue createValue(JsonSchemaMappingCompiler<T, C> mappingCompiler, String item) {
		final JsonArrayBuilder arrayBuilder = mappingCompiler.getJsonBuilderFactory().createArrayBuilder();
		return arrayBuilder.add(new BigInteger(item)).build().get(0);
	}
}
