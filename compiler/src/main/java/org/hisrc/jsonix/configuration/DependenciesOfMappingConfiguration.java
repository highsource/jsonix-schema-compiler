package org.hisrc.jsonix.configuration;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = DependenciesOfMappingConfiguration.LOCAL_ELEMENT_NAME)
@XmlType(propOrder = {})
public class DependenciesOfMappingConfiguration {

	public static final String LOCAL_ELEMENT_NAME = "dependencies-of-mapping";

	private String name;

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String id;

	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
