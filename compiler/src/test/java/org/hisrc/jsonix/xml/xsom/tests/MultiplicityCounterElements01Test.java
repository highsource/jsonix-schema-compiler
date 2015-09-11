package org.hisrc.jsonix.xml.xsom.tests;

import org.hisrc.jsonix.xml.xsom.ParticleMultiplicityCounter;
import org.hisrc.xml.bind.model.util.MModelInfoLoader;
import org.hisrc.xml.xsom.XSFunctionApplier;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MModelInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;

import com.sun.tools.xjc.model.Multiplicity;
import com.sun.tools.xjc.model.nav.NClass;
import com.sun.tools.xjc.model.nav.NType;

public class MultiplicityCounterElements01Test {

	private MModelInfo<NType, NClass> model;

	@Before
	public void loadModel() throws Exception {
		model = MModelInfoLoader.INSTANCE
				.loadModel("jsonschema/minmaxoccurs/elements01.xsd");
	}

	@Test
	public void es01aOrB() throws Exception {

		final MClassInfo<NType, NClass> eType = model.getClassInfo("test.Es01");
		final MPropertyInfo<NType, NClass> a = eType.getProperty("aOrB");
		final Multiplicity multiplicity = new XSFunctionApplier<Multiplicity>(
				ParticleMultiplicityCounter.INSTANCE).apply(a.getOrigin());

		Assert.assertEquals(50, multiplicity.min.intValue());
		Assert.assertEquals(400, multiplicity.max.intValue());

	}

	@Test
	public void es02aOrB() throws Exception {

		final MClassInfo<NType, NClass> eType = model.getClassInfo("test.Es02");
		final MPropertyInfo<NType, NClass> a = eType.getProperty("aOrB");
		final Multiplicity multiplicity = new XSFunctionApplier<Multiplicity>(
				ParticleMultiplicityCounter.INSTANCE).apply(a.getOrigin());

		Assert.assertEquals(50, multiplicity.min.intValue());
		Assert.assertEquals(400, multiplicity.max.intValue());

	}
}
