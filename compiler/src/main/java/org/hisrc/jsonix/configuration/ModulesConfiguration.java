package org.hisrc.jsonix.configuration;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.analysis.ModelInfoGraphAnalyzer;
import org.hisrc.jsonix.context.JsonixContext;
import org.hisrc.jsonix.definition.Mapping;
import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Modules;
import org.hisrc.jsonix.naming.StandardNaming;
import org.jgrapht.DirectedGraph;
import org.jgrapht.EdgeFactory;
import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.jvnet.jaxb2_commons.xml.bind.model.MModelInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPackageInfo;
import org.slf4j.Logger;

@XmlRootElement(name = ModulesConfiguration.LOCAL_ELEMENT_NAME)
@XmlType(propOrder = {})
public class ModulesConfiguration {

	private final Logger logger;

	public static final String DEFAULT_PREFIX = "jsonix";

	public static final String NAMESPACE_URI = "http://jsonix.highsource.org/customizations";

	public static final String LOCAL_ELEMENT_NAME = "modules";
	public static final QName ELEMENT_NAME = new QName(
			ModulesConfiguration.NAMESPACE_URI, LOCAL_ELEMENT_NAME,
			ModulesConfiguration.DEFAULT_PREFIX);

	private List<ModuleConfiguration> moduleConfigurations = new LinkedList<ModuleConfiguration>();
	private List<MappingConfiguration> mappingConfigurations = new LinkedList<MappingConfiguration>();
	private List<OutputConfiguration> outputConfigurations = new LinkedList<OutputConfiguration>();

	private final JsonixContext context;

	public ModulesConfiguration(JsonixContext context) {
		this.context = Validate.notNull(context);
		this.logger = Validate.notNull(context).getLoggerFactory().getLogger(
				ModulesConfiguration.class.getName());

	}

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

	public <T, C extends T> Modules<T, C> build(MModelInfo<T, C> modelInfo) {

		final ModelInfoGraphAnalyzer<T, C> analyzer = new ModelInfoGraphAnalyzer<T, C>(
				context, modelInfo);

		final List<ModuleConfiguration> moduleConfigurations = new LinkedList<ModuleConfiguration>(
				getModuleConfigurations());

		createModuleConfigurationsForMappingConfigurations(
				moduleConfigurations, getMappingConfigurations());

		createModuleConfigurationsForUnmappedPackages(analyzer,
				moduleConfigurations);

		assignDefaultOutputConfigurations(moduleConfigurations);

		assignMappingNamesAndIds(moduleConfigurations);

		return buildModules(modelInfo, analyzer, moduleConfigurations);
	}

	private void assignMappingNamesAndIds(
			final List<ModuleConfiguration> moduleConfigurations) {
		// Generate ids where missing
		final Map<String, MappingConfiguration> idToMappingConfiguration = new HashMap<String, MappingConfiguration>();
		final Map<String, List<MappingConfiguration>> nameToMappingConfiguration = new HashMap<String, List<MappingConfiguration>>();

		// Set mapping name and ids
		for (final ModuleConfiguration moduleConfiguration : moduleConfigurations) {
			for (final MappingConfiguration mappingConfiguration : moduleConfiguration
					.getMappingConfigurations()) {

				assignMappingName(mappingConfiguration);

				assignMappingId(idToMappingConfiguration,
						nameToMappingConfiguration, mappingConfiguration);
			}
		}
	}

