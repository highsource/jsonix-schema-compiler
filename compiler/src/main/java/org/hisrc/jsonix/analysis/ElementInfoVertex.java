package org.hisrc.jsonix.analysis;

import java.text.MessageFormat;

import javax.xml.namespace.QName;

import org.apache.commons.lang3.Validate;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPackageInfo;

public class ElementInfoVertex<T, C extends T> extends InfoVertex<T,C> {

	private final MPackageInfo packageInfo;
	private final MElementInfo<T, C> elementInfo;

	public ElementInfoVertex(MElementInfo<T, C> elementInfo) {
		Validate.notNull(elementInfo);
		this.elementInfo = elementInfo;
		this.packageInfo = elementInfo.getPackageInfo();
	}

	@Override
	public MPackageInfo getPackageInfo() {
		return packageInfo;
	}

	public MElementInfo<T, C> getElementInfo() {
		return elementInfo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((elementInfo == null) ? 0 : elementInfo.hashCode());
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
		ElementInfoVertex other = (ElementInfoVertex) obj;
		if (elementInfo == null) {
			if (other.elementInfo != null)
				return false;
		} else if (!elementInfo.equals(other.elementInfo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		final QName elementName = this.elementInfo.getElementName();
		final MClassInfo<T, C> scope = elementInfo.getScope();
		return MessageFormat.format(
				"Element [{0}], scope [{1}]",
				elementName.toString(),
				(scope == null ? "null" : scope
						.acceptTypeInfoVisitor(MTypeInfoToString
								.<T, C> instance())));
	}

	@Override
	public <V> V accept(InfoVertexVisitor<T, C, V> visitor) {
		return visitor.visitElementInfoVertex(this);
	}
}
