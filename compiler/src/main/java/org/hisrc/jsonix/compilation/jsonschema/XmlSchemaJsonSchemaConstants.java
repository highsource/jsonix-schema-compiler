package org.hisrc.jsonix.compilation.jsonschema;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.jvnet.jaxb2_commons.xmlschema.XmlSchemaConstants;

public class XmlSchemaJsonSchemaConstants {

	private XmlSchemaJsonSchemaConstants() {
	}

	public static final String SCHEMA_ID = "foo";

	public static final Map<QName, String> TYPE_NAME_SCHEMA_REFS;
	static {
		final Map<QName, String> tnsr = new HashMap<QName, String>();
		for (QName typeName : XmlSchemaConstants.TYPE_NAMES) {
			tnsr.put(typeName, SCHEMA_ID + "/" + JsonSchemaKeywords.definitions + "/"
					+ typeName.getLocalPart());
		}
		TYPE_NAME_SCHEMA_REFS = Collections.unmodifiableMap(tnsr);
	}

	public static final String ID_TYPE_INFO_SCHEMA_REF = SCHEMA_ID + "/"
			+ JsonSchemaKeywords.definitions + "/" + XmlSchemaConstants.ID.getLocalPart();
	public static final String IDREFS_TYPE_INFO_SCHEMA_REF = SCHEMA_ID + "/"
			+ JsonSchemaKeywords.definitions + "/" + XmlSchemaConstants.IDREFS.getLocalPart();
	public static final String IDREF_TYPE_INFO_SCHEMA_REF = SCHEMA_ID + "/"
			+ JsonSchemaKeywords.definitions + "/"  + XmlSchemaConstants.IDREF.getLocalPart();
	public static final String STRING_TYPE_INFO_SCHEMA_REF = SCHEMA_ID + "/"
			+ JsonSchemaKeywords.definitions + "/" + XmlSchemaConstants.STRING.getLocalPart();
	public static final String QNAME_TYPE_INFO_SCHEMA_REF = SCHEMA_ID + "/"
			+ JsonSchemaKeywords.definitions + "/" + XmlSchemaConstants.QNAME.getLocalPart();

}
