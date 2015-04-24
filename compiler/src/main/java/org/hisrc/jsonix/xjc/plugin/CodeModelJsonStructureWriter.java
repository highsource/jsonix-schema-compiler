package org.hisrc.jsonix.xjc.plugin;

import java.io.IOException;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Collections;

import javax.json.JsonStructure;
import javax.json.JsonWriter;
import javax.json.spi.JsonProvider;
import javax.json.stream.JsonGenerator;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.compilation.jsc.JsonStructureWriter;
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

public class CodeModelJsonStructureWriter implements
		JsonStructureWriter<NType, NClass> {

	private final JCodeModel codeModel;
	private final ErrorHandler errorHandler;
	private final JsonProvider provider = JsonProvider.provider();

	public CodeModelJsonStructureWriter(JCodeModel codeModel,
			ErrorHandler errorHandler) {
		this.codeModel = Validate.notNull(codeModel);
		this.errorHandler = Validate.notNull(errorHandler);
	}

	@Override
	public void writeJsonStructure(Module<NType, NClass> module,
			JsonStructure structure, Output output) {
		try {
			final JPackage _package = codeModel._package(output
					.getOutputPackageName());
			_package.addResourceFile(createTextFile(output.getFileName(),
					structure));
		} catch (IOException ioex) {
			try {
				errorHandler.error(new SAXParseException(MessageFormat.format(
						"Could not create the code for the module [{0}].",
						module.getName()), null, ioex));
			} catch (SAXException ignored) {

			}
		}
	}

	private JTextFile createTextFile(String fileName,
			JsonStructure jsonStructure) throws IOException {
		Validate.notNull(fileName);
		final JTextFile textFile = new JTextFile(fileName);
		final StringWriter stringWriter = new StringWriter();
		final JsonWriter jsonWriter = provider.createWriterFactory(
				Collections.singletonMap(JsonGenerator.PRETTY_PRINTING,
						Boolean.TRUE)).createWriter(stringWriter);
		jsonWriter.write(jsonStructure);
		textFile.setContents(stringWriter.toString());
		return textFile;
	}
}