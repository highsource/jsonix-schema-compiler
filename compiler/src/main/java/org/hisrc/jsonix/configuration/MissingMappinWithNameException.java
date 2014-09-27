package org.hisrc.jsonix.configuration;

import java.text.MessageFormat;

import org.apache.commons.lang3.Validate;

public class MissingMappinWithNameException extends ConfigurationException {

	private static final long serialVersionUID = 2854565270126284458L;
	private final String mappingName;

	public MissingMappinWithNameException(String mappingId) {
		super(MessageFormat.format(
				"Could not find the referenced mapping with name [{0}].",
				Validate.notNull(mappingId)));
		this.mappingName = mappingId;
	}

	public String getMappingName() {
		return mappingName;
	}

}
