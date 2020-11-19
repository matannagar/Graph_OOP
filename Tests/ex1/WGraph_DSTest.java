package ex1;

import org.junit.jupiter.api.BeforeEach;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class WGraph_DSTest {

    private static weighted_graph g;

    @BeforeEach
    void generateGraph() {
        g = new WGraph_DS();
        for (int i = 0; i < 5; i++)
            g.addNode(i);
        g.connect(1, 2, 0.1);
        g.connect(3, 4, 1.2);
        g.connect(4, 1, 2.3);
        g.connect(3, 1, 3.4);
    }

    @Test
    void getNode() {
        assertEquals(g.getNode(10), null);
        assertEquals(g.getNode(3), g.getNode(3));
    }

    @Test
    void hasEdge() {
        assertEquals(false, g.hasEdge(1, 20));
        assertEquals(true, g.hasEdge(2, 1));
        assertEquals(true, g.hasEdge(3, 1));
        assertEquals(false, g.hasEdge(10, 100));
        assertEquals(false, g.hasEdge(0, 2));
        g.removeEdge(1, 2);
        assertEquals(false, g.hasEdge(2, 1));
    }

    @Test
    void getEdge() {
        assertEquals(0.1, g.getEdge(1, 2));
        assertEquals(-1, g.getEdge(1, 0));
        assertEquals(-1, g.getEdge(5, 7));
        assertEquals(-1, g.getEdge(2, 3));
        g.connect(1, 2, 3.4);
        assertEquals(3.4, g.getEdge(1, 2));
        assertEquals(3.4, g.getEdge(2, 1));
        assertEquals(-1, g.getEdge(0, 0));
        assertEquals(-1, g.getEdge(2, 2));
        g.addNode(10);
        g.addNode(11);
        g.addNode(12);
        g.connect(10, 11, 1.2);
        g.connect(11, 12, 1.2);
        assertEquals(g.getEdge(10, 11), g.getEdge(11, 12));
    }

    @Test
    void addNode() {
        int a = g.getMC();
        Collection c = g.getV(4);
        g.addNode(4);
        assertEquals(c, g.getV(4));
        assertEquals(a, g.getMC());
        int s = g.nodeSize();
        g.addNode(5);
        assertEquals(true, g.getV().contains(g.getNode(0)));
        assertEquals(s + 1, g.nodeSize());
    }

    @Test
    void connect() {
        int a = g.getMC();
        int s = g.edgeSize();
        g.connect(1, 2, 3.2);
        assertEquals(s, g.edgeSize());
        assertEquals(a + 1, g.getMC());
        assertEquals(g.getEdge(1, 2), 3.2);
        a = g.getMC();
        g.connect(1, 4, 2.3);
        assertEquals(a, g.getMC());
        g.connect(10, 2, 0);
        assertEquals(s, g.edgeSize());
        try {
            g.connect(0, 2, -3);
            //check that we don't go in and throw Exception.
            assertEquals(true, -1 > 0);
        } catch (Exception e) {
            assertEquals(1, 1);
        }
    }

    @Test
    void getV() {
        weighted_graph g10 = new WGraph_DS();
        assertEquals(true, g10.getV().isEmpty());
        assertEquals("[]", g10.getV().toString());
        assertEquals("[0, 1, 2, 3, 4]", g.getV().toString());
        g.addNode(0);
        assertEquals("[0, 1, 2, 3, 4]", g.getV().toString());
        g.addNode(10);
        assertEquals("[0, 1, 2, 3, 4, 10]", g.getV().toString());
        g.addNode(5);
        assertEquals("[0, 1, 2, 3, 4, 5, 10]", g.getV().toString());
        g.removeNode(1);
        assertEquals("[0, 2, 3, 4, 5, 10]", g.getV().toString());
    }

    @Test
    void GetVKey() {
        assertEquals(g.getV(10), null);
        assertEquals(g.getV(3).toString(), "[1, 4]");
        g.connect(3, 0, 2);
        assertEquals(g.getV(3).toString(), "[0, 1, 4]");
    }

    @Test
    void removeNode() {
        int m = g.getMC();
        g.removeNode(4);
        assertEquals(m + 3, g.getMC());
        assertEquals(null, g.getV(4));
        m = g.getMC();
        g.addNode(4);
        assertEquals(m + 1, g.getMC());
        assertEquals(g.getV(4).isEmpty(), g.getV(4).isEmpty());
        node_info n = g.getNode(0);
        m = g.getMC();
        assertEquals(n, g.removeNode(0));
        assertEquals(m + 1, g.getMC());
        for (int i = 1; i <= 4; i++) {
            g.removeNode(i);
        }
        assertEquals(0, g.nodeSize());
        assertEquals(null,g.removeNode(1));
    }

    @Test
    void removeEdge() {
        int m = g.getMC();
        int s = g.edgeSize();
        g.removeEdge(0, 0);
        assertEquals(s, g.edgeSize());
        assertEquals(m, g.getMC());
        g.removeEdge(2, 3);
        assertEquals(false, g.hasEdge(3, 2));
        assertEquals(false, g.hasEdge(2, 3));
        g.addNode(6);
        g.addNode(7);
        s = g.edgeSize();
        g.connect(6, 7, 8.6);
        g.removeEdge(7, 6);
        assertEquals(s, g.edgeSize());
        g.removeEdge(3, 2);
        assertEquals(s, g.edgeSize());
        g.removeEdge(1, 2);
        assertEquals(s - 1, g.edgeSize());
    }

    @Test
    void nodeSize() {
        weighted_graph g1 = new WGraph_DS();
        assertEquals(0, g1.nodeSize());
        g1.addNode(1);
        assertEquals(1, g1.nodeSize());
        g1.removeNode(1);
        assertEquals(0, g1.nodeSize());
        g1.addNode(2);
        g1.removeNode(3);
        assertEquals(1, g1.nodeSize());
        assertEquals(5, g.nodeSize());
        int s = g.nodeSize();
        g.removeNode(3);
        assertEquals(s - 1, g.nodeSize());
    }

    @Test
    void edgeSize() {
        weighted_graph g1 = new WGraph_DS();
        assertEquals(0, g1.edgeSize());
        g1.addNode(2);
        assertEquals(0, g1.edgeSize());
        g1.connect(2, 0, 3);
        assertEquals(0, g1.edgeSize());
        g1.connect(2, 2, 0);
        assertEquals(0, g1.edgeSize());
        g1.addNode(1);
        g1.connect(1, 2, 3);
        assertEquals(1, g1.edgeSize());
        g1.connect(2, 1, 4);
        assertEquals(1, g1.edgeSize());
        assertEquals(4, g.edgeSize());
        int s = g.edgeSize();
        g.removeNode(3);
        assertEquals(s - 2, g.edgeSize());
    }

    @Test
    void getMC() {
        weighted_graph g1 = new WGraph_DS();
        assertEquals(0, g1.getMC());
        assertEquals(9, g.getMC());
        int m = g.getMC();
        g.removeNode(3);
        assertEquals(m + 3, g.getMC());
        m = g.getMC();
        g.connect(1, 2, 0.1);
        assertEquals(m, g.getMC());
        g.connect(1, 2, 3);
        assertEquals(m + 1, g.getMC());
        m = g.getMC();
        g.removeNode(10);
        assertEquals(m, g.getMC());
        g.removeEdge(4, 5);
        assertEquals(m, g.getMC());
    }
}