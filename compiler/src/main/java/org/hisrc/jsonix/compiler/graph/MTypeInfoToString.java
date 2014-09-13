package org.hisrc.jsonix.compiler.graph;

import java.text.MessageFormat;

import javax.xml.namespace.QName;

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

public class MTypeInfoToString<T, C> implements MTypeInfoVisitor<T, C, String> {

	public static <X, Y> MTypeInfoVisitor<X, Y, String> instance() {
		@SuppressWarnings("unchecked")
		final MTypeInfoVisitor<X, Y, String> instance = (MTypeInfoVisitor<X, Y, String>) INSTANCE;
		return instance;
	}

	@SuppressWarnings({ "rawtypes" })
	private static MTypeInfoVisitor INSTANCE = new MTypeInfoToString();

	@Override
	public String visitClassInfo(MClassInfo<T, C> info) {
		return MessageFormat.format("Class [{0}]", info.getName());
	}

	@Override
	public String visitClassRef(MClassRef<T, C> info) {
		return MessageFormat.format("Class reference [{0}]", info.getName());
	}

	@Override
	public String visitList(MList<T, C> info) {
		return MessageFormat.format("List [{0}]", info.getItemTypeInfo()
				.acceptTypeInfoVisitor(this));
	}

	@Override
	public String visitID(MID<T, C> info) {
		return MessageFormat.format("ID [{0}]", info.getValueTypeInfo()
				.acceptTypeInfoVisitor(this));
	}

	@Override
	public String visitIDREF(MIDREF<T, C> info) {
		return MessageFormat.format("IDREF [{0}]", info.getValueTypeInfo()
				.acceptTypeInfoVisitor(this));
	}

	@Override
	public String visitIDREFS(MIDREFS<T, C> info) {
		return MessageFormat.format("IDREFS [{0}]", info.getItemTypeInfo()
				.acceptTypeInfoVisitor(this));
	}

	@Override
	public String visitBuiltinLeafInfo(MBuiltinLeafInfo<T, C> info) {
		final QName typeName = info.getTypeName();
		return MessageFormat.format("Builtin [{0}]",
				"http://www.w3.org/2001/XMLSchema".equals(typeName
						.getNamespaceURI()) ? "xs:" + typeName.getLocalPart()
						: typeName.toString());
	}

	@Override
	public String visitEnumLeafInfo(MEnumLeafInfo<T, C> info) {
		return MessageFormat.format("Enum leaf info [{0}]", info.getName());
	}

	@Override
	public String visitWildcardTypeInfo(MWildcardTypeInfo<T, C> info) {
		return "Wildcard";
	}

}
