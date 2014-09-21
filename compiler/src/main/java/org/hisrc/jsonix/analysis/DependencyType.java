package org.hisrc.jsonix.analysis;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.definition.ContainmentType;

public enum DependencyType {

	HARD {
		public ContainmentType targetContainmentType(ContainmentType sourceContainmentType) {
			Validate.notNull(sourceContainmentType);
			switch (sourceContainmentType) {
			case AS_SOFT_DEPENDENCY:
				return ContainmentType.AS_SOFT_DEPENDENCY;
			default:
				return ContainmentType.AS_HARD_DEPENDENCY;
			}
		}

	},
	SOFT {

		@Override
		public ContainmentType targetContainmentType(ContainmentType sourceContainmentType) {
			return ContainmentType.AS_SOFT_DEPENDENCY;
		}
	};

	public abstract ContainmentType targetContainmentType(
			ContainmentType sourceContainmentType);
}
