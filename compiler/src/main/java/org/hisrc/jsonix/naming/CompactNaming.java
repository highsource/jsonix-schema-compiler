package org.hisrc.jsonix.naming;


public class CompactNaming implements Naming {

	public static final Naming INSTANCE = new CompactNaming();

	public static final String TYPE = "t";
	public static final String TYPE_INFO = "ti";

	public static final String LIST = "l";
	public static final String CLASS_INFO = "c";
	public static final String ENUM_INFO = "enum";

	public static final String ELEMENT_NAME = "en";
	public static final String ATTRIBUTE_NAME = "an";
	public static final String LOCAL_NAME = "ln";
	public static final String LOCAL_PART = "lp";
	public static final String BASE_TYPE_INFO = "bti";
	public static final String PROPERTY_INFOS = "ps";
	public static final String SCOPE = "sc";
	public static final String SUBSTITUTION_HEAD = "sh";
	public static final String NAME = "n";
	public static final String DEPENDENCIES = "deps";
	public static final String DEFAULT_ELEMENT_NAMESPACE_URI = "dens";
	public static final String DEFAULT_ATTRIBUTE_NAMESPACE_URI = "dans";
	public static final String NAMESPACE_URI = "ns";
	public static final String PREFIX = "p";
	public static final String WRAPPER_ELEMENT_NAME = "wen";
	public static final String ALLOW_DOM = "dom";
	public static final String ALLOW_TYPED_OBJECT = "typed";
	public static final String COLLECTION = "col";
	public static final String ELEMENT_TYPE_INFOS = "etis";
	public static final String VALUES = "vs";

	public static final String TYPE_INFOS = "tis";
	public static final String ELEMENT_INFOS = "eis";

	public static final String MIXED = "mx";

	public static final String ELEMENT = "e";
	public static final String ELEMENTS = "es";
	public static final String ANY_ELEMENT = "ae";
	public static final String ATTRIBUTE = "a";
	public static final String ANY_ATTRIBUTE = "aa";
	public static final String VALUE = "v";
	public static final String ELEMENT_REF = "er";
	public static final String ELEMENT_REFS = "ers";

	public static final String NAMING_NAME = "compact";

	public CompactNaming() {
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
}
