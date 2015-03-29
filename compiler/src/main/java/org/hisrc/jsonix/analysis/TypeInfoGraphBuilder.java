package org.hisrc.jsonix.analysis;

import org.apache.commons.lang3.Validate;
import org.jvnet.jaxb2_commons.xml.bind.model.MBuiltinLeafInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassRef;
import org.jvnet.jaxb2_commons.xml.bind.model.MEnumLeafInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MID;
import org.jvnet.jaxb2_commons.xml.bind.model.MIDREF;
import org.jvnet.jaxb2_commons.xml.bind.model.MIDREFS;
import org.jvnet.jaxb2_commons.xml.bind.model.MList;
import org.jvnet.jaxb2_commons.xml.bind.model.MPackageInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfoVisitor;
import org.jvnet.jaxb2_commons.xml.bind.model.MWildcardTypeInfo;

public class TypeInfoGraphBuilder<T, C extends T> implements
		MTypeInfoVisitor<T, C, TypeInfoVertex<T, C>> {

	private final ModelInfoGraphBuilder<T, C> modelInfoGraphBuilder;
	private final MPackageInfo packageInfo;

	public TypeInfoGraphBuilder(
			ModelInfoGraphBuilder<T, C> modelInfoGraphBuilder,
			MPackageInfo packageInfo) {
		Validate.notNull(modelInfoGraphBuilder);
		Validate.notNull(packageInfo);
		this.modelInfoGraphBuilder = modelInfoGraphBuilder;
		this.packageInfo = packageInfo;
	}

	@Override
	public TypeInfoVertex<T, C> visitBuiltinLeafInfo(MBuiltinLeafInfo<T, C> info) {
		final TypeInfoVertex<T, C> typeVertex = new TypeInfoVertex<T, C>(null,
				info);
		addInfoVertex(typeVertex);
		return typeVertex;
	}

	@Override
	public TypeInfoVertex<T, C> visitWildcardTypeInfo(
			MWildcardTypeInfo<T, C> info) {
		final TypeInfoVertex<T, C> typeVertex = new TypeInfoVertex<T, C>(null,
				info);
		addInfoVertex(typeVertex);
		return typeVertex;
	}

	@Override
	public TypeInfoVertex<T, C> visitID(MID<T, C> info) {
		final TypeInfoVertex<T, C> typeVertex = new TypeInfoVertex<T, C>(
				null, info);
		if (addInfoVertex(typeVertex)) {
			final TypeInfoVertex<T, C> valueTypeVertex = modelInfoGraphBuilder
					.typeInfo(packageInfo, info.getValueTypeInfo());
			modelInfoGraphBuilder
					.addHardDependency(typeVertex, valueTypeVertex);
		}
		return typeVertex;
	}

	@Override
	public TypeInfoVertex<T, C> visitIDREF(MIDREF<T, C> info) {
		final TypeInfoVertex<T, C> typeVertex = new TypeInfoVertex<T, C>(
				null, info);
		if (addInfoVertex(typeVertex)) {
			final TypeInfoVertex<T, C> valueTypeVertex = modelInfoGraphBuilder
					.typeInfo(packageInfo, info.getValueTypeInfo());
			modelInfoGraphBuilder
					.addHardDependency(typeVertex, valueTypeVertex);
		}
		return typeVertex;
	}

	@Override
	public TypeInfoVertex<T, C> visitIDREFS(MIDREFS<T, C> info) {
		final TypeInfoVertex<T, C> typeVertex = new TypeInfoVertex<T, C>(
				null, info);
		if (addInfoVertex(typeVertex)) {
			final TypeInfoVertex<T, C> valueTypeVertex = modelInfoGraphBuilder
					.typeInfo(packageInfo, info.getItemTypeInfo());
			modelInfoGraphBuilder
					.addHardDependency(typeVertex, valueTypeVertex);
		}
		return typeVertex;
	}

	@Override
	public TypeInfoVertex<T, C> visitList(MList<T, C> info) {
		final TypeInfoVertex<T, C> typeVertex = new TypeInfoVertex<T, C>(
				null, info);
		if (addInfoVertex(typeVertex)) {
			final TypeInfoVertex<T, C> itemTypeVertex = modelInfoGraphBuilder
					.typeInfo(packageInfo, info.getItemTypeInfo());
			modelInfoGraphBuilder.addHardDependency(typeVertex, itemTypeVertex);
		}
		return typeVertex;
	}

	@Override
	public TypeInfoVertex<T, C> visitEnumLeafInfo(MEnumLeafInfo<T, C> info) {
		final TypeInfoVertex<T, C> typeVertex = new TypeInfoVertex<T, C>(
				info.getPackageInfo(), info);
		if (addInfoVertex(typeVertex)) {
			if (info.getBaseTypeInfo() != null) {
				final TypeInfoVertex<T, C> baseTypeVertex = modelInfoGraphBuilder
						.typeInfo(info.getPackageInfo(), info.getBaseTypeInfo());
				modelInfoGraphBuilder.addHardDependency(typeVertex,
						baseTypeVertex);
			}
			final PackageInfoVertex<T, C> packageInfoVertex = modelInfoGraphBuilder
					.packageInfo(info.getPackageInfo());
			modelInfoGraphBuilder.addSoftDependency(packageInfoVertex,
					typeVertex);
		}
		return typeVertex;
	}

	@Override
	public TypeInfoVertex<T, C> visitClassRef(MClassRef<T, C> info) {
		final TypeInfoVertex<T, C> typeVertex = new TypeInfoVertex<T, C>(
				info.getPackageInfo(), info);
		if (addInfoVertex(typeVertex)) {
			final PackageInfoVertex<T, C> packageInfoVertex = modelInfoGraphBuilder
					.packageInfo(info.getPackageInfo());
			modelInfoGraphBuilder.addSoftDependency(packageInfoVertex,
					typeVertex);

		}
		return typeVertex;
	}

	@Override
	public TypeInfoVertex<T, C> visitClassInfo(MClassInfo<T, C> info) {
		final TypeInfoVertex<T, C> typeVertex = new TypeInfoVertex<T, C>(
				info.getPackageInfo(), info);
		if (addInfoVertex(typeVertex)) {
			if (info.getBaseTypeInfo() != null) {
				final TypeInfoVertex<T, C> baseTypeVertex = modelInfoGraphBuilder
						.typeInfo(info.getPackageInfo(), info.getBaseTypeInfo());
				modelInfoGraphBuilder.addHardDependency(typeVertex,
						baseTypeVertex);
			}
			for (MPropertyInfo<T, C> propertyInfo : info.getProperties()) {
				final InfoVertex<T, C> propertyVertex = modelInfoGraphBuilder
						.propertyInfo(propertyInfo);
				modelInfoGraphBuilder.addSoftDependency(typeVertex,
						propertyVertex);
			}
			final PackageInfoVertex<T, C> packageInfoVertex = modelInfoGraphBuilder
					.packageInfo(info.getPackageInfo());
			modelInfoGraphBuilder.addSoftDependency(packageInfoVertex,
					typeVertex);
		}
		return typeVertex;
	}

	private boolean addInfoVertex(InfoVertex<T, C> vertex) {
		return modelInfoGraphBuilder.addInfoVertex(vertex);
	}

}
