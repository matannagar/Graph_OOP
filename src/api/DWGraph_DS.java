package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class represents an directional weighted graph.
 *
 * @param: Nodes- A HashMap contains all the vertexes in this graph.
 * @param: Rdges- A HashMap contains all the edges in this graph.
 * @param: mc- How many changes we does in this graph.
 *
 * @author Reut-Maslansky & Matan-Ben-Nagar
 */

public class DWGraph_DS implements directed_weighted_graph {
	private HashMap<Integer,node_data> Nodes;
	private HashMap<String,edge_data> Edges;
	private int mc;

	/**
	 * Default constructor
	 */
	public DWGraph_DS() {
		this.mc=0;
		this.Nodes =new HashMap<>();
		this.Edges =new HashMap<>();
	}

	/**
	 * Copy constructor
	 *
	 * @param g
	 */
	public DWGraph_DS(directed_weighted_graph g) {
		if(g==null) return;

		this.Nodes = new HashMap<>();
		this.Edges = new HashMap<>();

		for (node_data n : g.getV()) {
			node_data no = new NodeData(n);
			addNode(no);
		}
		for (node_data n : g.getV()) {
			for (edge_data e : g.getE(n.getKey())) {
				connect(e.getSrc(),e.getDest(),e.getWeight());
			}
		}

		this.mc=g.nodeSize()+g.edgeSize();
	}

	/**
	 * returns the node throw the key number.
	 * @param key - the node_id
	 * @return
	 */
	@Override
	public node_data getNode(int key) {
		return Nodes.get(key);
	}

	/**
	 * Return the weight between this vertexes.
	 * If there is no such edge- return -1.
	 *
	 * @param src
	 * @param dest
	 * @return
	 * */
	@Override
	public edge_data getEdge(int src, int dest) {
		String pair = getPair(src,dest);
		return Edges.get(pair);
	}

	/**
	 * Add a new node to the graph with the given key.
	 */
	@Override
	public void addNode(node_data n) {
		if (!Nodes.containsKey(n.getKey())) {
			Nodes.put(n.getKey(), n);
			mc++;
		}
	}

	/**
	 * Connect an edge between src and dest, with an edge with weight >=0.
	 * if the edge src-dest already exists - the method simply updates the weight of the edge.
	 */
	@Override
	public void connect(int src, int dest, double w) {
		if (!(this.Nodes.containsKey(src)) || !(this.Nodes.containsKey(dest))) return;
		if (src == dest) return;
		if(w<0) throw new RuntimeException("The weight should be >=0. Do choose a new weight to this edge");

		String pair= getPair(src,dest);

		// Update the weight edge between those nodes.
		if(Edges.containsKey(pair)) {
			if(((Edges.get(pair))).getWeight()==w) return;
			((Edge_DS)(Edges.get(pair))).setWeight(w);
			mc++;
			return;
		}

		mc++;

		NodeData s = (NodeData) getNode(src);
		NodeData d = (NodeData) getNode(dest);
		edge_data e = new Edge_DS(src, dest, w);
		s.addSrc(e);
		d.addDest(e);

		Edges.put(pair, e);
	}

	/**
	 * This method return a pointer for the collection representing all the nodes in the graph.
	 *
	 * @return Collection<node_data>
	 */
	@Override
	public Collection<node_data> getV() {
		return Nodes.values();
	}

	/**
	 * This method return a pointer for the collection representing all the edges that leaves the src node
	 * @param node_id
	 * @return
	 */
	@Override
	public Collection<edge_data> getE(int node_id) {
		if(!(Nodes.containsKey(node_id))) return null;
		return ((NodeData)(getNode(node_id))).getSrc();
	}


	/**
	 * This method return a pointer for the collection representing all the edges that are coming from the dest node
	 * @param node_id
	 * @return
	 */
	public Collection<edge_data> getEDest(int node_id) {
		if(!(Nodes.containsKey(node_id))) return null;
		return ((NodeData)(getNode(node_id))).getDest();
	}


	/**
	 * This method return a pointer for the collection representing all the edges in this graph
	 * @return
	 */
	public Collection<edge_data> getEdgesCollection() {
		return Edges.values();
	}

	/**
	 * Delete the node (with the given ID) from the graph -
	 * and removes all edges which starts or ends at this node.
	 *
	 * @param key
	 * @return the data of the removed node (null if none).
	 */
	@Override
	public node_data removeNode(int key) {
		if (Nodes.containsKey(key)) {
			mc++;
			node_data node = getNode(key);

			Iterator<edge_data> it = ((NodeData)node).getSrc().iterator();

			//This "while" will loop through the list of neighbors
			//and delete the edge between the vertex and his neighbor.

			while (it.hasNext()) {
				edge_data n = it.next();
				removeEdge(key, n.getDest());
				it = ((NodeData)node).getSrc().iterator();
			}

			it = ((NodeData)node).getDest().iterator();

			//This "while" will loop through the list of neighbors
			//and delete the edge between the vertex and his neighbor.

			while (it.hasNext()) {
				edge_data n = it.next();
				removeEdge(n.getSrc(), key);
				it = ((NodeData)node).getDest().iterator();
			}

			Nodes.remove(key);

			return node;
		}
		return null;
	}

	/**
	 * Delete the edge from the graph between the vertexes.
	 *
	 * @param src
	 * @param dest
	 */
	@Override
	public edge_data removeEdge(int src, int dest) {
		if(src==dest) return null;
		String pair = getPair(src,dest);
		if (!(Edges.containsKey(pair))) return null;
		edge_data e = Edges.get(pair);
		mc++;
		node_data s = getNode(src);
		node_data d = getNode(dest);
		((NodeData) s).removeSrc(dest);
		((NodeData) d).removeDest(src);
		Edges.remove(getPair(src,dest));
		return e;
	}

	/** return the number of vertices in this graph.*/
	@Override
	public int nodeSize() {
		return Nodes.size();
	}

	/** return the number of edges in this graph.*/
	@Override
	public int edgeSize() {
		return Edges.size();
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
	 * return String pair of src node and dest node of an edge.
	 * @param src
	 * @param dest
	 * @return
	 */
	public String getPair(int src, int dest) {
		return String.valueOf(src) + "," + String.valueOf(dest);
	}

	@Override
	public String toString() {
		return "DWGraph_DS: "+"\n"+ "\t"+
				"vertex=" + getV() +"\n"+"\t"+
				"edges=" + Edges.values() +"\n"+"\t"+
				"mc: " + mc;
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
		if(!(o instanceof DWGraph_DS)) return false;
		HashMap v= ((DWGraph_DS) o).Nodes;
		if(!(Nodes.equals(v))) return false;
		HashMap e= ((DWGraph_DS) o).Edges;
		if(!(Edges.equals(e))) return false;
		return true;
	}
}