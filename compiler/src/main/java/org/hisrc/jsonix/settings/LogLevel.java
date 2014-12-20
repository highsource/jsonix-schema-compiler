package org.hisrc.jsonix.settings;

import org.slf4j.spi.LocationAwareLogger;

public enum LogLevel {

	TRACE(LocationAwareLogger.TRACE_INT), DEBUG(LocationAwareLogger.DEBUG_INT), INFO(
			LocationAwareLogger.INFO_INT), WARN(LocationAwareLogger.WARN_INT), ERROR(
			LocationAwareLogger.ERROR_INT);

	private final int levelInt;

	private LogLevel(int levelInt) {
		this.levelInt = levelInt;
	}

	public int asInt() {
		return this.levelInt;
	}

}
