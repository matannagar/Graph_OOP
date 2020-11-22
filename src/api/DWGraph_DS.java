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
		// TODO Auto-generated constructor stub
		this.mc=0;
		this.vertex=new HashMap<>();
		this.edges=new HashMap<>();
	}
	//	public DWGraph_DS(directed_weighted_graph g) {
	//		if(g==null) return;
	//
	//		this.vertex = new HashMap<>();
	//		this.edges= new HashMap<>();
	//
	//		for (node_data n : g.getV()) {
	//			node_data no = new Node_Data(n);
	//			addNode(no);
	//		}
	//		for (node_data n : g.getV()) {
	//			for (node_data ni : g.getV(n.getKey())) {
	//				connect();
	//			}
	//		}
	//
	//		this.mc=g.nodeSize()+g.edgeSize();
	//	}
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
		Node_Data s = (Node_Data) getNode(src);
		Node_Data d = (Node_Data) getNode(dest);
		s.neighbor.put(dest, d);
		d.neighbor.put(src, s);

		Edge_DS e = new Edge_DS(src, dest, w);

		edges.put(pair, e);
	}


	@Override
	public Collection<node_data> getV() {
		return vertex.values();
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		return edges.values();
	}

	@Override
	public node_data removeNode(int key) {
		if (vertex.containsKey(key)) {
			mc++;
			node_data node = getNode(key);

			Iterator<node_data> it = ((Node_Data)node).getNi().iterator();

			//This "while" will loop through the list of neighbors
			//and delete the edge between the vertex and his neighbor.

			while (it.hasNext()) {
				node_data n = it.next();
				removeEdge(key, n.getKey());
				removeEdge(n.getKey(), key);
				it = ((Node_Data)node).getNi().iterator();
			}

			vertex.remove(key);
			return node;
		}
		return null;
	}

	@Override
	public edge_data removeEdge(int src, int dest) {
		String pair = getPair(src,dest);
		if (!(edges.containsKey(pair))) return null;
		edge_data e = edges.get(pair);
		mc++;
		node_data s = getNode(src);
		node_data d = getNode(dest);
		((Node_Data) s).removeNei(dest);
		((Node_Data) d).removeNei(src);
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
	private class Node_Data implements node_data {
		private int idNode;
		private double weight;
		private geo_location geo;
		private HashMap<Integer, node_data> neighbor;
		private String info;
		private int tag;


		@Override
		public int getKey() {
			return this.idNode;
		}

		public Node_Data(int key, double w) {

			this.idNode = key;
			this.weight=w;
			this.neighbor = new HashMap<>();
			this.geo= new Geo_Location();
			this.info = "";
			this.tag = 0;
		}

		public Node_Data(node_data n) {
			this.idNode = n.getKey();
			this.neighbor = new HashMap<>();
			this.info = n.getInfo() + "";
			this.tag = n.getTag();
			this.weight=n.getWeight();
			this.geo= new Geo_Location(n.getLocation());
		}

		public Collection<node_data> getNi() {
			return neighbor.values();
		}

		public void removeNei(int node) {
			this.neighbor.remove(node);
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
			this.weight=w;
		}

		@Override
		public String getInfo() {
			return info;
		}

		@Override
		public void setInfo(String s) {
			this.info= s+"";
		}

		@Override
		public int getTag() {
			return this.tag;
		}

		@Override
		public void setTag(int t) {
			this.tag=t;
		}

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
			if(!(o instanceof node_data)) return false;
			int k= ((node_data)o).getKey();
			if(getKey()!=k) return false;
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

			public Geo_Location(){
				this.x=0;
				this.y=0;
				this.z=0;
			}

			public Geo_Location(double x, double y, double z){
				this.x=x;
				this.y=y;
				this.z=z;
			}

			public Geo_Location(geo_location g){
				this.x=g.x();
				this.y=g.y();
				this.z=g.z();
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
	private class Edge_DS implements edge_data  {
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
			// TODO Auto-generated method stub
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
	}
}
