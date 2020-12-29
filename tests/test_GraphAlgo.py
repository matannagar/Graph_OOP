from typing import List
from unittest import TestCase

from GraphAlgo import GraphAlgo
from DiGraph import DiGraph


def generate_graph1() -> DiGraph():
    graph = DiGraph()
    for i in range(6):
        graph.add_node(i)

    graph.add_edge(3, 1, 3.1)
    graph.add_edge(4, 3, 4.3)
    graph.add_edge(4, 5, 4.5)
    graph.add_edge(2, 4, 2.4)
    graph.add_edge(5, 2, 5.2)
    graph.add_edge(2, 5, 2.5)

    return graph


def generate_graph2() -> DiGraph():
    graph = DiGraph()
    for x in range(7):
        graph.add_node(x)

    graph.add_edge(1, 2, 2.5)
    graph.add_edge(1, 3, 1.7)
    graph.add_edge(2, 3, 0.3)
    graph.add_edge(3, 2, 5.6)
    graph.add_edge(5, 1, 3.2)
    graph.add_edge(5, 0, 9.8)
    graph.add_edge(0, 6, 3.8)

    return graph


class TestGraphAlgo(TestCase):
    def test_load_from_json(self):
        self.fail()

    def test_save_to_json(self):
        self.fail()

    def test_shortest_path(self):
        self.fail()

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

        list1= [[graph.get_node(0)],[graph.get_node(1)],algo.connected_component(2), [graph.get_node(3)]]
        self.assertEqual(list1, algo.connected_components())

    def test_plot_graph(self):
        self.fail()
