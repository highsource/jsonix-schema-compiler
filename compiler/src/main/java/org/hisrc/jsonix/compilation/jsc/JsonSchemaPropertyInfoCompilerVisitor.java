package org.hisrc.jsonix.compilation.jsc;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.jvnet.jaxb2_commons.xml.bind.model.MAnyAttributePropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MAnyElementPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MAttributePropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementRefPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementRefsPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementsPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfoVisitor;
import org.jvnet.jaxb2_commons.xml.bind.model.MValuePropertyInfo;

public class JsonSchemaPropertyInfoCompilerVisitor<T, C extends T> implements
		MPropertyInfoVisitor<T, C, JsonSchemaBuilder> {

	private final JsonSchemaClassInfoCompiler<T, C> classInfoCompiler;

	public JsonSchemaPropertyInfoCompilerVisitor(
			JsonSchemaClassInfoCompiler<T, C> classInfoCompiler) {
		Validate.notNull(classInfoCompiler);
		this.classInfoCompiler = classInfoCompiler;
	}

	public JsonSchemaClassInfoCompiler<T, C> getClassInfoCompiler() {
		return classInfoCompiler;
	}

	@Override
	public JsonSchemaBuilder visitElementPropertyInfo(
			MElementPropertyInfo<T, C> info) {
		throw new UnsupportedOperationException();
	}

	@Override
	public JsonSchemaBuilder visitElementsPropertyInfo(
			MElementsPropertyInfo<T, C> info) {
		throw new UnsupportedOperationException();
	}

	@Override
	public JsonSchemaBuilder visitAnyElementPropertyInfo(
			MAnyElementPropertyInfo<T, C> info) {
		throw new UnsupportedOperationException();
	}

	@Override
	public JsonSchemaBuilder visitAttributePropertyInfo(
			MAttributePropertyInfo<T, C> info) {
		throw new UnsupportedOperationException();
	}

	@Override
	public JsonSchemaBuilder visitAnyAttributePropertyInfo(
			MAnyAttributePropertyInfo<T, C> info) {
		throw new UnsupportedOperationException();
	}

	@Override
	public JsonSchemaBuilder visitValuePropertyInfo(
			MValuePropertyInfo<T, C> info) {
		throw new UnsupportedOperationException();
	}

	@Override
	public JsonSchemaBuilder visitElementRefPropertyInfo(
			MElementRefPropertyInfo<T, C> info) {
		throw new UnsupportedOperationException();
	}

	@Override
	public JsonSchemaBuilder visitElementRefsPropertyInfo(
			MElementRefsPropertyInfo<T, C> info) {
		throw new UnsupportedOperationException();
	}
}
