package org.hisrc.jsonix.compilation.jsc;

public class JsonixJsonSchemaConstants {

	private JsonixJsonSchemaConstants() {
	}

	public static final String SCHEMA_ID = "todo:_jsonix_schema_id";

	public static final String WILDCARD_TYPE_INFO_SCHEMA_REF = SCHEMA_ID + "/"
			+ JsonSchemaKeywords.definitions + "/" + "wildcard";
	public static final String DOM_TYPE_INFO_SCHEMA_REF = SCHEMA_ID + "/"
			+ JsonSchemaKeywords.definitions + "/" + "dome";

	public static String TYPE_TYPE_PROPERTY_NAME = "typeType";

	public static String PROPERTY_TYPE_PROPERTY_NAME = "propertyType";

	public static String ELEMENT_NAME_PROPERTY_NAME = "elementName";

	public static String WRAPPER_ELEMENT_NAME_PROPERTY_NAME = "wrapperElementName";

	public static String ATTRIBUTE_NAME_PROPERTY_NAME = "attributeName";

	public static String TYPE_NAME_PROPERTY_NAME = "typeName";

	public static String LOCAL_PART_PROPERTY_NAME = "localPart";

	public static String NAMESPACE_URI_PROPERTY_NAME = "namespaceURI";
}
