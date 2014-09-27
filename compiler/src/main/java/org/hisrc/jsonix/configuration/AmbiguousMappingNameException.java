package org.hisrc.jsonix.configuration;

import java.text.MessageFormat;

import org.apache.commons.lang3.Validate;

public class AmbiguousMappingNameException extends ConfigurationException {

	private static final long serialVersionUID = -7107929604268428687L;
	private final String name;

	public AmbiguousMappingNameException(String name) {
		super(
				MessageFormat
						.format("There is more than one mapping with the name [{0}], please set and use id attributes for mapping references.",
								Validate.notNull(name)));
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
