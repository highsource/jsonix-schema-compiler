package org.hisrc.jsonix.slf4j;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.spi.LocationAwareLogger;

public abstract class LevelledLoggerWrapper implements Logger {

	protected final Logger delegate;

	LevelledLoggerWrapper(Logger logger) {
		this.delegate = Validate.notNull(logger);
	}

	protected abstract int getLevel();

	@Override
	public String getName() {
		return delegate.getName();
	}

	@Override
	public boolean isDebugEnabled() {
		return getLevel() <= LocationAwareLogger.DEBUG_INT
				&& delegate.isDebugEnabled();
	}

	@Override
	public boolean isDebugEnabled(Marker marker) {
		return getLevel() <= LocationAwareLogger.DEBUG_INT
				&& delegate.isDebugEnabled(marker);
	}

	@Override
	public boolean isErrorEnabled() {
		return getLevel() <= LocationAwareLogger.ERROR_INT
				&& delegate.isErrorEnabled();
	}

	@Override
	public boolean isErrorEnabled(Marker marker) {
		return getLevel() <= LocationAwareLogger.ERROR_INT
				&& delegate.isErrorEnabled(marker);
	}

	@Override
	public boolean isInfoEnabled() {
		return getLevel() <= LocationAwareLogger.INFO_INT
				&& delegate.isInfoEnabled();
	}

	@Override
	public boolean isInfoEnabled(Marker marker) {
		return getLevel() <= LocationAwareLogger.INFO_INT
				&& delegate.isInfoEnabled(marker);
	}

	@Override
	public boolean isTraceEnabled() {
		return getLevel() <= LocationAwareLogger.TRACE_INT
				&& delegate.isTraceEnabled();
	}

	public boolean isTraceEnabled(Marker marker) {
		return getLevel() <= LocationAwareLogger.TRACE_INT
				&& delegate.isTraceEnabled(marker);
	}

	@Override
	public boolean isWarnEnabled() {
		return getLevel() <= LocationAwareLogger.WARN_INT
				&& delegate.isWarnEnabled();
	}

	@Override
	public boolean isWarnEnabled(Marker marker) {
		return getLevel() <= LocationAwareLogger.WARN_INT
				&& delegate.isWarnEnabled(marker);
	}

	public void trace(String msg) {
		if (getLevel() <= LocationAwareLogger.TRACE_INT) {
			delegate.trace(msg);
		}
	}

	public void trace(String format, Object arg) {
		if (getLevel() <= LocationAwareLogger.TRACE_INT) {
			delegate.trace(format, arg);
		}
	}

	public void trace(String format, Object arg1, Object arg2) {
		if (getLevel() <= LocationAwareLogger.TRACE_INT) {
			delegate.trace(format, arg1, arg2);
		}
	}

	public void trace(String format, Object... arguments) {
		if (getLevel() <= LocationAwareLogger.TRACE_INT) {
			delegate.trace(format, arguments);
		}
	}

	public void trace(String msg, Throwable t) {
		if (getLevel() <= LocationAwareLogger.TRACE_INT) {
			delegate.trace(msg, t);
		}
	}

	public void trace(Marker marker, String msg) {
		if (getLevel() <= LocationAwareLogger.TRACE_INT) {
			delegate.trace(marker, msg);
		}
	}

	public void trace(Marker marker, String format, Object arg) {
		if (getLevel() <= LocationAwareLogger.TRACE_INT) {
			delegate.trace(marker, format, arg);
		}
	}

	public void trace(Marker marker, String format, Object arg1, Object arg2) {
		if (getLevel() <= LocationAwareLogger.TRACE_INT) {
			delegate.trace(marker, format, arg1, arg2);
		}
	}

	public void trace(Marker marker, String format, Object... argArray) {
		if (getLevel() <= LocationAwareLogger.TRACE_INT) {
			delegate.trace(marker, format, argArray);
		}
	}

	public void trace(Marker marker, String msg, Throwable t) {
		if (getLevel() <= LocationAwareLogger.TRACE_INT) {
			delegate.trace(marker, msg, t);
		}
	}

	public void debug(String msg) {
		if (getLevel() <= LocationAwareLogger.DEBUG_INT) {
			delegate.debug(msg);
		}
	}

	public void debug(String format, Object arg) {
		if (getLevel() <= LocationAwareLogger.DEBUG_INT) {
			delegate.debug(format, arg);
		}
	}

	public void debug(String format, Object arg1, Object arg2) {
		if (getLevel() <= LocationAwareLogger.DEBUG_INT) {
			delegate.debug(format, arg1, arg2);
		}
	}

	public void debug(String format, Object... arguments) {
		if (getLevel() <= LocationAwareLogger.DEBUG_INT) {
			delegate.debug(format, arguments);
		}
	}

	public void debug(String msg, Throwable t) {
		if (getLevel() <= LocationAwareLogger.DEBUG_INT) {
			delegate.debug(msg, t);
		}
	}

	public void debug(Marker marker, String msg) {
		if (getLevel() <= LocationAwareLogger.DEBUG_INT) {
			delegate.debug(marker, msg);
		}
	}

	public void debug(Marker marker, String format, Object arg) {
		if (getLevel() <= LocationAwareLogger.DEBUG_INT) {
			delegate.debug(marker, format, arg);
		}
	}

	public void debug(Marker marker, String format, Object arg1, Object arg2) {
		if (getLevel() <= LocationAwareLogger.DEBUG_INT) {
			delegate.debug(marker, format, arg1, arg2);
		}
	}

	public void debug(Marker marker, String format, Object... arguments) {
		if (getLevel() <= LocationAwareLogger.DEBUG_INT) {
			delegate.debug(marker, format, arguments);
		}
	}

