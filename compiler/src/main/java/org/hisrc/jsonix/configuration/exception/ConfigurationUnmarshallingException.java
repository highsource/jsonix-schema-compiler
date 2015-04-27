package org.hisrc.jsonix.configuration.exception;

import java.text.MessageFormat;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.xml.sax.LocatorUtils;
import org.xml.sax.Locator;

public class ConfigurationUnmarshallingException extends ConfigurationException {

	private static final long serialVersionUID = -924228342493741985L;
	private final String description;
	private final Locator locator;

	public ConfigurationUnmarshallingException(String description) {
		super(MessageFormat.format("Could not unmarshal the {0}.",
				Validate.notNull(description)));
		this.description = description;
		this.locator = null;
	}

	public ConfigurationUnmarshallingException(String description,
			Locator locator) {
		super(MessageFormat.format(
				"Could not unmarshal the {0} located at [{1}].",
				Validate.notNull(description),
				LocatorUtils.toString(Validate.notNull(locator))));
		this.description = description;
		this.locator = locator;
	}

	public String getDescription() {
		return description;
	}

	public Locator getLocator() {
		return locator;
	}

}
