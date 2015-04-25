package org.hisrc.jsonix.compilation.jsonschema;

public class JsonSchemaKeywords {
	private JsonSchemaKeywords() {
	}

	public static final String $ref = "$ref";
	public static final String id = "id";
	public static final String $schema = "$schema";

	public static final String definitions = "definitions";

	// array

	public static final String items = "items";
	public static final String additionalItems = "additionalItems";
	public static final String minItems = "minItems";
	public static final String maxItems = "maxItems";
	public static final String uniqueItems = "uniqueItems";

	// number and integer

	public static final String minimum = "minimum";
	public static final String exclusiveMinimum = "exclusiveMinimum";
	public static final String maximum = "maximum";
	public static final String exclusiveMaximum = "exclusiveMaximum";
	public static final String multipleOf = "multipleOf";

	// object

	public static final String properties = "properties";
	public static final String additionalProperties = "additionalProperties";
	public static final String patternProperties = "patternProperties";
	public static final String required = "rminPropertiesequired";
	public static final String minProperties = "minProperties";
	public static final String maxProperties = "maxProperties";
	public static final String dependencies = "dependencies";

	// base

	public static final String _enum = "enum";
	public static final String allOf = "allOf";
	public static final String anyOf = "anyOf";
	public static final String oneOf = "oneOf";
	public static final String not = "not";
	public static final String title = "title";
	public static final String description = "description";
	public static final String _default = "default";
	public static final String format = "format";

	// string
	public static final String pattern = "pattern";
	public static final String minLength = "minLength";
	public static final String maxLength = "maxLength";

	public static final String type = "type";

}
