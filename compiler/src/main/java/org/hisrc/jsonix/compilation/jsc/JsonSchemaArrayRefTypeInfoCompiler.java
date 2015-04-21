package org.hisrc.jsonix.compilation.jsc;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;

public class JsonSchemaArrayRefTypeInfoCompiler<T, C extends T> implements
		JsonSchemaTypeInfoCompiler<MTypeInfo<T, C>, T, C> {

	private final String $ref;

	public JsonSchemaArrayRefTypeInfoCompiler(String $ref) {
		Validate.notNull($ref);
		this.$ref = $ref;
	}

	@Override
	public JsonSchemaBuilder compile(MTypeInfo<T, C> typeInfo) {
		return new JsonSchemaBuilder().addType("array").addItem(
				new JsonSchemaBuilder().addRef($ref));
	}

}
