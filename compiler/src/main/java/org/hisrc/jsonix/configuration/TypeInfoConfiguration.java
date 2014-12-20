package org.hisrc.jsonix.configuration;

import java.text.MessageFormat;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.analysis.ModelInfoGraphAnalyzer;
import org.hisrc.jsonix.context.JsonixContext;
import org.jvnet.jaxb2_commons.xml.bind.model.MPackageInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;
import org.slf4j.Logger;

@XmlRootElement(name = TypeInfoConfiguration.LOCAL_ELEMENT_NAME)
@XmlType(propOrder = {})
public class TypeInfoConfiguration {

	public static final String LOCAL_ELEMENT_NAME = "type";

	private String name;

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public <T, C extends T> MTypeInfo<T, C> findTypeInfo(JsonixContext context,
			ModelInfoGraphAnalyzer<T, C> analyzer, MPackageInfo packageInfo) {
		Validate.notNull(analyzer);
		Validate.notNull(packageInfo);
		final Logger logger = Validate.notNull(context).getLoggerFactory()
				.getLogger(TypeInfoConfiguration.class.getName());
		final String name = getName();
		if (name == null) {
			logger.warn(MessageFormat
					.format("The [{0}] element does not provide the required [name] attribute and will be ignored.",
							LOCAL_ELEMENT_NAME));
			return null;
		} else {
			final MTypeInfo<T, C> typeInfo = analyzer.findTypeInfoByName(
					packageInfo, name);
			if (typeInfo == null) {
				logger.warn(MessageFormat
						.format("Could not find the type [{0}] in the package [{1}], the declaring [{2}] element will be ignored.",
								name, packageInfo.getPackageName(),
								LOCAL_ELEMENT_NAME));
			}
			return typeInfo;
		}

	}
}
