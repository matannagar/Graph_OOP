package ex1;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class represents an undirectional weighted graph.
 *
 * @param: nodes- A HashMap contains all the vertexes in this graph.
 * @param: edges- A HashMap contains all the edges in this graph.
 * @param: mc- How many changes we does in this graph.
 *
 * @author Reut-Maslansky
 */

public class WGraph_DS implements weighted_graph, java.io.Serializable {
    private HashMap<Integer, node_info> nodes;
    private HashMap<String, Edge> edges;
    private int mc;

    /**
     * Default constructor
     */
    public WGraph_DS() {
            mc = 0;
            this.nodes = new HashMap<>();
            this.edges = new HashMap<>();
    }

    /**
     * Copy constructor
     *
     * @param g
     */
    public WGraph_DS(weighted_graph g) {
        if(g==null) return;

        this.nodes = new HashMap<>();
        this.edges= new HashMap<>();

        for (node_info n : g.getV()) {
            node_info no = new Node_Info(n);
            addNode(no.getKey());
        }
        for (node_info n : g.getV()) {
            for (node_info ni : g.getV(n.getKey())) {
                connect(n.getKey(), ni.getKey(),g.getEdge(n.getKey(), ni.getKey()));
            }
        }

       this.mc=g.nodeSize()+g.edgeSize();
    }


    @Override
    public node_info getNode(int key) {
        return nodes.get(key);
    }

    /**
     * Return if between this vertexes has edge.
     *
     * @param node1
     * @param node2
     * @return
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        String pair = getPair(node1, node2);
        return edges.containsKey(pair);
    }

    /**
     * Return the weight between this vertexes.
     * If there is no such edge- return -1.
     *
     * @param node1
     * @param node2
     * @return
     * */
    @Override
    public double getEdge(int node1, int node2) {
       String pair=getPair(node1,node2);
        if(edges.containsKey(pair)){
            return edges.get(pair).weight;
        }
        return -1;
    }

    /**
     * Add a new node to the graph with the given key.
     * */
    @Override
    public void addNode(int key) {
        if(nodes.containsKey(key)) return;
        node_info n= new Node_Info(key);
        nodes.put(key, n);
        mc++;
    }

    /**
     * Connect an edge between node1 and node2, with an edge with weight >=0.
     * if the edge node1-node2 already exists - the method simply updates the weight of the edge.
     */
    @Override
    public void connect(int node1, int node2, double w) {
        if (!(this.nodes.containsKey(node1)) || !(this.nodes.containsKey(node2))) return;
        if (node1 == node2) return;
        if(w<0) throw new RuntimeException("The weight should be >=0. Do choose a new weight to this edge");
        String pair= getPair(node1,node2);


        // Update the weight edge between those nodes.
        if(edges.containsKey(pair)) {
            if(edges.get(pair).weight==w) return;
            edges.get(pair).setWeight(w);
            mc++;
            return;
        }

        mc++;

        Node_Info n1 = (Node_Info) getNode(node1);
        Node_Info n2 = (Node_Info) getNode(node2);
        n1.neighbor.put(node2, getNode(node2));
        n2.neighbor.put(node1, getNode(node1));
        Edge e = new Edge(node1, node2, w);
        edges.put(e.pair, e);
    }

    /**
     * This method return a pointer for the collection representing all the nodes in the graph.
     *
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_info> getV() {
        return nodes.values();
    }

    /**
     * This method returns a collection containing all the vertexes connected to node_id
     *
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        if (!(nodes.containsKey(node_id))) return null;
        Node_Info n = (Node_Info) getNode(node_id);
        return n.getNi();
    }

    /**
     * Delete the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     *
     * @param key
     * @return the data of the removed node (null if none).
     */
    @Override
    public node_info removeNode(int key) {
        if (nodes.containsKey(key)) {
            mc++;
            node_info node = getNode(key);

            Iterator<node_info> it = this.getV(key).iterator();

            //This "while" will loop through the list of neighbors
            //and delete the edge between the vertex and his neighbor.

            while (it.hasNext()) {
                node_info n = it.next();
                removeEdge(key, n.getKey());
                it = this.getV(key).iterator();
            }

            nodes.remove(key);
            return node;
        }
        return null;
    }

