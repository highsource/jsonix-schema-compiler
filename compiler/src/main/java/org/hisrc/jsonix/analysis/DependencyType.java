package org.hisrc.jsonix.analysis;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.definition.ContainmentType;

public enum DependencyType {

	HARD {
		public ContainmentType combineWith(
				ContainmentType sourceContainmentType) {
			Validate.notNull(sourceContainmentType);
			switch (sourceContainmentType) {
			case EXCLUDED_EXPLICITLY:
				return ContainmentType.EXCLUDED_AS_HARD_DEPENDENCY;
			case EXCLUDED_AS_HARD_DEPENDENCY:
				return ContainmentType.EXCLUDED_AS_HARD_DEPENDENCY;
			case INCLUDED_EXPLICITLY:
				return ContainmentType.INCLUDED_AS_HARD_DEPENDENCY;
			case INCLUDED_AS_HARD_DEPENDENCY:
				return ContainmentType.INCLUDED_AS_HARD_DEPENDENCY;
			case INCLUDED_AS_SOFT_DEPENDENCY:
				return ContainmentType.INCLUDED_AS_SOFT_DEPENDENCY;
			default:
				throw new IllegalArgumentException();
			}
		}

	},
	SOFT {
		@Override
		public ContainmentType combineWith(
				ContainmentType sourceContainmentType) {
			Validate.notNull(sourceContainmentType);
			switch (sourceContainmentType) {
			// TODO I don't like this here
			case EXCLUDED_EXPLICITLY:
				return null;
			case EXCLUDED_AS_HARD_DEPENDENCY:
				return null;
			case INCLUDED_EXPLICITLY:
				return ContainmentType.INCLUDED_AS_SOFT_DEPENDENCY;
			case INCLUDED_AS_HARD_DEPENDENCY:
				return ContainmentType.INCLUDED_AS_SOFT_DEPENDENCY;
			case INCLUDED_AS_SOFT_DEPENDENCY:
				return ContainmentType.INCLUDED_AS_SOFT_DEPENDENCY;
			default:
				throw new IllegalArgumentException();
			}
		}
	};

	public abstract ContainmentType combineWith(
			ContainmentType sourceContainmentType);

}
