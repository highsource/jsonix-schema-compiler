package org.hisrc.jsonix.settings;

import org.hisrc.jsonix.naming.CompactNaming;
import org.hisrc.jsonix.naming.Naming;
import org.hisrc.jsonix.naming.StandardNaming;

public enum NamingSetting {

	COMPACT(new CompactNaming()), STANDARD(new StandardNaming());

	private final Naming naming;

	private NamingSetting(Naming naming) {
		this.naming = naming;
	}

	public Naming getNaming() {
		return this.naming;
	}
}
