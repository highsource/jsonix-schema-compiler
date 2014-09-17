package org.hisrc.jsonix.configuration;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

@XmlRootElement(name = PackageMapping.LOCAL_ELEMENT_NAME)
@XmlType
public class PackageMapping {

	public static final String LOCAL_ELEMENT_NAME = "packageMapping";
	private String packageName;

	@XmlAttribute
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String name) {
		this.packageName = name;
	}

	private String outputPackageName;

	@XmlAttribute
	public String getOutputPackageName() {
		return outputPackageName;
	}

	public void setOutputPackageName(String name) {
		this.outputPackageName = name;
	}

	private String spaceName;

	@XmlAttribute
	public String getSpaceName() {
		return spaceName;
	}

	public void setSpaceName(String space) {
		this.spaceName = space;
	}

	private String defaultElementNamespaceURI = null;

	@XmlAttribute
	public String getDefaultElementNamespaceURI() {
		return defaultElementNamespaceURI;
	}

	public void setDefaultElementNamespaceURI(String namespaceURI) {
		this.defaultElementNamespaceURI = namespaceURI;
	}

	private String defaultAttributeNamespaceURI = null;

	@XmlAttribute
	public String getDefaultAttributeNamespaceURI() {
		return defaultAttributeNamespaceURI;
	}

	public void setDefaultAttributeNamespaceURI(String namespaceURI) {
		this.defaultAttributeNamespaceURI = namespaceURI;
	}

	private String defaultPrefix;

	@XmlAttribute
	public String getDefaultPrefix() {
		return defaultPrefix;
	}

	public void setDefaultPrefix(String defaultPrefix) {
		this.defaultPrefix = defaultPrefix;
	}

	private String directory;

	@XmlAttribute
	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	private String fileName;

	@XmlAttribute
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String file) {
		this.fileName = file;
	}

	@Deprecated
	private String declarationsFileName;

	@Deprecated
	@XmlAttribute
	public String getDeclarationsFileName() {
		return declarationsFileName;
	}

	@Deprecated
	public void setDeclarationsFileName(String declarationsFile) {
		this.declarationsFileName = declarationsFile;
	}

	@Deprecated
	private String structuresFileName;
	public static final QName PACKAGE_MAPPING_NAME = new QName(ModulesConfiguration.NAMESPACE_URI,
	LOCAL_ELEMENT_NAME, ModulesConfiguration.DEFAULT_PREFIX);

	@Deprecated
	@XmlAttribute
	public String getStructuresFileName() {
		return structuresFileName;
	}

	@Deprecated
	public void setStructuresFileName(String structuresFile) {
		this.structuresFileName = structuresFile;
	}

}
