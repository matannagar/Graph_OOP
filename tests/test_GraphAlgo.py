from typing import List
from unittest import TestCase

from GraphAlgo import GraphAlgo
from DiGraph import DiGraph


def main():
    # graphAlgo.__init__(g3)
    # print(graphAlgo.shortest_path(0, 5))
    # print(graphAlgo.shortest_path(0, 2))
    # g3.remove_edge(1, 3)
    # print(graphAlgo.shortest_path(0, 3))
    #
    # graphAlgo = GraphAlgo()
    # # # graphAlgo.load_from_json("A0.json")
    # # graphAlgo.__init__(g3)
    # # graphAlgo.save_to_json("myGraph")
    # #
    # # g3.remove_node(2)
    # #
    # # graphAlgo.load_from_json("myGraph")
    # #
    # # graphAlgo.graph.remove_node(2)
    # #
    # # graphAlgo.load_from_json("A0.json")
    # # graphAlgo.plot_graph()
    # #
    #
    # #
    # graphAlgo.__init__(gr)
    # graphAlgo.plot_graph()
    print("")


if __name__ == '__main__':
    main()


def generate_graph1() -> DiGraph():
    g1 = DiGraph()
    for i in range(6):
        g1.add_node(i)

    g1.add_edge(3, 1, 3.1)
    g1.add_edge(4, 3, 4.3)
    g1.add_edge(4, 5, 4.5)
    g1.add_edge(2, 4, 2.4)
    g1.add_edge(5, 2, 5.2)
    g1.add_edge(2, 5, 2.5)

    return g1


def generate_graph2() -> DiGraph():
    g2 = DiGraph()
    for x in range(7):
        g2.add_node(x)

    g2.add_edge(1, 2, 0.5)
    g2.add_edge(1, 3, 1.7)
    g2.add_edge(2, 3, 0.3)
    g2.add_edge(3, 2, 5.6)
    g2.add_edge(5, 1, 3.2)
    g2.add_edge(5, 0, 9.8)
    g2.add_edge(0, 6, 3.8)

    return g2


'''graph with pos of nodes'''


def generate_graph3() -> DiGraph:
    g3 = DiGraph()
    for i in range(6):
        g3.add_node(node_id=i, pos=(i, i + 1, 0))
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

    return g3


'''graph with partial pos of nodes'''


def generate_graph4() -> DiGraph:
    g4 = DiGraph()
    g4.add_node(node_id=0, pos=(7, 8, 0))
    g4.add_node(node_id=1, pos=(3, 5, 0))
    g4.add_node(node_id=2, pos=(2, 5, 0))
    g4.add_edge(0, 1, 2)
    g4.add_node(node_id=3)
    g4.add_edge(3, 2, 4)

    return g4


