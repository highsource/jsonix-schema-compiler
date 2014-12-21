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

package org.hisrc.jsonix.xjc.plugin;

import java.io.IOException;
import java.util.List;

import org.hisrc.jsonix.configuration.OutputConfiguration;
import org.hisrc.jsonix.configuration.PluginCustomizations;
import org.hisrc.jsonix.execution.JsonixInvoker;
import org.hisrc.jsonix.naming.CompactNaming;
import org.hisrc.jsonix.naming.StandardNaming;
import org.hisrc.jsonix.settings.Settings;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import com.sun.tools.xjc.BadCommandLineException;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.outline.Outline;

public class JsonixPlugin extends Plugin {

	// private final Logger logger =
	// LoggerFactory.getLogger(JsonixPlugin.class);
	private final PluginCustomizations pluginCustomizations = new PluginCustomizations();

	public static final String OPTION_NAME = "Xjsonix";
	public static final String OPTION = "-" + OPTION_NAME;
	public static final String OPTION_COMPACT = OPTION + "-compact";

	private OutputConfiguration defaultOutputConfiguration = new OutputConfiguration(
			StandardNaming.NAMING_NAME,
			OutputConfiguration.STANDARD_FILE_NAME_PATTERN);

	public OutputConfiguration getDefaultOutputConfiguration() {
		return defaultOutputConfiguration;
	}

	public void setDefaultOutputConfiguration(OutputConfiguration naming) {
		this.defaultOutputConfiguration = naming;
	}

	@Override
	public String getOptionName() {
		return OPTION_NAME;
	}

	@Override
	public String getUsage() {
		return "TBD";
	}

	@Override
	public int parseArgument(Options opt, String[] args, int i)
			throws BadCommandLineException, IOException {
		if (OPTION_COMPACT.equals(args[i])) {
			setDefaultOutputConfiguration(new OutputConfiguration(
					CompactNaming.NAMING_NAME,
					OutputConfiguration.STANDARD_FILE_NAME_PATTERN));
			return 1;
		} else {
			return super.parseArgument(opt, args, i);
		}
	}

	@Override
	public void onActivated(Options opts) throws BadCommandLineException {
		final ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory();
		if (iLoggerFactory instanceof NOPLoggerFactory) {
			System.err
					.println("You seem to be using the NOP provider of the SLF4j logging facade. "
							+ "With this configuration, log messages will be completely suppressed. "
							+ "Please consider adding a SLF4j provider (for instance slf4j-simple) to enable logging.");
		}
	}

	@Override
	public void postProcessModel(Model model, ErrorHandler errorHandler) {
	}

	@Override
	public List<String> getCustomizationURIs() {
		return this.pluginCustomizations.getCustomizationURIs();
	}

	@Override
	public boolean isCustomizationTagName(String nsUri, String localName) {
		return this.pluginCustomizations.isCustomizationTagName(nsUri,
				localName);
	}

	@Override
	public boolean run(Outline outline, Options opt,
			final ErrorHandler errorHandler) throws SAXException {

		final Model model = outline.getModel();
		final Settings settings = null;

		new JsonixInvoker().execute(context, settings, model,
				new CodeModelProgramWriter(model.codeModel, errorHandler));

		return true;
	}

}