	public void debug(Marker marker, String msg, Throwable t) {
		if (getLevel() <= LocationAwareLogger.DEBUG_INT) {
			delegate.debug(marker, msg, t);
		}
	}

	public void info(String msg) {
		if (getLevel() <= LocationAwareLogger.INFO_INT) {
			delegate.info(msg);
		}
	}

	public void info(String format, Object arg) {
		if (getLevel() <= LocationAwareLogger.INFO_INT) {
			delegate.info(format, arg);
		}
	}

	public void info(String format, Object arg1, Object arg2) {
		if (getLevel() <= LocationAwareLogger.INFO_INT) {
			delegate.info(format, arg1, arg2);
		}
	}

	public void info(String format, Object... arguments) {
		if (getLevel() <= LocationAwareLogger.INFO_INT) {
			delegate.info(format, arguments);
		}
	}

	public void info(String msg, Throwable t) {
		if (getLevel() <= LocationAwareLogger.INFO_INT) {
			delegate.info(msg, t);
		}
	}

	public void info(Marker marker, String msg) {
		if (getLevel() <= LocationAwareLogger.INFO_INT) {
			delegate.info(marker, msg);
		}
	}

	public void info(Marker marker, String format, Object arg) {
		if (getLevel() <= LocationAwareLogger.INFO_INT) {
			delegate.info(marker, format, arg);
		}
	}

	public void info(Marker marker, String format, Object arg1, Object arg2) {
		if (getLevel() <= LocationAwareLogger.INFO_INT) {
			delegate.info(marker, format, arg1, arg2);
		}
	}

	public void info(Marker marker, String format, Object... arguments) {
		if (getLevel() <= LocationAwareLogger.INFO_INT) {
			delegate.info(marker, format, arguments);
		}
	}

	public void info(Marker marker, String msg, Throwable t) {
		if (getLevel() <= LocationAwareLogger.INFO_INT) {
			delegate.info(marker, msg, t);
		}
	}

	public void warn(String msg) {
		if (getLevel() <= LocationAwareLogger.WARN_INT) {
			delegate.warn(msg);
		}
	}

	public void warn(String format, Object arg) {
		if (getLevel() <= LocationAwareLogger.WARN_INT) {
			delegate.warn(format, arg);
		}
	}

	public void warn(String format, Object... arguments) {
		if (getLevel() <= LocationAwareLogger.WARN_INT) {
			delegate.warn(format, arguments);
		}
	}

	public void warn(String format, Object arg1, Object arg2) {
		if (getLevel() <= LocationAwareLogger.WARN_INT) {
			delegate.warn(format, arg1, arg2);
		}
	}

	public void warn(String msg, Throwable t) {
		if (getLevel() <= LocationAwareLogger.WARN_INT) {
			delegate.warn(msg, t);
		}
	}

	public void warn(Marker marker, String msg) {
		if (getLevel() <= LocationAwareLogger.WARN_INT) {
			delegate.warn(marker, msg);
		}
	}

	public void warn(Marker marker, String format, Object arg) {
		if (getLevel() <= LocationAwareLogger.WARN_INT) {
			delegate.warn(marker, format, arg);
		}
	}

	public void warn(Marker marker, String format, Object arg1, Object arg2) {
		if (getLevel() <= LocationAwareLogger.WARN_INT) {
			delegate.warn(marker, format, arg1, arg2);
		}
	}

	public void warn(Marker marker, String format, Object... arguments) {
		if (getLevel() <= LocationAwareLogger.WARN_INT) {
			delegate.warn(marker, format, arguments);
		}
	}

	public void warn(Marker marker, String msg, Throwable t) {
		if (getLevel() <= LocationAwareLogger.WARN_INT) {
			delegate.warn(marker, msg, t);
		}
	}

	public void error(String msg) {
		if (getLevel() <= LocationAwareLogger.ERROR_INT) {
			delegate.error(msg);
		}
	}

	public void error(String format, Object arg) {
		if (getLevel() <= LocationAwareLogger.ERROR_INT) {
			delegate.error(format, arg);
		}
	}

	public void error(String format, Object arg1, Object arg2) {
		if (getLevel() <= LocationAwareLogger.ERROR_INT) {
			delegate.error(format, arg1, arg2);
		}
	}

	public void error(String format, Object... arguments) {
		if (getLevel() <= LocationAwareLogger.ERROR_INT) {
			delegate.error(format, arguments);
		}
	}

	public void error(String msg, Throwable t) {
		if (getLevel() <= LocationAwareLogger.ERROR_INT) {
			delegate.error(msg, t);
		}
	}

	public void error(Marker marker, String msg) {
		if (getLevel() <= LocationAwareLogger.ERROR_INT) {
			delegate.error(marker, msg);
		}
	}

	public void error(Marker marker, String format, Object arg) {
		if (getLevel() <= LocationAwareLogger.ERROR_INT) {
			delegate.error(marker, format, arg);
		}
	}

	public void error(Marker marker, String format, Object arg1, Object arg2) {
		if (getLevel() <= LocationAwareLogger.ERROR_INT) {
			delegate.error(marker, format, arg1, arg2);
		}
	}

	public void error(Marker marker, String format, Object... arguments) {
		if (getLevel() <= LocationAwareLogger.ERROR_INT) {
			delegate.error(marker, format, arguments);
		}
	}

	public void error(Marker marker, String msg, Throwable t) {
		if (getLevel() <= LocationAwareLogger.ERROR_INT) {
			delegate.error(marker, msg, t);
		}
	}
}