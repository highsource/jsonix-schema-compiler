package org.hisrc.jsonix.settings;

import org.hisrc.jsonix.naming.CompactNaming;
import org.hisrc.jsonix.naming.StandardNaming;

public enum NamingSetting {

	COMPACT(CompactNaming.NAMING_NAME), STANDARD(StandardNaming.NAMING_NAME);

	private final String name;

	private NamingSetting(String naming) {
		this.name = naming;
	}

	public String getName() {
		return this.name;
	}
}
