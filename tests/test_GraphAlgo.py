from typing import List
from unittest import TestCase

from GraphAlgo import GraphAlgo
from DiGraph import DiGraph

def main():
    # graphAlgo = GraphAlgo()
    #
    # graph = DiGraph()
    # for i in range(6):
    #     graph.add_node(i)
    #
    # graph.add_edge(3, 1, 3.1)
    # graph.add_edge(4, 3, 4.3)
    # graph.add_edge(4, 5, 4.5)
    # graph.add_edge(2, 4, 2.4)
    # graph.add_edge(5, 2, 5.2)
    # graph.add_edge(2, 5, 2.5)
    #
    # graphAlgo.__init__(graph)
    # print(graphAlgo.connected_component(2))
    # print(graphAlgo.connected_components())
    #
    # print(graphAlgo.shortest_path(2, 3))
    #
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
    #
    # graphAlgo.__init__(g3)
    # print(graphAlgo.shortest_path(0, 5))
    # print(graphAlgo.shortest_path(0, 2))
    # g3.remove_edge(1, 3)
    # print(graphAlgo.shortest_path(0, 3))
    #
    graphAlgo = GraphAlgo()
    # # graphAlgo.load_from_json("A0.json")
    # graphAlgo.__init__(g3)
    # graphAlgo.save_to_json("myGraph")
    #
    # g3.remove_node(2)
    #
    # graphAlgo.load_from_json("myGraph")
    #
    # graphAlgo.graph.remove_node(2)
    #
    # graphAlgo.load_from_json("A0.json")
    # graphAlgo.plot_graph()
    #
    gr = DiGraph()
    gr.add_node(node_id=0, pos=(7, 8, 0))
    gr.add_node(node_id=1, pos=(3, 5, 0))
    gr.add_node(node_id=2, pos=(2, 5, 0))
    gr.add_edge(0, 1, 2)
    gr.add_node(node_id=3)
    gr.add_edge(3, 2, 4)
    #
    graphAlgo.__init__(gr)
    graphAlgo.plot_graph()

if __name__ == '__main__':
    main()

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
