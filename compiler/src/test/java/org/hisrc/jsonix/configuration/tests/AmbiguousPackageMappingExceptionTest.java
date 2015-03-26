package org.hisrc.jsonix.configuration.tests;

import org.hisrc.jsonix.configuration.AmbiguousPackageMappingException;
import org.junit.Assert;
import org.junit.Test;

public class AmbiguousPackageMappingExceptionTest {

	@Test
	public void producesReasableMessage() {
		final Exception ex = new AmbiguousPackageMappingException("a", "b", "c");
		Assert.assertTrue(ex.getMessage().contains("b, c"));
	}
}
