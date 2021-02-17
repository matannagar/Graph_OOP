package api;

public class Edge_DS implements edge_data {
    private int src;
    private int dest;
    private String info;
    private int tag;
    private double weight;

    public Edge_DS(int src, int dest, double w) {
        this.src = src;
        this.dest = dest;
        this.weight = w;
        this.info = "";
        this.tag = -1;
    }

    /**
     * The id of the source node of this edge.
     * @return
     */
    @Override
    public int getSrc() {
        return this.src;
    }

    /**
     * The id of the destination node of this edge
     * @return
     */
    @Override
    public int getDest() {
        return this.dest;
    }

    /**
     * @return the weight of this edge (positive value).
     */
    @Override
    public double getWeight() {
        return this.weight;
    }

    /**
     * Returns the remark (meta data) associated with this edge.
     * @return
     */
    @Override
    public String getInfo() {
        return this.info;
    }

    /**
     * Allows changing the remark (meta data) associated with this edge.
     * @param s
     */
    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     * @return
     */
    @Override
    public int getTag() {
        return this.tag;
    }

    /**
     * This method allows setting the "tag" value for temporal marking an edge
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    /**
     * This method allows setting the weight value for this edge
     * @param w - the new value of the weight
     */
    public void setWeight(double w) {
        this.weight = w;
    }

    @Override
    public String toString() {
        return "Edge_DS{" +
                "src= " + src +
                ", dest= " + dest+ ", weight= " +weight;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof edge_data)) return false;
        if (weight != (((edge_data) o).getWeight())) return false;
        if (this.src != ((edge_data) o).getSrc()) return false;
        if (this.dest != ((edge_data) o).getDest()) return false;
        return true;
    }
}

