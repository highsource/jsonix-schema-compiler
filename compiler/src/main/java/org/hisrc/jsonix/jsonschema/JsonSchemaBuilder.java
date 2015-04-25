package org.hisrc.jsonix.jsonschema;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.json.util.JsonBuilderUtils;
import org.hisrc.jsonix.json.util.JsonObjectBuildable;

public class JsonSchemaBuilder implements JsonObjectBuildable {

	private String $ref;
	private String id;
	private String $schema;
	private String title;
	private String description;
	private Object _default;
	private String format;

	private BigDecimal multipleOf;
	private BigDecimal maximum;
	private Boolean exclusiveMaximum;
	private BigDecimal minimum;
	private Boolean exclusiveMinimum;
	private BigInteger maxLength;
	private BigInteger minLength;
	private String pattern;

	private Boolean hasAdditionalItems;
	private JsonSchemaBuilder additionalItem;
	private List<JsonSchemaBuilder> items;
	private BigInteger minItems;
	private BigInteger maxItems;
	private Boolean uniqueItems;

	private BigInteger maxProperties;
	private BigInteger minProperties;
	private Set<String> required;
	private JsonSchemaBuilder additionalProperties;
	private Boolean hasAdditionalProperties;
	private Map<String, JsonSchemaBuilder> definitions;
	private Map<String, JsonSchemaBuilder> properties;
	private Map<String, JsonSchemaBuilder> patternProperties;
	private Map<String, Set<String>> propertyDependencies;
	private Map<String, JsonSchemaBuilder> schemaDependencies;

	private Set<Object> _enum;
	private Set<String> types;

	private List<JsonSchemaBuilder> allOf;
	private List<JsonSchemaBuilder> anyOf;
	private List<JsonSchemaBuilder> oneOf;
	private JsonSchemaBuilder not;

	private Map<String, Object> anyProperties;

	public JsonSchemaBuilder addRef(String $ref) {
		Validate.notNull($ref);
		this.$ref = $ref;
		return this;
	}

	public JsonSchemaBuilder addId(String id) {
		this.id = id;
		return this;
	}

	public JsonSchemaBuilder addSchema(String $schema) {
		Validate.notNull($schema);
		this.$schema = $schema;
		return this;
	}

	public JsonSchemaBuilder addTitle(String title) {
		Validate.notNull(title);
		this.title = title;
		return this;
	}

	public JsonSchemaBuilder addDescription(String description) {
		Validate.notNull(description);
		this.description = description;
		return this;
	}

	public JsonSchemaBuilder addDefault(Object _default) {
		Validate.notNull(_default);
		this._default = _default;
		return this;
	}

	public JsonSchemaBuilder addFormat(String format) {
		Validate.notNull(format);
		this.format = format;
		return this;
	}

	public JsonSchemaBuilder addMultipleOf(BigDecimal multipleOf) {
		this.multipleOf = multipleOf;
		return this;
	}

	public JsonSchemaBuilder addMaximum(BigDecimal maximum, Boolean exclusive) {
		this.maximum = maximum;
		this.exclusiveMaximum = exclusive;
		return this;
	}

	public JsonSchemaBuilder addMinimum(BigDecimal minimum, Boolean exclusive) {
		this.minimum = minimum;
		this.exclusiveMinimum = exclusive;
		return this;
	}

	public JsonSchemaBuilder addMaxLength(long maxLength) {
		this.maxLength = BigInteger.valueOf(maxLength);
		return this;
	}

	public JsonSchemaBuilder addMaxLength(BigInteger maxLength) {
		Validate.notNull(maxLength);
		this.maxLength = maxLength;
		return this;
	}

	public JsonSchemaBuilder addMinLength(long minLength) {
		this.minLength = BigInteger.valueOf(minLength);
		return this;
	}

	public JsonSchemaBuilder addMinLength(BigInteger minLength) {
		Validate.notNull(minLength);
		this.minLength = minLength;
		return this;
	}

	public JsonSchemaBuilder addPattern(String pattern) {
		Validate.notNull(pattern);
		this.pattern = pattern;
		return this;
	}

	public JsonSchemaBuilder addAdditionalItems(boolean additionalItems) {
		this.hasAdditionalItems = additionalItems;
		return this;
	}

