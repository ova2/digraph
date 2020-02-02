package de.fractalsoft.digraph;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.fractalsoft.graph.Digraph;
import de.fractalsoft.graph.IDigraph;
import de.fractalsoft.graph.IDigraphBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the following Digraph with 4 nodes and 4 edges:
 * <pre>
 *                -------->--------
 *               /                 \
 * 1          2 /                   \ 3          4
 * o----->-----o----------<----------o----->-----o
 * </pre>
 */
class DigraphTest {

    private DummyNode node1;
    private DummyNode node2;
    private DummyNode node3;
    private DummyNode node4;

    private DummyEdge edge1;
    private DummyEdge edge2;
    private DummyEdge edge3;
    private DummyEdge edge4;

    private IDigraphBuilder<DummyEdge, DummyNode> digraphBuilder;

    @BeforeEach
    public void setup() {
        node1 = new DummyNode(1, "1");
        node2 = new DummyNode(2, "2");
        node3 = new DummyNode(3, "3");
        node4 = new DummyNode(4, "4");

        edge1 = new DummyEdge(12, "1 -> 2");
        edge2 = new DummyEdge(23, "2 -> 3");
        edge3 = new DummyEdge(32, "3 -> 2");
        edge4 = new DummyEdge(34, "3 -> 4");

        digraphBuilder = Digraph.<DummyEdge, DummyNode>builder()
                .idNodeExtractor(DummyNode::getId)
                .idEdgeExtractor(DummyEdge::getId)
                .edge(edge1, node1, node2)
                .edge(edge2, node2, node3)
                .edge(edge3, node3, node2)
                .edge(edge4, node3, node4);
    }

    @Test
    void getNodes() {
        assertThat(digraphBuilder.build().getNodes().size()).isEqualTo(4);
    }

    @Test
    void getEdges() {
        assertThat(digraphBuilder.build().getEdges().size()).isEqualTo(4);
    }

    @Test
    void findNodeById() {
        IDigraph<DummyEdge, DummyNode> digraph = digraphBuilder.build();
        assertThat(digraph.findNodeById(1).isPresent()).isTrue();
        assertThat(digraph.findNodeById(2).isPresent()).isTrue();
        assertThat(digraph.findNodeById(3).isPresent()).isTrue();
        assertThat(digraph.findNodeById(4).isPresent()).isTrue();
        assertThat(digraph.findNodeById(1234).isPresent()).isFalse();
    }

    @Test
    void findSourceNodeByEdge() {
        IDigraph<DummyEdge, DummyNode> digraph = digraphBuilder.build();
        assertThat(digraph.findSourceNodeByEdge(edge1).orElseThrow(IllegalStateException::new)).isEqualTo(node1);
        assertThat(digraph.findSourceNodeByEdge(edge2).orElseThrow(IllegalStateException::new)).isEqualTo(node2);
        assertThat(digraph.findSourceNodeByEdge(edge3).orElseThrow(IllegalStateException::new)).isEqualTo(node3);
        assertThat(digraph.findSourceNodeByEdge(edge4).orElseThrow(IllegalStateException::new)).isEqualTo(node3);
    }

    @Test
    void findTargetNodeByEdge() {
        IDigraph<DummyEdge, DummyNode> digraph = digraphBuilder.build();
        assertThat(digraph.findTargetNodeByEdge(edge1).orElseThrow(IllegalStateException::new)).isEqualTo(node2);
        assertThat(digraph.findTargetNodeByEdge(edge2).orElseThrow(IllegalStateException::new)).isEqualTo(node3);
        assertThat(digraph.findTargetNodeByEdge(edge3).orElseThrow(IllegalStateException::new)).isEqualTo(node2);
        assertThat(digraph.findTargetNodeByEdge(edge4).orElseThrow(IllegalStateException::new)).isEqualTo(node4);
    }

    @Test
    void findEdgeById() {
        IDigraph<DummyEdge, DummyNode> digraph = digraphBuilder.build();
        assertThat(digraph.findEdgeById(12).isPresent()).isTrue();
        assertThat(digraph.findEdgeById(23).isPresent()).isTrue();
        assertThat(digraph.findEdgeById(32).isPresent()).isTrue();
        assertThat(digraph.findEdgeById(34).isPresent()).isTrue();
        assertThat(digraph.findEdgeById(1234).isPresent()).isFalse();
    }

    @Test
    void findIncomingEdgesByNode() {
        IDigraph<DummyEdge, DummyNode> digraph = digraphBuilder.build();
        assertThat(digraph.findIncomingEdgesByNode(node1)).isEmpty();
        assertThat(digraph.findIncomingEdgesByNode(node2)).containsExactlyInAnyOrder(edge1, edge3);
        assertThat(digraph.findIncomingEdgesByNode(node3)).containsExactlyInAnyOrder(edge2);
        assertThat(digraph.findIncomingEdgesByNode(node4)).containsExactlyInAnyOrder(edge4);
    }

    @Test
    void findOutgoingEdgesByNode() {
        IDigraph<DummyEdge, DummyNode> digraph = digraphBuilder.build();
        assertThat(digraph.findOutgoingEdgesByNode(node1)).containsExactlyInAnyOrder(edge1);
        assertThat(digraph.findOutgoingEdgesByNode(node2)).containsExactlyInAnyOrder(edge2);
        assertThat(digraph.findOutgoingEdgesByNode(node3)).containsExactlyInAnyOrder(edge3, edge4);
        assertThat(digraph.findOutgoingEdgesByNode(node4)).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenMissingSourceNode() {
        // test a bad formed graph -> try to add an edge with non existing source node
        assertThrows(IllegalStateException.class, () -> {
            digraphBuilder.edgeWithNodeIds(new DummyEdge(5, "dummy edge"), 99, node1);
        });
    }

    @Test
    void shouldThrowExceptionWhenMissingTargetNode() {
        // test a bad formed graph -> try to add an edge with non existing target node
        assertThrows(IllegalStateException.class, () -> {
            digraphBuilder.edgeWithNodeIds(new DummyEdge(5, "dummy edge"), node1, 99);
        });
    }
}
