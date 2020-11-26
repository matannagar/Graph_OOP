package ex2.tests;

import ex2.src.api.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {
    private static directed_weighted_graph g1;

    @BeforeEach
    void generateGraph() {
        g1 = new DWGraph_DS();
        for (int i = 1; i < 5; i++){
            node_data n= new NodeData(i);
            g1.addNode(n);
        }
        g1.connect(1, 2, 0.1);
        g1.connect(2,1,3.7);
        g1.connect(3, 4, 1.2);
        g1.connect(4, 1, 2.3);
        g1.connect(3, 1, 3.4);
    }

    @Test
    void getNode() {
        assertEquals(null, g1.getNode(10), "this node that not exist in this graph");
        assertEquals(new NodeData(3), g1.getNode(3));
    }

    @Test
    void getEdge() {
        assertEquals(0.1, g1.getEdge(1, 2).getWeight());
        assertEquals(null, g1.getEdge(1, 0), "there is no edge between them");
        assertEquals(null, g1.getEdge(5, 7),"this nodes don't exist in the graph");
        assertEquals(null, g1.getEdge(2, 3),"there is no edge between them");
        assertEquals(3.7, g1.getEdge(2, 1).getWeight());
        assertEquals(null,g1.getEdge(1, 4), "there is no edge between them");

        g1.connect(1, 2, 3.4);
        assertEquals(3.4, g1.getEdge(1, 2).getWeight());
        assertEquals(3.7, g1.getEdge(2, 1).getWeight());
        assertEquals(null, g1.getEdge(0, 0));

        g1.addNode( new NodeData(10));
        g1.addNode( new NodeData(11));
        g1.addNode( new NodeData(12));

        g1.connect(10, 11, 1.2);
        g1.connect(11, 12, 1.2);
        assertEquals(g1.getEdge(10, 11).getWeight(), g1.getEdge(11, 12).getWeight());
    }

    @Test
    void addNode() {
        int a = g1.getMC();
        Collection c = g1.getE(4);
        ( (NodeData)(g1.getNode(4))).setInfo("test1");
        String i= ((NodeData)(g1.getNode(4))).getInfo();
        g1.addNode(new NodeData(4));
        assertEquals(c, g1.getE(4));
        assertNotEquals("",((NodeData)(g1.getNode(4))).getInfo());
        assertEquals(a, g1.getMC());
        int s = g1.nodeSize();
        g1.addNode(new NodeData(5));
        assertEquals(true, g1.getV().contains(g1.getNode(5)));
        assertEquals(s + 1, g1.nodeSize());
    }

    @Test
    //need add more tests
    void connect() {
        int a = g1.getMC();
        int s = g1.edgeSize();
        g1.connect(1, 2, 3.2);
        assertEquals(s, g1.edgeSize());
        assertEquals(a + 1, g1.getMC());
        assertEquals(3.2, g1.getEdge(1, 2).getWeight());
        a = g1.getMC();
        g1.connect(1, 4, 2.3);
        assertEquals(a+1, g1.getMC());
        assertEquals(s+1, g1.edgeSize());
        g1.connect(10, 2, 0);
        assertEquals(s+1, g1.edgeSize());
        try {
            g1.connect(0, 2, -3);
            //check that we don't go in and throw Exception.
            assertEquals(true, -1 > 0);
        } catch (Exception e) {
            assertEquals(1, 1);
        }
    }

    @Test
    void getV() {

    }

    @Test
    void getE() {
    }

    @Test
    void removeNode() {
      g1.removeNode(0);
      assertEquals(4,g1.nodeSize());
      int mc= g1.getMC();
      assertEquals(1,g1.removeNode(1).getKey());
      assertEquals(mc+5,g1.getMC());
      mc=g1.getMC();
      assertEquals(null,g1.removeNode(1));
      assertEquals(mc,g1.getMC());

    }

    @Test
    void removeEdge() {
        assertEquals(0.1,g1.removeEdge(1,2).getWeight());
        assertEquals(null,g1.removeEdge(1,2));
        assertNotEquals(null,g1.removeEdge(2,1));
        assertEquals(null,g1.removeEdge(4,3));
        assertEquals(1.2,g1.removeEdge(3,4).getWeight());
        assertEquals(null,g1.removeEdge(1,1));
        assertEquals(null,g1.removeEdge(0,10));
    }

    @Test
    void nodeSize() {
    }

    @Test
    void edgeSize() {
    }

    @Test
    void getMC() {
    }

    @Test
    void runTime(){
        long start = new Date().getTime();

        directed_weighted_graph gr = new DWGraph_DS();
        double w = 0.1;

        for (int i = 0; i < 1000000; i++) {
            gr.addNode(new NodeData(i));
        }
        for (int i = 0, j = 1; i < 1000000; i++, w++) {
            gr.connect(i, j, w);
            gr.connect(0, j, w);
            gr.connect(1, i, w);
            gr.connect(2, j, w);
            gr.connect(3, i, w);
            gr.connect(4, j, w);
            gr.connect(5, i, w);
            gr.connect(6, j, w);
            gr.connect(7, i, w);
            gr.connect(8, j, w);
            gr.connect(9, i, w);
            gr.connect(10, j, w);
            j += 2;
        }
        long end = new Date().getTime();
        double dt = (end - start) / 1000.0;
        boolean t = dt < 15;
        assertEquals(true, t);
        System.out.println("runTime: " + dt);
        System.out.println("\n edges: " + gr.edgeSize());
        System.out.println("\n nodes: " + gr.nodeSize());
        gr.removeEdge(1, 0);
        long end2 = new Date().getTime();
        dt = (end2 - end) / 1000.0;
        System.out.println("runTime remove 1 Edge: " + dt);
        gr.removeNode(11);
        long end3 = new Date().getTime();
        dt = (end3 - end2) / 1000.0;
        System.out.println("runTime remove 1 Node: " + dt);
    }

}