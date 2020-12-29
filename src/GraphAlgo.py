import json
import sys
from queue import Queue
import heapq
import random
from typing import List
import matplotlib.pyplot as plt
from DiGraph import DiGraph
from GraphAlgoInterface import GraphAlgoInterface


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


class GraphAlgo(GraphAlgoInterface):

    def __init__(self, graph: DiGraph = None):
        self.graph = graph

    def get_graph(self) -> DiGraph:
        return self.graph

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

        except FileExistsError:
            print("Graph was not loaded successfully")
            return False

        return True


    def save_to_json(self, file_name: str) -> bool:
        if self.graph is None:
            return False

        nodes = []
        for n in self.graph.nodes:
            id = self.graph.nodes.get(n).id
            pos = self.graph.nodes.get(n).pos
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
