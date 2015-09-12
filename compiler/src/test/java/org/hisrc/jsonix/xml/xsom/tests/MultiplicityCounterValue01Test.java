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

public class MultiplicityCounterValue01Test {

	private MModelInfo<NType, NClass> model;

	@Before
	public void loadModel() throws Exception {
		model = MModelInfoLoader.INSTANCE
				.loadModel("jsonschema/minmaxoccurs/value01.xsd");
	}

	@Test
	public void v01value() throws Exception {

		final MClassInfo<NType, NClass> v01Type = model
				.getClassInfo("test.V01");

		Assert.assertNotNull(v01Type);

		final MPropertyInfo<NType, NClass> value = v01Type.getProperty("value");

		Assert.assertNotNull(value);

		final Multiplicity multiplicity = new XSFunctionApplier<Multiplicity>(
				ParticleMultiplicityCounter.INSTANCE).apply(value.getOrigin());

		Assert.assertNull(multiplicity);

	}
}
