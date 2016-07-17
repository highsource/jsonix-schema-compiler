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

package org.hisrc.jsonix.compilation.jsonschema.typeinfo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.compilation.jsonschema.JsonixJsonSchemaConstants;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.builtin.Base64BinaryTypeInfoProducer;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.builtin.BooleanTypeInfoProducer;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.builtin.DecimalTypeInfoProducer;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.builtin.DurationTypeInfoProducer;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.builtin.HexBinaryTypeInfoProducer;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.builtin.IntegerTypeInfoProducer;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.builtin.NormalizedStringTypeInfoProducer;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.builtin.QNameTypeInfoProducer;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.builtin.StringTypeInfoProducer;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.builtin.XMLGregorianCalendarTypeInfoProducer;
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

public class CreateTypeInfoProducer<T, C extends T, O> implements MTypeInfoVisitor<T, C, TypeInfoProducer<T, C>> {

	private Map<QName, TypeInfoProducer<T, C>> XSD_TYPE_MAPPING = new HashMap<QName, TypeInfoProducer<T, C>>();
	{
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.ANYTYPE,
				new BuiltinLeafInfoProducer<T, C, O>(XmlSchemaConstants.ANYTYPE));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.ANYSIMPLETYPE,
				new BuiltinLeafInfoProducer<T, C, O>(XmlSchemaConstants.ANYSIMPLETYPE));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.STRING, new StringTypeInfoProducer<T, C, O>(XmlSchemaConstants.STRING));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.NORMALIZEDSTRING,
				new NormalizedStringTypeInfoProducer<T, C, O>(XmlSchemaConstants.NORMALIZEDSTRING));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.TOKEN,
				new NormalizedStringTypeInfoProducer<T, C, O>(XmlSchemaConstants.TOKEN));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.LANGUAGE,
				new NormalizedStringTypeInfoProducer<T, C, O>(XmlSchemaConstants.LANGUAGE));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.NAME,
				new NormalizedStringTypeInfoProducer<T, C, O>(XmlSchemaConstants.NAME));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.NCNAME,
				new NormalizedStringTypeInfoProducer<T, C, O>(XmlSchemaConstants.NCNAME));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.ID, new BuiltinLeafInfoProducer<T, C, O>(XmlSchemaConstants.ID));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.ID, new BuiltinLeafInfoProducer<T, C, O>(XmlSchemaConstants.ID));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.IDREF, new BuiltinLeafInfoProducer<T, C, O>(XmlSchemaConstants.IDREF));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.IDREF, new BuiltinLeafInfoProducer<T, C, O>(XmlSchemaConstants.IDREF));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.IDREFS,
				new BuiltinLeafInfoProducer<T, C, O>(XmlSchemaConstants.IDREFS));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.IDREFS,
				new BuiltinLeafInfoProducer<T, C, O>(XmlSchemaConstants.IDREFS));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.ENTITY,
				new NormalizedStringTypeInfoProducer<T, C, O>(XmlSchemaConstants.ENTITY));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.ENTITIES,
				new NormalizedStringTypeInfoProducer<T, C, O>(XmlSchemaConstants.ENTITIES));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.NMTOKEN,
				new NormalizedStringTypeInfoProducer<T, C, O>(XmlSchemaConstants.NMTOKEN));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.NMTOKENS,
				new NormalizedStringTypeInfoProducer<T, C, O>(XmlSchemaConstants.NMTOKENS));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.BOOLEAN, new BooleanTypeInfoProducer<T, C, O>());
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.BASE64BINARY, new Base64BinaryTypeInfoProducer<T, C, O>());
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.HEXBINARY, new HexBinaryTypeInfoProducer<T, C, O>());
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.FLOAT, new DecimalTypeInfoProducer<T, C, O>(XmlSchemaConstants.FLOAT));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.DECIMAL,
				new DecimalTypeInfoProducer<T, C, O>(XmlSchemaConstants.DECIMAL));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.INTEGER,
				new IntegerTypeInfoProducer<T, C, O>(XmlSchemaConstants.INTEGER));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.NONPOSITIVEINTEGER,
				new IntegerTypeInfoProducer<T, C, O>(XmlSchemaConstants.NONPOSITIVEINTEGER));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.NEGATIVEINTEGER,
				new IntegerTypeInfoProducer<T, C, O>(XmlSchemaConstants.NEGATIVEINTEGER));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.LONG, new IntegerTypeInfoProducer<T, C, O>(XmlSchemaConstants.LONG));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.INT, new IntegerTypeInfoProducer<T, C, O>(XmlSchemaConstants.INT));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.SHORT, new IntegerTypeInfoProducer<T, C, O>(XmlSchemaConstants.SHORT));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.BYTE, new IntegerTypeInfoProducer<T, C, O>(XmlSchemaConstants.BYTE));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.NONNEGATIVEINTEGER,
				new IntegerTypeInfoProducer<T, C, O>(XmlSchemaConstants.NONNEGATIVEINTEGER));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.UNSIGNEDLONG,
				new IntegerTypeInfoProducer<T, C, O>(XmlSchemaConstants.UNSIGNEDLONG));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.UNSIGNEDINT,
				new IntegerTypeInfoProducer<T, C, O>(XmlSchemaConstants.UNSIGNEDINT));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.UNSIGNEDSHORT,
				new IntegerTypeInfoProducer<T, C, O>(XmlSchemaConstants.UNSIGNEDSHORT));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.UNSIGNEDBYTE,
				new IntegerTypeInfoProducer<T, C, O>(XmlSchemaConstants.UNSIGNEDBYTE));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.POSITIVEINTEGER,
				new IntegerTypeInfoProducer<T, C, O>(XmlSchemaConstants.POSITIVEINTEGER));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.DOUBLE,
				new DecimalTypeInfoProducer<T, C, O>(XmlSchemaConstants.DOUBLE));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.ANYURI,
				new NormalizedStringTypeInfoProducer<T, C, O>(XmlSchemaConstants.ANYURI));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.ANYURI,
				new NormalizedStringTypeInfoProducer<T, C, O>(XmlSchemaConstants.ANYURI));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.QNAME, new QNameTypeInfoProducer<T, C, O>());
		// XSD_TYPE_MAPPING.put(XmlSchemaConstants.NOTATION, new
		// BuiltinLeafInfoProducer<T, C, O>("Notation"));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.DURATION, new DurationTypeInfoProducer<T, C, O>());
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.DATETIME,
				new XMLGregorianCalendarTypeInfoProducer<T, C, O>(XmlSchemaConstants.DATETIME));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.TIME,
				new XMLGregorianCalendarTypeInfoProducer<T, C, O>(XmlSchemaConstants.TIME));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.DATE,
				new XMLGregorianCalendarTypeInfoProducer<T, C, O>(XmlSchemaConstants.DATE));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.GYEARMONTH,
				new XMLGregorianCalendarTypeInfoProducer<T, C, O>(XmlSchemaConstants.GYEARMONTH));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.GYEAR,
				new XMLGregorianCalendarTypeInfoProducer<T, C, O>(XmlSchemaConstants.GYEAR));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.GMONTHDAY,
				new XMLGregorianCalendarTypeInfoProducer<T, C, O>(XmlSchemaConstants.GMONTHDAY));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.GDAY,
				new XMLGregorianCalendarTypeInfoProducer<T, C, O>(XmlSchemaConstants.GDAY));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.GMONTH,
				new XMLGregorianCalendarTypeInfoProducer<T, C, O>(XmlSchemaConstants.GMONTH));
		XSD_TYPE_MAPPING.put(XmlSchemaConstants.CALENDAR, new BuiltinLeafInfoProducer<T, C, O>(
				JsonixJsonSchemaConstants.JSONIX_JSON_SCHEMA_ID, XmlSchemaConstants.xsd("calendar")));
		// XSD_TYPE_MAPPING.put(XmlSchemaConstants.CALENDAR, new
		// BuiltinLeafInfoProducer<T, C, O>("String"));
	}

	private final MOriginated<O> originated;

	public CreateTypeInfoProducer(MOriginated<O> originated) {
		Validate.notNull(originated);
		this.originated = originated;
	}

	public TypeInfoProducer<T, C> visitEnumLeafInfo(MEnumLeafInfo<T, C> info) {
		return new EnumLeafInfoProducer<T, C>(info);
	}

	public TypeInfoProducer<T, C> visitClassInfo(MClassInfo<T, C> info) {
		return new ClassInfoProducer<T, C>(info);
	}

	@Override
	public TypeInfoProducer<T, C> visitClassRef(MClassRef<T, C> info) {
		return new ClassRefProducer<T, C>(info);
	}

	public TypeInfoProducer<T, C> visitList(MList<T, C> info) {
		return new ListProducer<T, C>(info, info.getItemTypeInfo().acceptTypeInfoVisitor(this));
	}

	public TypeInfoProducer<T, C> visitBuiltinLeafInfo(MBuiltinLeafInfo<T, C> info) {

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
			final TypeInfoProducer<T, C> typeInfoProducer = XSD_TYPE_MAPPING.get(candidateName);
			if (typeInfoProducer != null) {
				return typeInfoProducer;
			}
		}
		return null;
	}

	public TypeInfoProducer<T, C> visitWildcardTypeInfo(MWildcardTypeInfo<T, C> info) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TypeInfoProducer<T, C> visitID(MID<T, C> info) {
		return new BuiltinLeafInfoProducer<T, C, O>(XmlSchemaConstants.ID);
	}

	@Override
	public TypeInfoProducer<T, C> visitIDREF(MIDREF<T, C> info) {
		return new BuiltinLeafInfoProducer<T, C, O>(XmlSchemaConstants.IDREF);
	}

	@Override
	public TypeInfoProducer<T, C> visitIDREFS(MIDREFS<T, C> info) {
		return new BuiltinLeafInfoProducer<T, C, O>(XmlSchemaConstants.IDREFS);
	}
}