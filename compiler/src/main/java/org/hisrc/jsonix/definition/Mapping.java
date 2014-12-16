package org.hisrc.jsonix.definition;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.analysis.DefaultInfoVertexVisitor;
import org.hisrc.jsonix.analysis.DependencyEdge;
import org.hisrc.jsonix.analysis.DependencyType;
import org.hisrc.jsonix.analysis.ElementInfoVertex;
import org.hisrc.jsonix.analysis.InfoVertex;
import org.hisrc.jsonix.analysis.InfoVertexVisitor;
import org.hisrc.jsonix.analysis.ModelInfoGraphAnalyzer;
import org.hisrc.jsonix.analysis.PackageInfoVertex;
import org.hisrc.jsonix.analysis.PropertyInfoVertex;
import org.hisrc.jsonix.analysis.TypeInfoVertex;
import org.hisrc.jsonix.context.JsonixContext;
import org.jgrapht.DirectedGraph;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MEnumLeafInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPackageInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfoVisitor;
import org.jvnet.jaxb2_commons.xml.bind.model.util.DefaultTypeInfoVisitor;
import org.slf4j.Logger;

public class Mapping<T, C extends T> {

	private final Logger logger;
	private final ModelInfoGraphAnalyzer<T, C> analyzer;
	private final MPackageInfo packageInfo;
	private final Collection<MClassInfo<T, C>> classInfos = new HashSet<MClassInfo<T, C>>();
	private final Collection<MPropertyInfo<T, C>> propertyInfos = new HashSet<MPropertyInfo<T, C>>();
	private final Collection<MEnumLeafInfo<T, C>> enumLeafInfos = new HashSet<MEnumLeafInfo<T, C>>();
	private final Collection<MElementInfo<T, C>> elementInfos = new HashSet<MElementInfo<T, C>>();
	private final String packageName;
	private final String mappingName;
	private final String defaultElementNamespaceURI;
	private final String defaultAttributeNamespaceURI;
	private final Map<InfoVertex<T, C>, ContainmentType> verticesContainmentMap = new HashMap<InfoVertex<T, C>, ContainmentType>();

	public Mapping(JsonixContext context,
			ModelInfoGraphAnalyzer<T, C> analyzer, MPackageInfo packageInfo,
			String mappingName, String defaultElementNamespaceURI,
			String defaultAttributeNamespaceURI) {
		this.logger = Validate.notNull(context).getLoggerFactory()
				.getLogger(Mapping.class.getName());
		Validate.notNull(analyzer);
		Validate.notNull(packageInfo);
		Validate.notNull(mappingName);
		Validate.notNull(defaultElementNamespaceURI);
		Validate.notNull(defaultAttributeNamespaceURI);
		this.analyzer = analyzer;
		this.packageInfo = packageInfo;
		this.packageName = packageInfo.getPackageName();
		this.mappingName = mappingName;
		this.defaultElementNamespaceURI = defaultElementNamespaceURI;
		this.defaultAttributeNamespaceURI = defaultAttributeNamespaceURI;
	}

	public boolean isEmpty() {
		return this.elementInfos.isEmpty() && this.classInfos.isEmpty()
				&& this.enumLeafInfos.isEmpty();
	}

	public void includePackage(MPackageInfo packageInfo) {
		Validate.notNull(packageInfo);
		final PackageInfoVertex<T, C> vertex = new PackageInfoVertex<T, C>(
				packageInfo);
		includeInfoVertex(vertex);
	}

	public void includePropertyInfo(MPropertyInfo<T, C> propertyInfo) {
		Validate.notNull(propertyInfo);
		final PropertyInfoVertex<T, C> vertex = new PropertyInfoVertex<T, C>(
				propertyInfo);
		includeInfoVertex(vertex);
	}

	public void includeElementInfo(MElementInfo<T, C> elementInfo) {
		Validate.notNull(elementInfo);
		final ElementInfoVertex<T, C> vertex = new ElementInfoVertex<T, C>(
				elementInfo);
		includeInfoVertex(vertex);
	}

