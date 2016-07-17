package org.hisrc.jsonix.compilation.jsonschema.typeinfo;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.compilation.jsonschema.JsonSchemaMappingCompiler;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassRef;

public class ClassRefProducer<T, C extends T> extends PackagedTypeInfoProducer<T, C> {

	private MClassRef<T, C> classRef;

	public ClassRefProducer(MClassRef<T, C> classRef) {
		super(Validate.notNull(classRef));
		this.classRef = classRef;
	}

	@Override
	public JsonSchemaBuilder compile(JsonSchemaMappingCompiler<T, C> mappingCompiler) {
		throw new UnsupportedOperationException();
	}

}