	private void assignMappingId(
			final Map<String, MappingConfiguration> idToMappingConfiguration,
			final Map<String, List<MappingConfiguration>> nameToMappingConfiguration,
			final MappingConfiguration mappingConfiguration) {
		final String mappingName = mappingConfiguration.getName();
		String mappingId = mappingConfiguration.getId();

		if (mappingId != null) {
			// TODO throw an exception, don't try to correct
			if (idToMappingConfiguration.containsKey(mappingId)) {
				logger.error(MessageFormat
						.format("Mapping id [{0}] is already defined, generating a new mapping id.",
								mappingId));
				mappingId = null;
			}
		}
		if (mappingId != null) {
			idToMappingConfiguration.put(mappingId, mappingConfiguration);
		} else {
			List<MappingConfiguration> mappings = nameToMappingConfiguration
					.get(mappingName);
			if (mappings == null) {
				mappings = new ArrayList<MappingConfiguration>(2);
				logger.debug(MessageFormat.format(
						"Assigning id [{0}] to the mapping with name [{1}].",
						mappingName, mappingName));
				mappingId = mappingName;
				mappingConfiguration.setId(mappingId);
				mappings.add(mappingConfiguration);
				nameToMappingConfiguration.put(mappingName, mappings);
				idToMappingConfiguration.put(mappingId, mappingConfiguration);
			} else if (mappings.size() == 1) {
				logger.debug(MessageFormat
						.format("There are more than one mapping with the name [{0}] without id.",
								mappingName));
				final String mappingId0 = mappingName + "-0";
				final MappingConfiguration mappingConfiguration0 = mappings
						.get(0);
				logger.debug(MessageFormat.format(
						"Assigning id [{0}] to the mapping with name [{1}].",
						mappingId0, mappingName));
				mappingConfiguration0.setId(mappingId0);

				mappingId = mappingName + "-1";
				logger.debug(MessageFormat.format(
						"Assigning id [{0}] to the mapping with name [{1}].",
						mappingId, mappingName));
				mappingConfiguration.setId(mappingId);
				mappings.add(mappingConfiguration);
				idToMappingConfiguration.remove(mappingName);
				idToMappingConfiguration.put(mappingId0, mappingConfiguration0);
				idToMappingConfiguration.put(mappingId, mappingConfiguration);
			} else {
				mappingId = mappingName + mappings.size();
				logger.debug(MessageFormat.format(
						"Assigning id [{0}] to the mapping with name [{1}].",
						mappingId, mappingName));
				mappingConfiguration.setId(mappingId);
				mappings.add(mappingConfiguration);
				idToMappingConfiguration.put(mappingId, mappingConfiguration);
			}
		}
	}

	private String assignMappingName(
			final MappingConfiguration mappingConfiguration) {
		String mappingName = mappingConfiguration.getName();
		if (StringUtils.isBlank(mappingName)) {
			final String packageName = mappingConfiguration.getPackage();
			if (packageName != null) {
				mappingName = StringUtils.isBlank(packageName) ? "Mapping"
						: packageName.replace('.', '_');
			}
			mappingConfiguration.setName(mappingName);
		}
		return mappingName;
	}

	private <T, C extends T> void createModuleConfigurationsForUnmappedPackages(
			final ModelInfoGraphAnalyzer<T, C> analyzer,
			final List<ModuleConfiguration> moduleConfigurations) {
		final Set<String> packageNames = findUnmappedPackageNames(
				analyzer.getPackageNames(), moduleConfigurations);

		for (final String packageName : packageNames) {
			final MappingConfiguration mappingConfiguration = new MappingConfiguration(
					context);
			mappingConfiguration.setPackage(packageName);
			final ModuleConfiguration moduleConfiguration = new ModuleConfiguration(
					context);
			moduleConfiguration.getMappingConfigurations().add(
					mappingConfiguration);
			moduleConfigurations.add(moduleConfiguration);
		}
	}

	private void assignDefaultOutputConfigurations(
			final List<ModuleConfiguration> moduleConfigurations) {
		final List<OutputConfiguration> defaultOutputConfigurations = createDefaultOutputConfigurations();
		for (final ModuleConfiguration moduleConfiguration : moduleConfigurations) {
			if (moduleConfiguration.getOutputConfigurations().isEmpty()) {
				moduleConfiguration.getOutputConfigurations().addAll(
						defaultOutputConfigurations);
			}
		}
	}

	private Set<String> findUnmappedPackageNames(
			final Set<String> allPackageNames,
			final List<ModuleConfiguration> moduleConfigurations) {
		final Set<String> packageNames = new HashSet<String>(allPackageNames);

		final Set<String> mappedPackagesNames = new HashSet<String>();
		for (final ModuleConfiguration moduleConfiguration : moduleConfigurations) {
			for (final MappingConfiguration mappingConfiguration : moduleConfiguration
					.getMappingConfigurations()) {
				mappedPackagesNames.add(mappingConfiguration.getPackage());
			}
		}
		packageNames.removeAll(mappedPackagesNames);
		return packageNames;
	}

