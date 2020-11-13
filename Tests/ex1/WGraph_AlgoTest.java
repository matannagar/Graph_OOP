package ex1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_AlgoTest {

    private static weighted_graph_algorithms g;

    @BeforeEach
    void generateGraph() {

        g= new WGraph_Algo();
        weighted_graph g1 = new WGraph_DS();
        for (int i = 0; i < 5; i++)
            g1.addNode(i);
        g1.connect(1, 2, 0.1);
        g1.connect(3, 4, 1.2);
        g1.connect(4, 1, 2.3);
        g1.connect(3, 1, 3.4);

        g.init(g1);
    }

    @Test
    void init() {
    }

    @Test
    void getGraph() {
    }

    @Test
    void copy() {
        weighted_graph g3= g.copy();
        assertEquals(true, g.getGraph().equals(g3));
        g.getGraph().removeEdge(1,2);
        assertEquals(false, g.getGraph().equals(g3));
    }

    @Test
    void isConnected() {
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

    @Test
    void equalSaveLoad(){
        g.save("filename.ser");
        weighted_graph_algorithms algo= new WGraph_Algo();
        algo.load("filename.ser");
        weighted_graph loaded= algo.getGraph();
        assertEquals(g.getGraph(),loaded);
        loaded.removeNode(1);
        assertNotEquals(g.getGraph(),loaded);
    }
}