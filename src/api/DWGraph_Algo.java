package ex2.src.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.lang.reflect.Type;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

    @Override
    public double shortestPathDist(int src, int dest) {
        return 0;
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        return null;
    }

    @Override
    public boolean save(String file) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        /*JsonElement json= new
        JsonObject jsonObject = json.getAsJsonObject();*/
        return false;
    }

    @Override
    public boolean load(String file) {
        return false;
    }
}
