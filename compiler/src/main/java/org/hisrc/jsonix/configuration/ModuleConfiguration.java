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

import org.hisrc.jsonix.definition.Mapping;
import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Output;
import org.hisrc.jsonix.log.Log;
import org.jvnet.jaxb2_commons.xml.bind.model.MModelInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPackageInfo;

@XmlRootElement(name = ModuleConfiguration.LOCAL_ELEMENT_NAME)
@XmlType(propOrder = {})
public class ModuleConfiguration {

	public static final String LOCAL_ELEMENT_NAME = "module";

//	private Log log = new SystemLog();

	public static final String MODULE_NAME_PROPERTY = "${module.name}";
	public static final String MODULE_NAME_SEPARATOR = "_";

	private String name;
	private List<MappingConfiguration> mappingConfigurations = new LinkedList<MappingConfiguration>();
	private List<OutputConfiguration> outputConfigurations = new LinkedList<OutputConfiguration>();

	public static final QName MODULE_NAME = new QName(ModulesConfiguration.NAMESPACE_URI,
	LOCAL_ELEMENT_NAME, ModulesConfiguration.DEFAULT_PREFIX);

	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "mapping")
	public List<MappingConfiguration> getMappingConfigurations() {
		return mappingConfigurations;
	}

	public void setMappingConfigurations(
			List<MappingConfiguration> mappingConfigurations) {
		this.mappingConfigurations = mappingConfigurations;
	}

	@XmlElement(name = "output")
	public List<OutputConfiguration> getOutputConfigurations() {
		return outputConfigurations;
	}

	public void setOutputConfigurations(
			List<OutputConfiguration> outputConfigurations) {
		this.outputConfigurations = outputConfigurations;
	}

	public <T, C> Module build(Log log, MModelInfo<T, C> model,
			Map<String, MPackageInfo> packageInfos) {
		final List<Mapping> mappings = new ArrayList<Mapping>(
				this.mappingConfigurations.size());
		for (MappingConfiguration mappingConfiguration : this.mappingConfigurations) {
			final Mapping mapping = mappingConfiguration.build(log, model,
					packageInfos);
			if (mapping != null) {
				mappings.add(mapping);
			}
		}

		final String moduleName;
		if (this.name != null) {
			moduleName = this.name;
		} else {
			moduleName = createModuleName(mappings);
		}

		final List<Output> outputs = new ArrayList<Output>(
				this.outputConfigurations.size());
		for (OutputConfiguration outputConfiguration : this.outputConfigurations) {
			final Output output = outputConfiguration.build(moduleName);
			if (output != null) {
				outputs.add(output);
			}
		}
		return new Module(moduleName, mappings, outputs);
	}

	private String createModuleName(Iterable<Mapping> mappings) {
		boolean first = true;
		final StringBuilder sb = new StringBuilder();
		for (Mapping mapping : mappings) {
			if (!first) {
				sb.append(MODULE_NAME_SEPARATOR);
			}
			sb.append(mapping.getMappingName());
			first = false;
		}
		return sb.toString();
	}

}