	public void includeTypeInfo(MTypeInfo<T, C> typeInfo) {
		Validate.notNull(typeInfo);
		final TypeInfoVertex<T, C> vertex = new TypeInfoVertex<T, C>(
				this.packageInfo, typeInfo);
		includeInfoVertex(vertex);
	}

	public void includeDependenciesOfMapping(Mapping<T, C> mapping) {
		Validate.notNull(mapping);

		final Map<InfoVertex<T, C>, ContainmentType> dependendMappingVerticesContainmentMap = mapping.verticesContainmentMap;
		for (Map.Entry<InfoVertex<T, C>, ContainmentType> entry : dependendMappingVerticesContainmentMap
				.entrySet()) {
			final ContainmentType dependendMappingVertexContainmentType = entry
					.getValue();
			// If a given vertex is excluded in the dependend mapping
			if (!dependendMappingVertexContainmentType.isIncluded()) {
				// Check if this vertex is included in the given map
				final InfoVertex<T, C> dependendMappingVertex = entry.getKey();
				final ContainmentType containmentType = this.verticesContainmentMap
						.get(dependendMappingVertex);
				final ContainmentType newContainmentType = dependendMappingVertexContainmentType
						.combineWith(containmentType);
				if (newContainmentType != containmentType) {
					this.verticesContainmentMap.put(dependendMappingVertex,
							newContainmentType);
				}
			}
		}
		for (Map.Entry<InfoVertex<T, C>, ContainmentType> entry : dependendMappingVerticesContainmentMap
				.entrySet()) {
			final ContainmentType dependendMappingVertexContainmentType = entry
					.getValue();
			// If a given vertex is excluded in the dependend mapping
			if (dependendMappingVertexContainmentType.isIncluded()) {
				final InfoVertex<T, C> dependendMappingVertex = entry.getKey();
				includeInfoVertex(dependendMappingVertex);
			}
		}
	}

	public void excludePropertyInfo(MPropertyInfo<T, C> propertyInfo) {
		Validate.notNull(propertyInfo);
		final PropertyInfoVertex<T, C> vertex = new PropertyInfoVertex<T, C>(
				propertyInfo);
		excludeInfoVertex(vertex);
	}

	public void excludeElementInfo(MElementInfo<T, C> elementInfo) {
		Validate.notNull(elementInfo);
		final ElementInfoVertex<T, C> vertex = new ElementInfoVertex<T, C>(
				elementInfo);
		excludeInfoVertex(vertex);
	}

	public void excludeTypeInfo(MTypeInfo<T, C> typeInfo) {
		Validate.notNull(typeInfo);
		final TypeInfoVertex<T, C> vertex = new TypeInfoVertex<T, C>(
				this.packageInfo, typeInfo);
		excludeInfoVertex(vertex);
	}

