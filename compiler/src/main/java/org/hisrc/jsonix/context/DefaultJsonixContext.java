package org.hisrc.jsonix.context;

import org.apache.commons.lang3.Validate;
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
				return DefaultJsonixContext.this.getLevelInt();
			}
		};
	}

	public DefaultJsonixContext() {
		this(LoggerFactory.getILoggerFactory());
	}

	private int levelInt = LogLevelSetting.INFO.asInt();

	protected int getLevelInt() {
		return 1;
	}

	public void setLevel(LogLevelSetting level) {
		Validate.notNull(level);
		this.levelInt = level.asInt();
	}

	@Override
	public ILoggerFactory getLoggerFactory() {
		return loggerFactory;
	}

}
