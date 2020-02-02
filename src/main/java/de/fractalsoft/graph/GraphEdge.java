package de.fractalsoft.graph;

import java.util.Objects;

/**
 * Implementation for a generic edge in a digraph.
 *
 * @param <E> Typ of the underlying data representing an edge
 * @param <N> Typ of the underlying data representing a node
 */
public class GraphEdge<E, N> {

    private final E data;
    private final Object id;
    private final GraphNode<E, N> source;
    private final GraphNode<E, N> target;

    public GraphEdge(E data, Object id, GraphNode<E, N> source, GraphNode<E, N> target) {
        Objects.requireNonNull(data);
        Objects.requireNonNull(id);
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
        this.data = data;
        this.id = id;
        this.source = source;
        this.target = target;
        // set relations
        this.source.addOutEdge(this);
        this.target.addInEdge(this);
    }

    E getData() {
        return data;
    }

    Object getId() {
        return id;
    }

    GraphNode<E, N> getSource() {
        return source;
    }

    GraphNode<E, N> getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        @SuppressWarnings("unchecked")
        GraphEdge<E, N> that = (GraphEdge<E, N>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
