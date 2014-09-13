package org.hisrc.jsonix.compiler.scope;

import org.jvnet.jaxb2_commons.xml.bind.model.MElementInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;

public class PropertyInfoScope<T, C> implements Scope<T, C> {

	private final MPropertyInfo<T, C> propertyInfo;

	public PropertyInfoScope(MPropertyInfo<T, C> propertyInfo) {
		this.propertyInfo = propertyInfo;
	}

	@Override
	public boolean contains(MTypeInfo<T, C> info) {
		return false;
	}

	@Override
	public boolean contains(MElementInfo<T, C> info) {
		return false;
	}

	@Override
	public boolean contains(MPropertyInfo<T, C> info) {
		return this.propertyInfo == info;
	}

}
