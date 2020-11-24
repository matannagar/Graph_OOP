package ex2.tests;

import ex2.src.api.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
/*
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
        g5 = new DWGraph_DS();
        for (int i = 0; i < 10; i++)
            g5.addNode(i);
        double w = 0.1;
        for (int i = 1; i < 10; i++, w++)
            g5.connect(0, i, w);
*/
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
    }

    @Test
    void shortestPath() {
    }

    @Test
    void save() {
    }

    @Test
    void load() {
    }
}