	private void createModuleConfigurationsForMappingConfigurations(
			final List<ModuleConfiguration> moduleConfigurations,
			final List<MappingConfiguration> mappingConfigurations) {
		// Create one module configuration per mapping configuration
		for (final MappingConfiguration mappingConfiguration : mappingConfigurations) {
			final ModuleConfiguration moduleConfiguration = new ModuleConfiguration(
					this.context);
			moduleConfiguration.getMappingConfigurations().add(
					mappingConfiguration);
			moduleConfigurations.add(moduleConfiguration);
		}
	}

	private List<OutputConfiguration> createDefaultOutputConfigurations() {
		final List<OutputConfiguration> defaultOutputConfigurations = getOutputConfigurations();

		// If default output configurations are not configured, add the standard
		// configuration
		if (defaultOutputConfigurations.isEmpty()) {
			defaultOutputConfigurations.add(new OutputConfiguration(
					StandardNaming.NAMING_NAME));
		}
		return defaultOutputConfigurations;
	}

	private <T, C extends T> Modules<T, C> buildModules(
			MModelInfo<T, C> modelInfo,
			final ModelInfoGraphAnalyzer<T, C> analyzer,
			final List<ModuleConfiguration> moduleConfigurations) {

		final List<MappingConfiguration> mappingConfigurations = getTopologicallyOrderedMappingConfigurations(moduleConfigurations);

		final Map<String, Mapping<T, C>> mappings = buildMappings(modelInfo,
				analyzer, mappingConfigurations);

		final List<Module<T, C>> modules = new ArrayList<Module<T, C>>(
				moduleConfigurations.size());
		for (ModuleConfiguration moduleConfiguration : moduleConfigurations) {
			final Module<T, C> module = moduleConfiguration.build(analyzer,
					modelInfo, mappings);
			if (module != null) {
				modules.add(module);
			}
		}
		return new Modules<T, C>(context, modelInfo, modules);
	}

	private <T, C extends T> Map<String, Mapping<T, C>> buildMappings(
			MModelInfo<T, C> modelInfo,
			final ModelInfoGraphAnalyzer<T, C> analyzer,
			final List<MappingConfiguration> mappingConfigurations) {
		final Map<String, Mapping<T, C>> mappings = new HashMap<String, Mapping<T, C>>();
		for (MappingConfiguration mappingConfiguration : mappingConfigurations) {
			final String packageName = mappingConfiguration.getPackage();
			final MPackageInfo packageInfo = analyzer.getPackageInfoMap().get(
					packageName);
			if (packageInfo == null) {
				logger.warn(MessageFormat.format(
						"Package name [{0}] could not be found.",
						Validate.notNull(packageName)));
				// throw new MissingPackageException(packageName);
			} else {
				final Mapping<T, C> mapping = mappingConfiguration.build(
						analyzer, modelInfo, packageInfo, mappings);
				mappings.put(mappingConfiguration.getId(), mapping);
			}
		}
		return mappings;
	}

	private List<MappingConfiguration> getTopologicallyOrderedMappingConfigurations(
			final List<ModuleConfiguration> moduleConfigurations) {
		final DirectedGraph<MappingConfiguration, Object> mappingConfigurationDependencyGraph = buildMappingConfigurationDependencyGraph(moduleConfigurations);

		final StrongConnectivityInspector<MappingConfiguration, Object> strongConnectivityInspector = new StrongConnectivityInspector<MappingConfiguration, Object>(
				mappingConfigurationDependencyGraph);

		final List<Set<MappingConfiguration>> stronglyConnectedSets = strongConnectivityInspector
				.stronglyConnectedSets();

		for (Set<MappingConfiguration> stronglyConnectedSet : stronglyConnectedSets) {
			if (stronglyConnectedSet.size() > 1) {
				throw new IllegalArgumentException(MessageFormat.format(
						"Mappings have the following dependency cycle: {0}",
						stronglyConnectedSet.toString()));
			}
		}

		final List<MappingConfiguration> mappingConfigurations = new ArrayList<MappingConfiguration>(
				mappingConfigurationDependencyGraph.vertexSet().size());
		for (Iterator<MappingConfiguration> mappingConfigurationsInTopologicalOrderIterator = new TopologicalOrderIterator<MappingConfiguration, Object>(
				mappingConfigurationDependencyGraph); mappingConfigurationsInTopologicalOrderIterator
				.hasNext();) {
			mappingConfigurations
					.add(mappingConfigurationsInTopologicalOrderIterator.next());
		}
		return mappingConfigurations;
	}

