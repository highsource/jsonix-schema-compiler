package org.hisrc.jsonix;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;

import javax.json.JsonStructure;
import javax.json.JsonWriter;
import javax.json.spi.JsonProvider;
import javax.json.stream.JsonGenerator;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.compilation.jsc.JsonStructureWriter;
import org.hisrc.jsonix.definition.Module;
import org.xml.sax.SAXParseException;

import com.sun.tools.xjc.ErrorReceiver;
import com.sun.tools.xjc.model.nav.NClass;
import com.sun.tools.xjc.model.nav.NType;

public class TargetDirectoryJsonStructureWriter implements
		JsonStructureWriter<NType, NClass> {

	private final JsonProvider provider = JsonProvider.provider();
	private final File targetDirectory;
	private final ErrorReceiver errorReceiver;

	TargetDirectoryJsonStructureWriter(File targetDirectory,
			ErrorReceiver errorReceiver) {
		this.targetDirectory = Validate.notNull(targetDirectory);
		this.errorReceiver = Validate.notNull(errorReceiver);
	}

	@Override
	public void writeJsonStructure(Module<NType, NClass> module,
			JsonStructure schema, String fileName) {
		try {
			writeStructure(targetDirectory, "", fileName, schema);
		} catch (IOException ioex) {
			errorReceiver.error(new SAXParseException(MessageFormat.format(
					"Could not create the code for the module [{0}].",
					module.getName()), null, ioex));
		}
	}

	private File writeStructure(final File targetDir, final String directory,
			final String fileName, JsonStructure structure) throws IOException {
		Validate.notNull(fileName);

		final File dir = new File(targetDir, directory);
		dir.mkdirs();
		final File file = new File(dir, fileName);
		final FileWriter fileWriter = new FileWriter(file);
		try {
			final JsonWriter jsonWriter = provider.createWriterFactory(
					Collections.singletonMap(JsonGenerator.PRETTY_PRINTING,
							Boolean.TRUE)).createWriter(fileWriter);
			jsonWriter.write(structure);

		} finally {
			try {
				fileWriter.close();
			} catch (IOException ignored) {
			}
		}
		return file;
	}
}