package org.hisrc.jsonix.settings;

import java.io.File;

import org.kohsuke.args4j.Option;

public class Settings {
	
	private File targetDirectory;
	
	public File getTargetDirectory() {
		return targetDirectory;
	}
	
	@Option(name = "-d")
	public void setTargetDirectory(File targetDirectory) {
		this.targetDirectory = targetDirectory;
	}

	private LogLevelSetting logLevel = LogLevelSetting.INFO;

	@Option(name = "-logLevel", aliases = { "-Xjsonix-logLevel" })
	public void setLogLevel(LogLevelSetting logLevel) {
		this.logLevel = logLevel;
	}

	public LogLevelSetting getLogLevel() {
		return logLevel;
	}

	private NamingSetting defaultNaming = NamingSetting.STANDARD;

	public NamingSetting getDefaultNaming() {
		return defaultNaming;
	}

	@Option(name = "-defaultNaming", aliases = { "-Xjsonix-defaultNaming" })
	public void setDefaultNaming(NamingSetting defaultNaming) {
		this.defaultNaming = defaultNaming;
	}

	@Option(name = "-compact", aliases = { "-Xjsonix-compact" })
	public void setCompact(boolean value) {
		if (value) {
			defaultNaming = NamingSetting.COMPACT;
		}
	}

}
