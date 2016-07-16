package org.hisrc.jsonix.compilation.typeinfo.builtin;

import javax.xml.bind.DatatypeConverter;

public class HexBinaryTypeInfoCompiler<T, C extends T, O> extends BinaryTypeInfoCompiler<T, C, O> {

	public HexBinaryTypeInfoCompiler() {
		super("HexBinary");
	}

	@Override
	public byte[] parse(String item) {
		return DatatypeConverter.parseHexBinary(item);
	}
}
