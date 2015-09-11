package org.hisrc.jsonix.xml.xsom;

import java.math.BigInteger;

import org.hisrc.xml.xsom.DefaultFunctionImpl;

import com.sun.tools.xjc.model.Multiplicity;
import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.visitor.XSTermFunction;

public class ParticleMultiplicityCounter extends
		DefaultFunctionImpl<Multiplicity> {

	public static final ParticleMultiplicityCounter INSTANCE = new ParticleMultiplicityCounter();

	private final XSTermFunction<Multiplicity> counter = MultiplicityCounterNG.INSTANCE;

	protected ParticleMultiplicityCounter() {
		super();
	}

	@Override
	public Multiplicity particle(XSParticle p) {

		Multiplicity m = p.getTerm().apply(this.counter);

		BigInteger max;
		if (m.max == null
				|| (BigInteger.valueOf(XSParticle.UNBOUNDED).equals(p
						.getMaxOccurs())))
			max = null;
		else
			max = p.getMaxOccurs();

		return Multiplicity.multiply(m,
				Multiplicity.create(p.getMinOccurs(), max));
	}

	@Override
	public Multiplicity attributeUse(XSAttributeUse use) {
		return use.isRequired() ? Multiplicity.ONE : Multiplicity.OPTIONAL;
	}

}
