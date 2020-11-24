package ex2.src.api;

import com.google.gson.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms{

    private directed_weighted_graph gr;

    @Override
    public void init(directed_weighted_graph g) {
        this.gr=g;
    }

    @Override
    public directed_weighted_graph getGraph() {
        return this.gr;
    }

    @Override
    public directed_weighted_graph copy() {
        directed_weighted_graph g= new DWGraph_DS(this.gr);
        return g;
    }

    @Override
    public boolean isConnected() {
        if (gr == null) return false;

        //if graph contain N vertexes and less than N-1 edges the graph can't be connect.
        if (this.gr.edgeSize() < (this.gr.getV().size() - 1)) return false;
        if (this.gr.getV().size() < 2) return true;

        //Choose a idNode we will check from him and do on him bfs algo

        Iterator<node_data> it = this.gr.getV().iterator();
        while(it.hasNext()){
        node_data temp = it.next();
        int count = bfs(temp.getKey());
        if ((count < gr.getV().size())) return false;
        }

        return true;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        if (gr == null) return -1;
        if (!(this.gr.getV().contains(gr.getNode(src))) || !(this.gr.getV().contains(gr.getNode(dest)))) return -1;
        if (src == dest) return 0;

        //dij return the length of the shortest path between src to dest for dest-node.
        HashMap<Integer,Node_Temp> temp = dij(src, dest);

        //If dest.visit will be 0 we will understand that not exist a path between this nodes.
        if (temp.get(dest).visit == 0) return -1;

        return temp.get(dest).tag;
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        if (gr == null) return null;
        if (!(this.gr.getV().contains(gr.getNode(src))) || !(this.gr.getV().contains(gr.getNode(dest)))) return null;

        HashMap<Integer, Node_Temp> temp = dij(src, dest);

        if (temp.get(dest).visit == 0) return null;

        //We will return this list of path.
        LinkedList<node_data> list = new LinkedList<>();

        node_data current = this.gr.getNode(dest);
        list.add(current);

        if (src == dest) return list;

        // loop: check who is the "parent node" of each node.

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
   
      //Make JSON
        //Gson gson = new Gson();
      	Gson gson= new GsonBuilder().create();
        String json = gson.toJson(gr);

      		//Write JSON to file
      		try 
      		{
      			PrintWriter pw = new PrintWriter(new File(file));
      			pw.write(json);
      			pw.close();
      		} 
      		catch (FileNotFoundException e) 
      		{
      			e.printStackTrace();
      			return false;
      		}
        return true;
    }

    @Override
    public boolean load(String file) {
    	Gson gson = new Gson();
    	//from JSON file to Object
    			try 
    			{			
    				FileReader reader = new FileReader(file);

    				directed_weighted_graph g = gson.fromJson(reader,DWGraph_DS.class);
    				this.gr=g;
    			} 
    			catch (FileNotFoundException e) {
    				e.printStackTrace();
                    return false;

                }
    		return true;
    }
    private int bfs(int key) {

        //reset info&tag
        for (node_data n : gr.getV()) {
            n.setInfo("");
            n.setTag(0);
        }

        // init counter
        // When we pass over a node we will do count++ and then we could check if we pass all the nodes.
        int count = 0;

        // Init node that we will start with him.
        node_data start = this.gr.getNode(key);

        Queue<node_data> q = new LinkedList<>();

        // Work on start's neighbors.
        // We marked "red" a node that we work on him now.

        for (edge_data e : this.gr.getE(key)) {
            gr.getNode(e.getDest()).setInfo("red");
            q.add( gr.getNode(e.getDest()));
        }

        // We marked "blue" a node that we finish work with him.
        start.setInfo("blue");
        count++;

        while (!q.isEmpty()) {
            node_data current = q.poll();
            for (edge_data e : this.gr.getE(current.getKey())) {
                if ((gr.getNode(e.getDest()).getInfo().equals(""))) {
                    q.add(gr.getNode(e.getDest()));
                    gr.getNode(e.getDest()).setInfo("red");
                }
            }

            current.setInfo("blue");
            count++;
        }
        return count;
    }
    /**
     * This algorithm based on Dijkstra Algorithm.
     *
     * The data of this algorithm will save in a temporal Node-
     *      1. tag- The shortest weight from the source vertex to this vertex.
     *      2. parent- The vertex neighbor that connect this vertex and update him this value of weight accepted.
     *      (By default the vertex tag contains Max_Value_Int and the vertex parent points as a vertex with key -1).
     *
     * The function goes through all the neighbors of the source vertex and does them "weight relief" if necessary-
     * that is, if we have reached a vertex whose weight can be reduced relative to its current weight,
     * we will update the information contained the temporal vertex.
     *
     *
     * The implementation of the algorithm is done by a priority queue,
     * which is implementation by a function of comparing the weights (tag) of the temporal vertexes in the queue.
     *
     * @return a HashMap with all the temporal vertexes respectively-
     * so that we can retrieve the information about each vertex in the most efficient and convenient way.
     */
    private HashMap<Integer, Node_Temp> dij(int src, int dest) {

        HashMap<Integer, Node_Temp> temp = new HashMap<>();

        // init NodeTemp
        for (node_data n : gr.getV()) {
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
                for (edge_data n : this.gr.getE(keyC)) {
                    Node_Temp b = temp.get(n.getDest());
                    if (b.visit != 1) {

                        // Check if we need to update the shortest length.
                        if (b.tag > current.tag + n.getWeight()) {
                            b.tag = current.tag + n.getWeight();
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
    /**
     * Node_Temp class
     *
     * this class will help implement Dijkstra Algorithm.
     * */
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
