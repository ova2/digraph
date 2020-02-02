package de.fractalsoft.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementation for an immutable directed graph (often called "digraph"). An edge in the directed graph has a source and a target nodes. The given edge is designated as "outgoing edge" for
 * the source node and as "incoming edge" for the target node. This directed graph implementation provides several add- and finder-methods for edges and nodes.
 *
 * @param <E> Typ of the underlying data representing an edge
 * @param <N> Typ of the underlying data representing a node
 */
public class Digraph<E, N> implements IDigraph<E, N> {

    // optional functions to extract Ids from the underlying data. This can bring a better performance while accessing nodes and edges.
    private final Function<N, Object> idNodeExtractor;
    private final Function<E, Object> idEdgeExtractor;
    // indexed stores for nodes and edges for fast access
    private final Map<Object, GraphNode<E, N>> nodesById;
    private final Map<Object, GraphEdge<E, N>> edgesById;

    private Digraph(DigraphBuilder<E, N> builder) {
        this.nodesById = builder.nodesById;
        this.edgesById = builder.edgesById;
        this.idNodeExtractor = builder.idNodeExtractor;
        this.idEdgeExtractor = builder.idEdgeExtractor;
    }

    /**
     * Creates a builder instance for a digraph.
     *
     * @param <E> Typ of the underlying data representing an edge
     * @param <N> Typ of the underlying data representing a node
     * @return builder instance which satisfies the builder interface {@link IDigraphBuilder}
     */
    public static <E, N> IDigraphBuilder<E, N> builder() {
        return new DigraphBuilder<>();
    }

    @Override
    public Set<N> getNodes() {
        return nodesById.values().stream().map(GraphNode::getData).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<E> getEdges() {
        return edgesById.values().stream().map(GraphEdge::getData).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Optional<N> findNodeById(Object id) {
        return Optional.ofNullable(nodesById.get(id)).map(GraphNode::getData);
    }

    @Override
    public Optional<N> findSourceNodeByEdge(E edge) {
        return Optional.ofNullable(edgesById.get(idEdgeExtractor.apply(edge)))
                .map(GraphEdge::getSource)
                .map(GraphNode::getData);
    }

    @Override
    public Optional<N> findTargetNodeByEdge(E edge) {
        return Optional.ofNullable(edgesById.get(idEdgeExtractor.apply(edge)))
                .map(GraphEdge::getTarget)
                .map(GraphNode::getData);
    }

    @Override
    public Optional<E> findEdgeById(Object id) {
        return Optional.ofNullable(edgesById.get(id)).map(GraphEdge::getData);
    }

    @Override
    public Collection<E> findIncomingEdgesByNode(N node) {
        GraphNode<E, N> graphNode = nodesById.get(idNodeExtractor.apply(node));
        if (graphNode == null) {
            return Collections.emptySet();
        }
        return graphNode.inEdges().stream().map(GraphEdge::getData).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Collection<E> findOutgoingEdgesByNode(N node) {
        GraphNode<E, N> graphNode = nodesById.get(idNodeExtractor.apply(node));
        if (graphNode == null) {
            return Collections.emptySet();
        }
        return graphNode.outEdges().stream().map(GraphEdge::getData).collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Builder class to creates a directed graph (digraph) with edges and nodes.
     */
    public static class DigraphBuilder<E, N> implements IDigraphBuilder<E, N> {

        private Function<N, Object> idNodeExtractor;
        private Function<E, Object> idEdgeExtractor;
        private final Map<Object, GraphNode<E, N>> nodesById;
        private final Map<Object, GraphEdge<E, N>> edgesById;

        private DigraphBuilder() {
            this.nodesById = new HashMap<>();
            this.edgesById = new HashMap<>();
            this.idNodeExtractor = (n) -> n;
            this.idEdgeExtractor = (e) -> e;
        }

        @Override
        public IDigraphBuilder<E, N> idNodeExtractor(Function<N, Object> idNodeExtractor) {
            this.idNodeExtractor = idNodeExtractor;
            return this;
        }

        @Override
        public IDigraphBuilder<E, N> idEdgeExtractor(Function<E, Object> idEdgeExtractor) {
            this.idEdgeExtractor = idEdgeExtractor;
            return this;
        }

        @Override
        public IDigraphBuilder<E, N> nodes(Collection<N> nodes) {
            nodes.forEach(this::createNode);
            return this;
        }

        @Override
        public IDigraphBuilder<E, N> node(N node) {
            Objects.requireNonNull(node);
            createNode(node);
            return this;
        }

        @Override
        public IDigraphBuilder<E, N> edge(E edge, N source, N target) {
            Objects.requireNonNull(edge);
            Objects.requireNonNull(source);
            Objects.requireNonNull(target);

            // add GraphNodes if they don't exist yet
            GraphNode<E, N> graphNodeSource = nodesById.get(idNodeExtractor.apply(source));
            if (graphNodeSource == null) {
                graphNodeSource = createNode(source);
            }
            GraphNode<E, N> graphNodeTarget = nodesById.get(idNodeExtractor.apply(target));
            if (graphNodeTarget == null) {
                graphNodeTarget = createNode(target);
            }

            // create a GraphEdge
            createEdge(edge, graphNodeSource, graphNodeTarget);
            return this;
        }

        @Override
        public IDigraphBuilder<E, N> edgeWithNodeIds(E edge, Object sourceId, Object targetId) {
            Objects.requireNonNull(edge);
            Objects.requireNonNull(sourceId);
            Objects.requireNonNull(targetId);

            GraphNode<E, N> graphNodeSource = nodesById.get(sourceId);
            if (graphNodeSource == null) {
                throw new IllegalStateException("Digraph is not well-formed, no source node was found by the Id " + sourceId);
            }

            GraphNode<E, N> graphNodeTarget = nodesById.get(targetId);
            if (graphNodeTarget == null) {
                throw new IllegalStateException("Digraph is not well-formed, no target node was found by the Id " + targetId);
            }

            // create a GraphEdge
            createEdge(edge, graphNodeSource, graphNodeTarget);
            return this;
        }

        @Override
        public IDigraph<E, N> build() {
            return new Digraph<>(this);
        }

        // private methods

        private GraphNode<E, N> createNode(N node) {
            Object id = idNodeExtractor.apply(node);
            GraphNode<E, N> graphNode = new GraphNode<>(node, id);
            nodesById.put(id, graphNode);
            return graphNode;
        }

        private void createEdge(E edge, GraphNode<E, N> graphNodeSource, GraphNode<E, N> graphNodeTarget) {
            Object id = idEdgeExtractor.apply(edge);
            GraphEdge<E, N> graphEdge = new GraphEdge<>(edge, id, graphNodeSource, graphNodeTarget);
            edgesById.put(id, graphEdge);
        }
    }
}
