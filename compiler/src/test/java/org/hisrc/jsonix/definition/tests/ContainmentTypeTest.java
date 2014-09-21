package org.hisrc.jsonix.definition.tests;

import org.hisrc.jsonix.definition.ContainmentType;
import org.junit.Assert;
import org.junit.Test;

public class ContainmentTypeTest {

	@Test
	public void orderOfEnumsIsCorrect() {
		Assert.assertTrue(ContainmentType.INCLUDED_EXPLICITLY
				.compareTo(ContainmentType.INCLUDED_AS_SOFT_DEPENDENCY) < 0);
	}

	@Test
	public void toStringIsCorrect() {
		Assert.assertEquals("INCLUDED_EXPLICITLY",
				ContainmentType.INCLUDED_EXPLICITLY.toString());
	}

	@Test
	public void combineWithProducesCorrectResults() {
		Assert.assertEquals(ContainmentType.EXCLUDED_EXPLICITLY,
				ContainmentType.EXCLUDED_EXPLICITLY.combineWith(null));
		Assert.assertEquals(ContainmentType.EXCLUDED_EXPLICITLY,
				ContainmentType.EXCLUDED_EXPLICITLY
						.combineWith(ContainmentType.EXCLUDED_EXPLICITLY));
		Assert.assertEquals(
				ContainmentType.EXCLUDED_EXPLICITLY,
				ContainmentType.EXCLUDED_EXPLICITLY
						.combineWith(ContainmentType.EXCLUDED_AS_HARD_DEPENDENCY));
		Assert.assertEquals(ContainmentType.INCLUDED_EXPLICITLY,
				ContainmentType.EXCLUDED_EXPLICITLY
						.combineWith(ContainmentType.INCLUDED_EXPLICITLY));
		Assert.assertEquals(
				ContainmentType.INCLUDED_AS_HARD_DEPENDENCY,
				ContainmentType.EXCLUDED_EXPLICITLY
						.combineWith(ContainmentType.INCLUDED_AS_HARD_DEPENDENCY));
		Assert.assertEquals(
				ContainmentType.EXCLUDED_EXPLICITLY,
				ContainmentType.EXCLUDED_EXPLICITLY
						.combineWith(ContainmentType.INCLUDED_AS_SOFT_DEPENDENCY));

		Assert.assertEquals(ContainmentType.EXCLUDED_AS_HARD_DEPENDENCY,
				ContainmentType.EXCLUDED_AS_HARD_DEPENDENCY.combineWith(null));
		Assert.assertEquals(ContainmentType.EXCLUDED_EXPLICITLY,
				ContainmentType.EXCLUDED_AS_HARD_DEPENDENCY
						.combineWith(ContainmentType.EXCLUDED_EXPLICITLY));
		Assert.assertEquals(
				ContainmentType.EXCLUDED_AS_HARD_DEPENDENCY,
				ContainmentType.EXCLUDED_AS_HARD_DEPENDENCY
						.combineWith(ContainmentType.EXCLUDED_AS_HARD_DEPENDENCY));
		Assert.assertEquals(ContainmentType.INCLUDED_EXPLICITLY,
				ContainmentType.EXCLUDED_AS_HARD_DEPENDENCY
						.combineWith(ContainmentType.INCLUDED_EXPLICITLY));
		Assert.assertEquals(
				ContainmentType.INCLUDED_AS_HARD_DEPENDENCY,
				ContainmentType.EXCLUDED_AS_HARD_DEPENDENCY
						.combineWith(ContainmentType.INCLUDED_AS_HARD_DEPENDENCY));
		Assert.assertEquals(
				ContainmentType.EXCLUDED_AS_HARD_DEPENDENCY,
				ContainmentType.EXCLUDED_AS_HARD_DEPENDENCY
						.combineWith(ContainmentType.INCLUDED_AS_SOFT_DEPENDENCY));

		Assert.assertEquals(ContainmentType.INCLUDED_EXPLICITLY,
				ContainmentType.INCLUDED_EXPLICITLY.combineWith(null));
		Assert.assertEquals(ContainmentType.INCLUDED_EXPLICITLY,
				ContainmentType.INCLUDED_EXPLICITLY
						.combineWith(ContainmentType.EXCLUDED_EXPLICITLY));
		Assert.assertEquals(
				ContainmentType.INCLUDED_EXPLICITLY,
				ContainmentType.INCLUDED_EXPLICITLY
						.combineWith(ContainmentType.EXCLUDED_AS_HARD_DEPENDENCY));
		Assert.assertEquals(ContainmentType.INCLUDED_EXPLICITLY,
				ContainmentType.INCLUDED_EXPLICITLY
						.combineWith(ContainmentType.INCLUDED_EXPLICITLY));
		Assert.assertEquals(
				ContainmentType.INCLUDED_EXPLICITLY,
				ContainmentType.INCLUDED_EXPLICITLY
						.combineWith(ContainmentType.INCLUDED_AS_HARD_DEPENDENCY));
		Assert.assertEquals(
				ContainmentType.INCLUDED_EXPLICITLY,
				ContainmentType.INCLUDED_EXPLICITLY
						.combineWith(ContainmentType.INCLUDED_AS_SOFT_DEPENDENCY));

		Assert.assertEquals(ContainmentType.INCLUDED_AS_HARD_DEPENDENCY,
				ContainmentType.INCLUDED_AS_HARD_DEPENDENCY.combineWith(null));
		Assert.assertEquals(ContainmentType.INCLUDED_AS_HARD_DEPENDENCY,
				ContainmentType.INCLUDED_AS_HARD_DEPENDENCY
						.combineWith(ContainmentType.EXCLUDED_EXPLICITLY));
		Assert.assertEquals(
				ContainmentType.INCLUDED_AS_HARD_DEPENDENCY,
				ContainmentType.INCLUDED_AS_HARD_DEPENDENCY
						.combineWith(ContainmentType.EXCLUDED_AS_HARD_DEPENDENCY));
		Assert.assertEquals(ContainmentType.INCLUDED_EXPLICITLY,
				ContainmentType.INCLUDED_AS_HARD_DEPENDENCY
						.combineWith(ContainmentType.INCLUDED_EXPLICITLY));
		Assert.assertEquals(
				ContainmentType.INCLUDED_AS_HARD_DEPENDENCY,
				ContainmentType.INCLUDED_AS_HARD_DEPENDENCY
						.combineWith(ContainmentType.INCLUDED_AS_HARD_DEPENDENCY));
		Assert.assertEquals(
				ContainmentType.INCLUDED_AS_HARD_DEPENDENCY,
				ContainmentType.INCLUDED_AS_HARD_DEPENDENCY
						.combineWith(ContainmentType.INCLUDED_AS_SOFT_DEPENDENCY));

		Assert.assertEquals(ContainmentType.INCLUDED_AS_SOFT_DEPENDENCY,
				ContainmentType.INCLUDED_AS_SOFT_DEPENDENCY.combineWith(null));
		Assert.assertEquals(ContainmentType.EXCLUDED_EXPLICITLY,
				ContainmentType.INCLUDED_AS_SOFT_DEPENDENCY
						.combineWith(ContainmentType.EXCLUDED_EXPLICITLY));
		Assert.assertEquals(
				ContainmentType.EXCLUDED_AS_HARD_DEPENDENCY,
				ContainmentType.INCLUDED_AS_SOFT_DEPENDENCY
						.combineWith(ContainmentType.EXCLUDED_AS_HARD_DEPENDENCY));
		Assert.assertEquals(ContainmentType.INCLUDED_EXPLICITLY,
				ContainmentType.INCLUDED_AS_SOFT_DEPENDENCY
						.combineWith(ContainmentType.INCLUDED_EXPLICITLY));
		Assert.assertEquals(
				ContainmentType.INCLUDED_AS_HARD_DEPENDENCY,
				ContainmentType.INCLUDED_AS_SOFT_DEPENDENCY
						.combineWith(ContainmentType.INCLUDED_AS_HARD_DEPENDENCY));
		Assert.assertEquals(
				ContainmentType.INCLUDED_AS_SOFT_DEPENDENCY,
				ContainmentType.INCLUDED_AS_SOFT_DEPENDENCY
						.combineWith(ContainmentType.INCLUDED_AS_SOFT_DEPENDENCY));
	}
}
