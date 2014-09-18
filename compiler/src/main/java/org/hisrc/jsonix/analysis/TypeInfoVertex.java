package org.hisrc.jsonix.analysis;

import java.text.MessageFormat;

import org.apache.commons.lang3.Validate;
import org.jvnet.jaxb2_commons.xml.bind.model.MPackageInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;

public class TypeInfoVertex<T, C> extends InfoVertex<T,C> {

	private final MPackageInfo packageInfo;
	private final MTypeInfo<T, C> typeInfo;

	public TypeInfoVertex(MPackageInfo packageInfo, MTypeInfo<T, C> typeInfo) {
//		Validate.notNull(packageInfo);
		Validate.notNull(typeInfo);
		this.packageInfo = packageInfo;
		this.typeInfo = typeInfo;
	}

	@Override
	public MPackageInfo getPackageInfo() {
		return packageInfo;
	}

	public MTypeInfo<T, C> getTypeInfo() {
		return typeInfo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((typeInfo == null) ? 0 : typeInfo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		TypeInfoVertex other = (TypeInfoVertex) obj;
		if (typeInfo == null) {
			if (other.typeInfo != null)
				return false;
		} else if (!typeInfo.equals(other.typeInfo))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return MessageFormat.format("Type [{0}]", typeInfo.acceptTypeInfoVisitor(MTypeInfoToString.<T, C>instance()));
	}

	@Override
	public <V> V accept(InfoVertexVisitor<T, C, V> visitor) {
		return visitor.visitTypeInfoVertex(this);
	}
}
