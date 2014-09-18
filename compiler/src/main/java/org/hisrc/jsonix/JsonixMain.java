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

package org.hisrc.jsonix;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.hisrc.jscm.codemodel.JSCodeModel;
import org.hisrc.jscm.codemodel.JSProgram;
import org.hisrc.jscm.codemodel.impl.CodeModelImpl;
import org.hisrc.jscm.codemodel.writer.CodeWriter;
import org.hisrc.jsonix.compilation.Module;
import org.hisrc.jsonix.compilation.Modules;
import org.hisrc.jsonix.compilation.Output;
import org.hisrc.jsonix.compiler.CompactNaming;
import org.hisrc.jsonix.compiler.ModuleCompiler;
import org.hisrc.jsonix.compiler.Naming;
import org.hisrc.jsonix.compiler.StandardNaming;
import org.hisrc.jsonix.configuration.ModulesConfiguration;
import org.hisrc.jsonix.log.Log;
import org.hisrc.jsonix.log.SystemLog;
import org.hisrc.jsonix.xjc.customizations.CustomizationHandler;
import org.hisrc.jsonix.xjc.plugin.JsonixPlugin;
import org.jvnet.jaxb2_commons.xjc.model.concrete.XJCCMInfoFactory;
import org.jvnet.jaxb2_commons.xml.bind.model.MModelInfo;
import org.xml.sax.SAXParseException;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JPackage;
import com.sun.tools.xjc.ConsoleErrorReporter;
import com.sun.tools.xjc.ErrorReceiver;
import com.sun.tools.xjc.ModelLoader;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.model.nav.NClass;
import com.sun.tools.xjc.model.nav.NType;

public class JsonixMain {

	private Log log = new SystemLog();

	private CustomizationHandler customizationHandler = new CustomizationHandler(
			this.log);

	public static void main(String[] args) throws Exception {

		final List<String> arguments = new ArrayList<String>(
				Arrays.asList(args));

		if (!arguments.contains("-extension")) {
			arguments.add("-extension");
		}

		if (!arguments.contains(JsonixPlugin.OPTION)) {
			arguments.add(JsonixPlugin.OPTION);
		}

		final Naming naming;
		if (arguments.contains(JsonixPlugin.OPTION_COMPACT)) {
			naming = new CompactNaming();
		} else {
			naming = new StandardNaming();
		}
		// Driver.main(arguments.toArray(new String[arguments.size()]));

		Options options = new Options();

		options.parseArguments(arguments.toArray(new String[arguments.size()]));

		final JsonixMain main = new JsonixMain(options, naming);
		main.execute();
	}

	private final Options options;
	private final Naming defaultNaming;

	public JsonixMain(Options options, Naming naming) {
		Validate.notNull(options);
		Validate.notNull(naming);
		this.options = options;
		this.defaultNaming = naming;
	}

	private void execute() {

		final ConsoleErrorReporter receiver = new ConsoleErrorReporter();
		final Model model = ModelLoader.load(options, new JCodeModel(),
				receiver);
		final ModulesConfiguration modulesConfiguration = this.customizationHandler
				.unmarshal(model);

		final ErrorReceiver errorReceiver = new ConsoleErrorReporter();

		final MModelInfo<NType, NClass> mModel = new XJCCMInfoFactory(model)
				.createModel();

		final Modules modules = modulesConfiguration.build(mModel);

		final JSCodeModel codeModel = new CodeModelImpl();

		for (Module module : modules.getModules()) {
			for (Output output : module.getOutputs()) {
				// TODO isEmpty
				final ModuleCompiler<NType, NClass> moduleCompiler = new ModuleCompiler<NType, NClass>(
						codeModel, modules, module, output);

				final JSProgram program = moduleCompiler.compile(mModel);
				final JPackage _package = model.codeModel._package(output
						.getOutputPackageName());
				try {
					writePrograms(options.targetDir, output.getDirectory(),
							output.getFileName(), program);
				} catch (IOException ioex) {
					errorReceiver
							.error(new SAXParseException(
									MessageFormat
											.format("Could not create the code for the module [{0}].",
													module.getName()), null,
									ioex));
				}
			}
		}
	}

	private File writePrograms(final File targetDir, final String directory,
			final String fileName, JSProgram... programs) throws IOException {
		Validate.notNull(fileName);

		final File dir = new File(targetDir, directory);
		dir.mkdirs();
		final File file = new File(dir, fileName);
		final FileWriter fileWriter = new FileWriter(file);
		try {
			// final StringWriter stringWriter = new StringWriter();
			final CodeWriter codeWriter = new CodeWriter(fileWriter);
			for (JSProgram program : programs) {
				codeWriter.program(program);
				codeWriter.lineTerminator();
			}
		} finally {
			try {
				fileWriter.close();
			} catch (IOException ignored) {
			}
		}
		return file;
	}
}
