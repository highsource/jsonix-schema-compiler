package org.hisrc.jsonix.naming;

public interface Naming {

	String name();

	String dependencies();

	String targetNamespaceURI();

	String defaultElementNamespaceURI();

	String defaultAttributeNamespaceURI();

	String elementInfos();

	String typeInfos();

	String type();

	//
	String typeInfo();

	String list();

	//
	String localName();

	//
	String typeName();

	// classInfo properties
	String classInfo();

	String baseTypeInfo();

	String propertyInfos();

	// enumInfo properties
	String enumInfo();

	String values();

	// elementInfo properties
	String elementName();

	String scope();

	String substitutionHead();

	// propertyInfo properties
	String collection();

	String wrapperElementName();

	String allowDom();

	String allowTypedObject();

	String mixed();

	String attributeName();

	String elementTypeInfos();
	
	// types of property Infos
	String element();

	String elements();

	String elementRef();

	String elementRefs();

	String anyElement();

	String attribute();

	String anyAttribute();

	String value();

	String defaultValue();

	String namespaceURI();

	String prefix();

	String localPart();

	String required();

	String minOccurs();

	String maxOccurs();
}
