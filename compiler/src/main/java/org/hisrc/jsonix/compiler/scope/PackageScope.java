package org.hisrc.jsonix.compiler.scope;

import org.apache.commons.lang3.Validate;
import org.jvnet.jaxb2_commons.xml.bind.model.MBuiltinLeafInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassRef;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MEnumLeafInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MID;
import org.jvnet.jaxb2_commons.xml.bind.model.MIDREF;
import org.jvnet.jaxb2_commons.xml.bind.model.MIDREFS;
import org.jvnet.jaxb2_commons.xml.bind.model.MList;
import org.jvnet.jaxb2_commons.xml.bind.model.MPackageInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfoVisitor;
import org.jvnet.jaxb2_commons.xml.bind.model.MWildcardTypeInfo;

public class PackageScope<T, C> implements Scope<T, C> {

	private final MPackageInfo packageInfo;

	public PackageScope(MPackageInfo packageInfo) {
		Validate.notNull(packageInfo);
		this.packageInfo = packageInfo;
	}

	@Override
	public boolean contains(MTypeInfo<T, C> info) {
		return info.acceptTypeInfoVisitor(new MTypeInfoVisitor<T, C, Boolean>() {

			@Override
			public Boolean visitClassInfo(MClassInfo<T, C> info) {
				return PackageScope.this.packageInfo == info.getPackageInfo();
			}

			@Override
			public Boolean visitClassRef(MClassRef<T, C> info) {
				return PackageScope.this.packageInfo == info.getPackageInfo();
			}

			@Override
			public Boolean visitList(MList<T, C> info) {
				return false;
			}

			@Override
			public Boolean visitID(MID<T, C> info) {
				return false;
			}

			@Override
			public Boolean visitIDREF(MIDREF<T, C> info) {
				return false;
			}

			@Override
			public Boolean visitIDREFS(MIDREFS<T, C> info) {
				return false;
			}

			@Override
			public Boolean visitBuiltinLeafInfo(MBuiltinLeafInfo<T, C> info) {
				return false;
			}

			@Override
			public Boolean visitEnumLeafInfo(MEnumLeafInfo<T, C> info) {
				return PackageScope.this.packageInfo == info.getPackageInfo();
			}

			@Override
			public Boolean visitWildcardTypeInfo(MWildcardTypeInfo<T, C> info) {
				return false;
			}
		});
	}

	@Override
	public boolean contains(MElementInfo<T, C> info) {
		return this.packageInfo == info.getPackageInfo();
	}

	@Override
	public boolean contains(MPropertyInfo<T, C> info) {
		return this.packageInfo == info.getClassInfo().getPackageInfo();
	}
}
