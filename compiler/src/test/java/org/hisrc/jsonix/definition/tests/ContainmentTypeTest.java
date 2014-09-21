package org.hisrc.jsonix.definition.tests;

import org.hisrc.jsonix.definition.ContainmentType;
import org.junit.Assert;
import org.junit.Test;

public class ContainmentTypeTest {

	@Test
	public void orderOfEnumsIsCorrect() {
		Assert.assertTrue(ContainmentType.EXPLICIT
				.compareTo(ContainmentType.AS_SOFT_DEPENDENCY) < 0);
	}

	@Test
	public void toStringIsCorrect() {
		Assert.assertEquals("EXPLICIT", ContainmentType.EXPLICIT.toString());
	}
}
