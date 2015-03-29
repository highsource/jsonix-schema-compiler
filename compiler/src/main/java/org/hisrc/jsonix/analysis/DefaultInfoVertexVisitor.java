package org.hisrc.jsonix.analysis;

public class DefaultInfoVertexVisitor<T, C extends T, V> implements InfoVertexVisitor<T, C, V> {

	@Override
	public V visitPackageInfoVertex(PackageInfoVertex<T, C> vertex) {
		return null;
	}

	@Override
	public V visitTypeInfoVertex(TypeInfoVertex<T, C> vertex) {
		return null;
	}

	@Override
	public V visitElementInfoVertex(ElementInfoVertex<T, C> vertex) {
		return null;
	}

	@Override
	public V visitPropertyInfoVertex(PropertyInfoVertex<T, C> vertex) {
		return null;
	}

}
