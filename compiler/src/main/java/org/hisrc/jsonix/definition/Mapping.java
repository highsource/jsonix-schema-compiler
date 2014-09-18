package org.hisrc.jsonix.definition;

import java.text.MessageFormat;

import org.apache.commons.lang3.Validate;
import org.jvnet.jaxb2_commons.xml.bind.model.MPackageInfo;

public class Mapping {

	private final MPackageInfo packageInfo;
	private final String packageName;
	private final String mappingName;
	private final String defaultElementNamespaceURI;
	private final String defaultAttributeNamespaceURI;

	public Mapping(MPackageInfo packageInfo, String mappingName,
			String defaultElementNamespaceURI,
			String defaultAttributeNamespaceURI) {
		Validate.notNull(packageInfo);
		Validate.notNull(mappingName);
		Validate.notNull(defaultElementNamespaceURI);
		Validate.notNull(defaultAttributeNamespaceURI);
		this.packageInfo = packageInfo;
		this.packageName = packageInfo.getPackageName();
		this.mappingName = mappingName;
		this.defaultElementNamespaceURI = defaultElementNamespaceURI;
		this.defaultAttributeNamespaceURI = defaultAttributeNamespaceURI;
	}
	
	public MPackageInfo getPackageInfo() {
		return packageInfo;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getMappingName() {
		return mappingName;
	}


	public String getDefaultElementNamespaceURI() {
		return defaultElementNamespaceURI;
	}

	public String getDefaultAttributeNamespaceURI() {
		return defaultAttributeNamespaceURI;
	}

	@Override
	public String toString() {
		return MessageFormat.format("Mapping [{0}]", this.mappingName);
	}
}
