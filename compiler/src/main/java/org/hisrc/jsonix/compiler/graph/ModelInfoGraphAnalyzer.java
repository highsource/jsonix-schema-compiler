package org.hisrc.jsonix.compiler.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jvnet.jaxb2_commons.xml.bind.model.MModelInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPackageInfo;

public class ModelInfoGraphAnalyzer<T, C> {

	private final DirectedGraph<InfoVertex<T, C>, DependencyEdge> graph;

	private final Map<String, MPackageInfo> packageInfoMap;
	private final Set<String> packageNames;
	private final Collection<MPackageInfo> packageInfos;
	private final Map<String, PackageInfoVertex<T, C>> packageVertexMap;
	@SuppressWarnings("unused")
	private final Map<String, Set<InfoVertex<T, C>>> packageVerticesMap;

	// private final ConnectivityInspector<InfoVertex<T, C>, DependencyEdge>
	// connectivityInspector;

	public ModelInfoGraphAnalyzer(MModelInfo<T, C> modelInfo) {
		final DirectedGraph<InfoVertex<T, C>, DependencyEdge> graph = new ModelInfoGraphBuilder<T, C>(
				modelInfo).build();

		this.packageVertexMap = createPackageVertexMap(graph);
		this.packageInfoMap = createPackageInfoMap(this.packageVertexMap);
		this.packageNames = this.packageInfoMap.keySet();
		this.packageInfos = this.packageInfoMap.values();
		this.packageVerticesMap = createPackageVerticesMap(graph);
		this.graph = graph;
		// this.connectivityInspector = new ConnectivityInspector<InfoVertex<T,
		// C>, DependencyEdge>(
		// this.graph);

		// for (PackageInfoVertex<T, C> packageInfoVertex :
		// this.packageVertexMap
		// .values()) {
		// System.out
		// .println(packageInfoVertex
		// + " is connected to the following vertices in other packages:");
		// final Set<InfoVertex<T, C>> connectedVertices =
		// connectedSetOf(packageInfoVertex);
		// for (InfoVertex<T, C> connectedVertex : connectedVertices) {
		// if (connectedVertex.getPackageInfo() != null
		// && connectedVertex.getPackageInfo() != packageInfoVertex
		// .getPackageInfo()) {
		// System.out.println("          " + connectedVertex);
		// }
		// }
		// }
	}

	public Collection<MPackageInfo> getPackageInfos() {
		return packageInfos;
	}

	public Set<String> getPackageNames() {
		return packageNames;
	}
	
	public Map<String, MPackageInfo> getPackageInfoMap() {
		return packageInfoMap;
	}

	private Map<String, MPackageInfo> createPackageInfoMap(
			Map<String, PackageInfoVertex<T, C>> packageVertexMap) {
		Map<String, MPackageInfo> packageInfoMap = new HashMap<String, MPackageInfo>();
		for (Entry<String, PackageInfoVertex<T, C>> entry : packageVertexMap
				.entrySet()) {
			packageInfoMap.put(entry.getKey(), entry.getValue()
					.getPackageInfo());
		}
		return packageInfoMap;
	}

	private Map<String, PackageInfoVertex<T, C>> createPackageVertexMap(
			DirectedGraph<InfoVertex<T, C>, DependencyEdge> graph) {
		final Map<String, PackageInfoVertex<T, C>> packageVertexMap = new HashMap<String, PackageInfoVertex<T, C>>();

		for (InfoVertex<T, C> vertex : graph.vertexSet()) {

			vertex.accept(new DefaultInfoVertexVisitor<T, C, Void>() {
				@Override
				public Void visitPackageInfoVertex(
						PackageInfoVertex<T, C> vertex) {
					final MPackageInfo packageInfo = vertex.getPackageInfo();
					packageVertexMap.put(packageInfo.getPackageName(), vertex);
					return null;
				}
			});
		}

		return packageVertexMap;
	}

	private Map<String, Set<InfoVertex<T, C>>> createPackageVerticesMap(
			DirectedGraph<InfoVertex<T, C>, DependencyEdge> graph) {
		final Map<String, Set<InfoVertex<T, C>>> packageVerticesMap = new HashMap<String, Set<InfoVertex<T, C>>>();
		for (InfoVertex<T, C> vertex : graph.vertexSet()) {
			final MPackageInfo packageInfo = vertex.getPackageInfo();

			if (packageInfo != null) {
				Set<InfoVertex<T, C>> packageVertices = packageVerticesMap
						.get(packageInfo.getPackageName());
				if (packageVertices == null) {
					packageVertices = new HashSet<InfoVertex<T, C>>();
					packageVerticesMap.put(packageInfo.getPackageName(),
							packageVertices);
				}
				packageVertices.add(vertex);
			}
		}
		return packageVerticesMap;
	}

	Map<InfoVertex<T, C>, Set<InfoVertex<T, C>>> vertexToConnectedSet = new HashMap<InfoVertex<T, C>, Set<InfoVertex<T, C>>>();

	public Set<InfoVertex<T, C>> connectedSetOf(InfoVertex<T, C> vertex) {
		Set<InfoVertex<T, C>> connectedSet = vertexToConnectedSet.get(vertex);

		if (connectedSet == null) {
			connectedSet = new HashSet<InfoVertex<T, C>>();

			final BreadthFirstIterator<InfoVertex<T, C>, DependencyEdge> i = new BreadthFirstIterator<InfoVertex<T, C>, DependencyEdge>(
					graph, vertex);

			while (i.hasNext()) {
				connectedSet.add(i.next());
			}

			vertexToConnectedSet.put(vertex, connectedSet);
		}

		return connectedSet;
	}

	public SpaceBuilder createSpaceBuilder(String packageName) {
		final PackageInfoVertex<T, C> packageInfoVertex = this.packageVertexMap
				.get(packageName);
		if (packageInfoVertex == null) {
			throw new IllegalArgumentException("Package [" + packageName
					+ "] does not exist.");
		}

		return new SpaceBuilder();
	}
}
