package org.hisrc.jsonix.xml.xsom;

import java.util.LinkedList;
import java.util.List;

import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSAttGroupDecl;
import com.sun.xml.xsom.XSAttributeDecl;
import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSFacet;
import com.sun.xml.xsom.XSIdentityConstraint;
import com.sun.xml.xsom.XSListSimpleType;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSModelGroupDecl;
import com.sun.xml.xsom.XSNotation;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSRestrictionSimpleType;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.XSUnionSimpleType;
import com.sun.xml.xsom.XSWildcard;
import com.sun.xml.xsom.XSXPath;
import com.sun.xml.xsom.XmlString;
import com.sun.xml.xsom.visitor.XSSimpleTypeVisitor;
import com.sun.xml.xsom.visitor.XSVisitor;

public class CollectEnumerationValuesVisitor implements XSVisitor, XSSimpleTypeVisitor {

	// protected Log logger = LogFactory.getLog(getClass());

	private List<XmlString> values = new LinkedList<XmlString>();

	public List<XmlString> getValues() {
		return values;
	}

	public void annotation(XSAnnotation ann) {
		// todo("Annotation.");
	}

	public void attGroupDecl(XSAttGroupDecl decl) {
		// todo("Attribute group declaration [" + decl.getName() + "].");
	}

	public void attributeDecl(XSAttributeDecl decl) {
		decl.getType().visit((XSSimpleTypeVisitor) this);
	}

	public void attributeUse(XSAttributeUse use) {
		use.getDecl().visit(this);
	}

	public void complexType(XSComplexType type) {
		// todo("Complex type [" + type.getName() + "].");
	}

	public void facet(XSFacet facet) {
		// todo("Facet.");
	}

	public void identityConstraint(XSIdentityConstraint decl) {
		// todo("Identity constraint.");
	}

	public void notation(XSNotation notation) {
		// todo("Notation.");
	}

	public void schema(XSSchema schema) {
		// todo("Schema.");
	}

	public void xpath(XSXPath xp) {
		// todo("XPath.");
	}

	public void elementDecl(XSElementDecl decl) {
		decl.getType().visit(this);
	}

	public void modelGroup(XSModelGroup group) {
		for (XSParticle child : group.getChildren()) {
			child.visit(this);
		}
	}

	public void modelGroupDecl(XSModelGroupDecl decl) {
		// todo("Model group declaration.");
	}

	public void wildcard(XSWildcard wc) {
		// todo("Wildcard.");
	}

	public void empty(XSContentType empty) {
		// todo("Empty.");
	}

	public void particle(XSParticle particle) {
		particle.getTerm().visit(this);
	}

	public void simpleType(XSSimpleType simpleType) {
		simpleType.visit((XSSimpleTypeVisitor) this);
	}

	@Override
	public void listSimpleType(XSListSimpleType type) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unionSimpleType(XSUnionSimpleType type) {
		// for (XSSimpleType memberSimpleType : type) {
		// memberSimpleType.visit((XSSimpleTypeVisitor) this);
		// }
	}

	@Override
	public void restrictionSimpleType(XSRestrictionSimpleType type) {
		final List<XSFacet> facets = type.getFacets(XSFacet.FACET_ENUMERATION);
		if (facets != null) {
			for (XSFacet facet : facets) {
				final XmlString value = facet.getValue();
				if (value != null) {
					this.values.add(value);
				}
			}
		}
	}
}
