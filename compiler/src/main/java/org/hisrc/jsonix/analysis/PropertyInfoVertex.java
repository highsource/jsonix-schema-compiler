package org.hisrc.jsonix.analysis;

import java.text.MessageFormat;

import org.apache.commons.lang3.Validate;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPackageInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;

public class PropertyInfoVertex<T, C> extends InfoVertex<T,C> {

	private final MPackageInfo packageInfo;
	private final MClassInfo<T, C> classInfo;
	private final MPropertyInfo<T, C> propertyInfo;

	public PropertyInfoVertex(MPropertyInfo<T, C> propertyInfo) {
		Validate.notNull(propertyInfo);
		this.propertyInfo = propertyInfo;
		this.packageInfo = propertyInfo.getClassInfo().getPackageInfo();
		this.classInfo = propertyInfo.getClassInfo();
	}

	@Override
	public MPackageInfo getPackageInfo() {
		return packageInfo;
	}

	public MClassInfo<T, C> getClassInfo() {
		return classInfo;
	}

	public MPropertyInfo<T, C> getPropertyInfo() {
		return propertyInfo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((propertyInfo == null) ? 0 : propertyInfo.hashCode());
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
		PropertyInfoVertex other = (PropertyInfoVertex) obj;
		if (propertyInfo == null) {
			if (other.propertyInfo != null)
				return false;
		} else if (!propertyInfo.equals(other.propertyInfo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		final String propertyName = propertyInfo.getClassInfo().getName()
				+ "." + propertyInfo.getPrivateName();
		return MessageFormat.format("Property [{0}]", propertyName);
	}

	@Override
	public <V> V accept(InfoVertexVisitor<T, C, V> visitor) {
		return visitor.visitPropertyInfoVertex(this);
	}
}
