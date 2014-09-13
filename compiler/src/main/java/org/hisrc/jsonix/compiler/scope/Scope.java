package org.hisrc.jsonix.compiler.scope;

import org.jvnet.jaxb2_commons.xml.bind.model.MElementInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;

public interface Scope<T, C> {

	public boolean contains(MTypeInfo<T, C> info);

	public boolean contains(MElementInfo<T, C> info);

	public boolean contains(MPropertyInfo<T, C> info);
}
