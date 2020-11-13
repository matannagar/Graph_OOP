package ex1;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class WGraph_DS implements weighted_graph, java.io.Serializable {
    private HashMap<Integer, node_info> nodes;
    private HashMap<String, Edge> edges;
    private int mc;

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

        this.mc = g.getMC();
    }


    @Override
    public node_info getNode(int key) {
        return nodes.get(key);
    }

    @Override
    public boolean hasEdge(int node1, int node2) {
        String pair = getPair(node1, node2);
        return edges.containsKey(pair);
    }

    @Override
    public double getEdge(int node1, int node2) {
       String pair=getPair(node1,node2);
        if(edges.containsKey(pair)){
            return edges.get(pair).weight;
        }
        return -1;
    }

    @Override
    public void addNode(int key) {
        if(nodes.containsKey(key)) return;
        node_info n= new Node_Info(key);
        nodes.put(key, n);
        mc++;
    }

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

    @Override
    public Collection<node_info> getV() {
        return nodes.values();
    }

    @Override
    public Collection<node_info> getV(int node_id) {
        if (!(nodes.containsKey(node_id))) return null;
        Node_Info n = (Node_Info) getNode(node_id);
        return n.getNi();
    }

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

    @Override
    public int nodeSize() {
        return nodes.size();
    }

    @Override
    public int edgeSize() {
       // return sumEdge;
        return edges.size();
    }

    @Override
    public int getMC() {
        return mc;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof WGraph_DS)) return false;
        if(getMC()!=((WGraph_DS) o).getMC()) return false;
        HashMap n= ((WGraph_DS) o).nodes;
        if(!(nodes.equals(n))) return false;
        HashMap e= ((WGraph_DS) o).edges;
        if(!(edges.equals(e))) return false;
        return true;
    }

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

    //Node_Info class
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


        @Override
        public boolean equals(Object o){
            if(!(o instanceof node_info)) return false;
            int k= ((node_info)o).getKey();
            if(getKey()!=k) return false;
            if(!(getV(getKey()).equals(getV(k))))return false;
            if(getTag()!=((node_info) o).getTag()) return false;
            if(!(getInfo().equals(((node_info) o).getInfo()))) return false;
            return true;
        }
    }

    //Edge class
    private class Edge implements java.io.Serializable{

        private String pair;
        private double weight;

        public Edge(int node1, int node2, double w) {
            this.weight = w;
            pair=getPair(node1,node2);
        }
        public void setWeight(double w){
            this.weight=w;
        }

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
