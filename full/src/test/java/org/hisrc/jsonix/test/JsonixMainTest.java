/**
 * Jsonix is a JavaScript library which allows you to convert between XML
 * and JavaScript object structures.
 *
 * Copyright (c) 2010 - 2014, Alexey Valikov, Highsource.org
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice, this
 *   list of conditions and the following disclaimer in the documentation and/or
 *   other materials provided with the distribution.
 *
 * * Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.hisrc.jsonix.test;

import java.io.File;
import java.net.URL;

import org.hisrc.jsonix.JsonixMain;
import org.junit.Test;

public class JsonixMainTest {

	@Test
	public void compilesOWS_V_1_1_0() throws Exception {

		// Khm...
		new File("target/generated-sources/ows-v_1_1_0").mkdirs();

		URL schema = getClass().getResource("/ogc/ows/1.1.0/owsAll.xsd");
		URL binding = getClass().getResource("/ogc/ows-v_1_1_0.xjb");
		final String[] arguments = new String[] { "-xmlschema",
				schema.toExternalForm(), "-b", binding.toExternalForm(), "-d",
				"target/generated-sources/ows-v_1_1_0", "-Xjsonix-compact"

		};

		JsonixMain.main(arguments);
	}

	@Test
	public void compilesFilter_V_1_1_0() throws Exception {

		new File("target/generated-sources/filter-v_1_1_0").mkdirs();

		URL filter = getClass().getResource("/ogc/filter/1.1.0/filterAll.xsd");
		URL gml = getClass().getResource("/ogc/gml/3.1.1/base/gmlBase.xsd");
		URL xlink = getClass().getResource("/w3c/1999/xlink.xsd");
		URL binding = getClass().getResource("/ogc/filter-v_1_1_0.xjb");
		final String[] arguments = new String[] { "-xmlschema",
				filter.toExternalForm(), gml.toExternalForm(),
				xlink.toExternalForm(), "-b", binding.toExternalForm(), "-d",
				"target/generated-sources/filter-v_1_1_0", "-Xjsonix-compact"
		};

		JsonixMain.main(arguments);
	}

	@Test
	public void compilesWPS_V_1_1_0() throws Exception {

		new File("target/generated-sources/wps-v_1_0_0").mkdirs();

		URL wps = getClass().getResource("/ogc/wps/1.0.0/wpsAll.xsd");
		URL catalog = getClass().getResource("/catalog.cat");
		// URL gml = getClass().getResource("/ogc/gml/3.1.1/base/gmlBase.xsd");
		// URL xlink = getClass().getResource("/w3c/1999/xlink.xsd");
		URL binding = getClass().getResource("/ogc/wps-v_1_0_0.xjb");
		final String[] arguments = new String[] { "-xmlschema",
				wps.toExternalForm(), "-b", binding.toExternalForm(), "-d",
				"target/generated-sources/wps-v_1_0_0", "-catalog",
				catalog.toExternalForm(), "-Xjsonix-compact"

		};

		JsonixMain.main(arguments);
	}

}
