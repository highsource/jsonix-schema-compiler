package org.hisrc.jsonix.compilation.jsonschema.typeinfo;

import javax.json.JsonValue;

import org.hisrc.jsonix.compilation.jsonschema.JsonSchemaMappingCompiler;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;

import com.sun.xml.xsom.XmlString;

public interface TypeInfoProducer<T, C extends T> {

	public JsonSchemaBuilder compile(JsonSchemaMappingCompiler<T, C> mappingCompiler);

	public JsonSchemaBuilder createTypeInfoSchemaRef(JsonSchemaMappingCompiler<T, C> mappingCompiler);

	public JsonValue createValue(JsonSchemaMappingCompiler<T, C> mappingCompiler, XmlString item);

	public JsonValue createValue(JsonSchemaMappingCompiler<T, C> mappingCompiler, String item);

}
