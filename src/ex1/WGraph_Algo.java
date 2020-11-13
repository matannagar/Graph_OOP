package ex1;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms {
    private weighted_graph gr;

    /**
     * Default constructor
     */
    public WGraph_Algo() {
        this.gr = new WGraph_DS();
    }

    @Override
    public void init(weighted_graph g) {
        this.gr = g;
    }

    @Override
    public weighted_graph getGraph() {
        return gr;
    }

    @Override
    public weighted_graph copy() {
        weighted_graph g = new WGraph_DS(this.gr);
        return g;
    }

    @Override
    public boolean isConnected() {
        //if graph contain N vertexes and less than N-1 edges the graph can't be connect.
        if (this.gr.edgeSize() < (this.gr.getV().size() - 1)) return false;
        if (this.gr.getV().size() < 2) return true;

        //Choose a idNode we will check from him and do on him bfs algo
        Iterator<node_info> it = this.gr.getV().iterator();
        node_info temp = it.next();

        int count = bfsInt(temp.getKey());
        return (count == gr.getV().size());
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        if (!(this.gr.getV().contains(gr.getNode(src))) || !(this.gr.getV().contains(gr.getNode(dest)))) return -1;
        if (src == dest) return 0;

        //dij return the length of the shortest path between src to dest for dest-node.
        HashMap<Integer, Node_Temp> temp = dij(src, dest);

        //If dest.visit will be 0 we will understand that not exist a path between this nodes.
        if (temp.get(dest).visit == 0) return -1;

        return temp.get(dest).tag;
    }

    @Override
    public List<node_info> shortestPath(int src, int dest) {
        if (!(this.gr.getV().contains(gr.getNode(src))) || !(this.gr.getV().contains(gr.getNode(dest)))) return null;

        HashMap<Integer, Node_Temp> temp = dij(src, dest);

        if (temp.get(dest).visit == 0) return null;

        //We will return this list of path.
        LinkedList<node_info> list = new LinkedList<>();

        node_info current = this.gr.getNode(dest);
        list.add(current);

        if (src == dest) return list;

        // loop: check who is the "parent" node of each node.

        Node_Temp nt, parent;

        while (temp.get(current.getKey()).parentId != -1) {
            nt = temp.get(current.getKey());
            parent = temp.get(nt.parentId);
            current = gr.getNode(parent.idNode);
            list.addFirst(current);
        }
        return list;
    }

    @Override
    public boolean save(String file) {
        try {
            File f = new File(file);
            if (!f.exists()) {
                f.createNewFile();
            }
            PrintWriter pw = new PrintWriter(f);
            pw.print(gr);
            pw.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*else {
            throw new RuntimeException("save should not rewrite on existing file. Do choose a new log file name");
        }*/
        return false;
}

    @Override
    public boolean load(String file) {
        try{
            Scanner x = new Scanner(new File (file));
            while (x.hasNext()){
                String a= x.next();
                String b= x.next();
            }
        }
        catch (Exception e){
            System.out.println("Couldn't find the file");
        }




        return false;
    }

    private int bfsInt(int key) {

        //reset info&tag
        for (node_info n : gr.getV()) {
            n.setInfo("");
            n.setTag(0);
        }
        // init counter
        // When we pass over a node we will do count++ and then we could check if we pass all the nodes.
        int count = 0;

        // Init node that we will start with him.
        node_info start = this.gr.getNode(key);

        Queue<node_info> q = new LinkedList<>();

        // Work on start's neighbors.
        // We marked "red" a node that we work on him now.

        for (node_info n : this.gr.getV(key)) {
            n.setInfo("red");
            q.add(n);
        }

        // We marked "blue" a node that we finish work with him.
        start.setInfo("blue");
        count++;

        while (!q.isEmpty()) {
            node_info current = q.poll();
            for (node_info n : this.gr.getV(current.getKey())) {
                if ((n.getInfo().equals(""))) {
                    q.add(n);
                    n.setInfo("red");
                }
            }

            current.setInfo("blue");
            count++;
        }
        return count;
    }

    private HashMap<Integer, Node_Temp> dij(int src, int dest) {

        HashMap<Integer, Node_Temp> temp = new HashMap<>();

        // init NodeTemp
        for (node_info n : gr.getV()) {
            Node_Temp a = new Node_Temp(n.getKey());
            temp.put(a.idNode, a);
        }

        // We define Node_Temp.tag to be the length of the shortest path
        // from the src vertex to the current vertex as of this moment.

        // the shortest length of src-->src is 0.
        temp.get(src).tag = 0;

        PriorityQueue<Node_Temp> q = new PriorityQueue(gr.nodeSize(), new NodeComparator());

        q.add(temp.get(src));

        while (!q.isEmpty()) {
            Node_Temp current = q.poll();
            int keyC = current.idNode;

            // We define Node_Temp.visit to check if we done with this Node and his current length is final.
            if (current.visit != 1) {

                // We pass over the neighbors of the current Node.
                for (node_info n : this.gr.getV(keyC)) {
                    Node_Temp b = temp.get(n.getKey());
                    if (b.visit != 1) {

                        // Check if we need to update the shortest length.
                        if (b.tag > current.tag + gr.getEdge(n.getKey(), keyC)) {
                            b.tag = current.tag + gr.getEdge(n.getKey(), keyC);
                            b.parentId = keyC;
                        }
                        q.add(b);
                    }
                }
                current.visit = 1;
                if (current.idNode == dest) return temp;
            }
        }
        return temp;
    }

private class Node_Temp {
    private int idNode;
    private int visit;
    private double tag;
    private int parentId;

    public Node_Temp(int key) {
        this.idNode = key;
        this.visit = 0;
        this.tag = Integer.MAX_VALUE;
        this.parentId = -1;
    }
}

private class NodeComparator implements Comparator<Node_Temp> {
    public int compare(Node_Temp n1, Node_Temp n2) {
        if (n1.tag < n2.tag)
            return -1;
        else if (n1.tag > n2.tag)
            return 1;
        return 0;
    }
}
}