	private DirectedGraph<MappingConfiguration, Object> buildMappingConfigurationDependencyGraph(
			final List<ModuleConfiguration> moduleConfigurations) {
		final DirectedGraph<MappingConfiguration, Object> mappingDependenciesGraph = new DefaultDirectedGraph<MappingConfiguration, Object>(
				new EdgeFactory<MappingConfiguration, Object>() {
					public Object createEdge(MappingConfiguration sourceVertex,
							MappingConfiguration targetVertex) {
						return new Object();
					};
				});

		final Map<String, MappingConfiguration> idToMappingConfiguration = new HashMap<String, MappingConfiguration>();
		final Map<String, List<MappingConfiguration>> nameToMappingConfiguration = new HashMap<String, List<MappingConfiguration>>();
		for (ModuleConfiguration moduleConfiguration : moduleConfigurations) {
			for (MappingConfiguration mappingConfiguration : moduleConfiguration
					.getMappingConfigurations()) {
				final String id = mappingConfiguration.getId();
				final String name = mappingConfiguration.getName();
				idToMappingConfiguration.put(id, mappingConfiguration);
				List<MappingConfiguration> mappings = nameToMappingConfiguration
						.get(name);
				if (mappings == null) {
					mappings = new ArrayList<MappingConfiguration>(2);
					nameToMappingConfiguration.put(name, mappings);
				}
				mappings.add(mappingConfiguration);
			}
		}

		for (ModuleConfiguration moduleConfiguration : moduleConfigurations) {
			for (MappingConfiguration mappingConfiguration : moduleConfiguration
					.getMappingConfigurations()) {
				mappingDependenciesGraph.addVertex(mappingConfiguration);
				final IncludesConfiguration includesConfiguration = mappingConfiguration
						.getIncludesConfiguration();
				if (includesConfiguration != null) {
					for (DependenciesOfMappingConfiguration dependenciesOfMappingConfiguration : includesConfiguration
							.getDependenciesOfMappingConfiguration()) {
						final String id = dependenciesOfMappingConfiguration
								.getId();
						final String name = dependenciesOfMappingConfiguration
								.getName();
						MappingConfiguration dependingMappingConfiguration = null;
						if (id != null) {
							dependingMappingConfiguration = idToMappingConfiguration
									.get(id);
							if (dependingMappingConfiguration == null) {
								throw new MissingMappingWithIdException(id);
							}
							mappingDependenciesGraph
									.addVertex(dependingMappingConfiguration);
							mappingDependenciesGraph.addEdge(
									dependingMappingConfiguration,
									mappingConfiguration);
						} else if (name != null) {
							final List<MappingConfiguration> dependingMappingConfigurations = nameToMappingConfiguration
									.get(name);
							if (dependingMappingConfigurations == null
									|| dependingMappingConfigurations.isEmpty()) {
								throw new MissingMappinWithNameException(name);
							} else if (dependingMappingConfigurations.size() > 1) {
								throw new AmbiguousMappingNameException(name);
							} else {
								// Ok, now the payload
								dependingMappingConfiguration = dependingMappingConfigurations
										.get(0);
								dependenciesOfMappingConfiguration
										.setId(dependingMappingConfiguration
												.getId());
							}

							mappingDependenciesGraph
									.addVertex(dependingMappingConfiguration);
							mappingDependenciesGraph.addEdge(
									dependingMappingConfiguration,
									mappingConfiguration);

						} else {
							logger.warn(MessageFormat
									.format("Either [id] or [name] must be defined in the  [{0}] element.",
											DependenciesOfMappingConfiguration.LOCAL_ELEMENT_NAME));
						}

					}
				}
			}
		}
		return mappingDependenciesGraph;
	}
}
