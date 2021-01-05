import json
from logging import warning
from queue import Queue
import heapq
import random
from typing import List
import matplotlib.pyplot as plt
from DiGraph import DiGraph
import math
from GraphAlgoInterface import GraphAlgoInterface

"""Temporary Node class, meant to hold information regarding original nodes
and help implement Dijkstra's method"""


class NodeTemp:
    def __init__(self, id: int):
        self.idNode = id
        self.tag = math.inf
        self.parentId = -1
        self.visit = 0

    def __repr__(self):
        return f'NodeTemp: {self.idNode} , tag= {self.tag}'

    def __lt__(self, other):
        return self.tag < other.tag


"""
This class represents a directed (positive) Weighted Graph Theory algorithms including:
    0. getGraph()
    1. init(graph);
    2. connectedComponent()
    3. connectedComponents()
    4. shortestPath
    5. Save(file)-json;
    6. Load(file)-json;
        
@param: gr- a graph on which We'll operates the algorithms.
        
@author Reut-Maslansky & Matan-Ben-Nagar
"""


class GraphAlgo(GraphAlgoInterface):

    def __init__(self, graph: DiGraph = None):
        self.graph = graph
        self.com_nod = []


    """Return the underlying graph of which this class works."""

    def get_graph(self) -> DiGraph:
        return self.graph

    """
    This method load a graph to this graph algorithm.
    if the file was successfully loaded - the underlying graph
    of this class will be changed (to the loaded one), in case the
    graph was not loaded the original graph should remain "as is".
    
    we used json method.
    
    @param file - file name
    @return true - if the graph was successfully loaded.
    """

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
                fp.close()
        except FileExistsError:
            print("Graph was not loaded successfully")
            return False

        return True

    """
    Saves this weighted directed graph to the given file name.
    we used he json method.
    
    @param file - the file name (may include a relative path).
    @return true - if the file was successfully saved
    """

    def save_to_json(self, file_name: str) -> bool:
        if self.graph is None:
            return False

        nodes = []
        for n in self.graph.nodes:
            id = self.graph.nodes.get(n).id
            pos = self.graph.nodes.get(n).pos
            if pos is not None:
                pos_x = self.graph.nodes.get(n).pos[0]
                pos_y = self.graph.nodes.get(n).pos[1]
                pos = str(pos_x) + ',' + str(pos_y) + ',' + str(0.0)
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

    This method based on Dijkstra Algorithm.
    The data of this algorithm will save in a temporal Node, contains-
        1. The shortest weight from the source vertex to this vertex.
        2. The vertex neighbor that connect this vertex and update him this value of weight accepted.
        (By default the vertex tag contains INF and the vertex parent points as a vertex with key -1).
        
    Returns Tuple contain:
        1. The length of the shortest path between src to dest
        If no such path, returns inf and an empty list
        2. The shortest path between src to dest - as an ordered List of nodes: src--> n1-->n2-->...dest
        If no such path --> returns an empty List.

    The function goes through all the neighbors of the source vertex and does them "weight relief" if necessary-
    That is, if we have reached a vertex whose weight can be reduced relative to its current weight, we will update the information contained the temporal vertex.
    The implementation of the algorithm is done by a priority queue, which is implementation by a function of comparing the weights (tag) of the temporal vertexes in the queue.
    
    """

    def shortest_path(self, id1: int, id2: int) -> (float, list):
        if self.graph is None:
            return (math.inf, [])
        if str(id1) not in self.graph.nodes or str(id2) not in self.graph.nodes:
            return (math.inf, [])

        if id1 == id2:
            return 0, [id1]

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

        listShort = []
        if dest.tag != math.inf:
            listShort.append(int(dest.idNode))
            curr = dest
            while curr.parentId != -1:
                parent = curr.parentId
                curr = vertex.get(str(parent))
                listShort.append(int(curr.idNode))
            listShort.reverse()
        tup = (dest.tag, listShort)
        return tup

    '''
    Finds the Strongly Connected Component(SCC) that node id1 is a part of.
    @param id1: The node id
    @return: The list of nodes in the SCC
    
    This method is based on the BFS Algorithm- with little changes.
    We will send the node we want find his SSC and do BFS algorithm that will return the list of nodes that we can reach from this node.
    After this we will "reverse" the graph's edges and will send the node to BFS one more time.
    Finally, we merge the lists and return the union.  
    '''

    def connected_component(self, id1: int) -> list:
        if self.graph is None:
            return []

        if str(id1) not in self.graph.nodes.keys():
            return []

        lists = self.bfs(id1)
        # resets all the visited nodes to unvisited
        # נאפס רק בפעם הראשונה
        for n in lists[1]:
            n.tag = 0

        return lists[0]

    """
     bfs will pass over the nodes that we can reach from the src node we will send to the function.
     
     @Return list of those nodes.
     
     The function receives a vertex key from which we will perform the test on the graph connected test.
     During the test we will mark the vertices in the graph in the "tag" that each vertex holds.
 
     """

    def bfs(self, id1: int) -> (list,list):
        myList = []
        list1 = []

        queue = Queue()
        # ---> first run from src
        self.graph.get_node(id1).tag = 2 # maybe 2
        list1.append(self.graph.get_node(id1))
        myList.append(self.graph.get_node(id1))

        if str(id1) in self.com_nod:
            self.com_nod.remove(str(id1))
        for n in self.graph.get_node(id1).src:
            temp = self.graph.get_node(n)
            temp.tag = 1
            list1.append(temp)

            queue.put(temp)

        while not queue.empty():
            n = queue.get()

            for x in n.src:
                temp2 = self.graph.get_node(x)
                if temp2.tag == 0:
                    temp2.tag = 1
                    list1.append(temp2)
                    queue.put(temp2)

        for n in self.graph.get_node(id1).dest:
            temp = self.graph.get_node(n)
            if temp.tag == 1:
                queue.put(temp)

        # mark src node as visited
        while not queue.empty():
            n = queue.get()
            if n.tag == 1:
                n.tag = 2
                list1.append(n)
                myList.append(n)
                if str(n.id) in self.com_nod:
                    self.com_nod.remove(str(n.id))
                for x in n.dest:
                    temp2 = self.graph.get_node(x)
                    queue.put(temp2)

        return myList,list1

    """ 
    Finds all the Strongly Connected Component(SCC) in the graph.
    @return: The list all SCC
    """

    def connected_components(self) -> List[list]:

        if self.graph is None:
            return []
        self.com_nod= list(self.graph.nodes.keys())
        listAns = []
        while self.com_nod:
            listAns.append(self.connected_component(self.com_nod[0]))

        return listAns

    """
    Plots the graph.
    If the nodes have a position, the nodes will be placed there.
    Otherwise, they will be placed in a random but elegant manner.
    @return: None
    """

    def plot_graph(self) -> None:
        if self.graph is None:
            return

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
            w = self.graph.edges.get(e)
            # draws the arrow pointing in the edge direction --> (dest)
            pos = self.graph.get_node(dest).pos

            xlist.append(pos[0])
            ylist.append(pos[1])
            plt.plot(xlist, ylist, markersize=10, marker='*', color='gray')

            # draw the edge coming out from the src node to dest node
            pos = self.graph.get_node(src).pos

            xlist.append(pos[0])
            ylist.append(pos[1])
            plt.plot(xlist, ylist, '-', label=(str(e) + " :" + str(w)))
            # plt.plot(xlist, ylist, '-')

            # clears the x and y lists for the next edge
            xlist.clear()
            ylist.clear()

        plt.legend(loc='upper left')

        plt.title("My Graph")

        for key in self.graph.nodes:
            ver = self.graph.nodes.get(key)
            if not ver.info:
                ver.pos = None

        plt.show()
