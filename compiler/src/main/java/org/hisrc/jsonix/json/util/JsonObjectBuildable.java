package org.hisrc.jsonix.json.util;

import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public interface JsonObjectBuildable {

	public JsonObject build(JsonBuilderFactory builderFactory);

	public JsonObjectBuilder build(JsonBuilderFactory builderFactory,
			JsonObjectBuilder builder);
}
