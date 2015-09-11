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

public class MultiplicityCounterAnyElement01Test {

	private MModelInfo<NType, NClass> model;

	@Before
	public void loadModel() throws Exception {
		model = MModelInfoLoader.INSTANCE
				.loadModel("jsonschema/minmaxoccurs/anyElement01.xsd");
	}

	@Test
	public void e01a() throws Exception {

		final MClassInfo<NType, NClass> ae01Type = model.getClassInfo("test.Ae01");

		Assert.assertNotNull(ae01Type);

		final MPropertyInfo<NType, NClass> any = ae01Type.getProperty("any");

		Assert.assertNotNull(any);

		final Multiplicity multiplicity = new XSFunctionApplier<Multiplicity>(
				ParticleMultiplicityCounter.INSTANCE).apply(any.getOrigin());

		Assert.assertEquals(2, multiplicity.min.intValue());
		Assert.assertEquals(20, multiplicity.max.intValue());

	}
}
