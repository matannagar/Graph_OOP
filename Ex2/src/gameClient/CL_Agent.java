package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

public class CL_Agent {
    public static final double EPS = 0.0001;
    private int _id;
    private geo_location _pos;
    private double _speed;
    private edge_data _curr_edge;
    private node_data _curr_node;
    private directed_weighted_graph _gg;
    private double _value;


    public CL_Agent(directed_weighted_graph g, int start_node) {
        _gg = g;
        set_value(0);
        this._curr_node = _gg.getNode(start_node);
        _pos = _curr_node.getLocation();
        _id = -1;
        setSpeed(0);
    }

    /**
     * constantly creating agents from a json file.
     * @param json
     */
    public void update(String json) {
        JSONObject line;
        try {
            line = new JSONObject(json);
            JSONObject readA = line.getJSONObject("Agent");
            int id = readA.getInt("id");
            if (id == this.getID() || this.getID() == -1) {
                if (this.getID() == -1) {
                    _id = id;
                }
                double speed = readA.getDouble("speed");
                this.setSpeed(speed);
                String p = readA.getString("pos");
                Point3D posA = new Point3D(p);
                this._pos = posA;

                int src = readA.getInt("src");
                this.setCurrNode(src);

                int dest = readA.getInt("dest");
                this.setNextNode(dest);
                double value = readA.getDouble("value");
                this.set_value(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getSrcNode() {
        return this._curr_node.getKey();
    }

    private void set_value(double v) {
        _value = v;
    }

    public boolean setNextNode(int nextNode) {
        boolean ans = false;
        int src = this._curr_node.getKey();
        this._curr_edge = _gg.getEdge(src, nextNode);
        if (_curr_edge != null) {
            ans = true;
        }
        return ans;
    }

    public void setCurrNode(int src) {
        this._curr_node = _gg.getNode(src);
    }

    @Override
    public String toString() {
        int d = this.getNextNode();
        String ans = "{\"Agent\":{"
                + "\"id\":" + this._id + ","
                + "\"value\":" + this._value + ","
                + "\"src\":" + this._curr_node.getKey() + ","
                + "\"dest\":" + d + ","
                + "\"speed\":" + this.getSpeed() + ","
                + "\"pos\":\"" + _pos.toString() + "\""
                + "}"
                + "}";
        return ans;
    }

    public int getID() {
        return this._id;
    }

    public geo_location getLocation() {
        return _pos;
    }

    public double getValue() {
        return this._value;
    }

    public int getNextNode() {
        int ans;
        if (this._curr_edge == null) {
            ans = -1;
        } else {
            ans = this._curr_edge.getDest();
        }
        return ans;
    }

    public double getSpeed() {
        return this._speed;
    }

    public void setSpeed(double v) {
        this._speed = v;
    }
}
