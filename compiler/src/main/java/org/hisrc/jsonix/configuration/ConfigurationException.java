package org.hisrc.jsonix.configuration;

public abstract class ConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 421326587379604013L;

	public ConfigurationException() {
		super();
	}

	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigurationException(String message) {
		super(message);
	}

	public ConfigurationException(Throwable cause) {
		super(cause);
	}

}
