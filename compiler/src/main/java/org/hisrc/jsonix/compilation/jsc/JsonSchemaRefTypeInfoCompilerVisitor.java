package org.hisrc.jsonix.compilation.jsc;

import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.jvnet.jaxb2_commons.xml.bind.model.MBuiltinLeafInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassRef;
import org.jvnet.jaxb2_commons.xml.bind.model.MEnumLeafInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MID;
import org.jvnet.jaxb2_commons.xml.bind.model.MIDREF;
import org.jvnet.jaxb2_commons.xml.bind.model.MIDREFS;
import org.jvnet.jaxb2_commons.xml.bind.model.MList;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfoVisitor;
import org.jvnet.jaxb2_commons.xml.bind.model.MWildcardTypeInfo;

public class JsonSchemaRefTypeInfoCompilerVisitor<T, C extends T> implements
		MTypeInfoVisitor<T, C, JsonSchemaBuilder> {

	@Override
	public JsonSchemaBuilder visitClassInfo(MClassInfo<T, C> info) {
		throw new UnsupportedOperationException();
	}

	@Override
	public JsonSchemaBuilder visitClassRef(MClassRef<T, C> info) {
		throw new UnsupportedOperationException();
	}

	@Override
	public JsonSchemaBuilder visitList(MList<T, C> info) {
		throw new UnsupportedOperationException();
	}

	@Override
	public JsonSchemaBuilder visitID(MID<T, C> info) {
		throw new UnsupportedOperationException();
	}

	@Override
	public JsonSchemaBuilder visitIDREF(MIDREF<T, C> info) {
		throw new UnsupportedOperationException();
	}

	@Override
	public JsonSchemaBuilder visitIDREFS(MIDREFS<T, C> info) {
		throw new UnsupportedOperationException();
	}

	@Override
	public JsonSchemaBuilder visitBuiltinLeafInfo(MBuiltinLeafInfo<T, C> info) {
		throw new UnsupportedOperationException();
	}

	@Override
	public JsonSchemaBuilder visitEnumLeafInfo(MEnumLeafInfo<T, C> info) {
		throw new UnsupportedOperationException();
	}

	@Override
	public JsonSchemaBuilder visitWildcardTypeInfo(MWildcardTypeInfo<T, C> info) {
		throw new UnsupportedOperationException();
	}
}
