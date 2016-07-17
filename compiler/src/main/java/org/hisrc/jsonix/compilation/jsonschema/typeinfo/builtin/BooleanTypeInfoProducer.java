package org.hisrc.jsonix.compilation.jsonschema.typeinfo.builtin;

import javax.json.JsonValue;

import org.hisrc.jsonix.compilation.jsonschema.JsonSchemaMappingCompiler;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.BuiltinLeafInfoProducer;
import org.jvnet.jaxb2_commons.xmlschema.XmlSchemaConstants;

public class BooleanTypeInfoProducer<T, C extends T, O> extends BuiltinLeafInfoProducer<T, C, O> {

	public BooleanTypeInfoProducer() {
		super(XmlSchemaConstants.BOOLEAN);
	}

	@Override
	public JsonValue createValue(JsonSchemaMappingCompiler<T, C> mappingCompiler, String item) {
		return Boolean.valueOf(item) ? JsonValue.TRUE : JsonValue.FALSE;
	}
}
