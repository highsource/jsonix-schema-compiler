package org.hisrc.jsonix.analysis;

import java.text.MessageFormat;

import org.apache.commons.lang3.Validate;
import org.jvnet.jaxb2_commons.xml.bind.model.MPackageInfo;

public class PackageInfoVertex<T, C> extends InfoVertex<T,C> {

	private final MPackageInfo packageInfo;

	public PackageInfoVertex(MPackageInfo packageInfo) {
		Validate.notNull(packageInfo);
		this.packageInfo = packageInfo;
	}

	@Override
	public MPackageInfo getPackageInfo() {
		return packageInfo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((packageInfo == null) ? 0 : packageInfo.hashCode());
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
		PackageInfoVertex other = (PackageInfoVertex) obj;
		if (packageInfo == null) {
			if (other.packageInfo != null)
				return false;
		} else if (!packageInfo.equals(other.packageInfo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return MessageFormat.format("Package [{0}]",
				this.packageInfo.getPackageName());
	}

	@Override
	public <V> V accept(InfoVertexVisitor<T, C, V> visitor) {
		return visitor.visitPackageInfoVertex(this);
	}
}
