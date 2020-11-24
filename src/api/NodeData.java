package ex2.src.api;

import java.util.Collection;
import java.util.HashMap;

public class NodeData implements node_data {
    private int idNode;
    private double weight;
    private geo_location geo;
    private HashMap<Integer, edge_data> src;
    private HashMap<Integer, edge_data> dest;
    private String info;
    private int tag;


    public NodeData(int key) {

        this.idNode = key;
        this.weight = 0;
        this.src= new HashMap<>();
        this.dest= new HashMap<>();
        this.geo = new Geo_Location();
        this.info = "";
        this.tag = 0;
    }

    public NodeData(int key, double w) {

        this.idNode = key;
        this.weight = w;
        this.src= new HashMap<>();
        this.dest= new HashMap<>();
        this.geo = new Geo_Location();
        this.info = "";
        this.tag = 0;
    }

    public NodeData(node_data n) {
        this.idNode = n.getKey();
        this.src= new HashMap<>();
        this.dest= new HashMap<>();
        this.info = n.getInfo() + "";
        this.tag = n.getTag();
        this.weight = n.getWeight();
        this.geo = new Geo_Location(n.getLocation());
    }

    @Override
    public int getKey() {
        return this.idNode;
    }

    public void addSrc(edge_data n){
        this.src.put(n.getDest(),n);
    }

    public void addDest(edge_data n){
        this.dest.put(n.getSrc(),n);
    }

    public Collection<edge_data> getSrc() {
        return src.values();
    }
    public Collection<edge_data> getDest() {
        return dest.values();
    }

    public void removeSrc(int node) {
        this.src.remove(node);
    }
    public void removeDest(int node) {
        this.dest.remove(node);
    }

    @Override
    public geo_location getLocation() {
        return this.geo;
    }

    //////////////////////
    @Override
    public void setLocation(geo_location p) {

    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(double w) {
        this.weight = w;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s + "";
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    public String toString() {
        return "" + idNode;
    }

    /**
     * This method returns if this object and this node_info equals of their values.
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof node_data)) return false;
        int k = ((node_data) o).getKey();
        if (getKey() != k) return false;
			/* if(getV(this.getKey())!=null && getV(k)!=null){
                if(!(getV(this.getKey()).equals(getV(k)))) return false;
            }
            if(getTag()!=((node_info) o).getTag()) return false;
            if(!(getInfo().equals(((node_info) o).getInfo()))) return false;
            return true;
        }*/
        return true;
    }

    private class Geo_Location implements geo_location {
        private double x;
        private double y;
        private double z;

        public Geo_Location() {
            this.x = 0;
            this.y = 0;
            this.z = 0;
        }

        public Geo_Location(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Geo_Location(geo_location g) {
            this.x = g.x();
            this.y = g.y();
            this.z = g.z();
        }

        @Override
        public double x() {
            return x;
        }

        @Override
        public double y() {
            return y;
        }

        @Override
        public double z() {
            return z;
        }

        //////////////////////////////
        /////fix/////////
        @Override
        public double distance(geo_location g) {
            return 0;
        }
    }
}

