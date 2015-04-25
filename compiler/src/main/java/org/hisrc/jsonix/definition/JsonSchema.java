package org.hisrc.jsonix.definition;

import java.text.MessageFormat;

import org.apache.commons.lang3.Validate;

public class JsonSchema {

	private final String directory = "";
	private final String fileName;

	public JsonSchema(final String fileName) {
		Validate.notNull(fileName);
		this.fileName = fileName;
	}

	public String getDirectory() {
		return this.directory;
	}

	public String getFileName() {
		return this.fileName;
	}

	@Override
	public String toString() {
		return MessageFormat.format("JSON Schema [{0}]", this.fileName);
	}

}
