package org.hisrc.jsonix.configuration;

import java.text.MessageFormat;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.analysis.ModelInfoGraphAnalyzer;
import org.hisrc.jsonix.configuration.exception.MissingMappingWithIdException;
import org.hisrc.jsonix.context.JsonixContext;
import org.hisrc.jsonix.definition.Mapping;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MModelInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPackageInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.util.PackageInfoQNameAnalyzer;
import org.slf4j.Logger;

@XmlRootElement(name = MappingConfiguration.LOCAL_ELEMENT_NAME)
@XmlType(propOrder = {})
public class MappingConfiguration {

	public static final String MAPPING_NAME_PROPERTY = "${mapping.name}";
	public static final String MAPPING_TARGET_NAMESPACE_URI_PROPERTY = "${mapping.targetNamespace}";
	public static final String LOCAL_ELEMENT_NAME = "mapping";
	public static final String DEFAULT_MAPPING_STYLE = "default";

	private ModuleConfiguration moduleConfiguration;
	private String id;
	private String name;
	private String _package;
	private String schemaId = MappingConfiguration.MAPPING_TARGET_NAMESPACE_URI_PROPERTY + "#";
	private String mappingStyle;
	private String targetNamespaceURI;
	private String defaultElementNamespaceURI;
	private String defaultAttributeNamespaceURI;
	private IncludesConfiguration includesConfiguration;
	private ExcludesConfiguration excludesConfiguration;

	public static final QName MAPPING_NAME = new QName(
			ModulesConfiguration.NAMESPACE_URI,
			MappingConfiguration.LOCAL_ELEMENT_NAME,
			ModulesConfiguration.DEFAULT_PREFIX);

	@XmlAttribute(name = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name = "package")
	public String getPackage() {
		return _package;
	}

	public void setPackage(String _package) {
		this._package = _package;
	}

	@XmlAttribute(name = "schemaId")
	public String getSchemaId() {
		return schemaId;
	}

	public void setSchemaId(String schemaId) {
		this.schemaId = schemaId;
	}

	@XmlAttribute(name = "mappingStyle")
	public String getMappingStyle() {
		return mappingStyle;
	}

	public void setMappingStyle(String mappingStyle) {
		this.mappingStyle = mappingStyle;
	}

	@XmlAttribute(name = "targetNamespace")
	public String getTargetNamespaceURI() {
		return targetNamespaceURI;
	}

	public void setTargetNamespaceURI(String targetNamespaceURI) {
		this.targetNamespaceURI = targetNamespaceURI;
	}

	@XmlAttribute(name = "defaultElementNamespaceURI")
	public String getDefaultElementNamespaceURI() {
		return defaultElementNamespaceURI;
	}

	public void setDefaultElementNamespaceURI(String defaultElementNamespaceURI) {
		this.defaultElementNamespaceURI = defaultElementNamespaceURI;
	}

	@XmlAttribute(name = "defaultAttributeNamespaceURI")
	public String getDefaultAttributeNamespaceURI() {
		return defaultAttributeNamespaceURI;
	}

	public void setDefaultAttributeNamespaceURI(
			String defaultAttributeNamespaceURI) {
		this.defaultAttributeNamespaceURI = defaultAttributeNamespaceURI;
	}

	@XmlElement(name = IncludesConfiguration.LOCAL_ELEMENT_NAME)
	public IncludesConfiguration getIncludesConfiguration() {
		return includesConfiguration;
	}

	public void setIncludesConfiguration(
			IncludesConfiguration includesConfiguration) {
		this.includesConfiguration = includesConfiguration;
	}

	@XmlElement(name = ExcludesConfiguration.LOCAL_ELEMENT_NAME)
	public ExcludesConfiguration getExcludesConfiguration() {
		return excludesConfiguration;
	}

	public void setExcludesConfiguration(
			ExcludesConfiguration excludesConfiguration) {
		this.excludesConfiguration = excludesConfiguration;
	}

