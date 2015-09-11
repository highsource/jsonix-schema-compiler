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

public class MultiplicityCounterElement01Test {

	private MModelInfo<NType, NClass> model;

	@Before
	public void loadModel() throws Exception {
		model = MModelInfoLoader.INSTANCE
				.loadModel("jsonschema/minmaxoccurs/element01.xsd");
	}

	@Test
	public void e01a() throws Exception {

		final MClassInfo<NType, NClass> eType = model.getClassInfo("test.E01");

		Assert.assertNotNull(eType);

		final MPropertyInfo<NType, NClass> a = eType.getProperty("a");

		Assert.assertNotNull(a);

		final Multiplicity multiplicity = new XSFunctionApplier<Multiplicity>(
				ParticleMultiplicityCounter.INSTANCE).apply(a.getOrigin());

		Assert.assertEquals(10, multiplicity.min.intValue());
		Assert.assertEquals(20, multiplicity.max.intValue());

	}
}
