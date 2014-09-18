package org.hisrc.jsonix.analysis;

public class DependencyEdge {

	private DependencyType type;

	public DependencyEdge(DependencyType type) {
		this.type = type;
	}

	public DependencyType getType() {
		return type;
	}

}
