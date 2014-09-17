package org.hisrc.jsonix.configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import org.hisrc.jsonix.compilation.Module;
import org.hisrc.jsonix.compilation.Modules;
import org.hisrc.jsonix.compiler.StandardNaming;
import org.hisrc.jsonix.compiler.graph.ModelInfoGraphAnalyzer;
import org.jvnet.jaxb2_commons.xml.bind.model.MModelInfo;

@XmlRootElement(name = ModulesConfiguration.LOCAL_ELEMENT_NAME)
@XmlType(propOrder = {})
public class ModulesConfiguration {

	public static final String DEFAULT_PREFIX = "jsonix";

	public static final String NAMESPACE_URI = "http://jsonix.highsource.org/customizations";

	public static final String LOCAL_ELEMENT_NAME = "modules";
	public static final QName ELEMENT_NAME = new QName(
			ModulesConfiguration.NAMESPACE_URI, LOCAL_ELEMENT_NAME,
			ModulesConfiguration.DEFAULT_PREFIX);

	private List<ModuleConfiguration> moduleConfigurations = new LinkedList<ModuleConfiguration>();
	private List<MappingConfiguration> mappingConfigurations = new LinkedList<MappingConfiguration>();
	private List<OutputConfiguration> outputConfigurations = new LinkedList<OutputConfiguration>();

	public List<ModuleConfiguration> getModuleConfigurations() {
		return moduleConfigurations;
	}

	public void setModuleConfigurations(
			List<ModuleConfiguration> moduleConfigurations) {
		this.moduleConfigurations = moduleConfigurations;
	}

	public List<MappingConfiguration> getMappingConfigurations() {
		return mappingConfigurations;
	}

	public void setMappingConfigurations(
			List<MappingConfiguration> mappingConfigurations) {
		this.mappingConfigurations = mappingConfigurations;
	}

	public List<OutputConfiguration> getOutputConfigurations() {
		return outputConfigurations;
	}

	public void setOutputConfigurations(
			List<OutputConfiguration> outputConfigurations) {
		this.outputConfigurations = outputConfigurations;
	}

	public <T, C> Modules build(MModelInfo<T, C> model) {

		final ModelInfoGraphAnalyzer<T, C> analyzer = new ModelInfoGraphAnalyzer<T, C>(
				model);

		final Set<String> packageNames = new HashSet<String>(
				analyzer.getPackageNames());

		final List<OutputConfiguration> defaultOutputConfigurations = getOutputConfigurations();

		// If default output configurations are not configured, add the standard
		// configuration
		if (defaultOutputConfigurations.isEmpty()) {
			defaultOutputConfigurations.add(new OutputConfiguration(
					StandardNaming.NAMING_NAME));
		}

		final Set<String> mappedPackagesNames = new HashSet<String>();

		final List<ModuleConfiguration> moduleConfigurations = new LinkedList<ModuleConfiguration>(
				getModuleConfigurations());

		// Create one module configuration per mapping configuration
		for (final MappingConfiguration mappingConfiguration : this.mappingConfigurations) {
			mappedPackagesNames.add(mappingConfiguration.getPackage());
			final ModuleConfiguration moduleConfiguration = new ModuleConfiguration();
			moduleConfiguration.getMappingConfigurations().add(
					mappingConfiguration);
			moduleConfiguration.getOutputConfigurations().addAll(
					defaultOutputConfigurations);
			moduleConfigurations.add(moduleConfiguration);
		}

		for (final ModuleConfiguration moduleConfiguration : moduleConfigurations) {
			if (moduleConfiguration.getOutputConfigurations().isEmpty()) {
				moduleConfiguration.getOutputConfigurations().addAll(
						defaultOutputConfigurations);
			}
			for (final MappingConfiguration mappingConfiguration : moduleConfiguration
					.getMappingConfigurations()) {
				mappedPackagesNames.add(mappingConfiguration.getPackage());
				if (mappingConfiguration.getOutputConfigurations().isEmpty()) {
					mappingConfiguration.getOutputConfigurations().addAll(
							moduleConfiguration.getOutputConfigurations());
				}
			}
		}

		packageNames.removeAll(mappedPackagesNames);

		for (final String packageName : packageNames) {
			final MappingConfiguration mappingConfiguration = new MappingConfiguration();
			mappingConfiguration.setPackage(packageName);
			mappingConfiguration.getOutputConfigurations().addAll(
					defaultOutputConfigurations);
			final ModuleConfiguration moduleConfiguration = new ModuleConfiguration();
			moduleConfiguration.getMappingConfigurations().add(
					mappingConfiguration);
			moduleConfiguration.getOutputConfigurations().addAll(
					defaultOutputConfigurations);
			moduleConfigurations.add(moduleConfiguration);
			mappedPackagesNames.add(packageName);
		}

		final List<Module> modules = new ArrayList<Module>(
				moduleConfigurations.size());
		for (ModuleConfiguration moduleConfiguration : moduleConfigurations) {
			final Module module = moduleConfiguration.build(model,
					analyzer.getPackageInfoMap());
			if (module != null) {
				modules.add(module);
			}
		}
		return new Modules(modules);
	}
}
