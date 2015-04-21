package org.hisrc.jsonix.compilation.jsc;

import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;

public class JsonSchemaRefTypeInfoCompiler<T, C extends T> implements
		JsonSchemaTypeInfoCompiler<MTypeInfo<T, C>, T, C> {

	private final String $ref;

	public JsonSchemaRefTypeInfoCompiler(String $ref) {
		this.$ref = $ref;
	}

	@Override
	public JsonSchemaBuilder compile(MTypeInfo<T, C> typeInfo) {
		return new JsonSchemaBuilder().addRef($ref);
	}

}
