package org.hisrc.jsonix.configuration;

import java.text.MessageFormat;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.definition.Output;
import org.hisrc.jsonix.naming.CompactNaming;
import org.hisrc.jsonix.naming.Naming;
import org.hisrc.jsonix.naming.StandardNaming;

@XmlRootElement(name = OutputConfiguration.LOCAL_ELEMENT_NAME)
@XmlType(propOrder = {})
public class OutputConfiguration {

	public static final String LOCAL_ELEMENT_NAME = "output";
	private static final String COMPACT_FILE_NAME_PATTERN = ModuleConfiguration.MODULE_NAME_PROPERTY
			+ ".compact.js";
	private static final String STANDARD_FILE_NAME_PATTERN = ModuleConfiguration.MODULE_NAME_PROPERTY
			+ ".js";

	private String fileName;
	private String naming;
	public static final QName OUTPUT_NAME = new QName(ModulesConfiguration.NAMESPACE_URI,
	LOCAL_ELEMENT_NAME, ModulesConfiguration.DEFAULT_PREFIX);

	public OutputConfiguration() {

	}

	public OutputConfiguration(String naming) {
		super();
		this.naming = naming;
	}

	@XmlAttribute(name = "fileName")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@XmlAttribute(name = "naming")
	public String getNaming() {
		return naming;
	}

	public void setNaming(String naming) {
		this.naming = naming;
	}

	public Output build(String moduleName) {
		Validate.notNull(moduleName);
		final Naming naming;
		final String defaultFileNamePattern;
		// TODO move this to enum?
		if (null == this.naming
				|| StandardNaming.NAMING_NAME.equals(this.naming)) {
			naming = StandardNaming.INSTANCE;
			defaultFileNamePattern = STANDARD_FILE_NAME_PATTERN;
		} else if (CompactNaming.NAMING_NAME.equals(this.naming)) {
			naming = StandardNaming.INSTANCE;
			defaultFileNamePattern = COMPACT_FILE_NAME_PATTERN;
		} else {
			throw new IllegalArgumentException(MessageFormat.format(
					"Unsupported naming [{0}].", this.naming));
		}
		final String fileNamePattern = this.fileName == null ? defaultFileNamePattern
				: this.fileName;
		final String fileName = fileNamePattern.replace(
				ModuleConfiguration.MODULE_NAME_PROPERTY, moduleName);
		return new Output(fileName, naming);
	}
}
