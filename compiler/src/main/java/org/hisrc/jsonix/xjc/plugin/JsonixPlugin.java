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
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.hisrc.jscm.codemodel.JSProgram;
import org.hisrc.jscm.codemodel.writer.CodeWriter;
import org.hisrc.jsonix.compilation.ModulesCompiler;
import org.hisrc.jsonix.compilation.ProgramWriter;
import org.hisrc.jsonix.configuration.ModulesConfiguration;
import org.hisrc.jsonix.configuration.ModulesConfigurationUnmarshaller;
import org.hisrc.jsonix.configuration.OutputConfiguration;
import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Modules;
import org.hisrc.jsonix.definition.Output;
import org.hisrc.jsonix.log.Log;
import org.hisrc.jsonix.log.SystemLog;
import org.hisrc.jsonix.naming.CompactNaming;
import org.hisrc.jsonix.naming.StandardNaming;
import org.jvnet.jaxb2_commons.xjc.model.concrete.XJCCMInfoFactory;
import org.jvnet.jaxb2_commons.xml.bind.model.MModelInfo;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sun.codemodel.JPackage;
import com.sun.codemodel.fmt.JTextFile;
import com.sun.tools.xjc.BadCommandLineException;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.model.nav.NClass;
import com.sun.tools.xjc.model.nav.NType;
import com.sun.tools.xjc.outline.Outline;

public class JsonixPlugin extends Plugin {

	private final Log log = new SystemLog();
	private final ModulesConfigurationUnmarshaller customizationHandler = new ModulesConfigurationUnmarshaller(
			this.log);

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
	}

	@Override
	public void postProcessModel(Model model, ErrorHandler errorHandler) {
	}

	@Override
	public List<String> getCustomizationURIs() {
		return this.customizationHandler.getCustomizationURIs();
	}

	@Override
	public boolean isCustomizationTagName(String nsUri, String localName) {
		return this.customizationHandler.isCustomizationTagName(nsUri,
				localName);
	}

	@Override
	public boolean run(Outline outline, Options opt,
			final ErrorHandler errorHandler) throws SAXException {

		final Model model = outline.getModel();

		final ModulesConfiguration modulesConfiguration = this.customizationHandler
				.unmarshal(model, this.defaultOutputConfiguration);

		final MModelInfo<NType, NClass> modelinfo = new XJCCMInfoFactory(model)
				.createModel();

		final Modules<NType, NClass> modules = modulesConfiguration.build(log,
				modelinfo);

		final ModulesCompiler<NType, NClass> modulesCompiler = new ModulesCompiler<NType, NClass>(
				modules);

		modulesCompiler.compile(new ProgramWriter<NType, NClass>() {

			@Override
			public void writeProgram(Module<NType, NClass> module,
					JSProgram program, Output output) {
				try {
					final JPackage _package = model.codeModel._package(output
							.getOutputPackageName());
					_package.addResourceFile(createTextFile(
							output.getFileName(), program));
				} catch (IOException ioex) {
					try {
						errorHandler.error(new SAXParseException(
								MessageFormat
										.format("Could not create the code for the module [{0}].",
												module.getName()), null, ioex));
					} catch (SAXException ignored) {

					}
				}
			}
		});

		return true;
	}

	private JTextFile createTextFile(String fileName, JSProgram... programs)
			throws IOException {
		Validate.notNull(fileName);
		final JTextFile textFile = new JTextFile(fileName);
		final StringWriter stringWriter = new StringWriter();
		final CodeWriter codeWriter = new CodeWriter(stringWriter);
		for (JSProgram program : programs) {
			codeWriter.program(program);
			codeWriter.lineTerminator();
		}
		textFile.setContents(stringWriter.toString());
		return textFile;
	}

}
