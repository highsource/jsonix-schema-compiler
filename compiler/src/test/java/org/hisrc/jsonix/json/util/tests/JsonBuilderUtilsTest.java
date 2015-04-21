package org.hisrc.jsonix.json.util.tests;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;

import org.hisrc.jsonix.json.util.JsonBuilderUtils;
import org.junit.Test;

public class JsonBuilderUtilsTest {

	@Test
	public void addsProperty() {
		final JsonProvider provider = JsonProvider.provider();
		final JsonBuilderFactory builderFactory = provider
				.createBuilderFactory(null);
		final JsonObjectBuilder builder = builderFactory.createObjectBuilder();
		JsonBuilderUtils.add(builderFactory, builder, "null", null);
		JsonBuilderUtils.add(builderFactory, builder, "true", true);
		JsonBuilderUtils.add(builderFactory, builder, "string", "string");
		JsonBuilderUtils.add(builderFactory, builder, "char", 'c');
		JsonBuilderUtils.add(builderFactory, builder, "bigInteger",
				BigInteger.TEN);
		JsonBuilderUtils.add(builderFactory, builder, "bigDecimal",
				BigDecimal.valueOf(1111, 2));
		JsonBuilderUtils.add(builderFactory, builder, "float", 22f);
		JsonBuilderUtils.add(builderFactory, builder, "double", 22d);
		JsonBuilderUtils.add(builderFactory, builder, "byte", (byte) 33);
		JsonBuilderUtils.add(builderFactory, builder, "int", (int) 44);
		JsonBuilderUtils.add(builderFactory, builder, "short", (int) 55);
		JsonBuilderUtils.add(builderFactory, builder, "list",
				Arrays.<Object> asList("a", 0xbc, "d"));
		JsonBuilderUtils.add(builderFactory, builder, "array", new Object[] {
				1, "2", 3, false });

		JsonBuilderUtils.add(builderFactory, builder, "map",
				Collections.singletonMap("foo", "bar"));
		// provider.createWriter(System.out).write(builder.build());
	}

	@Test
	public void addsItem() {
		final JsonProvider provider = JsonProvider.provider();
		final JsonBuilderFactory builderFactory = provider
				.createBuilderFactory(null);
		final JsonArrayBuilder builder = builderFactory.createArrayBuilder();
		JsonBuilderUtils.add(builderFactory, builder, null);
		JsonBuilderUtils.add(builderFactory, builder, true);
		JsonBuilderUtils.add(builderFactory, builder, "string");
		JsonBuilderUtils.add(builderFactory, builder, 'c');
		JsonBuilderUtils.add(builderFactory, builder, BigInteger.TEN);
		JsonBuilderUtils.add(builderFactory, builder,
				BigDecimal.valueOf(1111, 2));
		JsonBuilderUtils.add(builderFactory, builder, 22f);
		JsonBuilderUtils.add(builderFactory, builder, 22d);
		JsonBuilderUtils.add(builderFactory, builder, (byte) 33);
		JsonBuilderUtils.add(builderFactory, builder, (int) 44);
		JsonBuilderUtils.add(builderFactory, builder, (int) 55);
		JsonBuilderUtils.add(builderFactory, builder,
				Arrays.<Object> asList("a", 0xbc, "d"));
		JsonBuilderUtils.add(builderFactory, builder, new Object[] { 1, "2", 3,
				false });

		JsonBuilderUtils.add(builderFactory, builder,
				Collections.singletonMap("foo", "bar"));
		// provider.createWriter(System.out).write(builder.build());
	}
}
