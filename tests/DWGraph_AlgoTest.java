package ex2.tests;

import ex2.src.api.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class DWGraph_AlgoTest {

    private static dw_graph_algorithms g;
    private static directed_weighted_graph g1, g2, g3, g4, g5, g6;

    @BeforeEach
    void generateGraph() {
        g = new DWGraph_Algo();
        //new graph g1
        g1 = new DWGraph_DS();
        for (int i = 1; i < 5; i++){
            node_data n= new NodeData(i);
            g1.addNode(n);
        }

        g1.connect(1, 2, 5.6);
        g1.connect(2, 1, 6.3);
        g1.connect(1, 3, 1.2);
        g1.connect(3, 1, 0.4);
        g1.connect(3, 4, 10);
        g1.connect(4, 3, 2.4);
        g1.connect(2, 4, 2.7);
        g1.connect(4, 2, 1);

        // new graph g2
        g2 = new DWGraph_DS();
        for (int i = 0; i < 5; i++){
            node_data n= new NodeData(i);
            g2.addNode(n);
        }
        g2.connect(0, 4, 2.8);
        g2.connect(4, 3, 8.5);
        g2.connect(3, 2, 1.2);
        g2.connect(3, 1, 5);
        g2.connect(3, 0, 4.7);
        g2.connect(2, 1, 5);
        g2.connect(1, 0, 4.9);

        //new graph g3
        g3 = new DWGraph_DS();
        for (int i = 0; i < 6; i++){
            node_data n= new NodeData(i);
            g3.addNode(n);
        }
        g3.connect(0,1,5);
        g3.connect(0,2,1);
        g3.connect(1,2,2);
        g3.connect(2,1,3);
        g3.connect(1,3,3);
        g3.connect(3,2,3);
        g3.connect(2,4,12);
        g3.connect(4,5,1);
        g3.connect(3,5,6);
        g3.connect(3,4,2);
        g3.connect(1,4,20);


        //new graph g6
        g6 = new DWGraph_DS();
        for (int i = 0; i < 5; i++){
            node_data n= new NodeData(i);
            g6.addNode(n);
        }
        g6.connect(1, 2, 0.1);
        g6.connect(2,1,3.7);
        g6.connect(3, 4, 1.2);
        g6.connect(4, 1, 2.3);
        g6.connect(3, 1, 3.4);
    }

    @Test
    void init() {
    }

    @Test
    void getGraph() {
    }

    @Test
    void copy() {

    }

    @Test
    void isConnected() {
        g.init(g6);
        assertFalse(g.isConnected());
        g.init(g1);
        assertTrue(g.isConnected());
        g.init(g2);
        assertTrue(g.isConnected());
        g2.removeEdge(3,2);
        g2.connect(2,3,1.2);
        assertFalse(g.isConnected());
        g2.removeEdge(4,3);
        assertFalse(g.isConnected());
    }

    @Test
    void shortestPathDist() {
        directed_weighted_graph g10= new DWGraph_DS();
        for (int i = 0; i < 3; i++){
            node_data n= new NodeData(i);
            g10.addNode(n);
        }
        g10.connect(0,1,1);
        g10.connect(1,2,3);
        g10.connect(2,0,3);
        g10.addNode(new NodeData(5));

        g.init(g10);
        assertEquals(4,g.shortestPathDist(0,2));
        assertEquals(-1,g.shortestPathDist(5,2));

        //graph g3
        g.init(g3);
        assertEquals(10,g.shortestPathDist(0,5));
        assertEquals(1,g.shortestPathDist(0,2));
        g3.removeEdge(1,3);
        assertEquals(-1,g.shortestPathDist(0,3));

        //graph g6
        g.init(g6);
        assertEquals(-1,g.shortestPathDist(2,3));
        assertEquals(3.4,g.shortestPathDist(3,1));
    }

    @Test
    void shortestPath() {
        g.init(g3);
        List l = new LinkedList<node_data>();
        l.add(g3.getNode(0));
        l.add(g3.getNode(2));
        l.add(g3.getNode(1));
        l.add(g3.getNode(3));
        l.add(g3.getNode(4));
        l.add(g3.getNode(5));
        assertEquals(l.toString(),g.shortestPath(0,5).toString());

        g.init(g6);
        assertEquals(null,g.shortestPath(2,3));
        l = new LinkedList<node_data>();
        l.add(3);
        l.add(1);
        assertEquals(l.toString(),g.shortestPath(3,1).toString());

    }

    @Test
    void equalsSaveLoad() {
        directed_weighted_graph g10= new DWGraph_DS();
        g10.addNode(new NodeData(0));
        g10.addNode(new NodeData(1));
        g10.addNode(new NodeData(2));
        g10.connect(0,1,2.3);
        g10.connect(2,1,0.4);
        g.init(g10);
        g.save("graph.json");
        dw_graph_algorithms algo = new DWGraph_Algo();
        algo.load("graph.json");
        directed_weighted_graph loaded = algo.getGraph();
        assertEquals(g.getGraph(), loaded);
        loaded.removeNode(1);
        assertNotEquals(g.getGraph(), loaded);
        g.init(g2);
        g.save("graph.json");
        algo.load("graph.json");
        loaded = algo.getGraph();
        assertNotEquals(g1, loaded);
        assertEquals(g.getGraph(), loaded);

    }
}