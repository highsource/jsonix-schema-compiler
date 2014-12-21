package org.hisrc.jsonix.context;

import org.hisrc.jsonix.settings.LogLevelSetting;
import org.hisrc.jsonix.slf4j.LevelledLoggerFactoryWrapper;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

public class DefaultJsonixContext implements JsonixContext {

	private final ILoggerFactory loggerFactory;

	public DefaultJsonixContext(ILoggerFactory loggerFactory) {
		this.loggerFactory = new LevelledLoggerFactoryWrapper(loggerFactory) {
			@Override
			protected int getLevel() {
				return DefaultJsonixContext.this.getLogLevel();
			}
		};
	}

	public DefaultJsonixContext() {
		this(LoggerFactory.getILoggerFactory());
	}

	private int logLevel = LogLevelSetting.INFO.asInt();

	protected int getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(int level) {
		this.logLevel = level;
	}

	@Override
	public ILoggerFactory getLoggerFactory() {
		return loggerFactory;
	}

}
