package org.hisrc.jsonix.configuration;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = IncludesConfiguration.LOCAL_ELEMENT_NAME)
@XmlType(propOrder = {})
public class IncludesConfiguration {

	public static final String LOCAL_ELEMENT_NAME = "includes";

	private List<TypeInfoConfiguration> typeInfoConfigurations = new LinkedList<TypeInfoConfiguration>();
	private List<ElementInfoConfiguration> elementInfoConfigurations = new LinkedList<ElementInfoConfiguration>();

	private List<PropertyInfoConfiguration> propertyInfoConfigurations = new LinkedList<PropertyInfoConfiguration>();

	@XmlElement(name = TypeInfoConfiguration.LOCAL_ELEMENT_NAME)
	public List<TypeInfoConfiguration> getTypeInfoConfigurations() {
		return typeInfoConfigurations;
	}

	public void setTypeInfoConfigurations(
			List<TypeInfoConfiguration> typeInfoConfigurations) {
		this.typeInfoConfigurations = typeInfoConfigurations;
	}

	@XmlElement(name = ElementInfoConfiguration.LOCAL_ELEMENT_NAME)
	public List<ElementInfoConfiguration> getElementInfoConfigurations() {
		return elementInfoConfigurations;
	}

	public void setElementInfoConfigurations(
			List<ElementInfoConfiguration> elementConfigurations) {
		this.elementInfoConfigurations = elementConfigurations;
	}

	@XmlElement(name = PropertyInfoConfiguration.LOCAL_ELEMENT_NAME)
	public List<PropertyInfoConfiguration> getPropertyInfoConfigurations() {
		return propertyInfoConfigurations;
	}

	public void setPropertyInfoConfigurations(
			List<PropertyInfoConfiguration> propertyInfoConfigurations) {
		this.propertyInfoConfigurations = propertyInfoConfigurations;
	}
}
