package de.fractalsoft.graph;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * Interface for a digraph.
 *
 * @param <E> Typ of the underlying data representing an edge
 * @param <N> Typ of the underlying data representing a node
 */
public interface IDigraph<E, N> {

    /**
     * Gets all nodes.
     *
     * @return Set<N> nodes of generic type N
     */
    Set<N> getNodes();

    /**
     * Gets all edges.
     *
     * @return Set<E> edges of generic type E
     */
    Set<E> getEdges();

    /**
     * Finds a node by Id if the extractor function for node's Id was defined.
     *
     * @param id Object node's Id
     * @return Optional<N> found node as Optional
     */
    Optional<N> findNodeById(Object id);

    /**
     * Finds a source node by the given edge.
     *
     * @param edge edge of generic type E
     * @return Optional<N> found node as Optional
     */
    Optional<N> findSourceNodeByEdge(E edge);

    /**
     * Finds a target node by the given edge.
     *
     * @param edge edge of generic type E
     * @return Optional<N> found node as Optional
     */
    Optional<N> findTargetNodeByEdge(E edge);

    /**
     * Finds an edge by Id if the extractor function for edge's Id was defined.
     *
     * @param id Object edge's Id
     * @return Optional<N> found edge as Optional
     */
    Optional<E> findEdgeById(Object id);

    /**
     * Finds incoming edges by the given node.
     *
     * @param node node of generic type N
     * @return Collection<E> collection of found incoming edges
     */
    Collection<E> findIncomingEdgesByNode(N node);

    /**
     * Finds outgoing edges by the given node.
     *
     * @param node node of generic type N
     * @return Collection<E> collection of found outgoing edges
     */
    Collection<E> findOutgoingEdgesByNode(N node);
}
