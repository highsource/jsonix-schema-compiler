package org.hisrc.jsonix.configuration.tests;

import org.hisrc.jsonix.configuration.exception.AmbiguousPackageMappingNameException;
import org.junit.Assert;
import org.junit.Test;

public class AmbiguousPackageMappingExceptionTest {

	@Test
	public void producesReadableMessage() {
		final Exception ex = new AmbiguousPackageMappingNameException("a", "b",
				"c");
		Assert.assertTrue(ex.getMessage().contains("b, c"));
	}
}