	public JsonSchemaBuilder addAdditionalItem(JsonSchemaBuilder additionalItem) {
		this.additionalItem = additionalItem;
		return this;
	}

	public JsonSchemaBuilder addItem(JsonSchemaBuilder schema) {
		Validate.notNull(schema);
		if (this.items == null) {
			this.items = new LinkedList<JsonSchemaBuilder>();
		}
		this.items.add(schema);
		return this;
	}

	public JsonSchemaBuilder addMaxItems(BigInteger maxItems) {
		this.maxItems = maxItems;
		return this;
	}

	public JsonSchemaBuilder addMaxItems(long maxItems) {
		return this.addMaxItems(BigInteger.valueOf(maxItems));
	}

	public JsonSchemaBuilder addMinItems(BigInteger minItems) {
		this.minItems = minItems;
		return this;
	}

	public JsonSchemaBuilder addMinItems(long minItems) {
		return this.addMinItems(BigInteger.valueOf(minItems));
	}

	public JsonSchemaBuilder addUniqueItems(Boolean uniqueItems) {
		this.uniqueItems = uniqueItems;
		return this;
	}

	public JsonSchemaBuilder addMaxProperties(BigInteger maxProperties) {
		this.maxProperties = maxProperties;
		return this;
	}

	public JsonSchemaBuilder addMaxProperties(long maxProperties) {
		return this.addMaxProperties(maxProperties);
	}

	public JsonSchemaBuilder addMinProperties(BigInteger minProperties) {
		this.minProperties = minProperties;
		return this;
	}

	public JsonSchemaBuilder addMinProperties(long minProperties) {
		return this.addMinProperties(minProperties);
	}

	public JsonSchemaBuilder addRequired(String name) {
		Validate.notNull(name);
		if (this.required == null) {
			this.required = new LinkedHashSet<String>();
		}
		this.required.add(name);
		return this;
	}

	public JsonSchemaBuilder addAdditionalProperties(
			Boolean hasAdditionalProperties) {
		this.hasAdditionalProperties = hasAdditionalProperties;
		return this;
	}

	public JsonSchemaBuilder addAdditionalProperties(
			JsonSchemaBuilder additionalProperties) {
		Validate.notNull(additionalProperties);
		this.additionalProperties = additionalProperties;
		return this;
	}

	public JsonSchemaBuilder addDefinition(String name, JsonSchemaBuilder schema) {
		Validate.notNull(name);
		Validate.notNull(schema);
		if (this.definitions == null) {
			this.definitions = new LinkedHashMap<String, JsonSchemaBuilder>();
		}
		this.definitions.put(name, schema);
		return this;
	}

	public JsonSchemaBuilder addProperty(String name, JsonSchemaBuilder schema) {
		Validate.notNull(name);
		Validate.notNull(schema);
		if (this.properties == null) {
			this.properties = new LinkedHashMap<String, JsonSchemaBuilder>();
		}
		this.properties.put(name, schema);
		return this;
	}

	public JsonSchemaBuilder addProperties(
			Map<String, JsonSchemaBuilder> properties) {
		Validate.notNull(properties);
		if (this.properties == null) {
			this.properties = new LinkedHashMap<String, JsonSchemaBuilder>();
		}
		this.properties.putAll(properties);
		return this;
	}

	public JsonSchemaBuilder addPatternProperty(String pattern,
			JsonSchemaBuilder schema) {
		Validate.notNull(pattern);
		Validate.notNull(schema);
		if (this.patternProperties == null) {
			this.patternProperties = new LinkedHashMap<String, JsonSchemaBuilder>();
		}
		this.patternProperties.put(pattern, schema);
		return this;
	}

	public JsonSchemaBuilder addPropertyDependency(String name,
			String dependency) {
		Validate.notNull(name);
		Validate.notNull(dependency);
		if (this.propertyDependencies == null) {
			this.propertyDependencies = new LinkedHashMap<String, Set<String>>();
		}
		Set<String> dependencies = this.propertyDependencies.get(name);
		if (dependencies == null) {
			dependencies = new LinkedHashSet<String>();
			this.propertyDependencies.put(name, dependencies);
		}
		dependencies.add(dependency);
		return this;
	}

