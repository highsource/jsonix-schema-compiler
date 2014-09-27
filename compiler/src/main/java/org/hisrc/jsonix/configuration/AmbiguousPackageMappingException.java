package org.hisrc.jsonix.configuration;

import java.text.MessageFormat;

import org.apache.commons.lang3.Validate;

public class AmbiguousPackageMappingException extends ConfigurationException {

	private static final long serialVersionUID = 277619834810758946L;
	private final String packageName;
	private final String[] mappingNames;

	public AmbiguousPackageMappingException(String packageName,
			String... mappingNames) {
		super(MessageFormat.format(
				"Package [{0}] is mapped using different mapping names [{1}].",
				Validate.notNull(packageName),
				Validate.noNullElements(mappingNames).toString()));
		this.packageName = packageName;
		this.mappingNames = mappingNames;
	}

	public String getPackageName() {
		return packageName;
	}

	public String[] getMappingNames() {
		return mappingNames;
	}

}
