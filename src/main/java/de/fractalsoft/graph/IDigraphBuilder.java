package de.fractalsoft.graph;

import java.util.Collection;
import java.util.function.Function;

/**
 * Interface for a builder which creates a digraph.
 *
 * @param <E> Typ of the underlying data representing an edge
 * @param <N> Typ of the underlying data representing a node
 */
public interface IDigraphBuilder<E, N> {

    /**
     * Adds a given node to the digraph.
     *
     * @param node node of generic type N to be added
     */
    IDigraphBuilder<E, N> node(N node);

    /**
     * Adds all given nodes to the digraph.
     *
     * @param nodes Collection<N> collection of nodes to be added
     */
    IDigraphBuilder<E, N> nodes(Collection<N> nodes);

    /**
     * Adds a given edge with given source and target nodes to the digraph. The nodes will be added automatically if not already present in this digraph.
     *
     * @param edge edge of generic type E to be added
     * @param source source node of generic type N
     * @param target target node of generic type N
     */
    IDigraphBuilder<E, N> edge(E edge, N source, N target);

    /**
     * Adds a given edge with given source and target nodes' Ids to the digraph. The source and target nodes should be already present in this digraph. Otherwise, an IllegalStateException will
     * be thrown.
     *
     * @param edge edge of generic type E to be added
     * @param sourceId id of the source node
     * @param targetId id of the target node
     */
    IDigraphBuilder<E, N> edgeWithNodeIds(E edge, Object sourceId, Object targetId);

    /**
     * Sets Id extractor function to extract Id from a node object.
     *
     * @param idNodeExtractor Function<N, Object> id extractor function
     */
    IDigraphBuilder<E, N> idNodeExtractor(Function<N, Object> idNodeExtractor);

    /**
     * Sets Id extractor function to extract Id from an edge object.
     *
     * @param idEdgeExtractor Function<N, Object> id extractor function
     */
    IDigraphBuilder<E, N> idEdgeExtractor(Function<E, Object> idEdgeExtractor);

    /**
     * Creats a digraph instance from this builder instance.
     *
     * @return digraph instance which satisfies the interface {@link IDigraph}.
     */
    IDigraph<E, N> build();
}
