package ex1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


import java.util.Date;

public class RunTimeTest {
    
    @Test
    void runTime1000000() {
        long start = new Date().getTime();

        weighted_graph gr = new WGraph_DS();
        double w = 0.1;

        for (int i = 0; i < 1000000; i++) {
            gr.addNode(i);
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

    @Test
    void runTime3000() {
        long start = new Date().getTime();

        weighted_graph gr = new WGraph_DS();
        double w = 0.1;

        for (int i = 0; i < 3000; i++) {
            gr.addNode(i);
        }
        for (int i = 0, j = 1; i < 3000; i++, w++) {
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
        System.out.println("runTime building the graph: " + dt);
        System.out.println("\tedges: " + gr.edgeSize());
        System.out.println("\tnodes: " + gr.nodeSize());
        gr.removeEdge(1, 0);
        long end2 = new Date().getTime();
        dt = (end2 - end) / 1000.0;
        System.out.println("\nrunTime remove 1 Edge: " + dt);

        int s = gr.edgeSize();
        int s1 = gr.getV(1).size();

        gr.removeNode(1);
        long end3 = new Date().getTime();
        dt = (end3 - end2) / 1000.0;
        System.out.println("runTime remove 1 Node=> remove 2998 edges: " + dt);
        assertEquals(s - s1, gr.edgeSize());
    }

    @Test
    void shortestPathDist() {
        weighted_graph g = new WGraph_DS();
        for (int i = 0; i < 9; i++)
            g.addNode(i);
        g.connect(0, 1, 4);
        g.connect(0, 4, 8);
        g.connect(1, 2, 8);
        g.connect(1, 4, 11);
        g.connect(2, 8, 2);
        g.connect(2, 3, 7);
        g.connect(2, 6, 4);
        g.connect(3, 6, 14);
        g.connect(3, 7, 9);
        g.connect(4, 8, 7);
        g.connect(4, 5, 1);
        g.connect(5, 8, 6);
        g.connect(5, 6, 2);
        g.connect(6, 7, 10);
        weighted_graph_algorithms a=new WGraph_Algo();
        a.init(g);
        long start= new Date().getTime();
        assertEquals(4,a.shortestPathDist(0,1));
        assertEquals(12,a.shortestPathDist(0,2));
        assertEquals(19,a.shortestPathDist(0,3));
        assertEquals(8,a.shortestPathDist(0,4));
        assertEquals(9,a.shortestPathDist(0,5));
        assertEquals(11,a.shortestPathDist(0,6));
        assertEquals(21,a.shortestPathDist(0,7));
        assertEquals(14,a.shortestPathDist(0,8));
        long end = new Date().getTime();
        double dt = (end - start) / 1000.0;
        boolean t= dt<0.5;
        assertTrue(t);
        System.out.println("runTime shortestPath " + dt);
    }
}
