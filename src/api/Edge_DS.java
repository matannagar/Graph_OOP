package ex2.src.api;

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
        //**********NOTICE ME************
        this.tag = -1;
    }

    @Override
    public int getSrc() {
        return this.src;
    }

    @Override
    public int getDest() {
        return this.dest;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s;

    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }

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

