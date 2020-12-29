from queue import Queue
from typing import List

from DiGraph import DiGraph
from GraphAlgoInterface import GraphAlgoInterface


class GraphAlgo(GraphAlgoInterface):

    def __init__(self, graph: DiGraph = None):
        self.graph = graph

    def load_from_json(self, file_name: str) -> bool:
        pass

    def save_to_json(self, file_name: str) -> bool:
        pass

    def shortest_path(self, id1: int, id2: int) -> (float, list):
        pass

    def connected_component(self, id1: int) -> list:
        if self.graph is None:
            return None
        # check if we should return None or empty list
        if str(id1) not in self.graph.nodes.keys():
            return []
        list1 = self.bfs(id1, 0)
        list2 = self.bfs(id1, 1)
        return set(list1) & set(list2)

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
        tempList = [List]
        for n in self.graph.nodes:
            tempList.append(self.connected_component(n))
        list1 = [List]
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


if __name__ == '__main__':
    main()
