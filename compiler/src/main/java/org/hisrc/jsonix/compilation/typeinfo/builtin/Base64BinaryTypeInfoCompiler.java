package org.hisrc.jsonix.compilation.typeinfo.builtin;

import javax.xml.bind.DatatypeConverter;

public class Base64BinaryTypeInfoCompiler<T, C extends T, O> extends BinaryTypeInfoCompiler<T, C, O> {

	public Base64BinaryTypeInfoCompiler() {
		super("Base64Binary");
	}

	@Override
	public byte[] parse(String item) {
		return DatatypeConverter.parseBase64Binary(item);
	}
}