	public JsonSchemaBuilder addSchemaDependency(String name,
			JsonSchemaBuilder schema) {
		Validate.notNull(name);
		Validate.notNull(schema);
		if (this.schemaDependencies == null) {
			this.schemaDependencies = new LinkedHashMap<String, JsonSchemaBuilder>();
		}
		this.schemaDependencies.put(name, schema);
		return this;
	}

	public JsonSchemaBuilder addEnum(Object enumValue) {
		Validate.notNull(enumValue);
		if (this._enum == null) {
			this._enum = new LinkedHashSet<Object>();
		}
		this._enum.add(enumValue);
		return this;
	}

	public JsonSchemaBuilder addType(String type) {
		Validate.notNull(type);
		if (this.types == null) {
			this.types = new LinkedHashSet<String>();
		}
		this.types.add(type);
		return this;
	}

	public JsonSchemaBuilder addAllOf(JsonSchemaBuilder schema) {
		Validate.notNull(schema);
		if (this.allOf == null) {
			this.allOf = new LinkedList<JsonSchemaBuilder>();
		}
		this.allOf.add(schema);
		return this;
	}

	public void addAllOf(Iterable<JsonSchemaBuilder> schemas) {
		Validate.notNull(schemas);
		for (JsonSchemaBuilder schema : schemas) {
			addAllOf(schema);
		}
	}

	public JsonSchemaBuilder addAnyOf(JsonSchemaBuilder schema) {
		Validate.notNull(schema);
		if (this.anyOf == null) {
			this.anyOf = new LinkedList<JsonSchemaBuilder>();
		}
		this.anyOf.add(schema);
		return this;
	}

	public void addAnyOf(Iterable<JsonSchemaBuilder> schemas) {
		Validate.notNull(schemas);
		for (JsonSchemaBuilder schema : schemas) {
			addAnyOf(schema);
		}
	}

	public JsonSchemaBuilder addOneOf(JsonSchemaBuilder schema) {
		Validate.notNull(schema);
		if (this.oneOf == null) {
			this.oneOf = new LinkedList<JsonSchemaBuilder>();
		}
		this.oneOf.add(schema);
		return this;
	}

	public void addOneOf(Iterable<JsonSchemaBuilder> schemas) {
		Validate.notNull(schemas);
		for (JsonSchemaBuilder schema : schemas) {
			addOneOf(schema);
		}
	}

	public JsonSchemaBuilder addNot(JsonSchemaBuilder schema) {
		Validate.notNull(schema);
		this.not = schema;
		return this;
	}

	public JsonSchemaBuilder add(String name, Object value) {
		if (this.anyProperties == null) {
			this.anyProperties = new LinkedHashMap<String, Object>();
		}
		this.anyProperties.put(name, value);
		return this;
	}

	@Override
	public JsonObject build(JsonBuilderFactory builderFactory) {
		return build(builderFactory, builderFactory.createObjectBuilder())
				.build();
	}

