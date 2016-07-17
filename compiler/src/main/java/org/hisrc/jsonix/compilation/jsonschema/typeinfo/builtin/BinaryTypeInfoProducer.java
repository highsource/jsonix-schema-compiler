package org.hisrc.jsonix.compilation.jsonschema.typeinfo.builtin;

import javax.json.JsonArrayBuilder;
import javax.json.JsonValue;
import javax.xml.namespace.QName;

import org.hisrc.jsonix.compilation.jsonschema.JsonSchemaMappingCompiler;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.BuiltinLeafInfoProducer;

public abstract class BinaryTypeInfoProducer<T, C extends T, O> extends BuiltinLeafInfoProducer<T, C, O> {

	public BinaryTypeInfoProducer(QName qualifiedName) {
		super(qualifiedName);
	}

	@Override
	public JsonValue createValue(JsonSchemaMappingCompiler<T, C> mappingCompiler, String item) {
		final JsonArrayBuilder arrayBuilder = mappingCompiler.getJsonBuilderFactory().createArrayBuilder();
		final byte[] value = parse(item);
		for (final byte b : value) {
			final int v = b >= 0 ? b : b + 256;
			arrayBuilder.add(v);
		}
		return arrayBuilder.build();
	}

	public abstract byte[] parse(String item);
}
