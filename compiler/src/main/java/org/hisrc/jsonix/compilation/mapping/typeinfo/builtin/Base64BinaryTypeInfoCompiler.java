package org.hisrc.jsonix.compilation.mapping.typeinfo.builtin;

import javax.xml.bind.DatatypeConverter;

import org.jvnet.jaxb2_commons.xmlschema.XmlSchemaConstants;

public class Base64BinaryTypeInfoCompiler<T, C extends T, O> extends BinaryTypeInfoCompiler<T, C, O> {

	public Base64BinaryTypeInfoCompiler() {
		super("Base64Binary", XmlSchemaConstants.BASE64BINARY);
	}

	@Override
	public byte[] parse(String item) {
		return DatatypeConverter.parseBase64Binary(item);
	}
}
