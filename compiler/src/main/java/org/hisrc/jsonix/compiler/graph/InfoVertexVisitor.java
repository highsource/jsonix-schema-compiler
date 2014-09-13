package org.hisrc.jsonix.compiler.graph;

public interface InfoVertexVisitor<T, C, V> {

	public V visitPackageInfoVertex(PackageInfoVertex<T, C> vertex);

	public V visitTypeInfoVertex(TypeInfoVertex<T, C> vertex);

	public V visitElementInfoVertex(ElementInfoVertex<T, C> vertex);

	public V visitPropertyInfoVertex(PropertyInfoVertex<T, C> vertex);

}
