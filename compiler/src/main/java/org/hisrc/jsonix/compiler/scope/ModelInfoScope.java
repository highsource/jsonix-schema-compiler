package org.hisrc.jsonix.compiler.scope;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MModelInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;

public class ModelInfoScope<T, C> implements Scope<T, C> {
	
	private final MModelInfo<T, C> modelInfo;
	
	private final Set<MTypeInfo<T, C>> typeInfos;
	private final Set<MElementInfo<T, C>> elementInfos;

	public ModelInfoScope(MModelInfo<T, C> modelInfo) {
		Validate.notNull(modelInfo);
		this.modelInfo = modelInfo;
		this.typeInfos = new HashSet<MTypeInfo<T,C>>(modelInfo.getTypeInfos());
		this.elementInfos = new HashSet<MElementInfo<T,C>>(modelInfo.getElementInfos());
	}
	
	@Override
	public boolean contains(MTypeInfo<T, C> info) {
		return this.typeInfos.contains(info);
	}

	@Override
	public boolean contains(MElementInfo<T, C> info) {
		return this.elementInfos.contains(info);
	}
	
	@Override
	public boolean contains(MPropertyInfo<T, C> info) {
		return this.typeInfos.contains(info.getClassInfo());
	}
}
