package org.hisrc.jsonix.configuration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import org.hisrc.jsonix.analysis.ModelInfoGraphAnalyzer;
import org.hisrc.jsonix.definition.JsonSchema;
import org.hisrc.jsonix.definition.Mapping;
import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Output;
import org.jvnet.jaxb2_commons.xml.bind.model.MModelInfo;

@XmlRootElement(name = ModuleConfiguration.LOCAL_ELEMENT_NAME)
@XmlType(propOrder = {})
public class ModuleConfiguration {

	public static final String LOCAL_ELEMENT_NAME = "module";

	// private Log log = new SystemLog();

	public static final String MODULE_NAME_PROPERTY = "${module.name}";
	public static final String MODULE_SCHEMA_ID_PROPERTY = "${module.schemaId}";
	public static final String MODULE_NAME_SEPARATOR = "_";

	private String name;
	private String schemaId = MODULE_NAME_PROPERTY + ".jsonschema#";
	private List<MappingConfiguration> mappingConfigurations = new LinkedList<MappingConfiguration>();
	private List<OutputConfiguration> outputConfigurations = new LinkedList<OutputConfiguration>();
	private List<JsonSchemaConfiguration> jsonSchemaConfigurations = new LinkedList<JsonSchemaConfiguration>();

	public static final QName MODULE_NAME = new QName(
			ModulesConfiguration.NAMESPACE_URI, LOCAL_ELEMENT_NAME,
			ModulesConfiguration.DEFAULT_PREFIX);

	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name = "schemaId")
	public String getSchemaId() {
		return schemaId;
	}

	public void setSchemaId(String schemaId) {
		this.schemaId = schemaId;
	}

	@XmlElement(name = "mapping")
	public List<MappingConfiguration> getMappingConfigurations() {
		return mappingConfigurations;
	}

	public void setMappingConfigurations(
			List<MappingConfiguration> mappingConfigurations) {
		this.mappingConfigurations = mappingConfigurations;
	}

	@XmlElement(name = OutputConfiguration.LOCAL_ELEMENT_NAME)
	public List<OutputConfiguration> getOutputConfigurations() {
		return outputConfigurations;
	}

	public void setOutputConfigurations(
			List<OutputConfiguration> outputConfigurations) {
		this.outputConfigurations = outputConfigurations;
	}

	@XmlElement(name = JsonSchemaConfiguration.LOCAL_ELEMENT_NAME)
	public List<JsonSchemaConfiguration> getJsonSchemaConfigurations() {
		return jsonSchemaConfigurations;
	}

	public void setJsonSchemaConfigurations(
			List<JsonSchemaConfiguration> jsonSchemaConfigurations) {
		this.jsonSchemaConfigurations = jsonSchemaConfigurations;
	}

	public <T, C extends T> Module<T, C> build(
			ModelInfoGraphAnalyzer<T, C> analyzer, MModelInfo<T, C> modelInfo,
			Map<String, Mapping<T, C>> mappings) {

		// final Logger logger =
		// Validate.notNull(context).getLoggerFactory().getLogger(
		// ModuleConfiguration.class.getName());

		final List<MappingConfiguration> mappingConfigurations = getMappingConfigurations();

		final List<Mapping<T, C>> moduleMappings = new ArrayList<Mapping<T, C>>(
				mappingConfigurations.size());
		for (MappingConfiguration mappingConfiguration : mappingConfigurations) {
			Mapping<T, C> moduleMapping = mappings.get(mappingConfiguration
					.getId());
			if (moduleMapping != null) {
				moduleMappings.add(moduleMapping);
			}
		}

		if (moduleMappings.isEmpty()) {
			return null;
		}

		final String moduleName = this.name;
		if (moduleName == null) {
			throw new IllegalStateException("Module name was not assigned yet.");
		}

		final String moduleSchemaId = schemaId.replace(
				ModuleConfiguration.MODULE_NAME_PROPERTY, moduleName);

		final List<Output> outputs = new ArrayList<Output>(
				this.outputConfigurations.size());
		for (OutputConfiguration outputConfiguration : this.outputConfigurations) {
			final Output output = outputConfiguration.build(moduleName);
			if (output != null) {
				outputs.add(output);
			}
		}
		final List<JsonSchema> jsonSchemas = new ArrayList<JsonSchema>(
				this.jsonSchemaConfigurations.size());
		for (JsonSchemaConfiguration jsonSchemaConfiguration : this.jsonSchemaConfigurations) {
			final JsonSchema jsonSchema = jsonSchemaConfiguration
					.build(moduleName);
			if (jsonSchema != null) {
				jsonSchemas.add(jsonSchema);
			}
		}
		return new Module<T, C>(moduleName, moduleSchemaId, moduleMappings,
				outputs, jsonSchemas);
	}

//	private <T, C extends T> String createModuleName(
//			Iterable<Mapping<T, C>> mappings) {
//		boolean first = true;
//		final StringBuilder sb = new StringBuilder();
//		for (Mapping<T, C> mapping : mappings) {
//			if (!first) {
//				sb.append(MODULE_NAME_SEPARATOR);
//			}
//			sb.append(mapping.getMappingName());
//			first = false;
//		}
//		return sb.toString();
//	}

}
