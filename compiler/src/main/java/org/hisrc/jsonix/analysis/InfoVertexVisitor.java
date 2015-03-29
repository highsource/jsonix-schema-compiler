package org.hisrc.jsonix.analysis;

public interface InfoVertexVisitor<T, C extends T, V> {

	public V visitPackageInfoVertex(PackageInfoVertex<T, C> vertex);

	public V visitTypeInfoVertex(TypeInfoVertex<T, C> vertex);

	public V visitElementInfoVertex(ElementInfoVertex<T, C> vertex);

	public V visitPropertyInfoVertex(PropertyInfoVertex<T, C> vertex);

}
