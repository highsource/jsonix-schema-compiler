package org.hisrc.jsonix.compilation.jsonschema.typeinfo.builtin;

import javax.xml.bind.DatatypeConverter;

import org.jvnet.jaxb2_commons.xmlschema.XmlSchemaConstants;

public class HexBinaryTypeInfoProducer<T, C extends T, O> extends BinaryTypeInfoProducer<T, C, O> {

	public HexBinaryTypeInfoProducer() {
		super(XmlSchemaConstants.HEXBINARY);
	}

	@Override
	public byte[] parse(String item) {
		return DatatypeConverter.parseHexBinary(item);
	}
}
