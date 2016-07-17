package org.hisrc.jsonix.compilation.jsonschema.typeinfo.builtin;

import javax.xml.bind.DatatypeConverter;

import org.jvnet.jaxb2_commons.xmlschema.XmlSchemaConstants;

public class Base64BinaryTypeInfoProducer<T, C extends T, O> extends BinaryTypeInfoProducer<T, C, O> {

	public Base64BinaryTypeInfoProducer() {
		super(XmlSchemaConstants.BASE64BINARY);
	}

	@Override
	public byte[] parse(String item) {
		return DatatypeConverter.parseBase64Binary(item);
	}
}
