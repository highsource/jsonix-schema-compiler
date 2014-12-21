package org.hisrc.jsonix.samples.zero;

import java.io.File;
import java.net.URL;

import org.hisrc.jsonix.JsonixMain;
import org.junit.Test;

public class ZeroMainTest {

	@Test
	public void compilesZero() throws Exception {

		// Khm...
		new File("target/generated-sources/zero").mkdirs();

		URL schema = getClass().getResource("schema.xsd");

		final String[] arguments = new String[] { "-compact", "-extension", "-npa", "-logLevel", "INFO", "-xmlschema",
				schema.toExternalForm(), "-d", "target/generated-sources/zero"

		};

		JsonixMain.main(arguments);
	}

}
