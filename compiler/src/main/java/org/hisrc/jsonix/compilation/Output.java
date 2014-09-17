package org.hisrc.jsonix.compilation;

import java.text.MessageFormat;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.compiler.Naming;

public class Output {
	
	private final String outputPackageName = "";
	private final String directory = "";
	private final String fileName;
	private final Naming naming;

	public Output(final String fileName, final Naming naming) {
		Validate.notNull(fileName);
		Validate.notNull(naming);
		this.fileName = fileName;
		this.naming = naming;
	}

	public String getOutputPackageName() {
		return outputPackageName;
	}

	public String getDirectory() {
		return this.directory;
	}

	public String getFileName() {
		return this.fileName;
	}

	public Naming getNaming() {
		return this.naming;
	}
	
	@Override
	public String toString() {
		return MessageFormat.format("Output [{0}]", this.fileName);
	}
	
}
