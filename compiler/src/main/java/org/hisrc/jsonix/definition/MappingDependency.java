package org.hisrc.jsonix.definition;

import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.analysis.DefaultInfoVertexVisitor;
import org.hisrc.jsonix.analysis.ElementInfoVertex;
import org.hisrc.jsonix.analysis.InfoVertex;
import org.hisrc.jsonix.analysis.InfoVertexVisitor;
import org.hisrc.jsonix.analysis.PropertyInfoVertex;
import org.hisrc.jsonix.analysis.TypeInfoVertex;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MEnumLeafInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPackageInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfoVisitor;
import org.jvnet.jaxb2_commons.xml.bind.model.util.DefaultTypeInfoVisitor;

public class MappingDependency<T, C extends T> {

	private final MPackageInfo packageInfo;
	private final Collection<MClassInfo<T, C>> classInfos = new HashSet<MClassInfo<T, C>>();
	private final Collection<MPropertyInfo<T, C>> propertyInfos = new HashSet<MPropertyInfo<T, C>>();
	private final Collection<MEnumLeafInfo<T, C>> enumLeafInfos = new HashSet<MEnumLeafInfo<T, C>>();
	private final Collection<MElementInfo<T, C>> elementInfos = new HashSet<MElementInfo<T, C>>();

	public MappingDependency(MPackageInfo packageInfo) {
		this.packageInfo = Validate.notNull(packageInfo);
	}

	public MPackageInfo getPackageInfo() {
		return packageInfo;
	}

	public Collection<MClassInfo<T, C>> getClassInfos() {
		return classInfos;
	}

	public Collection<MElementInfo<T, C>> getElementInfos() {
		return elementInfos;
	}

	public Collection<MEnumLeafInfo<T, C>> getEnumLeafInfos() {
		return enumLeafInfos;
	}

	public Collection<MPropertyInfo<T, C>> getPropertyInfos() {
		return propertyInfos;
	}

	private void addTypeInfo(MTypeInfo<T, C> typeInfo) {
		Validate.notNull(typeInfo);
		typeInfo.acceptTypeInfoVisitor(typeInfoAdder);
	}

	private void addElementInfo(MElementInfo<T, C> elementInfo) {
		Validate.notNull(elementInfo);
		this.elementInfos.add(elementInfo);
	}

	private void addClassInfo(MClassInfo<T, C> classInfo) {
		Validate.notNull(classInfo);
		Validate.isTrue(this.packageInfo.equals(classInfo.getPackageInfo()));
		this.classInfos.add(classInfo);
	}

	private void addEnumLeafInfo(MEnumLeafInfo<T, C> enumLeafInfo) {
		Validate.notNull(enumLeafInfo);
		Validate.isTrue(this.packageInfo.equals(enumLeafInfo.getPackageInfo()));
		this.enumLeafInfos.add(enumLeafInfo);
	}

	private void addPropertyInfo(MPropertyInfo<T, C> propertyInfo) {
		Validate.notNull(propertyInfo);
		Validate.isTrue(this.packageInfo.equals(propertyInfo.getClassInfo()
				.getPackageInfo()));
		this.propertyInfos.add(propertyInfo);
	}

	public void addInfoVertex(InfoVertex<T, C> vertex) {
		Validate.notNull(vertex);
		vertex.accept(infoVertexAdder);
	}

	private final InfoVertexVisitor<T, C, Void> infoVertexAdder = new DefaultInfoVertexVisitor<T, C, Void>() {

		@Override
		public Void visitTypeInfoVertex(TypeInfoVertex<T, C> vertex) {
			addTypeInfo(vertex.getTypeInfo());
			return null;
		}

		@Override
		public Void visitElementInfoVertex(ElementInfoVertex<T, C> vertex) {
			addElementInfo(vertex.getElementInfo());
			return null;
		}

		@Override
		public Void visitPropertyInfoVertex(PropertyInfoVertex<T, C> vertex) {
			addPropertyInfo(vertex.getPropertyInfo());
			return null;
		}
	};

	private final MTypeInfoVisitor<T, C, Void> typeInfoAdder = new DefaultTypeInfoVisitor<T, C, Void>() {

		@Override
		public Void visitClassInfo(MClassInfo<T, C> info) {
			addClassInfo(info);
			return null;
		}

		@Override
		public Void visitEnumLeafInfo(MEnumLeafInfo<T, C> info) {
			addEnumLeafInfo(info);
			return null;
		}
	};
}