	@Override
	public JsonObjectBuilder build(JsonBuilderFactory builderFactory,
			JsonObjectBuilder builder) {
		Validate.notNull(builderFactory);
		Validate.notNull(builder);

		if ($ref != null) {
			builder.add(JsonSchemaKeywords.$ref, $ref);
		}
		if (id != null) {
			builder.add(JsonSchemaKeywords.id, id);
		}
		if ($schema != null) {
			builder.add(JsonSchemaKeywords.$schema, $schema);
		}
		if (types != null) {
			if (types.size() == 1) {
				builder.add(JsonSchemaKeywords.type, types.iterator().next());
			} else {
				JsonBuilderUtils.add(builderFactory, builder,
						JsonSchemaKeywords.type, types);
			}
		}
		if (title != null) {
			builder.add(JsonSchemaKeywords.title, title);
		}
		if (description != null) {
			builder.add(JsonSchemaKeywords.description, description);
		}
		if (_default != null) {
			JsonBuilderUtils.add(builderFactory, builder,
					JsonSchemaKeywords._default, _default);
		}
		if (format != null) {
			builder.add(JsonSchemaKeywords.format, format);
		}

		if (multipleOf != null) {
			builder.add(JsonSchemaKeywords.multipleOf, multipleOf);
		}

		if (maximum != null) {
			builder.add(JsonSchemaKeywords.maximum, maximum);
		}

		if (exclusiveMaximum != null) {
			builder.add(JsonSchemaKeywords.exclusiveMaximum, exclusiveMaximum);
		}

		if (minimum != null) {
			builder.add(JsonSchemaKeywords.minimum, minimum);
		}

		if (exclusiveMinimum != null) {
			builder.add(JsonSchemaKeywords.exclusiveMinimum, exclusiveMinimum);
		}

		if (maxLength != null) {
			builder.add(JsonSchemaKeywords.maxLength, maxLength);
		}

		if (minLength != null) {
			builder.add(JsonSchemaKeywords.minLength, minLength);
		}

		if (pattern != null) {
			builder.add(JsonSchemaKeywords.pattern, pattern);
		}

		if (additionalItem != null) {
			builder.add(
					JsonSchemaKeywords.additionalItems,
					additionalItem.build(builderFactory,
							builderFactory.createObjectBuilder()));
		}

		if (hasAdditionalItems != null) {
			builder.add(JsonSchemaKeywords.additionalItems,
					hasAdditionalItems.booleanValue());
		}

		if (items != null) {
			if (items.size() == 1) {
				builder.add(
						JsonSchemaKeywords.items,
						items.iterator()
								.next()
								.build(builderFactory,
										builderFactory.createObjectBuilder()));
			} else {
				JsonBuilderUtils.add(builderFactory, builder,
						JsonSchemaKeywords.items, items);
			}
		}

		if (maxItems != null) {
			builder.add(JsonSchemaKeywords.maxItems, maxItems);
		}
		if (minItems != null) {
			builder.add(JsonSchemaKeywords.minItems, minItems);
		}
		if (uniqueItems != null) {
			builder.add(JsonSchemaKeywords.uniqueItems, uniqueItems);
		}
		if (maxProperties != null) {
			builder.add(JsonSchemaKeywords.maxProperties, maxProperties);
		}
		if (minProperties != null) {
			builder.add(JsonSchemaKeywords.minProperties, minProperties);
		}
		if (required != null) {
			JsonBuilderUtils.add(builderFactory, builder,
					JsonSchemaKeywords.required, required);
		}

		if (additionalProperties != null) {
			builder.add(
					JsonSchemaKeywords.additionalProperties,
					additionalProperties.build(builderFactory,
							builderFactory.createObjectBuilder()));
		}

		if (hasAdditionalProperties != null) {
			builder.add(JsonSchemaKeywords.additionalProperties,
					hasAdditionalProperties.booleanValue());
		}

		if (definitions != null) {
			JsonBuilderUtils.add(builderFactory, builder,
					JsonSchemaKeywords.definitions, definitions);
		}
		if (properties != null) {
			JsonBuilderUtils.add(builderFactory, builder,
					JsonSchemaKeywords.properties, properties);
		}
		if (patternProperties != null) {
			JsonBuilderUtils.add(builderFactory, builder,
					JsonSchemaKeywords.patternProperties, patternProperties);
		}

		if (schemaDependencies != null) {
			JsonBuilderUtils.add(builderFactory, builder,
					JsonSchemaKeywords.dependencies, schemaDependencies);
		}

		if (propertyDependencies != null) {
			JsonBuilderUtils.add(builderFactory, builder,
					JsonSchemaKeywords.dependencies, propertyDependencies);
		}

		if (_enum != null) {
			JsonBuilderUtils.add(builderFactory, builder,
					JsonSchemaKeywords._enum, _enum);
		}

		if (allOf != null) {
			JsonBuilderUtils.add(builderFactory, builder,
					JsonSchemaKeywords.allOf, allOf);
		}

		if (anyOf != null) {
			JsonBuilderUtils.add(builderFactory, builder,
					JsonSchemaKeywords.anyOf, anyOf);
		}

		if (oneOf != null) {
			JsonBuilderUtils.add(builderFactory, builder,
					JsonSchemaKeywords.oneOf, oneOf);
		}

		if (not != null) {
			JsonBuilderUtils.add(builderFactory, builder,
					JsonSchemaKeywords.not, not);
		}

		if (anyProperties != null) {
			for (Entry<String, Object> entry : this.anyProperties.entrySet()) {
				JsonBuilderUtils.add(builderFactory, builder, entry.getKey(),
						entry.getValue());
			}
		}

		return builder;
	}

}
