package org.hisrc.jsonix.settings.tests;

import org.hisrc.jsonix.args4j.PartialCmdLineParser;
import org.hisrc.jsonix.settings.LogLevelSetting;
import org.hisrc.jsonix.settings.NamingSetting;
import org.hisrc.jsonix.settings.Settings;
import org.junit.Assert;
import org.junit.Test;
import org.kohsuke.args4j.CmdLineException;

public class SettingsTest {

	@Test
	public void parsesCommandLine() throws CmdLineException {
		final Settings settings = new Settings();
		final PartialCmdLineParser parser = new PartialCmdLineParser(settings);

		final String[] args = { "-a", "-b", "-c", "-logLevel", "TRACE",
				"-defaultNaming", "COMPACT", "-unknown", "-unknown" };

		Assert.assertEquals(4, parser.parseArgument(args, 3));
		Assert.assertEquals(LogLevelSetting.TRACE, settings.getLogLevel());
		Assert.assertEquals(NamingSetting.COMPACT, settings.getDefaultNaming());
	}

	@Test
	public void parsesAliasedCommandLine() throws CmdLineException {
		final Settings settings = new Settings();
		final PartialCmdLineParser parser = new PartialCmdLineParser(settings);

		final String[] args = { "-a", "-b", "-c", "-Xjsonix-logLevel", "TRACE",
				"-Xjsonix-defaultNaming", "COMPACT", "-unknown", "-unknown" };

		Assert.assertEquals(4, parser.parseArgument(args, 3));
		Assert.assertEquals(LogLevelSetting.TRACE, settings.getLogLevel());
		Assert.assertEquals(NamingSetting.COMPACT, settings.getDefaultNaming());
	}

	@Test
	public void parsesCompactSetting() throws CmdLineException {
		final Settings settings = new Settings();
		final PartialCmdLineParser parser = new PartialCmdLineParser(settings);

		final String[] args = { "-Xjsonix-compact" };

		Assert.assertEquals(1, parser.parseArgument(args, 0));
		Assert.assertEquals(NamingSetting.COMPACT, settings.getDefaultNaming());
	}

}
