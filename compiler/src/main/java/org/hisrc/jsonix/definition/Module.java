package org.hisrc.jsonix.definition;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang3.Validate;

public class Module<T, C extends T> {

	private final String name;
	private final List<Mapping<T, C>> mappings;
	private final List<Output> outputs;
	private final List<JsonSchema> jsonSchemas;

	public Module(String name, List<Mapping<T, C>> mappings,
			List<Output> outputs, List<JsonSchema> jsonSchemas) {
		Validate.notNull(name);
		Validate.noNullElements(mappings);
		Validate.noNullElements(outputs);
		Validate.noNullElements(jsonSchemas);
		this.name = name;
		this.mappings = mappings;
		this.outputs = outputs;
		this.jsonSchemas = jsonSchemas;
	}

	public boolean isEmpty() {
		for (Mapping<T, C> mapping : getMappings()) {
			if (!mapping.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public String getName() {
		return name;
	}

	public List<Mapping<T, C>> getMappings() {
		return mappings;
	}

	public List<Output> getOutputs() {
		return outputs;
	}

	public List<JsonSchema> getJsonSchemas() {
		return jsonSchemas;
	}

	@Override
	public String toString() {
		return MessageFormat.format("Module [{0}].", this.name);
	}
}
