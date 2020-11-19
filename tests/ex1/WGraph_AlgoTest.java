package ex1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_AlgoTest {

    private static weighted_graph_algorithms g;
    private static weighted_graph g1, g2, g3, g4, g5;

    @BeforeEach
    void generateGraph() {
        g = new WGraph_Algo();
        //new graph g1
        g1 = new WGraph_DS();
        for (int i = 0; i < 5; i++)
            g1.addNode(i);
        g1.connect(1, 2, 0.1);
        g1.connect(3, 4, 1.2);
        g1.connect(4, 1, 2.3);
        g1.connect(3, 1, 3.4);

        // new graph g2
        g2 = new WGraph_DS();
        for (int i = 0; i < 5; i++)
            g2.addNode(i);
        g2.connect(1, 0, 3.4);
        g2.connect(1, 2, 0.1);
        g2.connect(1, 3, 1.2);
        g2.connect(1, 4, 2.3);

        //new graph g3
        g3 = new WGraph_DS();
        for (int i = 1; i < 7; i++)
            g3.addNode(i);
        g3.connect(1, 3, 14.1);
        g3.connect(1, 4, 9.1);
        g3.connect(1, 6, 7.1);
        g3.connect(2, 3, 9.1);
        g3.connect(2, 5, 6.1);
        g3.connect(4, 3, 2.1);
        g3.connect(4, 5, 11.1);
        g3.connect(4, 6, 10.1);
        g3.connect(5, 6, 15.1);

        //new graph g4
        g4 = new WGraph_DS();
        for (int i = 1; i < 6; i++)
            g4.addNode(i);
        g4.connect(1, 2, 5.1);
        g4.connect(1, 3, 7.1);
        g4.connect(3, 5, 10.1);
        g4.connect(2, 5, 3.1);
        g4.connect(2, 4, 12.1);
        g4.connect(4, 5, 2.1);

        //new graph g5
        g5 = new WGraph_DS();
        for (int i = 0; i < 10; i++)
            g5.addNode(i);
        double w = 0.1;
        for (int i = 1; i < 10; i++, w++)
            g5.connect(0, i, w);
    }

    @Test
    void init() {
        g.init(g2);
        assertSame(g2, g.getGraph());
        g.init(g3);
        assertSame(g3, g.getGraph());
    }

    @Test
    void getGraph() {
        g.init(g2);
        assertEquals(g2, g.getGraph());
        g2.addNode(8);
        assertEquals(g2, g.getGraph());
    }

    @Test
    void copy() {
        g.init(g1);
        weighted_graph g0 = g.copy();
        assertEquals(true, g.getGraph().equals(g0));
        g.getGraph().removeEdge(1, 2);
        assertEquals(false, g.getGraph().equals(g0));
        g.init(new WGraph_DS());
        assertEquals(new WGraph_DS(),g.copy());
    }

    @Test
    void isConnected() {

        g.init(g2);

        assertTrue(g.isConnected());
        g2.removeNode(1);
        assertFalse(g.isConnected());
        g2.addNode(1);
        g2.connect(1, 0, 3.4);
        g2.connect(1, 2, 0.1);
        g2.connect(1, 3, 1.2);
        g2.connect(1, 4, 2.3);
        g2.addNode(5);
        g2.addNode(6);
        g2.connect(5, 6, 7.3);
        assertFalse(g.isConnected());
        g2.connect(2, 5, 3.2);
        assertTrue(g.isConnected());
        g2.removeEdge(6, 5);
        assertFalse(g.isConnected());

        //empty Graph && one node in graph
        g = new WGraph_Algo();
        weighted_graph g1 = new WGraph_DS();
        g.init(g1);
        assertTrue(g.isConnected());
        g1.addNode(0);
        assertTrue(g.isConnected());

        //graph g5
        g.init(g5);
        assertTrue(g.isConnected());
        g5.removeNode(0);
        assertFalse(g.isConnected());
    }

    @Test
    void shortestPathDist() {

        g.init(g3);

        assertEquals(20.3, g.shortestPathDist(1, 2),0.01);
        assertEquals(11.2, g.shortestPathDist(1, 3));
        assertEquals(9.1, g.shortestPathDist(1, 4));
        assertEquals(20.2, g.shortestPathDist(1, 5));
        assertEquals(7.1, g.shortestPathDist(1, 6));
        assertEquals(0, g.shortestPathDist(1, 1));
        assertEquals(-1, g.shortestPathDist(8, 8));

        //new graph g4
        g.init(g4);
        assertEquals(5.1, g.shortestPathDist(1, 2));
        assertEquals(7.1, g.shortestPathDist(1, 3));
        assertEquals(10.3, g.shortestPathDist(1, 4),0.01);
        assertEquals(8.2, g.shortestPathDist(1, 5));
        assertEquals(-1, g.shortestPathDist(1, 8));

        //new graph g5
        g.init(g5);
        assertEquals(8.1,g.shortestPathDist(0,9));
        g5.connect(2,9,0.4);
        assertEquals(1.5,g.shortestPathDist(0,9));
        g5.removeNode(0);
        assertEquals(-1,g.shortestPathDist(2,3));
    }

    @Test
    void shortestPath() {
        g.init(g3);
        List l = new LinkedList<node_info>();
        l.add(g3.getNode(1));
        List li = g.shortestPath(1, 1);
        assertEquals(l, li);
        l.add(g3.getNode(6));
        li = g.shortestPath(1, 6);
        assertEquals(l, li);
        l = new LinkedList<node_info>();
        l.add(g3.getNode(6));
        l.add(g3.getNode(1));
        li = g.shortestPath(6, 1);
        assertEquals(l, li);
        l = new LinkedList<node_info>();
        l.add(g3.getNode(1));
        l.add(g3.getNode(4));
        l.add(g3.getNode(3));
        l.add(g3.getNode(2));
        li = g.shortestPath(1, 2);
        assertEquals(l, li);


        //new graph g4
        g.init(g4);

        l = new LinkedList<node_info>();
        l.add(g4.getNode(1));
        l.add(g4.getNode(2));
        li = g.shortestPath(1, 2);
        assertEquals(l, li);
        l = new LinkedList<node_info>();
        l.add(g4.getNode(1));
        l.add(g4.getNode(3));
        li = g.shortestPath(1, 3);
        assertEquals(l, li);
        l = new LinkedList<node_info>();
        l.add(g4.getNode(1));
        l.add(g4.getNode(2));
        l.add(g4.getNode(5));
        l.add(g4.getNode(4));
        li = g.shortestPath(1, 4);
        assertEquals(l, li);
        l = new LinkedList<node_info>();
        l.add(g4.getNode(1));
        l.add(g4.getNode(2));
        l.add(g4.getNode(5));
        li = g.shortestPath(1, 5);
        assertEquals(l, li);
        li = g.shortestPath(1, 8);
        assertEquals(null, li);
    }

    @Test
    void equalSaveLoad() {
        g.init(g1);
        g.save("filename.ser");
        weighted_graph_algorithms algo = new WGraph_Algo();
        algo.load("filename.ser");
        weighted_graph loaded = algo.getGraph();
        assertEquals(g.getGraph(), loaded);
        loaded.removeNode(1);
        assertNotEquals(g.getGraph(), loaded);
        g.init(g2);
        g.save("filename.ser");
        algo.load("filename.ser");
        loaded = algo.getGraph();
        assertNotEquals(g1, loaded);
        assertEquals(g.getGraph(), loaded);
    }
}