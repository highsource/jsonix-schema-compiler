package org.hisrc.jsonix.definition;

public enum ContainmentType {

	INCLUDED_EXPLICITLY {
		@Override
		public ContainmentType combineWith(ContainmentType current) {
			return this;
		}

		@Override
		public boolean isIncluded() {
			return true;
		}
	},
	INCLUDED_AS_HARD_DEPENDENCY {
		@Override
		public ContainmentType combineWith(ContainmentType current) {
			return INCLUDED_EXPLICITLY.equals(current) ? INCLUDED_EXPLICITLY
					: this;
		}

		@Override
		public boolean isIncluded() {
			return true;
		}
	},
	INCLUDED_AS_SOFT_DEPENDENCY {
		@Override
		public ContainmentType combineWith(ContainmentType current) {
			return current == null ? this : current;
		}

		@Override
		public boolean isIncluded() {
			return true;
		}
	},
	EXCLUDED_EXPLICITLY {
		@Override
		public ContainmentType combineWith(ContainmentType current) {
			return (INCLUDED_EXPLICITLY.equals(current) || INCLUDED_AS_HARD_DEPENDENCY
					.equals(current)) ? current : this;
		}

		@Override
		public boolean isIncluded() {
			return false;
		}
	},
	EXCLUDED_AS_HARD_DEPENDENCY {
		@Override
		public ContainmentType combineWith(ContainmentType current) {
			return (INCLUDED_EXPLICITLY.equals(current)
					|| EXCLUDED_EXPLICITLY.equals(current) || INCLUDED_AS_HARD_DEPENDENCY
					.equals(current)) ? current : this;
		}

		@Override
		public boolean isIncluded() {
			return false;
		}
	};

	public abstract ContainmentType combineWith(ContainmentType current);

	public abstract boolean isIncluded();
}
