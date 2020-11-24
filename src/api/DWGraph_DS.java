package ex2.src.api;


import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class DWGraph_DS implements directed_weighted_graph {
	private HashMap<Integer,node_data> vertex;
	private HashMap<String,edge_data> edges;
	private int mc;

	@Override
	public node_data getNode(int key) {
		return vertex.get(key);
	}

	public DWGraph_DS() {
		this.mc=0;
		this.vertex=new HashMap<>();
		this.edges=new HashMap<>();
	}
		public DWGraph_DS(directed_weighted_graph g) {
			if(g==null) return;

	this.vertex = new HashMap<>();
	this.edges= new HashMap<>();

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
	@Override
	public edge_data getEdge(int src, int dest) {
		String pair = getPair(src,dest);
		return edges.get(pair);
	}

	@Override
	////check
	public void addNode(node_data n) {
		if (!vertex.containsKey(n.getKey())) {
			vertex.put(n.getKey(), n);
			mc++;
		}
	}

	@Override
	public void connect(int src, int dest, double w) {
		if (!(this.vertex.containsKey(src)) || !(this.vertex.containsKey(dest))) return;
		if (src == dest) return;
		if(w<0) throw new RuntimeException("The weight should be >=0. Do choose a new weight to this edge");


		String pair= getPair(src,dest);


		// Update the weight edge between those nodes.
		if(edges.containsKey(pair)) {
			if(((Edge_DS)(edges.get(pair))).weight==w) return;
			((Edge_DS)(edges.get(pair))).weight=w;
			mc++;
			return;
		}

		mc++;

		NodeData s = (NodeData) getNode(src);
		NodeData d = (NodeData) getNode(dest);
		edge_data e = new Edge_DS(src, dest, w);
		s.addSrc(e);
		d.addDest(e);

		edges.put(pair, e);
	}


	@Override
	public Collection<node_data> getV() {
		return vertex.values();
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		return ((NodeData)(getNode(node_id))).getSrc();
	}

	@Override
	public node_data removeNode(int key) {
		if (vertex.containsKey(key)) {
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


			vertex.remove(key);

			return node;
		}
		return null;
	}

	@Override
	public edge_data removeEdge(int src, int dest) {
		if(src==dest) return null;
		String pair = getPair(src,dest);
		if (!(edges.containsKey(pair))) return null;
		edge_data e = edges.get(pair);
		mc++;
		node_data s = getNode(src);
		node_data d = getNode(dest);
		((NodeData) s).removeSrc(dest);
		((NodeData) d).removeDest(src);
		edges.remove(getPair(src,dest));
		return e;
	}


	@Override
	public int nodeSize() {
		return vertex.size();
	}

	@Override
	public int edgeSize() {
		return edges.size();
	}

	@Override
	public int getMC() {
		return mc;
	}
	private String getPair(int src, int dest) {
		return String.valueOf(src) + "," + String.valueOf(dest);
	}

	@Override
	public String toString() {
		return "DWGraph_DS: "+"\n"+ "\t"+
				"vertex=" + getV() +"\n"+"\t"+
				", edges=" + edges.values() +"\n"+"\t"+
				"mc: " + mc;

	}

	public class Edge_DS implements edge_data  {
		private int src;
		private int dest;
		private String info;
		private int tag;
		private double weight;

		public Edge_DS (int src, int dest, double w) {
			this.src = src;
			this.dest=dest;
			this.weight = w;
			this.info = "";
			//**********NOTICE ME************
			this.tag = -1;
		}

		@Override
		public int getSrc() {
			// TODO Auto-generated method stub
			return this.src;
		}

		@Override
		public int getDest() {
			// TODO Auto-generated method stub
			return this.dest;
		}

		@Override
		public double getWeight() {
			return this.weight;
		}

		@Override
		public String getInfo() {
			// TODO Auto-generated method stub
			return this.info;
		}

		@Override
		public void setInfo(String s) {
			this.info = s;

		}

		@Override
		public int getTag() {
			// TODO Auto-generated method stub
			return this.tag;
		}

		@Override
		public void setTag(int t) {
			this.tag=t;
		}
		public void setWeight(double w) {
			this.weight= w;
		}

		@Override
		public String toString() {
			return "Edge_DS{" +
					"src=" + src +
					", dest=" + dest;
		}
	}
}