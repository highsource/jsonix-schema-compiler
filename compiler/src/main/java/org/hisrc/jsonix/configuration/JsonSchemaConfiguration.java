package org.hisrc.jsonix.configuration;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.definition.JsonSchema;

@XmlRootElement(name = JsonSchemaConfiguration.LOCAL_ELEMENT_NAME)
@XmlType(propOrder = {})
public class JsonSchemaConfiguration {

	public static final String LOCAL_ELEMENT_NAME = "jsonSchema";
	public static final String STANDARD_FILE_NAME_PATTERN = ModuleConfiguration.MODULE_NAME_PROPERTY +
//			+ "." + MappingConfiguration.MAPPING_NAME_PROPERTY + 
			".jsonschema";

	private String fileName;
	public static final QName JSON_SCHEMA_NAME = new QName(
			ModulesConfiguration.NAMESPACE_URI, LOCAL_ELEMENT_NAME,
			ModulesConfiguration.DEFAULT_PREFIX);

	public JsonSchemaConfiguration() {

	}

	public JsonSchemaConfiguration(String fileName) {
		super();
		this.fileName = fileName;
	}

	@XmlAttribute(name = "fileName")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public JsonSchema build(String moduleName) {
		Validate.notNull(moduleName);
		final String fileNamePattern = this.fileName == null ? STANDARD_FILE_NAME_PATTERN
				: this.fileName;
		final String fileName = fileNamePattern.replace(
				ModuleConfiguration.MODULE_NAME_PROPERTY, moduleName);
		return new JsonSchema(fileName);
	}
}
