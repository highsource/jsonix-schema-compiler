package org.hisrc.jsonix.compilation.jsonschema.typeinfo.builtin;

import javax.json.JsonArrayBuilder;
import javax.json.JsonValue;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.namespace.QName;

import org.hisrc.jsonix.compilation.jsonschema.JsonSchemaMappingCompiler;

public class NormalizedStringTypeInfoProducer<T, C extends T, O> extends StringTypeInfoProducer<T, C, O> {

	private final NormalizedStringAdapter normalizedStringAdapter = new NormalizedStringAdapter();

	public NormalizedStringTypeInfoProducer(QName qualifiedName) {
		super(qualifiedName);
	}
	
	@Override
	public JsonValue createValue(JsonSchemaMappingCompiler<T, C> mappingCompiler, String item) {
		final JsonArrayBuilder arrayBuilder = mappingCompiler.getJsonBuilderFactory().createArrayBuilder();
		return arrayBuilder.add(normalizedStringAdapter.unmarshal(item)).build().get(0);
	}
}
