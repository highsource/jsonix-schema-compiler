package org.hisrc.jsonix.slf4j;

import org.slf4j.Marker;
import org.slf4j.spi.LocationAwareLogger;

public abstract class LevelledLocationAwareLoggerWrapper extends
		LevelledLoggerWrapper implements LocationAwareLogger {

	private LocationAwareLogger locationAwareDelegate;

	LevelledLocationAwareLoggerWrapper(LocationAwareLogger logger) {
		super(logger);
		this.locationAwareDelegate = logger;
	}

	@Override
	public void log(Marker marker, String fqcn, int level, String message,
			Object[] argArray, Throwable t) {
		if (this.getLevel() >= level) {
			this.locationAwareDelegate.log(marker, fqcn, level, message,
					argArray, t);
		}
	}

}
