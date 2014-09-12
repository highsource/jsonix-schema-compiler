package org.hisrc.jsonix.compiler;

public interface Naming {
	
	String name();

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
	
	String namespaceURI();
	String prefix();
	String localPart();
	

}