	public <T, C extends T> Mapping<T, C> build(JsonixContext context,
			ModelInfoGraphAnalyzer<T, C> analyzer, MModelInfo<T, C> modelInfo,
			MPackageInfo packageInfo, Map<String, Mapping<T, C>> mappings) {
		Validate.notNull(modelInfo);
		Validate.notNull(packageInfo);
		Validate.notNull(mappings);

		final String packageName = getPackage();

		final String mappingName = getName();

		final Logger logger = context.getLoggerFactory().getLogger(
				MappingConfiguration.class.getName());

		logger.debug(MessageFormat.format(
				"Package [{0}] will be mapped by the mapping [{1}].",
				packageName, mappingName));

		final PackageInfoQNameAnalyzer<T, C> qnameAnalyzer = new PackageInfoQNameAnalyzer<T, C>(
				modelInfo);

		final String draftMostUsedElementNamespaceURI = qnameAnalyzer
				.getMostUsedElementNamespaceURI(packageInfo);
		final String mostUsedElementNamespaceURI = draftMostUsedElementNamespaceURI == null ? ""
				: draftMostUsedElementNamespaceURI;

		final String targetNamespaceURI;
		if (this.targetNamespaceURI != null) {
			targetNamespaceURI = this.targetNamespaceURI;
		} else {
			logger.debug(MessageFormat
					.format("Mapping [{0}] will use \"{1}\" as the target namespace as it is the most used element namespace URI in the package [{2}].",
							mappingName, mostUsedElementNamespaceURI,
							packageName));
			targetNamespaceURI = mostUsedElementNamespaceURI;
		}

		final String defaultElementNamespaceURI;
		if (this.defaultElementNamespaceURI != null) {
			defaultElementNamespaceURI = this.defaultElementNamespaceURI;
		} else {
			defaultElementNamespaceURI = targetNamespaceURI;
		}

		final String draftMostUsedAttributeNamespaceURI = qnameAnalyzer
				.getMostUsedAttributeNamespaceURI(packageInfo);
		final String mostUsedAttributeNamespaceURI = draftMostUsedAttributeNamespaceURI == null ? ""
				: draftMostUsedAttributeNamespaceURI;

		final String defaultAttributeNamespaceURI;
		if (this.defaultAttributeNamespaceURI != null) {
			defaultAttributeNamespaceURI = this.defaultAttributeNamespaceURI;
		} else {
			logger.debug(MessageFormat
					.format("Mapping [{0}] will use \"{1}\" as it is the most used attribute namespace URI in the package [{2}].",
							mappingName, mostUsedAttributeNamespaceURI,
							packageName));
			defaultAttributeNamespaceURI = mostUsedAttributeNamespaceURI;
		}

		final String mappingSchemaId = schemaId
				.replace(ModuleConfiguration.MODULE_SCHEMA_ID_PROPERTY,
						getModuleConfiguration().getSchemaId())
				.replace(ModuleConfiguration.MODULE_NAME_PROPERTY,
						getModuleConfiguration().getName())
				.replace(MappingConfiguration.MAPPING_NAME_PROPERTY, getName())
				.replace(MappingConfiguration.MAPPING_TARGET_NAMESPACE_URI_PROPERTY, targetNamespaceURI);

		final String mappingStyle;
		if (this.mappingStyle != null) {
			mappingStyle = this.mappingStyle;
		} else {
			logger.debug(MessageFormat
					.format("Mapping [{0}] will use Standard Mapping Style in the package [{2}].",
							mappingName, packageName));
			mappingStyle = MappingConfiguration.DEFAULT_MAPPING_STYLE;
		}

		final Mapping<T, C> mapping = new Mapping<T, C>(context, analyzer,
				packageInfo, mappingName, mappingSchemaId, mappingStyle, targetNamespaceURI,
				defaultElementNamespaceURI, defaultAttributeNamespaceURI);

		if (getExcludesConfiguration() != null) {
			final ExcludesConfiguration excludesConfiguration = getExcludesConfiguration();
			for (TypeInfoConfiguration typeInfoConfiguration : excludesConfiguration
					.getTypeInfoConfigurations()) {
				final MTypeInfo<T, C> typeInfo = typeInfoConfiguration
						.findTypeInfo(context, analyzer, packageInfo);
				if (typeInfo != null) {
					mapping.excludeTypeInfo(typeInfo);
				}
			}
			for (ElementInfoConfiguration elementInfoConfiguration : excludesConfiguration
					.getElementInfoConfigurations()) {
				final MElementInfo<T, C> elementInfo = elementInfoConfiguration
						.findElementInfo(context, analyzer, packageInfo);
				if (elementInfo != null) {
					mapping.excludeElementInfo(elementInfo);
				}
			}
			for (PropertyInfoConfiguration propertyInfoConfiguration : excludesConfiguration
					.getPropertyInfoConfigurations()) {
				final MPropertyInfo<T, C> propertyInfo = propertyInfoConfiguration
						.findPropertyInfo(context, analyzer, packageInfo);
				if (propertyInfo != null) {
					mapping.excludePropertyInfo(propertyInfo);
				}
			}
		}

		if (getIncludesConfiguration() == null) {
			logger.trace(MessageFormat
					.format("Includes configuration for the mapping [{0}] is not provided, including the whole package.",
							mappingName));
			mapping.includePackage(packageInfo);
		} else {
			final IncludesConfiguration includesConfiguration = getIncludesConfiguration();
			for (TypeInfoConfiguration typeInfoConfiguration : includesConfiguration
					.getTypeInfoConfigurations()) {
				final MTypeInfo<T, C> typeInfo = typeInfoConfiguration
						.findTypeInfo(context, analyzer, packageInfo);
				if (typeInfo != null) {
					mapping.includeTypeInfo(typeInfo);
				}
			}
			for (ElementInfoConfiguration elementInfoConfiguration : includesConfiguration
					.getElementInfoConfigurations()) {
				final MElementInfo<T, C> elementInfo = elementInfoConfiguration
						.findElementInfo(context, analyzer, packageInfo);
				if (elementInfo != null) {
					mapping.includeElementInfo(elementInfo);
				}
			}
			for (PropertyInfoConfiguration propertyInfoConfiguration : includesConfiguration
					.getPropertyInfoConfigurations()) {
				final MPropertyInfo<T, C> propertyInfo = propertyInfoConfiguration
						.findPropertyInfo(context, analyzer, packageInfo);
				if (propertyInfo != null) {
					mapping.includePropertyInfo(propertyInfo);
				}
			}
			for (DependenciesOfMappingConfiguration dependenciesOfMappingConfiguration : includesConfiguration
					.getDependenciesOfMappingConfiguration()) {
				final String id = dependenciesOfMappingConfiguration.getId();
				final Mapping<T, C> dependingMapping = mappings.get(id);
				if (dependingMapping == null) {
					throw new MissingMappingWithIdException(id);
				}
				mapping.includeDependenciesOfMapping(dependingMapping);
			}
		}

		return mapping;

	}

	@Override
	public String toString() {
		return MessageFormat.format("[{0}:{1}]", getId(), getName());
	}

	public ModuleConfiguration getModuleConfiguration() {
		return moduleConfiguration;
	}

	public void setModuleConfiguration(ModuleConfiguration moduleConfiguration) {
		if (this.moduleConfiguration != null) {
			throw new IllegalStateException(
					"Module configuration was already assigned.");
		}
		this.moduleConfiguration = moduleConfiguration;
	}
}
