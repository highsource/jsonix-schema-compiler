package org.hisrc.jsonix.json.util;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Map.Entry;

import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import org.apache.commons.lang3.Validate;
import org.hisrc.json.JsonObjectBuildable;

public class JsonBuilderUtils {
	private JsonBuilderUtils() {
	}

	public static JsonArrayBuilder add(JsonBuilderFactory builderFactory,
			JsonArrayBuilder builder, Object value) {
		Validate.notNull(builderFactory);
		Validate.notNull(builder);
		if (value == null) {
			return builder.addNull();
		} else if (value instanceof JsonValue) {
			return builder.add(((JsonValue) value));
		} else if (value instanceof Boolean) {
			return builder.add(((Boolean) value).booleanValue());
		} else if (value instanceof CharSequence) {
			return builder.add(((CharSequence) value).toString());
		} else if (value instanceof Character) {
			return builder.add(((Character) value).toString());
		} else if (value instanceof Number) {
			if (value instanceof BigInteger) {
				return builder.add((BigInteger) value);
			} else if (value instanceof BigDecimal) {
				return builder.add((BigDecimal) value);
			} else if (value instanceof Double || value instanceof Float) {
				return builder.add(((Number) value).doubleValue());
			} else if (value instanceof Byte || value instanceof Short
					|| value instanceof Integer) {
				return builder.add(((Number) value).intValue());
			} else if (value instanceof Long) {
				return builder.add(((Number) value).longValue());
			} else {
				return builder.add(((Number) value).doubleValue());
			}
		} else if (value instanceof JsonObjectBuildable) {
			final JsonObjectBuilder objectBuilder = builderFactory
					.createObjectBuilder();
			return builder.add(((JsonObjectBuildable) value).build(
					builderFactory, objectBuilder));
		} else if (value instanceof Iterable) {
			final JsonArrayBuilder arrayBuilder = builderFactory
					.createArrayBuilder();
			for (Object item : (Iterable<?>) value) {
				JsonBuilderUtils.add(builderFactory, arrayBuilder, item);
			}
			return builder.add(arrayBuilder);
		} else if (value.getClass().isArray()) {
			final JsonArrayBuilder arrayBuilder = builderFactory
					.createArrayBuilder();
			int length = Array.getLength(value);
			for (int index = 0; index < length; index++) {
				Object item = Array.get(value, index);
				JsonBuilderUtils.add(builderFactory, arrayBuilder, item);
			}
			return builder.add(arrayBuilder);
		} else if (value instanceof Map) {
			final JsonObjectBuilder objectBuilder = builderFactory
					.createObjectBuilder();
			for (Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
				JsonBuilderUtils.add(builderFactory, objectBuilder, entry
						.getKey().toString(), entry.getValue());
			}
			return builder.add(objectBuilder);
		} else {
			return builder.add(value.toString());
		}
	}

	public static JsonObjectBuilder add(JsonBuilderFactory builderFactory,
			JsonObjectBuilder builder, String name, Object value) {
		Validate.notNull(builderFactory);
		Validate.notNull(builder);
		Validate.notNull(name);
		if (value == null) {
			return builder.addNull(name);
		} else if (value instanceof JsonValue) {
			return builder.add(name, ((JsonValue) value));
		} else if (value instanceof Boolean) {
			return builder.add(name, ((Boolean) value).booleanValue());
		} else if (value instanceof CharSequence) {
			return builder.add(name, ((CharSequence) value).toString());
		} else if (value instanceof Character) {
			return builder.add(name, ((Character) value).toString());
		} else if (value instanceof Number) {
			if (value instanceof BigInteger) {
				return builder.add(name, (BigInteger) value);
			} else if (value instanceof BigDecimal) {
				return builder.add(name, (BigDecimal) value);
			} else if (value instanceof Double || value instanceof Float) {
				return builder.add(name, ((Number) value).doubleValue());
			} else if (value instanceof Byte || value instanceof Short
					|| value instanceof Integer) {
				return builder.add(name, ((Number) value).intValue());
			} else if (value instanceof Long) {
				return builder.add(name, ((Number) value).longValue());
			} else {
				return builder.add(name, ((Number) value).doubleValue());
			}
		} else if (value instanceof JsonObjectBuildable) {
			final JsonObjectBuilder objectBuilder = builderFactory
					.createObjectBuilder();
			return builder.add(name, ((JsonObjectBuildable) value).build(
					builderFactory, objectBuilder));
		} else if (value instanceof Iterable) {
			final JsonArrayBuilder arrayBuilder = builderFactory
					.createArrayBuilder();
			for (Object item : (Iterable<?>) value) {
				JsonBuilderUtils.add(builderFactory, arrayBuilder, item);
			}
			return builder.add(name, arrayBuilder);
		} else if (value.getClass().isArray()) {
			final JsonArrayBuilder arrayBuilder = builderFactory
					.createArrayBuilder();
			int length = Array.getLength(value);
			for (int index = 0; index < length; index++) {
				Object item = Array.get(value, index);
				JsonBuilderUtils.add(builderFactory, arrayBuilder, item);
			}
			return builder.add(name, arrayBuilder);
		} else if (value instanceof Map) {
			final JsonObjectBuilder objectBuilder = builderFactory
					.createObjectBuilder();
			for (Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
				JsonBuilderUtils.add(builderFactory, objectBuilder, entry
						.getKey().toString(), entry.getValue());
			}
			return builder.add(name, objectBuilder);
		} else {
			return builder.add(name, value.toString());
		}
	}
}
