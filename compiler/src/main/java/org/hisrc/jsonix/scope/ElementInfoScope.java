package org.hisrc.jsonix.scope;

import org.apache.commons.lang3.Validate;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;

public class ElementInfoScope<T, C> implements Scope<T, C> {

	private MElementInfo<T, C> elementInfo;

	public ElementInfoScope(MElementInfo<T, C> elementInfo) {
		Validate.notNull(elementInfo);
		this.elementInfo = elementInfo;
	}

	@Override
	public boolean contains(MTypeInfo<T, C> info) {
		return false;
	}

	@Override
	public boolean contains(MElementInfo<T, C> info) {
		return this.elementInfo == info;
	}

	@Override
	public boolean contains(MPropertyInfo<T, C> info) {
		return false;
	}

}
