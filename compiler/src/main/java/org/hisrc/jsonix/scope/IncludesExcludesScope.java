package org.hisrc.jsonix.scope;

import org.apache.commons.lang3.Validate;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;

public class IncludesExcludesScope<T, C> implements Scope<T, C> {

	private final Scope<T, C> includes;
	private final Scope<T, C> excludes;
	
	public IncludesExcludesScope(Scope<T, C> includes, Scope<T, C> excludes) {
		Validate.notNull(includes);
		Validate.notNull(excludes);
		this.includes = includes;
		this.excludes = excludes;
	}

	@Override
	public boolean contains(MTypeInfo<T, C> info) {
		return includes.contains(info) && !excludes.contains(info);
	}

	@Override
	public boolean contains(MElementInfo<T, C> info) {
		return includes.contains(info) && !excludes.contains(info);
	}

	@Override
	public boolean contains(MPropertyInfo<T, C> info) {
		return includes.contains(info) && !excludes.contains(info);
	}

}
