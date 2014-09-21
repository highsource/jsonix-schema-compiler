package org.hisrc.jsonix.definition;

public enum ContainmentType {

	EXPLICIT {
		@Override
		public ContainmentType appendTo(ContainmentType current) {
			return this;
		}
	},
	AS_HARD_DEPENDENCY {
		@Override
		public ContainmentType appendTo(ContainmentType current) {
			return EXPLICIT.equals(current) ? EXPLICIT : this;
		}
	},
	AS_SOFT_DEPENDENCY {
		@Override
		public ContainmentType appendTo(ContainmentType current) {
			return current == null ? this : current;
		}
	};

	public abstract ContainmentType appendTo(ContainmentType current);
}
