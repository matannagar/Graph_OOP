package api;

import java.util.Collection;
import java.util.HashMap;

/**
 * This class represents the set of operations applicable on a
 * node (vertex) in a (directional) weighted graph.
 *
 * @author Reut-Maslansky & Matan-Ben-Nagar
 */

public class NodeData implements node_data {
    private int id;
    private double weight;
    private geo_location geo;
    private HashMap<Integer, edge_data> src;
    private HashMap<Integer, edge_data> dest;
    private String info;
    private int tag;


    public NodeData(int key) {

        this.id = key;
        this.weight = 0;
        this.src = new HashMap<>();
        this.dest = new HashMap<>();
        this.geo = new GeoLocation();
        this.info = "";
        this.tag = 0;
    }

    public NodeData(int key, geo_location ge) {

        this.id = key;
        this.weight = -1;
        this.src = new HashMap<>();
        this.dest = new HashMap<>();
        this.geo = ge;
        this.info = "";
        this.tag = 0;
    }

    public NodeData(node_data n) {
        this.id = n.getKey();
        this.src = new HashMap<>();
        this.dest = new HashMap<>();
        this.info = n.getInfo() + "";
        this.tag = n.getTag();
        this.weight = n.getWeight();
        this.geo = new GeoLocation(n.getLocation());
    }

    /**
     * Returns the key (id) associated with this node.
     *
     * @return
     */
    @Override
    public int getKey() {
        return this.id;
    }

    public void addSrc(edge_data n) {
        this.src.put(n.getDest(), n);
    }

    public void addDest(edge_data n) {
        this.dest.put(n.getSrc(), n);
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

    /**
     * Returns the location of this node, if none return null.
     *
     * @return
     */
    @Override
    public geo_location getLocation() {
        return this.geo;
    }

    /**
     * Allows changing this node's location.
     *
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(geo_location p) {
        this.geo = p;
    }

    /**
     * Returns the weight associated with this node.
     *
     * @return
     */
    @Override
    public double getWeight() {
        return this.weight;
    }

    /**
     * Allows changing this node's weight.
     *
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        this.weight = w;
    }

    /**
     * Returns the remark (meta data) associated with this node.
     *
     * @return
     */
    @Override
    public String getInfo() {
        return info;
    }

    /**
     * Allows changing the remark (meta data) associated with this node.
     *
     * @param s
     */
    @Override
    public void setInfo(String s) {
        this.info = s + "";
    }

    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     *
     * @return
     */
    @Override
    public int getTag() {
        return this.tag;
    }

    /**
     * Allows setting the "tag" value for temporal marking an node - common
     * practice for marking by algorithms.
     *
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    public String toString() {
        return "" + id;
    }

    /**
     * This method returns if this object and this nodeData equals of their values.
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
        return true;
    }
}

