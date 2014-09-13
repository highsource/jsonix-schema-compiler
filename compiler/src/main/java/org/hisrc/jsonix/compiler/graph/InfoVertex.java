package org.hisrc.jsonix.compiler.graph;

import org.jvnet.jaxb2_commons.xml.bind.model.MPackageInfo;

public abstract class InfoVertex<T, C> {

	public abstract MPackageInfo getPackageInfo();

	public abstract <V> V accept(InfoVertexVisitor<T, C, V> visitor);

}
