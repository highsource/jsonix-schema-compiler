package org.hisrc.jsonix.xjc.plugin;

import java.io.IOException;
import java.io.StringWriter;
import java.text.MessageFormat;

import org.apache.commons.lang3.Validate;
import org.hisrc.jscm.codemodel.JSProgram;
import org.hisrc.jscm.codemodel.writer.CodeWriter;
import org.hisrc.jsonix.compilation.ProgramWriter;
import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Output;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.fmt.JTextFile;
import com.sun.tools.xjc.model.nav.NClass;
import com.sun.tools.xjc.model.nav.NType;

public class CodeModelProgramWriter implements ProgramWriter<NType, NClass> {

	private final JCodeModel codeModel;
	private final ErrorHandler errorHandler;

	public CodeModelProgramWriter(JCodeModel codeModel,
			ErrorHandler errorHandler) {
		this.codeModel = Validate.notNull(codeModel);
		this.errorHandler = Validate.notNull(errorHandler);
	}

	@Override
	public void writeProgram(Module<NType, NClass> module, JSProgram program,
			Output output) {
		try {
			final JPackage _package = codeModel._package(output
					.getOutputPackageName());
			_package.addResourceFile(createTextFile(output.getFileName(),
					program));
		} catch (IOException ioex) {
			try {
				errorHandler.error(new SAXParseException(MessageFormat.format(
						"Could not create the code for the module [{0}].",
						module.getName()), null, ioex));
			} catch (SAXException ignored) {

			}
		}
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