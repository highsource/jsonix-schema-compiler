package org.hisrc.jsonix.configuration;

import java.text.MessageFormat;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.configuration.exception.ConfigurationException;

public class MissingPackageException extends ConfigurationException {

	private static final long serialVersionUID = 7839703475748475817L;
	private final String packageName;

	public MissingPackageException(String packageName) {
		super(
				MessageFormat
						.format("Package name [{0}] could not be found.",
								Validate.notNull(packageName)));
		this.packageName = packageName;
	}

	public String getPackageName() {
		return packageName;
	}

}
