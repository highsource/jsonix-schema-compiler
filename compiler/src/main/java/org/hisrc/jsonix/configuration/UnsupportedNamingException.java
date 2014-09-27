package org.hisrc.jsonix.configuration;

import java.text.MessageFormat;

import org.apache.commons.lang3.Validate;

public class UnsupportedNamingException extends ConfigurationException {

	private static final long serialVersionUID = 3272513965273810862L;
	private final String naming;

	public UnsupportedNamingException(String naming) {
		super(MessageFormat.format("Unsupported naming [{0}].",
				Validate.notNull(naming)));
		this.naming = naming;
	}

	public String getNaming() {
		return naming;
	}

}
