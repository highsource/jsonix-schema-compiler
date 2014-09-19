package org.hisrc.jsonix.definition;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang3.Validate;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MEnumLeafInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MModelInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPackageInfo;

public class Mapping<T, C extends T> {

	private final MModelInfo<T, C> modelInfo;
	private final MPackageInfo packageInfo;
	private final Collection<MClassInfo<T, C>> classInfos = new HashSet<MClassInfo<T, C>>();
	private final Collection<MEnumLeafInfo<T, C>> enumLeafInfos = new HashSet<MEnumLeafInfo<T, C>>();
	private final Collection<MElementInfo<T, C>> elementInfos = new HashSet<MElementInfo<T, C>>();
	private final String packageName;
	private final String mappingName;
	private final String defaultElementNamespaceURI;
	private final String defaultAttributeNamespaceURI;

	public Mapping(MModelInfo<T, C> modelInfo, MPackageInfo packageInfo,
			String mappingName, String defaultElementNamespaceURI,
			String defaultAttributeNamespaceURI) {
		Validate.notNull(modelInfo);
		Validate.notNull(packageInfo);
		Validate.notNull(mappingName);
		Validate.notNull(defaultElementNamespaceURI);
		Validate.notNull(defaultAttributeNamespaceURI);
		this.modelInfo = modelInfo;
		this.packageInfo = packageInfo;
		this.packageName = packageInfo.getPackageName();
		this.mappingName = mappingName;
		this.defaultElementNamespaceURI = defaultElementNamespaceURI;
		this.defaultAttributeNamespaceURI = defaultAttributeNamespaceURI;
		includePackage(packageInfo);
	}

	public void includePackage(MPackageInfo packageInfo) {
		for (MClassInfo<T, C> classInfo : this.modelInfo.getClassInfos()) {
			if (packageInfo == classInfo.getPackageInfo()) {
				this.classInfos.add(classInfo);
			}
		}

		for (MEnumLeafInfo<T, C> enumLeafInfo : this.modelInfo
				.getEnumLeafInfos()) {
			if (packageInfo == enumLeafInfo.getPackageInfo()) {
				this.enumLeafInfos.add(enumLeafInfo);
			}
		}

		for (MElementInfo<T, C> elementInfo : this.modelInfo.getElementInfos()) {
			if (packageInfo == elementInfo.getPackageInfo()) {
				this.elementInfos.add(elementInfo);
			}
		}
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

	public Collection<MClassInfo<T, C>> getClassInfos() {
		return this.classInfos;

	}

	public Collection<MEnumLeafInfo<T, C>> getEnumLeafInfos() {
		return this.enumLeafInfos;
	}

	public Collection<MElementInfo<T, C>> getElementInfos() {
		return this.elementInfos;
	}

	@Override
	public String toString() {
		return MessageFormat.format("Mapping [{0}]", this.mappingName);
	}
}
