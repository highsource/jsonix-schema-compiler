package org.hisrc.jsonix.context;

import org.apache.commons.lang3.Validate;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

public class DefaultJsonixContext implements JsonixContext {

	private final ILoggerFactory loggerFactory;

	public DefaultJsonixContext(ILoggerFactory loggerFactory) {
		this.loggerFactory = Validate.notNull(loggerFactory);
	}

	public DefaultJsonixContext() {
		this(LoggerFactory.getILoggerFactory());
	}

	@Override
	public ILoggerFactory getLoggerFactory() {
		return loggerFactory;
	}

}
