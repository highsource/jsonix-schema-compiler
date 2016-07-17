package org.hisrc.jsonix.compilation.mapping.typeinfo.builtin;

import javax.xml.bind.DatatypeConverter;

import org.jvnet.jaxb2_commons.xmlschema.XmlSchemaConstants;

public class HexBinaryTypeInfoCompiler<T, C extends T, O> extends BinaryTypeInfoCompiler<T, C, O> {

	public HexBinaryTypeInfoCompiler() {
		super("HexBinary", XmlSchemaConstants.HEXBINARY);
	}

	@Override
	public byte[] parse(String item) {
		return DatatypeConverter.parseHexBinary(item);
	}
}
