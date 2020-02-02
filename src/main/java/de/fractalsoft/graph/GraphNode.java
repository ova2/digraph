package de.fractalsoft.graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Implementation for a generic node in a digraph.
 *
 * @param <E> Typ of the underlying data representing an edge
 * @param <N> Typ of the underlying data representing a node
 */
public class GraphNode<E, N> {

    private final N data;
    private final Object id;
    // incoming edges
    private final Set<GraphEdge<E, N>> inEdges = new HashSet<>();
    // outgoing edges
    private final Set<GraphEdge<E, N>> outEdges = new HashSet<>();

    public GraphNode(N data, Object id) {
        Objects.requireNonNull(data);
        Objects.requireNonNull(id);
        this.data = data;
        this.id = id;
    }

    N getData() {
        return data;
    }

    Object getId() {
        return id;
    }

    Set<GraphEdge<E, N>> inEdges() {
        return Collections.unmodifiableSet(inEdges);
    }

    Set<GraphEdge<E, N>> outEdges() {
        return Collections.unmodifiableSet(outEdges);
    }

    void addInEdge(GraphEdge<E, N> edge) {
        inEdges.add(edge);
    }

    void addOutEdge(GraphEdge<E, N> edge) {
        outEdges.add(edge);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        @SuppressWarnings("unchecked")
        GraphNode<E, N> that = (GraphNode<E, N>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