class TestGraphAlgo(TestCase):
    def test_save_load_from_json(self):
        algo = GraphAlgo()
        graph = generate_graph1()
        self.assertEqual(False, algo.save_to_json("myGraph"), "graph is None")

        '''graph without pos of nodes'''
        algo.__init__(graph)
        self.assertEqual(True, algo.save_to_json("myGraph"))
        self.assertEqual(True, algo.load_from_json("myGraph"))
        self.assertEqual(graph, algo.get_graph())

        '''graph with pos of nodes'''
        graph = generate_graph3()
        algo.__init__(graph)
        self.assertEqual(True, algo.save_to_json("myGraph"))
        self.assertEqual(True, algo.load_from_json("myGraph"))
        self.assertEqual(graph, algo.get_graph())

        '''graph with partial pos of nodes'''
        graph = generate_graph4()
        algo.__init__(graph)
        self.assertEqual(True, algo.save_to_json("myGraph"))
        self.assertEqual(True, algo.load_from_json("myGraph"))
        self.assertEqual(graph, algo.get_graph())

    def test_shortest_path(self):
        algo = GraphAlgo()
        graph = generate_graph2()
        algo.__init__(graph)
        self.assertEqual((0.8, [1, 2, 3]), algo.shortest_path(1, 3))
        self.assertEqual((-1, None), algo.shortest_path(1, 5), 'No edge between 1,5')

        graph = generate_graph3()
        algo.__init__(graph)
        l = [0, 2, 1, 3, 4, 5]
        self.assertEqual((10, l), algo.shortest_path(0, 5))

        self.assertEqual((1, [0, 2]), algo.shortest_path(0, 2))
        graph.remove_edge(1, 3)
        self.assertEqual((-1, None), algo.shortest_path(0, 3), "Removed this edge, and no path between this nodes")
        self.assertEqual((0, [3]), algo.shortest_path(3, 3), "No path between a node and itself")
        self.assertEqual(None, algo.shortest_path(13, 3), "Node 13 doesn't exist in the graph")

        graph = None
        algo.__init__(graph)
        self.assertEqual(None, algo.shortest_path(0, 3))

    def test_connected_component(self):
        algo = GraphAlgo()
        graph = generate_graph1()
        algo.__init__(graph)

        list1 = [graph.get_node(2), graph.get_node(4), graph.get_node(5)]
        self.assertEqual(list1, algo.connected_component(2), "Should return [2,4,5]")

        self.assertEqual(algo.connected_component(5), algo.connected_component(2),
                         "2 and 5 have the same connected components")

        for x in range(1):
            list1 = [graph.get_node(x)]
            self.assertEqual(list1, algo.connected_component(x), "Have no other connected components")

        graph = generate_graph2()
        algo.__init__(graph)
        self.assertEqual([graph.get_node(1)], algo.connected_component(1), "Have no other connected components")
        self.assertEqual([graph.get_node(2), graph.get_node(3)], algo.connected_component(2), "Should return [2,3]")

        graph.add_edge(2, 5, 1.2)
        list1 = [graph.get_node(1), graph.get_node(2), graph.get_node(3), graph.get_node(5)]
        self.assertEqual(list1, algo.connected_component(2), "Should return [1,2,3,5]")
        self.assertEqual(list1, algo.connected_component(5), "Should return [1,2,3,5]")
        graph.add_edge(6, 5, 1.3)

        list1 = [graph.get_node(0), graph.get_node(1), graph.get_node(2), graph.get_node(3), graph.get_node(5),
                 graph.get_node(6)]
        self.assertEqual(list1, algo.connected_component(2), "Should return [0,1,2,3,5,6]")
        self.assertEqual(list1, algo.connected_component(0), "Should return [0,1,2,3,5,6]")

    def test_connected_components(self):
        algo = GraphAlgo()
        graph = generate_graph1()
        algo.__init__(graph)

        list1 = [[graph.get_node(0)], [graph.get_node(1)], algo.connected_component(2), [graph.get_node(3)]]
        self.assertEqual(list1, algo.connected_components())

        graph = generate_graph2()
        algo.__init__(graph)
        list1 = [[graph.get_node(0)], [graph.get_node(1)], algo.connected_component(2), [graph.get_node(4)],
                 [graph.get_node(5)], [graph.get_node(6)]]
        self.assertEqual(list1, algo.connected_components())
        graph.add_edge(2, 1, 3.4)
        list1 = [[graph.get_node(0)], algo.connected_component(2), [graph.get_node(4)], [graph.get_node(5)],
                 [graph.get_node(6)]]

    def test_plot_graph(self):
        algo = GraphAlgo()
        # graph=None
        algo.plot_graph()
        '''graph without pos of nodes'''
        graph = generate_graph2()
        algo.__init__(graph)
        algo.plot_graph()

        '''graph with pos of nodes'''
        graph = generate_graph3()
        algo.__init__(graph)
        algo.plot_graph()

        '''graph with partial pos of nodes'''
        graph = generate_graph4()
        algo.__init__(graph)
        algo.plot_graph()

