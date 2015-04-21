package org.hisrc.jsonix.compilation.jsc;

import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;

public interface JsonSchemaTypeInfoCompiler<MTI extends MTypeInfo<T, C>, T, C extends T> {

	public JsonSchemaBuilder compile(MTI typeInfo);
}
