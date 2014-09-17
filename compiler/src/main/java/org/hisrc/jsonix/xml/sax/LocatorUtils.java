package org.hisrc.jsonix.xml.sax;

import org.xml.sax.Locator;

public class LocatorUtils {

	private LocatorUtils() {
	}

	public static String toString(Locator locator) {
		if (locator == null) {
			return null;
		} else {
			final StringBuilder sb = new StringBuilder();
			final String publicId = locator.getPublicId();
			if (publicId != null) {
				sb.append(publicId).append(':');
			}
			final String systemId = locator.getSystemId();
			if (systemId != null) {
				sb.append(systemId).append(':');
			}
			sb.append(locator.getLineNumber()).append(':');
			sb.append(locator.getColumnNumber());
			return sb.toString();
		}
	}
}
