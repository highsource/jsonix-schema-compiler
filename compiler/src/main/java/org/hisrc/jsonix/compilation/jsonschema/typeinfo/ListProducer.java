package org.hisrc.jsonix.compilation.jsonschema.typeinfo;

import javax.json.JsonArrayBuilder;
import javax.json.JsonValue;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.compilation.jsonschema.JsonSchemaMappingCompiler;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.hisrc.jsonix.jsonschema.JsonSchemaConstants;
import org.jvnet.jaxb2_commons.xml.bind.model.MList;

import com.sun.xml.xsom.XmlString;

public class ListProducer<T, C extends T> implements TypeInfoProducer<T, C> {

	private final MList<T, C> typeInfo;
	private final TypeInfoProducer<T, C> itemTypeInfoProducer;

	public ListProducer(MList<T, C> typeInfo, TypeInfoProducer<T, C> itemTypeInfoProducer) {
		Validate.notNull(typeInfo);
		Validate.notNull(itemTypeInfoProducer);
		this.typeInfo = typeInfo;
		this.itemTypeInfoProducer = itemTypeInfoProducer;
	}

	@Override
	public JsonSchemaBuilder compile(JsonSchemaMappingCompiler<T, C> mappingCompiler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public JsonSchemaBuilder createTypeInfoSchemaRef(JsonSchemaMappingCompiler<T, C> mappingCompiler) {
		return new JsonSchemaBuilder().addType(JsonSchemaConstants.ARRAY_TYPE)
				.addItem(itemTypeInfoProducer.createTypeInfoSchemaRef(mappingCompiler));
	}

	@Override
	public JsonValue createValue(JsonSchemaMappingCompiler<T, C> mappingCompiler, XmlString item) {
		final JsonArrayBuilder arrayBuilder = mappingCompiler.getJsonBuilderFactory().createArrayBuilder();
		final String[] values = item.value.split(" ");
		for (String value : values) {
			if (!value.isEmpty()) {
				JsonValue v = itemTypeInfoProducer.createValue(mappingCompiler, new XmlString(value, item.context));
				if (v == null) {
					return null;
				} else {
					arrayBuilder.add(v);
				}
			}
		}
		return arrayBuilder.build();
	}

	@Override
	public JsonValue createValue(JsonSchemaMappingCompiler<T, C> mappingCompiler, String item) {
		final JsonArrayBuilder arrayBuilder = mappingCompiler.getJsonBuilderFactory().createArrayBuilder();
		final String[] values = item.split(" ");
		for (String value : values) {
			if (!value.isEmpty()) {
				JsonValue v = itemTypeInfoProducer.createValue(mappingCompiler, value);
				if (v == null) {
					return null;
				} else {
					arrayBuilder.add(v);
				}
			}
		}
		return arrayBuilder.build();
	}

}
