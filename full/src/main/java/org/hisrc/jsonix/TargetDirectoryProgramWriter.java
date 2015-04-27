package org.hisrc.jsonix;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;

import org.apache.commons.lang3.Validate;
import org.hisrc.jscm.codemodel.JSProgram;
import org.hisrc.jscm.codemodel.writer.CodeWriter;
import org.hisrc.jsonix.compilation.mapping.ProgramWriter;
import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Output;
import org.xml.sax.SAXParseException;

import com.sun.tools.xjc.ErrorReceiver;
import com.sun.tools.xjc.model.nav.NClass;
import com.sun.tools.xjc.model.nav.NType;

public class TargetDirectoryProgramWriter implements
		ProgramWriter<NType, NClass> {

	private final File targetDirectory;
	private final ErrorReceiver errorReceiver;

	TargetDirectoryProgramWriter(File targetDirectory,
			ErrorReceiver errorReceiver) {
		this.targetDirectory = Validate.notNull(targetDirectory);
		this.errorReceiver = Validate.notNull(errorReceiver);
	}

	@Override
	public void writeProgram(Module<NType, NClass> module,
			JSProgram program, Output output) {
		try {
			writePrograms(targetDirectory, output.getDirectory(),
					output.getFileName(), program);
		} catch (IOException ioex) {
			errorReceiver.error(new SAXParseException(MessageFormat.format(
					"Could not create the code for the module [{0}].",
					module.getName()), null, ioex));
		}
	}

	private File writePrograms(final File targetDir,
			final String directory, final String fileName,
			JSProgram... programs) throws IOException {
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