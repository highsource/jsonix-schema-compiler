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
import org.hisrc.jsonix.compilation.Modules;
import org.hisrc.jsonix.compilation.Output;
import org.hisrc.jsonix.compiler.CompactNaming;
import org.hisrc.jsonix.compiler.JsonixCompiler;
import org.hisrc.jsonix.compiler.JsonixModule;
import org.hisrc.jsonix.compiler.Naming;
import org.hisrc.jsonix.compiler.StandardNaming;
import org.hisrc.jsonix.compiler.log.Log;
import org.hisrc.jsonix.compiler.log.SystemLog;
import org.hisrc.jsonix.configuration.ModulesConfiguration;
import org.hisrc.jsonix.xjc.customizations.CustomizationHandler;
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
	private final CustomizationHandler customizationHandler = new CustomizationHandler(
			this.log);

	public static final String OPTION_NAME = "Xjsonix";
	public static final String OPTION = "-" + OPTION_NAME;
	public static final String OPTION_COMPACT = OPTION + "-compact";

	private Naming naming = new StandardNaming();

	public Naming getNaming() {
		return naming;
	}

	public void setNaming(Naming naming) {
		this.naming = naming;
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
			setNaming(new CompactNaming());
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
	public boolean run(Outline outline, Options opt, ErrorHandler errorHandler)
			throws SAXException {

		final Model model = outline.getModel();

		final ModulesConfiguration modulesConfiguration = this.customizationHandler
				.unmarshal(model);

		final MModelInfo<NType, NClass> mModel = new XJCCMInfoFactory(model)
				.createModel();

		final Modules modules = modulesConfiguration.build(mModel);

		final JsonixCompiler<NType, NClass> compiler = new JsonixCompiler<NType, NClass>(
				mModel, modules);

		final Iterable<JsonixModule> compiledModules = compiler.compile();

		for (JsonixModule compiledModule : compiledModules) {
			try {
				if (!compiledModule.isEmpty()) {
					final Output output = compiledModule.getOutput();
					final JPackage _package = model.codeModel._package(output
							.getOutputPackageName());
					_package.addResourceFile(createTextFile(
							output.getFileName(), compiledModule.declarations,
							compiledModule.exportDeclarations));
				}
			} catch (IOException ioex) {
				errorHandler.error(new SAXParseException(MessageFormat.format(
						"Could not create the code for the module [{0}].",
						compiledModule.mappingName), null, ioex));
			}

		}

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
