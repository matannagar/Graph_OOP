import sys
from queue import Queue
import heapq
from typing import List

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

    def load_from_json(self, file_name: str) -> bool:
        pass

    def save_to_json(self, file_name: str) -> bool:
        pass

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
        while heap:
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

                # if current.idNode==id2: dist=vertex.get(str(id2)).tag
        dest = vertex.get(str(id2))  # NodeTemp
        print(type(dest))
        if dest.tag == sys.maxsize:
            dest.tag = -1

        listShort = []
        if dest.tag == -1:
            listShort = None
        else:
            # listShort.append(self.graph.get_node(dest.idNode))
            print(dest.idNode)
            listShort.append(dest.idNode)
            curr = dest
            while curr.parentId != -1:
                parent = curr.parentId
                curr = vertex.get(str(parent))
                # listShort.append(self.graph.get_node(curr.idNode))
                listShort.append(curr.idNode)
                print(type(curr.idNode))
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
        pass


def main():
    graphAlgo = GraphAlgo()

    graph = DiGraph()
    for i in range(6):
        graph.add_node(i)

    graph.add_edge(3, 1, 3.1)
    graph.add_edge(4, 3, 4.3)
    graph.add_edge(4, 5, 4.5)
    graph.add_edge(2, 4, 2.4)
    graph.add_edge(5, 2, 5.2)
    graph.add_edge(2, 5, 2.5)

    graphAlgo.__init__(graph)
    print(graphAlgo.connected_component(2))
    print(graphAlgo.connected_components())

    print(graphAlgo.shortest_path(2, 3))

    g3 = DiGraph()
    for i in range(6):
        g3.add_node(i)
    g3.add_edge(0, 1, 5)
    g3.add_edge(0, 2, 1)
    g3.add_edge(1, 2, 2)
    g3.add_edge(2, 1, 3)
    g3.add_edge(1, 3, 3)
    g3.add_edge(3, 2, 3)
    g3.add_edge(2, 4, 12)
    g3.add_edge(4, 5, 1)
    g3.add_edge(3, 5, 6)
    g3.add_edge(3, 4, 2)
    g3.add_edge(1, 4, 20)

    graphAlgo.__init__(g3)
    print(graphAlgo.shortest_path(0, 5))
    print(graphAlgo.shortest_path(0, 2))
    g3.remove_edge(1, 3)
    print(graphAlgo.shortest_path(0, 3))


if __name__ == '__main__':
    main()
