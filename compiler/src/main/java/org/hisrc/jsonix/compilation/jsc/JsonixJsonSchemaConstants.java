package org.hisrc.jsonix.compilation.jsc;


public class JsonixJsonSchemaConstants {

	private JsonixJsonSchemaConstants() {
	}

	public static final String SCHEMA_ID = "todo:_jsonix_schema_id";

	public static final String WILDCARD_TYPE_INFO_SCHEMA_REF = SCHEMA_ID + "/"
			+ JsonSchemaKeywords.definitions + "/" + "wildcard";
}
