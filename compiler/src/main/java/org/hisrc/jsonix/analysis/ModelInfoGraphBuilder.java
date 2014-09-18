package org.hisrc.jsonix.analysis;

import java.text.MessageFormat;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.log.Log;
import org.jgrapht.DirectedGraph;
import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MEnumLeafInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MModelInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPackageInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;

public class ModelInfoGraphBuilder<T, C> {

	private final Log log;

	private final MModelInfo<T, C> modelInfo;

	private final EdgeFactory<InfoVertex<T, C>, DependencyEdge> edgeFactory = new EdgeFactory<InfoVertex<T, C>, DependencyEdge>() {
		@Override
		public DependencyEdge createEdge(InfoVertex<T, C> sourceVertex,
				InfoVertex<T, C> targetVertex) {
			return new DependencyEdge(DependencyType.HARD);
		}
	};

	private final DirectedGraph<InfoVertex<T, C>, DependencyEdge> graph;

	public ModelInfoGraphBuilder(Log log, MModelInfo<T, C> modelInfo) {
		Validate.notNull(log);
		Validate.notNull(modelInfo);
		this.log = log;
		this.modelInfo = modelInfo;
		this.graph = new DefaultDirectedGraph<InfoVertex<T, C>, DependencyEdge>(
				this.edgeFactory);
	}

	public DirectedGraph<InfoVertex<T, C>, DependencyEdge> build() {
		for (final MClassInfo<T, C> info : modelInfo.getClassInfos()) {
			typeInfo(info.getPackageInfo(), info);
		}
		for (final MEnumLeafInfo<T, C> info : modelInfo.getEnumLeafInfos()) {
			typeInfo(info.getPackageInfo(), info);
		}
		for (final MElementInfo<T, C> info : modelInfo.getElementInfos()) {
			elementInfo(info);
		}
		return this.graph;
	}

	public PackageInfoVertex<T, C> packageInfo(MPackageInfo info) {
		final PackageInfoVertex<T, C> packageInfoVertex = new PackageInfoVertex<T, C>(
				info);
		addInfoVertex(packageInfoVertex);
		return packageInfoVertex;
	}

	public ElementInfoVertex<T, C> elementInfo(MElementInfo<T, C> info) {
		final ElementInfoVertex<T, C> elementInfoVertex = new ElementInfoVertex<T, C>(
				info);
		if (addInfoVertex(elementInfoVertex)) {
			final TypeInfoVertex<T, C> typeInfoVertex = typeInfo(
					info.getPackageInfo(), info.getTypeInfo());
			addHardDependency(elementInfoVertex, typeInfoVertex);

			final PackageInfoVertex<T, C> packageInfoVertex = packageInfo(info
					.getPackageInfo());
			addSoftDependency(packageInfoVertex, elementInfoVertex);
		}
		return elementInfoVertex;

	}

	public TypeInfoVertex<T, C> typeInfo(MPackageInfo packageInfo,
			MTypeInfo<T, C> info) {
		return info.acceptTypeInfoVisitor(new TypeInfoGraphBuilder<T, C>(this,
				packageInfo));
	}

	public PropertyInfoVertex<T, C> propertyInfo(MPropertyInfo<T, C> info) {
		return info
				.acceptPropertyInfoVisitor(new PropertyInfoGraphBuilder<T, C>(
						this, this.modelInfo));
	}

	public boolean addInfoVertex(InfoVertex<T, C> vertex) {
		Validate.notNull(vertex);
		final boolean added = this.graph.addVertex(vertex);
		if (added) {
			log.trace(MessageFormat.format("Added ({0}).", vertex.toString()));
		}
		return added;
	}

	public boolean addHardDependency(InfoVertex<T, C> source,
			InfoVertex<T, C> target) {
		Validate.notNull(source);
		Validate.notNull(target);
		final boolean added = this.graph.addEdge(source, target,
				new DependencyEdge(DependencyType.HARD));
		if (added) {
			log.trace(MessageFormat.format(
					"Added hard dependency ({0})->({1}).", source.toString(),
					target.toString()));
		}
		return added;
	}

	public boolean addSoftDependency(InfoVertex<T, C> source,
			InfoVertex<T, C> target) {
		Validate.notNull(source);
		Validate.notNull(target);
		final boolean added = this.graph.addEdge(source, target,
				new DependencyEdge(DependencyType.SOFT));
		if (added) {
			log.trace(MessageFormat.format(
					"Added soft dependency ({0})->({1}).", source.toString(),
					target.toString()));
		}
		return added;
	}

}
