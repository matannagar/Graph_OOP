import json
import sys
from queue import Queue
import heapq
import random
from typing import List
import matplotlib.pyplot as plt
from DiGraph import DiGraph
from GraphAlgoInterface import GraphAlgoInterface

"""Temporary Node class, ment to hold information regarding original nodes"""


class NodeTemp:
    def __init__(self, id: int):
        self.idNode = id
        self.visit = 0
        self.tag = sys.maxsize
        self.parentId = -1

    def __repr__(self):
        return f'NodeTemp: {self.idNode} , tag= {self.tag}'

    def __lt__(self, other):
        return self.tag < other.tag


"""
        * This class represents a directed (positive) Weighted Graph Theory algorithms including:
        *      0. clone();
        *      1. init(graph);
        *      2. isConnected();
        *      3. double shortestPathDist(int src, int dest);
        *      4. List<node_data> shortestPath(int src, int dest);
        *      5. Save(file);
        *      6. Load(file);
        *
        * @param: gr- a graph on which We'll operates the algorithms.
        *
        * @author Reut-Maslansky & Matan-Ben-Nagar
"""


class GraphAlgo(GraphAlgoInterface):

    def __init__(self, graph: DiGraph = None):
        self.graph = graph

    """Return the underlying graph of which this class works."""
    def get_graph(self) -> DiGraph:
        return self.graph

    """ /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     *
     * we used json method.
     *
     * @param file - file name
     * @return true - if the graph was successfully loaded.
     */"""
    def load_from_json(self, file_name: str) -> bool:
        try:
            fp = open(file_name)

            self.graph = DiGraph()
            graph_file = json.load(fp)
            edges = graph_file.get('Edges')
            nodes = graph_file.get('Nodes')

            for n in nodes:
                if n.get('pos') is not None:
                    posarr = str(n.get('pos')).split(",")
                    pos = (float(posarr[0]), float(posarr[1]), 0.0)
                    self.graph.add_node(node_id=n.get('id'), pos=pos)
                else:
                    self.graph.add_node(node_id=n.get('id'))

            for x in edges:
                src = x.get('src')
                dest = x.get('dest')
                w = x.get('w')
                self.graph.add_edge(src, dest, w)
                # fp.close()
        except FileExistsError:
            print("Graph was not loaded successfully")
            return False

        return True

    """    /**
     * Saves this weighted (undirected) graph to the given file name.
     * we used he json method.
     *
     * @param file - the file name (may include a relative path).
     * @return true - if the file was successfully saved
     *
     */"""
    def save_to_json(self, file_name: str) -> bool:
        if self.graph is None:
            return False

        nodes = []
        for n in self.graph.nodes:
            id = self.graph.nodes.get(n).id
            pos= self.graph.nodes.get(n).pos
            if pos is not None:
                pos_x = self.graph.nodes.get(n).pos[0]
                pos_y= self.graph.nodes.get(n).pos[1]
                pos=str(pos_x)+','+str(pos_y)+','+str(0.0)
            nodes.append({"pos": pos, "id": id})

        edges = []
        for n in self.graph.nodes:
            for dest in self.graph.nodes.get(n).src:
                w = self.graph.nodes.get(n).src.get(dest)
                edges.append({"src": int(n), "dest": int(dest), "w": w})

        mygraph = {"Edges": edges, "Nodes": nodes}

        with open(file_name, 'w') as json_file:
            json.dump(mygraph, json_file)

        return True

    """
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     *
     * Note if no such path --> returns null;
     * @param src - start node
     * @param dest - end (target) node
     * @return
     *
     *
     * This method based on Dijkstra Algorithm.
     * We use the private function- dij- that will return a HashMap.
     *ALSO:
     * In this HasMap, the data of this algorithm will save in a temporal Node- contains:
     *      1. The shortest weight from the source vertex to this vertex.
     *      2. The vertex neighbor that connect this vertex and update him this value of weight accepted.
      /**
     * Returns the length of the shortest path between src to dest
     * if no such path, returns -1
     *
     * @param src - start node
     * @param dest - end (target) node
     * @return
     *
     * This method based on Dijkstra Algorithm.
     * The data of this algorithm will save in a temporal Node-
     * 1. The shortest weight from the source vertex to this vertex.
     * 2. The vertex neighbor that connect this vertex and update him this value of weight accepted.
     */"""
    def shortest_path(self, id1: int, id2: int) -> (float, list):
        if self.graph is None:
            return None
        if str(id1) not in self.graph.nodes or str(id2) not in self.graph.nodes:
            return None

        if id1 == id2:
            return 0, [self.graph.get_node(id1)]

        vertex = {}
        for n in self.graph.nodes:
            nodetemp = NodeTemp(n)
            vertex[str(n)] = nodetemp

        vertex.get(str(id1)).tag = 0
        heap = []
        heapq.heappush(heap, vertex.get(str(id1)))
        flag = True
        while heap and flag:
            heapq.heapify(heap)
            current = heapq.heappop(heap)
            if current.visit != 1:
                for n in self.graph.get_node(current.idNode).src:
                    b = vertex.get(n)
                    if b.visit != 1:
                        if b.tag > current.tag + self.graph.edges.get(str(current.idNode) + '->' + str(n)):
                            b.tag = current.tag + self.graph.edges.get(str(current.idNode) + '->' + str(n))
                            b.parentId = current.idNode
                    heapq.heappush(heap, b)
                current.visit = 1

                if current.idNode == id2:
                    flag = False
        dest = vertex.get(str(id2))  # NodeTemp
        if dest.tag == sys.maxsize:
            dest.tag = -1

        listShort = []
        if dest.tag == -1:
            listShort = None
        else:
            # listShort.append(self.graph.get_node(dest.idNode))
            listShort.append(int(dest.idNode))
            curr = dest
            while curr.parentId != -1:
                parent = curr.parentId
                curr = vertex.get(str(parent))
                # listShort.append(self.graph.get_node(curr.idNode))
                listShort.append(int(curr.idNode))
            listShort.reverse()
        tup = (dest.tag, listShort)
        return tup

    """ Finds the Strongly Connected Component(SCC) that node id1 is a part of.
        @param id1: The node id
        @return: The list of nodes in the SCC
    """
    def connected_component(self, id1: int) -> list:
        if self.graph is None:
            return None
        #   return []

        # check if we should return None or empty list
        if str(id1) not in self.graph.nodes.keys():
            return []
        list1 = self.bfs(id1, 0)
        list2 = self.bfs(id1, 1)
        return list(set(list1) & set(list2))

    """/**
     * bfs will pass over the nodes.
     *
     * @Return how many nodes we marked- then we will know if we pass all the nodes in the graph.
     *
     * The function receives a vertex key from which we will perform the test on the graph connected test.
     * During the test we will mark the vertices in the graph in the "info" that each vertex holds.
     * At the end we will return the number of times we changed the vertex info,
     * and thus we will know if during the iterations we went over the amount of vertices that exist in the graph.
     * In this operation, of comparing the counter with the number of vertices in the graph -
     * we saved another pass over all the vertices in the graph - in a loop of o(v) where v is the number of vertices in the graph.
     */"""
    def bfs(self, id1: int, flag: int) -> list:
        # resets all the visited nodes to unvisited
        for n in self.graph.nodes:
            self.graph.get_node(n).tag = 0

        queue = Queue()
        list1 = [self.graph.get_node(id1)]
        self.graph.get_node(id1).tag = 1

        neigh = self.graph.get_node(id1).src
        if flag == 1:
            neigh = self.graph.get_node(id1).dest
        for n in neigh:
            temp = self.graph.get_node(n)
            queue.put(temp)
            list1.append(temp)

        # mark src node as visited
        while not queue.empty():
            n = queue.get()
            n.tag = 1
            neigh = n.src
            if flag == 1:
                neigh = n.dest
            for x in neigh:
                temp2 = self.graph.get_node(x)
                if temp2.tag == 0:
                    queue.put(temp2)
                    list1.append(temp2)
        list1 = list(dict.fromkeys(list1))
        return list1

    """ 
    Finds all the Strongly Connected Component(SCC) in the graph.
    @return: The list all SCC
    """
    def connected_components(self) -> List[list]:
        # tempList=[List]
        tempList = []
        for n in self.graph.nodes:
            tempList.append(self.connected_component(n))
        # list1=[List]
        list1 = []
        for l in tempList:
            if l not in list1:
                list1.append(l)
        return list1

    """
        Plots the graph.
        If the nodes have a position, the nodes will be placed there.
        Otherwise, they will be placed in a random but elegant manner.
        @return: None
        """
    def plot_graph(self) -> None:
        xlist = []
        ylist = []

        # paints all the nodes in the graph
        for x in self.graph.nodes:
            pos = self.graph.get_node(x).pos
            if pos is None:
                self.graph.nodes.get(x).pos = (random.uniform(0.0, 50), random.uniform(0.0, 50), 0.0)
                pos = self.graph.nodes.get(x).pos
            xlist.append(pos[0])
            ylist.append(pos[1])

        plt.plot(xlist, ylist, '.', color='red')

        # clear the x and y lists from the nodes
        xlist.clear()
        ylist.clear()
        # loop over edges in the graph and draw them one by one
        for e in self.graph.edges:
            edge = str(e).split('->')
            src = int(edge[0])
            dest = int(edge[1])
            # draws the arrow pointing in the edge direction --> (dest)
            pos = self.graph.get_node(dest).pos

            xlist.append(pos[0])
            ylist.append(pos[1])
            plt.plot(xlist, ylist, markersize=10, marker='*', color='red')

            # draw the edge coming out from the src node to dest node
            pos = self.graph.get_node(src).pos

            xlist.append(pos[0])
            ylist.append(pos[1])
            plt.plot(xlist, ylist, '-', color='pink')

            # clears the x and y lists for the next edge
            xlist.clear()
            ylist.clear()
        plt.title("My Graph")

        for key in self.graph.nodes:
            ver = self.graph.nodes.get(key)
            if not ver.info:
                ver.pos = None

        plt.show()
