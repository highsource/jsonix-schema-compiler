package org.hisrc.jsonix.compilation.jsonschema;

import org.hisrc.jsonix.jsonschema.JsonSchemaKeywords;

public class JsonixJsonSchemaConstants {

	private JsonixJsonSchemaConstants() {
	}

	public static final String JSONIX_BASE_URI = "http://www.jsonix.org";
	public static final String JSONIX_JSONSCHEMAS_BASE_URI = JSONIX_BASE_URI
			+ "/jsonschemas";

	public static final String JSONIX_JSON_SCHEMA_ID = JSONIX_JSONSCHEMAS_BASE_URI
			+ "/jsonix/Jsonix.jsonschema#";

	public static final String CALENDAR_TYPE_INFO_SCHEMA_REF = JSONIX_JSON_SCHEMA_ID
			+ "/" + JsonSchemaKeywords.definitions + "/calendar";
	public static final String WILDCARD_TYPE_INFO_SCHEMA_REF = JSONIX_JSON_SCHEMA_ID
			+ "/" + JsonSchemaKeywords.definitions + "/wildcard";
	public static final String DOM_TYPE_INFO_SCHEMA_REF = JSONIX_JSON_SCHEMA_ID
			+ "/" + JsonSchemaKeywords.definitions + "/dom";

	public static String TYPE_TYPE_PROPERTY_NAME = "typeType";

	public static String PROPERTIES_ORDER_PROPERTY_NAME = "propertiesOrder";

	public static String PROPERTY_TYPE_PROPERTY_NAME = "propertyType";

	public static String ELEMENT_NAME_PROPERTY_NAME = "elementName";

	public static String WRAPPER_ELEMENT_NAME_PROPERTY_NAME = "wrapperElementName";

	public static String ATTRIBUTE_NAME_PROPERTY_NAME = "attributeName";

	public static String TYPE_NAME_PROPERTY_NAME = "typeName";

	public static String LOCAL_PART_PROPERTY_NAME = "localPart";

	public static String NAMESPACE_URI_PROPERTY_NAME = "namespaceURI";

	public static String SCOPE_PROPERTY_NAME = "scope";
}
