/**
 * Jsonix is a JavaScript library which allows you to convert between XML
 * and JavaScript object structures.
 *
 * Copyright (c) 2010 - 2014, Alexey Valikov, Highsource.org
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice, this
 *   list of conditions and the following disclaimer in the documentation and/or
 *   other materials provided with the distribution.
 *
 * * Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.hisrc.jsonix.compilation.mapping.typeinfo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.compilation.mapping.typeinfo.builtin.Base64BinaryTypeInfoCompiler;
import org.hisrc.jsonix.compilation.mapping.typeinfo.builtin.BooleanTypeInfoCompiler;
import org.hisrc.jsonix.compilation.mapping.typeinfo.builtin.DecimalTypeInfoCompiler;
import org.hisrc.jsonix.compilation.mapping.typeinfo.builtin.DurationTypeInfoCompiler;
import org.hisrc.jsonix.compilation.mapping.typeinfo.builtin.HexBinaryTypeInfoCompiler;
import org.hisrc.jsonix.compilation.mapping.typeinfo.builtin.IntegerTypeInfoCompiler;
import org.hisrc.jsonix.compilation.mapping.typeinfo.builtin.NormalizedStringTypeInfoCompiler;
import org.hisrc.jsonix.compilation.mapping.typeinfo.builtin.QNameTypeInfoCompiler;
import org.hisrc.jsonix.compilation.mapping.typeinfo.builtin.StringTypeInfoCompiler;
import org.hisrc.jsonix.compilation.mapping.typeinfo.builtin.XMLGregorianCalendarTypeInfoCompiler;
import org.hisrc.jsonix.xml.xsom.CollectSimpleTypeNamesVisitor;
import org.hisrc.xml.xsom.SchemaComponentAware;
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
import org.jvnet.jaxb2_commons.xml.bind.model.origin.MOriginated;
import org.jvnet.jaxb2_commons.xmlschema.XmlSchemaConstants;

import com.sun.xml.xsom.XSComponent;

public class CreateTypeInfoCompiler<T, C extends T, O> implements MTypeInfoVisitor<T, C, TypeInfoCompiler<T, C>> {

	private static final String IDREFS_TYPE_INFO_NAME = "IDREFS";
	private static final String IDREF_TYPE_INFO_NAME = "IDREF";
	private static final String ID_TYPE_INFO_NAME = "ID";

	private Map<QName, TypeInfoCompiler<T, C>> XSD_TYPE_MAPPING = new HashMap<QName, TypeInfoCompiler<T, C>>();
	{
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.ANYTYPE,
				new BuiltinLeafInfoCompiler<T, C, O>("AnyType", XmlSchemaConstants.ANYTYPE));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.ANYSIMPLETYPE,
				new BuiltinLeafInfoCompiler<T, C, O>("AnySimpleType", XmlSchemaConstants.ANYSIMPLETYPE));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.STRING,
				new StringTypeInfoCompiler<T, C, O>("String", XmlSchemaConstants.STRING));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.NORMALIZEDSTRING,
				new NormalizedStringTypeInfoCompiler<T, C, O>("NormalizedString", XmlSchemaConstants.NORMALIZEDSTRING));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.TOKEN,
				new NormalizedStringTypeInfoCompiler<T, C, O>("Token", XmlSchemaConstants.TOKEN));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.LANGUAGE,
				new NormalizedStringTypeInfoCompiler<T, C, O>("Language", XmlSchemaConstants.LANGUAGE));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.NAME,
				new NormalizedStringTypeInfoCompiler<T, C, O>("Name", XmlSchemaConstants.NAME));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.NCNAME,
				new NormalizedStringTypeInfoCompiler<T, C, O>("NCName", XmlSchemaConstants.NCNAME));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.ID,
				new BuiltinLeafInfoCompiler<T, C, O>("ID", XmlSchemaConstants.ID));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.ID,
				new BuiltinLeafInfoCompiler<T, C, O>("String", XmlSchemaConstants.ID));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.IDREF,
				new BuiltinLeafInfoCompiler<T, C, O>("IDREF", XmlSchemaConstants.IDREF));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.IDREF,
				new BuiltinLeafInfoCompiler<T, C, O>("String", XmlSchemaConstants.IDREF));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.IDREFS,
				new BuiltinLeafInfoCompiler<T, C, O>("IDREFS", XmlSchemaConstants.IDREFS));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.IDREFS,
				new BuiltinLeafInfoCompiler<T, C, O>("Strings", XmlSchemaConstants.IDREFS));
		// XSD_TYPE_MAPPING.put(XmlSchemaConstants.ENTITY, new
		// BuiltinLeafInfoCompiler<T, C, O>("Entity"));
		// XSD_TYPE_MAPPING.put(XmlSchemaConstants.ENTITIES, new
		// BuiltinLeafInfoCompiler<T, C, O>("Entities"));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.NMTOKEN,
				new NormalizedStringTypeInfoCompiler<T, C, O>("NMToken", XmlSchemaConstants.NMTOKEN));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.NMTOKENS,
				new NormalizedStringTypeInfoCompiler<T, C, O>("NMTokens", XmlSchemaConstants.NMTOKENS));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.BOOLEAN, new BooleanTypeInfoCompiler<T, C, O>());
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.BASE64BINARY, new Base64BinaryTypeInfoCompiler<T, C, O>());
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.HEXBINARY, new HexBinaryTypeInfoCompiler<T, C, O>());
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.FLOAT,
				new DecimalTypeInfoCompiler<T, C, O>("Float", XmlSchemaConstants.FLOAT));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.DECIMAL,
				new DecimalTypeInfoCompiler<T, C, O>("Decimal", XmlSchemaConstants.DECIMAL));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.INTEGER,
				new IntegerTypeInfoCompiler<T, C, O>("Integer", XmlSchemaConstants.INTEGER));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.NONPOSITIVEINTEGER,
				new IntegerTypeInfoCompiler<T, C, O>("NonPositiveInteger", XmlSchemaConstants.NONPOSITIVEINTEGER));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.NEGATIVEINTEGER,
				new IntegerTypeInfoCompiler<T, C, O>("NegativeInteger", XmlSchemaConstants.NEGATIVEINTEGER));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.LONG,
				new IntegerTypeInfoCompiler<T, C, O>("Long", XmlSchemaConstants.LONG));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.INT,
				new IntegerTypeInfoCompiler<T, C, O>("Int", XmlSchemaConstants.INT));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.SHORT,
				new IntegerTypeInfoCompiler<T, C, O>("Short", XmlSchemaConstants.SHORT));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.BYTE,
				new IntegerTypeInfoCompiler<T, C, O>("Byte", XmlSchemaConstants.BYTE));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.NONNEGATIVEINTEGER,
				new IntegerTypeInfoCompiler<T, C, O>("NonNegativeInteger", XmlSchemaConstants.NONNEGATIVEINTEGER));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.UNSIGNEDLONG,
				new IntegerTypeInfoCompiler<T, C, O>("UnsignedLong", XmlSchemaConstants.UNSIGNEDLONG));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.UNSIGNEDINT,
				new IntegerTypeInfoCompiler<T, C, O>("UnsignedInt", XmlSchemaConstants.UNSIGNEDINT));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.UNSIGNEDSHORT,
				new IntegerTypeInfoCompiler<T, C, O>("UnsignedShort", XmlSchemaConstants.UNSIGNEDSHORT));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.UNSIGNEDBYTE,
				new IntegerTypeInfoCompiler<T, C, O>("UnsignedByte", XmlSchemaConstants.UNSIGNEDBYTE));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.POSITIVEINTEGER,
				new IntegerTypeInfoCompiler<T, C, O>("PositiveInteger", XmlSchemaConstants.POSITIVEINTEGER));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.DOUBLE,
				new DecimalTypeInfoCompiler<T, C, O>("Double", XmlSchemaConstants.DOUBLE));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.ANYURI,
				new NormalizedStringTypeInfoCompiler<T, C, O>("AnyURI", XmlSchemaConstants.ANYURI));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.ANYURI,
				new NormalizedStringTypeInfoCompiler<T, C, O>("String", XmlSchemaConstants.ANYURI));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.QNAME, new QNameTypeInfoCompiler<T, C, O>());
		// XSD_TYPE_MAPPING.put(XmlSchemaConstants.NOTATION, new
		// BuiltinLeafInfoCompiler<T, C, O>("Notation"));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.DURATION, new DurationTypeInfoCompiler<T, C, O>());
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.DATETIME,
				new XMLGregorianCalendarTypeInfoCompiler<T, C, O>("DateTime", XmlSchemaConstants.DATETIME));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.TIME,
				new XMLGregorianCalendarTypeInfoCompiler<T, C, O>("Time", XmlSchemaConstants.TIME));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.DATE,
				new XMLGregorianCalendarTypeInfoCompiler<T, C, O>("Date", XmlSchemaConstants.DATE));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.GYEARMONTH,
				new XMLGregorianCalendarTypeInfoCompiler<T, C, O>("GYearMonth", XmlSchemaConstants.GYEARMONTH));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.GYEAR,
				new XMLGregorianCalendarTypeInfoCompiler<T, C, O>("GYear", XmlSchemaConstants.GYEAR));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.GMONTHDAY,
				new XMLGregorianCalendarTypeInfoCompiler<T, C, O>("GMonthDay", XmlSchemaConstants.GMONTHDAY));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.GDAY,
				new XMLGregorianCalendarTypeInfoCompiler<T, C, O>("GDay", XmlSchemaConstants.GDAY));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.GMONTH,
				new XMLGregorianCalendarTypeInfoCompiler<T, C, O>("GMonth", XmlSchemaConstants.GMONTH));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.CALENDAR,
				new BuiltinLeafInfoCompiler<T, C, O>("Calendar", XmlSchemaConstants.CALENDAR));
		// XSD_TYPE_MAPPING.put(XmlSchemaConstants.CALENDAR, new
		// BuiltinLeafInfoCompiler<T, C, O>("String"));
	}

	private final MOriginated<O> originated;

	public CreateTypeInfoCompiler(MOriginated<O> originated) {
		Validate.notNull(originated);
		this.originated = originated;
	}

	public TypeInfoCompiler<T, C> visitEnumLeafInfo(MEnumLeafInfo<T, C> info) {
		return new EnumLeafInfoCompiler<T, C>(info);
	}

	public TypeInfoCompiler<T, C> visitClassInfo(MClassInfo<T, C> info) {
		return new ClassInfoCompiler<T, C>(info);
	}

	@Override
	public TypeInfoCompiler<T, C> visitClassRef(MClassRef<T, C> info) {
		return new ClassRefCompiler<T, C>(info);
	}

	public TypeInfoCompiler<T, C> visitList(MList<T, C> info) {
		return new ListCompiler<T, C>(info, info.getItemTypeInfo().acceptTypeInfoVisitor(this));
	}

	public TypeInfoCompiler<T, C> visitBuiltinLeafInfo(MBuiltinLeafInfo<T, C> info) {

		final O origin = this.originated.getOrigin();

		final List<QName> simpleTypeNames = new LinkedList<QName>();
		if (origin instanceof SchemaComponentAware) {
			final XSComponent component = ((SchemaComponentAware) origin).getSchemaComponent();
			if (component != null) {
				final CollectSimpleTypeNamesVisitor visitor = new CollectSimpleTypeNamesVisitor();
				component.visit(visitor);
				simpleTypeNames.addAll(visitor.getTypeNames());
			}
		}

		simpleTypeNames.add(info.getTypeName());

		for (QName candidateName : simpleTypeNames) {
			final TypeInfoCompiler<T, C> typeInfoCompiler = XSD_TYPE_MAPPING.get(candidateName);
			if (typeInfoCompiler != null) {
				return typeInfoCompiler;
			}
		}
		return null;
	}

	public TypeInfoCompiler<T, C> visitWildcardTypeInfo(MWildcardTypeInfo<T, C> info) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TypeInfoCompiler<T, C> visitID(MID<T, C> info) {
		return new BuiltinLeafInfoCompiler<T, C, O>(ID_TYPE_INFO_NAME, XmlSchemaConstants.ID);
	}

	@Override
	public TypeInfoCompiler<T, C> visitIDREF(MIDREF<T, C> info) {
		return new BuiltinLeafInfoCompiler<T, C, O>(IDREF_TYPE_INFO_NAME, XmlSchemaConstants.IDREF);
	}

	@Override
	public TypeInfoCompiler<T, C> visitIDREFS(MIDREFS<T, C> info) {
		return new BuiltinLeafInfoCompiler<T, C, O>(IDREFS_TYPE_INFO_NAME, XmlSchemaConstants.IDREFS);
	}
}