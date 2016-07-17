package org.hisrc.jsonix.compilation.jsonschema.typeinfo.builtin;

import java.math.BigDecimal;

import javax.json.JsonArrayBuilder;
import javax.json.JsonValue;
import javax.xml.namespace.QName;

import org.hisrc.jsonix.compilation.jsonschema.JsonSchemaMappingCompiler;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.BuiltinLeafInfoProducer;

public class DecimalTypeInfoProducer<T, C extends T, O> extends BuiltinLeafInfoProducer<T, C, O> {

	public DecimalTypeInfoProducer(QName qualifiedName) {
		super(qualifiedName);
	}
	
	@Override
	public JsonValue createValue(JsonSchemaMappingCompiler<T, C> mappingCompiler, String item) {
		
		JsonArrayBuilder arrayBuilder = mappingCompiler.getJsonBuilderFactory().createArrayBuilder();
		// Hack to make -INF an INF work
		if ("-INF".equals(item)) {
			return arrayBuilder.add("-Infinity").build().get(0);
		} else if ("INF".equals(item)) {
			return arrayBuilder.add("Infinity").build().get(0);
		} else {
			return arrayBuilder.add(new BigDecimal(item)).build().get(0);
		}
	}
}
