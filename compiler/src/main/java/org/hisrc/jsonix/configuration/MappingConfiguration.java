package org.hisrc.jsonix.configuration;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.compilation.Mapping;
import org.hisrc.jsonix.compiler.log.Log;
import org.hisrc.jsonix.compiler.log.SystemLog;
import org.jvnet.jaxb2_commons.xml.bind.model.MModelInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPackageInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.util.PackageInfoQNameAnalyzer;

@XmlRootElement(name = MappingConfiguration.LOCAL_ELEMENT_NAME)
@XmlType(propOrder = {})
public class MappingConfiguration {

	public static final String LOCAL_ELEMENT_NAME = "mapping";

	private Log log = new SystemLog();

	private String name;
	private String _package;
	private String defaultElementNamespaceURI;
	private String defaultAttributeNamespaceURI;
	private List<OutputConfiguration> outputConfigurations = new ArrayList<OutputConfiguration>(
			2);

	public static final QName MAPPING_NAME = new QName(
			ModulesConfiguration.NAMESPACE_URI, LOCAL_ELEMENT_NAME,
			ModulesConfiguration.DEFAULT_PREFIX);

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

	@XmlElement(name = "output")
	public List<OutputConfiguration> getOutputConfigurations() {
		return outputConfigurations;
	}

	public void setOutputConfigurations(
			List<OutputConfiguration> outputConfigurations) {
		Validate.noNullElements(outputConfigurations);
		this.outputConfigurations = outputConfigurations;
	}

	public <T, C> Mapping build(MModelInfo<T, C> model,
			Map<String, MPackageInfo> packageInfos) {
		Validate.notNull(model);
		Validate.notNull(packageInfos);

		final String packageName = createPackageName();

		final MPackageInfo packageInfo = packageInfos.get(packageName);
		if (packageInfo == null) {
			log.warn(MessageFormat
					.format("Package name [{0}] could not be found in the given, mapping configuration will be ignored",
							packageName));
			return null;
		}

		final String mappingName = createMappingName(packageName);

		log.debug(MessageFormat.format(
				"Package [{0}] will be mapped by the mapping [{1}].",
				packageName, mappingName));

		final PackageInfoQNameAnalyzer<T, C> analyzer = new PackageInfoQNameAnalyzer<T, C>(
				model);

		final String draftMostUsedElementNamespaceURI = analyzer
				.getMostUsedElementNamespaceURI(packageInfo);
		final String mostUsedElementNamespaceURI = draftMostUsedElementNamespaceURI == null ? ""
				: draftMostUsedElementNamespaceURI;

		final String defaultElementNamespaceURI;
		if (this.defaultElementNamespaceURI != null) {
			defaultElementNamespaceURI = this.defaultElementNamespaceURI;
		} else {
			log.debug(MessageFormat
					.format("Mapping [{0}] will use \"{1}\" as it is the most used element namespace URI in the package [{2}].",
							mappingName, mostUsedElementNamespaceURI,
							packageName));
			defaultElementNamespaceURI = mostUsedElementNamespaceURI;

		}

		final String draftMostUsedAttributeNamespaceURI = analyzer
				.getMostUsedAttributeNamespaceURI(packageInfo);
		final String mostUsedAttributeNamespaceURI = draftMostUsedAttributeNamespaceURI == null ? ""
				: draftMostUsedAttributeNamespaceURI;

		final String defaultAttributeNamespaceURI;
		if (this.defaultAttributeNamespaceURI != null) {
			defaultAttributeNamespaceURI = this.defaultAttributeNamespaceURI;
		} else {
			log.debug(MessageFormat
					.format("Mapping [{0}] will use \"{1}\" as it is the most used attribute namespace URI in the package [{2}].",
							mappingName, mostUsedAttributeNamespaceURI,
							packageName));
			defaultAttributeNamespaceURI = mostUsedAttributeNamespaceURI;

		}

		return new Mapping(packageInfo, mappingName,
				defaultElementNamespaceURI, defaultAttributeNamespaceURI);

	}

	private String createMappingName(final String packageName) {
		final String name;
		if (StringUtils.isBlank(this.name)) {
			name = StringUtils.isBlank(packageName) ? "Mapping" : packageName
					.replace('.', '_');
		} else {
			name = StringUtils.trim(this.name);
		}
		return name;
	}

	private String createPackageName() {
		final String packageName;
		if (this._package == null) {
			// TODO configuration exception?
			throw new IllegalArgumentException(
					"Package name is missing, please provide the package name in the [package] attribute.");
		} else {
			packageName = StringUtils.trim(this._package);
		}
		return packageName;
	}
}
