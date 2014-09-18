package org.hisrc.jsonix.scope;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;

public class ClassInfoScope<T, C> implements Scope<T, C> {

	private final MClassInfo<T, C> classInfo;
	private final Set<MPropertyInfo<T, C>> propertyInfos;

	public ClassInfoScope(MClassInfo<T, C> classInfo) {
		Validate.notNull(classInfo);
		this.classInfo = classInfo;
		this.propertyInfos = new HashSet<MPropertyInfo<T, C>>(
				classInfo.getProperties());
	}

	public ClassInfoScope(MClassInfo<T, C> classInfo,
			Collection<MPropertyInfo<T, C>> propertyInfos) {
		Validate.notNull(classInfo);
		Validate.noNullElements(propertyInfos);
		this.classInfo = classInfo;
		this.propertyInfos = new HashSet<MPropertyInfo<T, C>>(propertyInfos);
	}

	@Override
	public boolean contains(MTypeInfo<T, C> info) {
		return this.classInfo == info;
	}

	@Override
	public boolean contains(MElementInfo<T, C> info) {
		return false;
	}

	@Override
	public boolean contains(MPropertyInfo<T, C> info) {
		return this.propertyInfos.contains(info);
	}

}