	private void includeInfoVertex(final InfoVertex<T, C> initialVertex) {
		logger.trace(MessageFormat.format("Including the vertex [{0}].",
				initialVertex));

		final DirectedGraph<InfoVertex<T, C>, DependencyEdge> graph = analyzer
				.getGraph();

		final SortedMap<ContainmentType, Deque<InfoVertex<T, C>>> deques = new TreeMap<ContainmentType, Deque<InfoVertex<T, C>>>();
		deques.put(ContainmentType.INCLUDED_EXPLICITLY,
				new LinkedList<InfoVertex<T, C>>());
		deques.put(ContainmentType.INCLUDED_AS_HARD_DEPENDENCY,
				new LinkedList<InfoVertex<T, C>>());
		deques.put(ContainmentType.INCLUDED_AS_SOFT_DEPENDENCY,
				new LinkedList<InfoVertex<T, C>>());
		deques.get(ContainmentType.INCLUDED_EXPLICITLY).add(initialVertex);

		for (Map.Entry<ContainmentType, Deque<InfoVertex<T, C>>> dequeEntry : deques
				.entrySet()) {
			final ContainmentType dequeContainmentType = dequeEntry.getKey();
			final Deque<InfoVertex<T, C>> deque = dequeEntry.getValue();
			while (!deque.isEmpty()) {
				final InfoVertex<T, C> sourceVertex = deque.removeFirst();

				final ContainmentType currentSourceContainmentType = getInfoVertexContainmentType(sourceVertex);
				final ContainmentType sourceContainmentType = dequeContainmentType
						.combineWith(currentSourceContainmentType);

				if (currentSourceContainmentType == null
						|| currentSourceContainmentType
								.compareTo(sourceContainmentType) > 0) {

					if (currentSourceContainmentType != null
							&& !currentSourceContainmentType.isIncluded()) {
						logger.warn(MessageFormat
								.format("The vertex [{0}] was excluded with the containment type [{1}], but it must be included with containment type [{2}], otherwise mappings will not be consistent.",
										sourceVertex,
										currentSourceContainmentType,
										sourceContainmentType));
					} else {

						logger.trace(MessageFormat
								.format("Including the vertex [{0}] with the containment type [{1}].",
										sourceVertex, dequeContainmentType));
					}

					setInfoVertexContainmentType(sourceVertex,
							sourceContainmentType);

					final Set<DependencyEdge> edges = graph
							.outgoingEdgesOf(sourceVertex);
					for (DependencyEdge edge : edges) {
						final InfoVertex<T, C> targetVertex = graph
								.getEdgeTarget(edge);

						final DependencyType dependencyType = edge.getType();
						final ContainmentType targetContainmentType = dependencyType
								.combineWith(sourceContainmentType);
						if (targetContainmentType != null) {
							final Deque<InfoVertex<T, C>> targetDeque = deques
									.get(targetContainmentType);
							if (targetDeque != null) {
								logger.trace(MessageFormat
										.format("Queueing the inclusion of the vertex [{0}] with the containment type [{1}].",
												targetVertex,
												targetContainmentType));
								targetDeque.add(targetVertex);
							}
						}
					}
				} else {
					logger.trace(MessageFormat
							.format("Vertex [{0}] is already included with the containment type [{1}].",
									sourceVertex, currentSourceContainmentType));
				}
			}
		}
	}

	private ContainmentType getInfoVertexContainmentType(
			final InfoVertex<T, C> sourceVertex) {
		ContainmentType sourceContainmentType = verticesContainmentMap
				.get(sourceVertex);
		return sourceContainmentType;
	}

