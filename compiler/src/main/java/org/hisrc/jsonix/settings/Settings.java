package org.hisrc.jsonix.settings;

import org.kohsuke.args4j.Option;

public class Settings {

	@Option(name = "-logLevel", aliases = { "-Xjsonix-logLevel" })
	public LogLevelSetting logLevel = LogLevelSetting.INFO;

	@Option(name = "-defaultNaming", aliases = { "-Xjsonix-defaultNaming" })
	public NamingSetting defaultNaming = NamingSetting.STANDARD;

}