    /**
     * Delete the edge from the graph between the vertexes,
     *
     * @param node1
     * @param node2
     * @return the data of the removed edge (null if none).
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if (!(this.nodes.containsKey(node1)) || !(this.nodes.containsKey(node2))) return;
        if (node1 == node2) return;
        if (!hasEdge(node1, node2)) return;

        mc++;
        Node_Info n1 = (Node_Info) getNode(node1);
        Node_Info n2 = (Node_Info) getNode(node2);
        n1.removeNei(n2);
        n2.removeNei(n1);
        edges.remove(getPair(node1,node2));
    }

    /** return the number of vertices in this graph.*/
     @Override
    public int nodeSize() {
        return nodes.size();
    }

     /** return the number of edges in this graph.*/
    @Override
    public int edgeSize() {
        return edges.size();
    }

    /**
     * Return how many changes we does in this graph.
     *
     * @return
     */
    @Override
    public int getMC() {
        return mc;
    }

    /**
     * This method returns if this object and this graph equals of
     * their values of vertexes and edges.
     *
     * @param o
     * @return
     * */
    @Override
    public boolean equals(Object o){
        if(!(o instanceof WGraph_DS)) return false;
        /*if(getMC()!=((WGraph_DS) o).getMC()) return false;*/
        HashMap n= ((WGraph_DS) o).nodes;
        if(!(nodes.equals(n))) return false;
        HashMap e= ((WGraph_DS) o).edges;
        if(!(edges.equals(e))) return false;
        return true;
    }

    /**
     * This method returns a unique key of string for each edge between two vertexes.
     *
     * @param node1
     * @param node2
     * @return
     * */
    private String getPair(int node1, int node2) {
        if (node1 < node2)
            return String.valueOf(node1) + "," + String.valueOf(node2);
        return String.valueOf(node2) + "," + String.valueOf(node1);
    }

    @Override
    public String toString() {
        return "WGraph_DS: "+"\n"+ "\t"+
                "Nodes: " + getV() +"\n"+"\t"+
                "Sum_Of_Edges: "+edges.size()+"\n"+ "\t"+
                "Edges: "+edges.values() +"\n"+"\t"+
                "mc: " + mc;
    }

    /**Node_Info class*/
    private class Node_Info implements node_info, java.io.Serializable {
        private int idNode;
        private HashMap<Integer, node_info> neighbor;
        private String info;
        private double tag;

        public Node_Info(int key) {
            this.idNode = key;
            this.neighbor = new HashMap<>();
            this.info = "";
            this.tag = 0;
        }

        public Node_Info(node_info n) {
            this.idNode = n.getKey();
            this.neighbor = new HashMap<>();
            this.info = n.getInfo() + "";
            this.tag = n.getTag();
        }

        public Collection<node_info> getNi() {
            return neighbor.values();
        }

        /**
         * This method remove the node info (node) from this node_info neighbors.
         *
         * @param node
        */

        public void removeNei(node_info node) {
            this.neighbor.remove(node.getKey());
        }

        @Override
        public int getKey() {
            return idNode;
        }

        @Override
        public String getInfo() {
            return this.info;
        }

        @Override
        public void setInfo(String s) {
            this.info = s + "";
        }

        @Override
        public double getTag() {
            return this.tag;
        }

        @Override
        public void setTag(double t) {
            this.tag = t;
        }

        @Override
        public String toString() {
            return "" + idNode;
        }

        /**
         * This method returns if this object and this node_info equals of their values.
         *
         * @param o
         * @return
         * */
        @Override
        public boolean equals(Object o){
            if(o==null) return false;
            if(!(o instanceof node_info)) return false;
            int k= ((node_info)o).getKey();
            if(getKey()!=k) return false;
            if(getV(this.getKey())!=null && getV(k)!=null){
            if(!(getV(this.getKey()).equals(getV(k)))) return false;
            }
            if(getTag()!=((node_info) o).getTag()) return false;
            if(!(getInfo().equals(((node_info) o).getInfo()))) return false;
            return true;
        }

    }

    /**Edge class*/
    private class Edge implements java.io.Serializable{

        private String pair;
        private double weight;

        /**
         * Default constructor
         *
         * @param node1
         * @param node2
         * @param w
         */
        public Edge(int node1, int node2, double w) {
            this.weight = w;
            pair=getPair(node1,node2);
        }
        public void setWeight(double w){
            this.weight=w;
        }

        /**
         * This method returns if this object and this Edge equals of their values.
         *
         * @param o
         * @return
         * */
        @Override
        public boolean equals(Object o){
            if(!(o instanceof Edge)) return false;
            if(weight!=((Edge)o).weight) return false;
            if(!(pair.equals(((Edge)o).pair))) return false;
            return true;
        }

        @Override
        public String toString() {
            return "("+pair+")=" + weight;
        }
    }
}
