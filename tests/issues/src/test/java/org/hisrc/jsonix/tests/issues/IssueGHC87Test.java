package org.hisrc.jsonix.tests.issues;

import static org.junit.Assert.assertEquals;

import org.hisrc.jsonix.tests.issues.IssueGHC87ExternalEnumType;
import org.hisrc.jsonix.tests.issues.IssueGHC87Type;
import org.junit.Test;

public class IssueGHC87Test {

	@Test
	public void testIssueGHC87TypeDefaults() {

		final IssueGHC87Type type = new IssueGHC87Type();

		assertEquals(type.getExternalEnum(), IssueGHC87ExternalEnumType.TEST_1);
		assertEquals(type.getInternalEnum(), "test1");
		assertEquals(type.getExternalPattern(), "123");
		assertEquals(type.getInternalPattern(), "123");
	}

}