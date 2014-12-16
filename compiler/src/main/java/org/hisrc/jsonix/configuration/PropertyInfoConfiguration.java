package org.hisrc.jsonix.configuration;

import java.text.MessageFormat;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.analysis.ModelInfoGraphAnalyzer;
import org.hisrc.jsonix.context.JsonixContext;
import org.jvnet.jaxb2_commons.xml.bind.model.MPackageInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;
import org.slf4j.Logger;

@XmlRootElement(name = PropertyInfoConfiguration.LOCAL_ELEMENT_NAME)
@XmlType(propOrder = {})
public class PropertyInfoConfiguration {

	private final Logger logger;

	public static final String LOCAL_ELEMENT_NAME = "property";

	private String name;

	public PropertyInfoConfiguration(JsonixContext context) {
		this.logger = Validate.notNull(context).getLoggerFactory()
				.getLogger(PropertyInfoConfiguration.class.getName());
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public <T, C extends T> MPropertyInfo<T, C> findPropertyInfo(
			ModelInfoGraphAnalyzer<T, C> analyzer, MPackageInfo packageInfo) {
		Validate.notNull(analyzer);
		Validate.notNull(packageInfo);
		final String name = getName();
		if (name == null) {
			logger.warn(MessageFormat
					.format("The [{0}] element does not provide the required [name] attribute and will be ignored.",
							LOCAL_ELEMENT_NAME));
			return null;
		} else {
			final MPropertyInfo<T, C> propertyInfo = analyzer
					.findPropertyInfoByName(packageInfo, name);
			if (propertyInfo == null) {
				logger.warn(MessageFormat
						.format("Could not find the type [{0}] in the package [{1}], the declaring [{2}] element will be ignored.",
								name, packageInfo.getPackageName(),
								LOCAL_ELEMENT_NAME));
			}
			return propertyInfo;
		}

	}
}
