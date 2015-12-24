package org.hisrc.jsonix.xml.xsom;

import static com.sun.tools.xjc.model.Multiplicity.ONE;
import static com.sun.tools.xjc.model.Multiplicity.ZERO;

import java.math.BigInteger;

import com.sun.tools.xjc.model.Multiplicity;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSModelGroupDecl;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSWildcard;
import com.sun.xml.xsom.visitor.XSTermFunction;

public class MultiplicityCounterNG implements XSTermFunction<Multiplicity> {

	public static final XSTermFunction<Multiplicity> INSTANCE = new MultiplicityCounterNG();

	private MultiplicityCounterNG() {
	}

	public Multiplicity particle(XSParticle p) {
		Multiplicity m = p.getTerm().apply(this);

		BigInteger max;
		if (m == null
				|| m.max == null
				|| (BigInteger.valueOf(XSParticle.UNBOUNDED).equals(p
						.getMaxOccurs())))
			max = null;
		else
			max = p.getMaxOccurs();

		return Multiplicity.multiply(m,
				Multiplicity.create(p.getMinOccurs(), max));
	}

	public Multiplicity wildcard(XSWildcard wc) {
		return ONE;
	}

	public Multiplicity modelGroupDecl(XSModelGroupDecl decl) {
		return modelGroup(decl.getModelGroup());
	}

	public Multiplicity modelGroup(XSModelGroup group) {
		boolean isChoice = group.getCompositor() == XSModelGroup.CHOICE;

		Multiplicity r = null;

		for (XSParticle p : group.getChildren()) {
			Multiplicity m = particle(p);

			if (r == null) {
				r = m;
				continue;
			}
			if (isChoice) {
				r = Multiplicity.choice(r, m);
			} else {
				r = Multiplicity.group(r, m);
			}
		}
		if (r == null)
		{
			return ZERO;
		}
		return r;
	}

	public Multiplicity elementDecl(XSElementDecl decl) {
		return ONE;
	}

}
