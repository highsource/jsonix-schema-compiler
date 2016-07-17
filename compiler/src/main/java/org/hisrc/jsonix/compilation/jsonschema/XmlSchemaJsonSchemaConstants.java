package org.hisrc.jsonix.compilation.jsonschema;

import org.hisrc.jsonix.jsonschema.JsonSchemaKeywords;
import org.jvnet.jaxb2_commons.xmlschema.XmlSchemaConstants;

public class XmlSchemaJsonSchemaConstants {

	private XmlSchemaJsonSchemaConstants() {
	}

	public static final String SCHEMA_ID = JsonixJsonSchemaConstants.JSONIX_JSONSCHEMAS_BASE_URI
			+ "/w3c/2001/XMLSchema.jsonschema#";

	public static final String STRING_TYPE_INFO_SCHEMA_REF = SCHEMA_ID + "/" + JsonSchemaKeywords.definitions + "/"
			+ XmlSchemaConstants.STRING.getLocalPart();
	public static final String QNAME_TYPE_INFO_SCHEMA_REF = SCHEMA_ID + "/" + JsonSchemaKeywords.definitions + "/"
			+ XmlSchemaConstants.QNAME.getLocalPart();

}
