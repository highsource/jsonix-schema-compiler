package org.hisrc.jsonix.configuration;

import java.text.MessageFormat;
import java.util.Arrays;

import org.apache.commons.lang3.Validate;

public class AmbiguousPackageSchemaIdException extends ConfigurationException {

	private static final long serialVersionUID = 277619834810758946L;
	private final String packageName;
	private final String[] schemaIds;

	public AmbiguousPackageSchemaIdException(String packageName,
			String... schemaIds) {
		super(MessageFormat.format(
				"Package [{0}] is mapped using different schema ids [{1}].",
				Validate.notNull(packageName),
				Arrays.asList(Validate.noNullElements(schemaIds)).toString()));
		this.packageName = packageName;
		this.schemaIds = schemaIds;
	}

	public String getPackageName() {
		return packageName;
	}

	public String[] getSchemaIds() {
		return schemaIds;
	}
}
