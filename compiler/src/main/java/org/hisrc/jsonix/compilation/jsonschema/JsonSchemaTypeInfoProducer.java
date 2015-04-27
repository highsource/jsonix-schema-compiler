package org.hisrc.jsonix.compilation.jsonschema;

import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;

public interface JsonSchemaTypeInfoProducer<MTI extends MTypeInfo<T, C>, T, C extends T> {

	public JsonSchemaBuilder produce(MTI typeInfo);
}
