package org.hisrc.jsonix.compiler.scope;

import java.util.Collection;

import org.apache.commons.lang3.Validate;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;

public class DisjunctiveScope<T, C> implements Scope<T, C> {

	private Collection<Scope<T, C>> items;

	public DisjunctiveScope(Collection<Scope<T, C>> items) {
		Validate.noNullElements(items);
		this.items = items;
	}

	@Override
	public boolean contains(MTypeInfo<T, C> info) {
		for (Scope<T, C> item : this.items) {
			if (item.contains(info)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean contains(MElementInfo<T, C> info) {
		for (Scope<T, C> item : this.items) {
			if (item.contains(info)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean contains(MPropertyInfo<T, C> info) {
		for (Scope<T, C> item : this.items) {
			if (item.contains(info)) {
				return true;
			}
		}
		return false;
	}

}
