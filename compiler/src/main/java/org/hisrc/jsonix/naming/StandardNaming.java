package org.hisrc.jsonix.naming;

public class StandardNaming implements Naming {

	public static final Naming INSTANCE = new StandardNaming();

	public static final String TYPE = "type"; // t
	public static final String TYPE_INFO = "typeInfo"; // ti
	public static final String LIST = "list"; // li
	public static final String CLASS_INFO = "classInfo"; // ci
	public static final String LOCAL_NAME = "localName"; // ln
	public static final String TYPE_NAME = "typeName"; // tn
	public static final String BASE_TYPE_INFO = "baseTypeInfo"; // base
	public static final String PROPERTY_INFOS = "propertyInfos"; // ps
	public static final String ENUM_INFO = "enumInfo"; // ei
	public static final String VALUES = "values"; // vs
	public static final String ELEMENT_NAME = "elementName"; // en
	public static final String SCOPE = "scope"; // sc
	public static final String SUBSTITUTION_HEAD = "substitutionHead"; // sh
	public static final String NAME = "name"; // n
	public static final String DEPENDENCIES = "dependencies"; // deps
	public static final String TARGET_NAMESPACE_URI = "targetNamespace"; // dens
	public static final String DEFAULT_ELEMENT_NAMESPACE_URI = "defaultElementNamespaceURI"; // dens
	public static final String DEFAULT_ATTRIBUTE_NAMESPACE_URI = "defaultAttributeNamespaceURI"; // dans
	public static final String TYPE_INFOS = "typeInfos"; // tis
	public static final String ELEMENT_INFOS = "elementInfos"; // eis
	public static final String LOCAL_PART = "localPart"; // lp
	public static final String NAMESPACE_URI = "namespaceURI"; // ns
	public static final String PREFIX = "prefix";// p
	public static final String WRAPPER_ELEMENT_NAME = "wrapperElementName"; // wr
	public static final String ALLOW_DOM = "allowDom"; // dom
	public static final String ALLOW_TYPED_OBJECT = "allowTypedObject"; // typ
	public static final String MIXED = "mixed"; // mx
	public static final String ELEMENT = "element"; // e
	public static final String ELEMENTS = "elements"; // es
	public static final String ELEMENT_TYPE_INFOS = "elementTypeInfos"; // etis
	public static final String ANY_ELEMENT = "anyElement"; // ae
	public static final String ATTRIBUTE_NAME = "attributeName"; // an
	public static final String ATTRIBUTE = "attribute"; // a
	public static final String ANY_ATTRIBUTE = "anyAttribute"; // aa
	public static final String VALUE = "value"; // v
	public static final String DEFAULT_VALUE = "defaultValue"; // dv
	public static final String ELEMENT_REF = "elementRef";// rf
	public static final String ELEMENT_REFS = "elementRefs";// rfs
	public static final String COLLECTION = "collection";// col

	public static final String REQUIRED = "required";
	public static final String MINOCCURS = "minOccurs";
	public static final String MAXOCCURS = "maxOccurs";

	public static final String NAMING_NAME = "standard";

	public StandardNaming() {
	}

	@Override
	public String type() {
		return TYPE;
	}

	@Override
	public String typeInfo() {
		return TYPE_INFO;
	}

	@Override
	public String list() {
		return LIST;
	}

	//
	@Override
	public String localName() {
		return LOCAL_NAME;
	}

	@Override
	public String typeName() {
		return TYPE_NAME;
	}

	// classInfo properties
	@Override
	public String classInfo() {
		return CLASS_INFO;
	}

	@Override
	public String baseTypeInfo() {
		return BASE_TYPE_INFO;
	}

	@Override
	public String propertyInfos() {
		return PROPERTY_INFOS;
	}

	// enumInfo
	@Override
	public String enumInfo() {
		return ENUM_INFO;
	}

	@Override
	public String values() {
		return VALUES;
	}

	@Override
	public String elementName() {
		return ELEMENT_NAME;
	}

	@Override
	public String scope() {
		return SCOPE;
	}

	@Override
	public String substitutionHead() {
		return SUBSTITUTION_HEAD;
	}

	// propertyInfo properties
	@Override
	public String name() {
		return NAME;
	}

	@Override
	public String collection() {
		return COLLECTION;
	}

	@Override
	public String wrapperElementName() {
		return WRAPPER_ELEMENT_NAME;
	}

	@Override
	public String allowDom() {
		return ALLOW_DOM;
	}

	@Override
	public String allowTypedObject() {
		return ALLOW_TYPED_OBJECT;
	}

	@Override
	public String mixed() {
		return MIXED;
	}

	@Override
	public String attributeName() {
		return ATTRIBUTE_NAME;
	}

	@Override
	public String element() {
		return ELEMENT;
	}

	@Override
	public String anyAttribute() {
		return ANY_ATTRIBUTE;
	}

	@Override
	public String anyElement() {
		return ANY_ELEMENT;
	}

	@Override
	public String attribute() {
		return ATTRIBUTE;
	}

	@Override
	public String elementRef() {
		return ELEMENT_REF;
	}

	@Override
	public String elementRefs() {
		return ELEMENT_REFS;
	}

	@Override
	public String elements() {
		return ELEMENTS;
	}

	@Override
	public String elementTypeInfos() {
		return ELEMENT_TYPE_INFOS;
	}

	@Override
	public String value() {
		return VALUE;
	}
	
	@Override
	public String defaultValue() {
		return DEFAULT_VALUE;
	}

	@Override
	public String namespaceURI() {
		return NAMESPACE_URI;
	}

	@Override
	public String prefix() {
		return PREFIX;
	}

	@Override
	public String localPart() {
		return LOCAL_PART;
	}

	@Override
	public String dependencies() {
		return DEPENDENCIES;
	}

	@Override
	public String targetNamespaceURI() {
		return TARGET_NAMESPACE_URI;
	}

	@Override
	public String defaultElementNamespaceURI() {
		return DEFAULT_ELEMENT_NAMESPACE_URI;
	}

	@Override
	public String defaultAttributeNamespaceURI() {
		return DEFAULT_ATTRIBUTE_NAMESPACE_URI;
	}

	@Override
	public String elementInfos() {
		return ELEMENT_INFOS;
	}

	@Override
	public String typeInfos() {
		return TYPE_INFOS;
	}

	@Override
	public String required() {
		return REQUIRED;
	}

	@Override
	public String minOccurs() {
		return MINOCCURS;
	}

	@Override
	public String maxOccurs() {
		return MAXOCCURS;
	}

}
