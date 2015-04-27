package org.hisrc.jsonix.configuration.exception;

import java.text.MessageFormat;

import org.apache.commons.lang3.Validate;

public class MissingMappingWithIdException extends ConfigurationException {

	private static final long serialVersionUID = 2854565270126284458L;
	private final String mappingId;

	public MissingMappingWithIdException(String mappingId) {
		super(MessageFormat.format(
				"Could not find the referenced mapping with id [{0}].",
				Validate.notNull(mappingId)));
		this.mappingId = mappingId;
	}

	public String getMappingId() {
		return mappingId;
	}

}
