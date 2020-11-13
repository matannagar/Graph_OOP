package ex1;

import java.io.File;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {

        WGraph_DS g = new WGraph_DS();
        g.addNode(1);
        g.addNode(2);
        g.addNode(4);
        g.addNode(3);
        g.connect(1, 2, 0.3);
        g.connect(3, 4, 1.2);
        System.out.println(g);
        System.out.println(g.hasEdge(1, 2) + " " + g.getEdge(1, 2));
        System.out.println(g.hasEdge(1, 3) + " " + g.getEdge(1, 3));
        g.removeEdge(1, 3);
        g.removeEdge(1, 2);
        System.out.println(g);
        g.connect(1, 2, 0.3);
        System.out.println(g);
        g.removeNode(3);
        System.out.println(g);
        g.connect(1, 2, 0.5);
        g.addNode(1);
        System.out.println(g);
        g.addNode(5);
        g.addNode(6);
        g.connect(5, 6, 7);
        g.connect(5, 1, 4.2);
        g.connect(5, 1, 0.3);
        g.connect(1, 6, 4.2);
        System.out.println(g);
        g.removeNode(1);
        System.out.println(g);
        System.out.println(g.hasEdge(1, 2));
        System.out.println(g.getV(2));
        WGraph_Algo g0= new WGraph_Algo();
        g0.init(g);
        System.out.println(g0.getGraph());
        System.out.println(g0.isConnected());
        g.connect(2,4,3.4);
        g.connect(2,5,6.7);
        System.out.println(g0.isConnected());
        System.out.println(g.getNode(10));
        g.addNode(7);
        g.addNode(3);
        g.connect(3,7,1.2);
        g.connect(2,7,1.6);
        System.out.println(g);
        System.out.println(g0.shortestPathDist(2,6));

        WGraph_DS g1 = new WGraph_DS();
        g1.addNode(1);
        g1.addNode(2);
        g1.addNode(4);
        g1.addNode(3);
        g1.addNode(5);
        g1.connect(1,2,5.1);
        g1.connect(1,3,7.1);
        g1.connect(3,5,10.1);
        g1.connect(2,5,3.1);
        g1.connect(2,4,12.1);
        g1.connect(4,5,2.1);
        System.out.println(g1);
        g0.init(g1);
        System.out.println("1,2: "+g0.shortestPathDist(1,2));
        System.out.println("1,3: "+g0.shortestPathDist(1,3));
        System.out.println("1,4: "+g0.shortestPathDist(1,4));
        System.out.println("1,5: "+g0.shortestPathDist(1,5));
        System.out.println("1,2: "+g0.shortestPath(1,2));
        System.out.println("1,3: "+g0.shortestPath(1,3));
        System.out.println("1,4: "+g0.shortestPath(1,4));
        System.out.println("1,5: "+g0.shortestPath(1,5));
        System.out.println("1,8: "+g0.shortestPathDist(1,8));
        System.out.println("1,8: "+g0.shortestPath(1,8));


        WGraph_DS g2 = new WGraph_DS();
        g0.init(g2);
        g2.addNode(1);
        g2.addNode(2);
        g2.addNode(4);
        g2.addNode(3);
        g2.addNode(5);
        g2.addNode(6);
        g2.connect(1,3,14.1);
        g2.connect(1,4,9.1);
        g2.connect(1,6,7.1);
        g2.connect(2,3,9.1);
        g2.connect(2,5,6.1);
        g2.connect(4,3,2.1);
        g2.connect(4,5,11.1);
        g2.connect(4,6,10.1);
        g2.connect(5,6,15.1);
        System.out.println(g2);
        System.out.println("1,2: "+g0.shortestPathDist(1,2));
        System.out.println("1,3: "+g0.shortestPathDist(1,3));
        System.out.println("1,4: "+g0.shortestPathDist(1,4));
        System.out.println("1,5: "+g0.shortestPathDist(1,5));
        System.out.println("1,6: "+g0.shortestPathDist(1,6));
        System.out.println("1,1: "+g0.shortestPath(1,1));
        System.out.println("1,6: "+g0.shortestPath(1,6));
        System.out.println("1,2: "+g0.shortestPath(1,2));

        //copy
        System.out.println(g0.getGraph());
        weighted_graph g3= g0.copy();
        System.out.println(g3);
        g0.getGraph().removeEdge(5,6);
        System.out.println(g0.getGraph());
        System.out.println(g3);
        //


        //load
        System.out.println(g0.save("MyGraph.txt"));
       // System.out.println(new File("MyGraph.txt").getAbsolutePath());

        try{
            Scanner x = new Scanner(new File("MyGraph.txt"));

            while (x.hasNext()){
                String a= x.next();
                System.out.println(a);
            }
        }
        catch (Exception e){
            System.out.println("Couldn't find the file");
        }
        //

        //empty Graph
        weighted_graph g10= new WGraph_DS();
        System.out.println(g10);
        //

        //edges weight= 0
        g10.addNode(0);
        g10.addNode(1);
        g10.addNode(2);
        g10.addNode(3);
        g10.connect(0,1,0);
        g10.connect(1,2,0);
        g0.init(g10);
        System.out.println(g0.shortestPathDist(0,2));
        System.out.println(g0.shortestPath(0,2));
        //

    }

}