	private void excludeInfoVertex(final InfoVertex<T, C> vertex) {
		Validate.notNull(vertex);
		logger.trace(MessageFormat.format("Excluding [{0}].", vertex));

		final DirectedGraph<InfoVertex<T, C>, DependencyEdge> graph = analyzer
				.getGraph();

		final SortedMap<ContainmentType, Deque<InfoVertex<T, C>>> deques = new TreeMap<ContainmentType, Deque<InfoVertex<T, C>>>();
		deques.put(ContainmentType.EXCLUDED_EXPLICITLY,
				new LinkedList<InfoVertex<T, C>>());
		deques.put(ContainmentType.EXCLUDED_AS_HARD_DEPENDENCY,
				new LinkedList<InfoVertex<T, C>>());
		deques.get(ContainmentType.EXCLUDED_EXPLICITLY).add(vertex);

		for (Map.Entry<ContainmentType, Deque<InfoVertex<T, C>>> dequeEntry : deques
				.entrySet()) {
			final ContainmentType dequeContainmentType = dequeEntry.getKey();
			final Deque<InfoVertex<T, C>> deque = dequeEntry.getValue();
			while (!deque.isEmpty()) {
				final InfoVertex<T, C> targetVertex = deque.removeFirst();

				final ContainmentType currentTargetContainmentType = getInfoVertexContainmentType(targetVertex);
				final ContainmentType targetContainmentType = dequeContainmentType
						.combineWith(currentTargetContainmentType);

				if (currentTargetContainmentType == null
						|| currentTargetContainmentType
								.compareTo(targetContainmentType) > 0) {

					logger.trace(MessageFormat
							.format("Excluding the vertex [{0}] with the containment type [{1}].",
									targetVertex, targetContainmentType));

					setInfoVertexContainmentType(targetVertex,
							targetContainmentType);

					final Set<DependencyEdge> edges = graph
							.incomingEdgesOf(targetVertex);
					for (DependencyEdge edge : edges) {
						final InfoVertex<T, C> sourceVertex = graph
								.getEdgeSource(edge);
						final DependencyType dependencyType = edge.getType();
						final ContainmentType sourceContainmentType = dependencyType
								.combineWith(targetContainmentType);
						if (sourceContainmentType != null) {
							final Deque<InfoVertex<T, C>> sourceDeque = deques
									.get(sourceContainmentType);
							if (sourceDeque != null) {
								logger.trace(MessageFormat
										.format("Queueing the exclusion of the vertex [{0}] with the containment type [{1}].",
												sourceVertex,
												sourceContainmentType));
								sourceDeque.add(sourceVertex);
							}
						}
					}
				} else {
					logger.trace(MessageFormat
							.format("Vertex [{0}] is already excluded with the containment type [{1}].",
									targetVertex, targetContainmentType));
				}
			}
		}
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

	private void setInfoVertexContainmentType(InfoVertex<T, C> vertex,
			ContainmentType containmentType) {
		Validate.notNull(vertex);
		verticesContainmentMap.put(vertex, containmentType);
		if (containmentType.isIncluded()) {
			vertex.accept(infoVertexAdder);
		}
	}

	private final MTypeInfoVisitor<T, C, Void> typeInfoAdder = new DefaultTypeInfoVisitor<T, C, Void>() {

		@Override
		public Void visitClassInfo(MClassInfo<T, C> info) {
			Mapping.this.addClassInfo(info);
			return null;
		}

		@Override
		public Void visitEnumLeafInfo(MEnumLeafInfo<T, C> info) {
			Mapping.this.addEnumLeafInfo(info);
			return null;
		}
	};

	private void addTypeInfo(MTypeInfo<T, C> typeInfo) {
		Validate.notNull(typeInfo);
		typeInfo.acceptTypeInfoVisitor(typeInfoAdder);
	}

	private void addElementInfo(MElementInfo<T, C> elementInfo) {
		Validate.notNull(elementInfo);
		if (this.packageInfo.equals(elementInfo.getPackageInfo())) {
			this.elementInfos.add(elementInfo);
		}
	}

	private void addClassInfo(MClassInfo<T, C> classInfo) {
		Validate.notNull(classInfo);
		if (this.packageInfo.equals(classInfo.getPackageInfo())) {
			this.classInfos.add(classInfo);
		}
	}

	private void addEnumLeafInfo(MEnumLeafInfo<T, C> enumLeafInfo) {
		Validate.notNull(enumLeafInfo);
		if (this.packageInfo.equals(enumLeafInfo.getPackageInfo())) {
			this.enumLeafInfos.add(enumLeafInfo);
		}
	}

	private void addPropertyInfo(MPropertyInfo<T, C> propertyInfo) {
		Validate.notNull(propertyInfo);
		if (this.packageInfo.equals(propertyInfo.getClassInfo()
				.getPackageInfo())) {
			this.propertyInfos.add(propertyInfo);
		}
	}

	public MPackageInfo getPackageInfo() {
		return packageInfo;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getMappingName() {
		return mappingName;
	}

	public String getDefaultElementNamespaceURI() {
		return defaultElementNamespaceURI;
	}

	public String getDefaultAttributeNamespaceURI() {
		return defaultAttributeNamespaceURI;
	}

	public Collection<MClassInfo<T, C>> getClassInfos() {
		return this.classInfos;
	}

	public Collection<MPropertyInfo<T, C>> getPropertyInfos() {
		return propertyInfos;
	}

	public Collection<MEnumLeafInfo<T, C>> getEnumLeafInfos() {
		return this.enumLeafInfos;
	}

	public Collection<MElementInfo<T, C>> getElementInfos() {
		return this.elementInfos;
	}

	@Override
	public String toString() {
		return MessageFormat.format("Mapping [{0}]", this.mappingName);
	}
}
