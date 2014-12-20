package org.hisrc.jsonix.slf4j;

import org.apache.commons.lang3.Validate;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.spi.LocationAwareLogger;

public abstract class LevelledLoggerFactoryWrapper implements ILoggerFactory {

	protected final ILoggerFactory loggerFactory;

	public LevelledLoggerFactoryWrapper(ILoggerFactory loggerFactory) {
		this.loggerFactory = Validate.notNull(loggerFactory);
	}

	protected abstract int getLevel();

	@Override
	public Logger getLogger(String name) {
		final Logger logger = loggerFactory.getLogger(name);
		if (logger instanceof LocationAwareLogger) {
			return new LevelledLocationAwareLoggerWrapper(
					(LocationAwareLogger) logger) {
				@Override
				protected int getLevel() {
					return LevelledLoggerFactoryWrapper.this.getLevel();
				}
			};
		} else {
			return new LevelledLoggerWrapper(logger) {
				@Override
				protected int getLevel() {
					return LevelledLoggerFactoryWrapper.this.getLevel();
				}
			};
		}
	}
}
