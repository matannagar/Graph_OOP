import unittest

from DiGraph import DiGraph


def graph_creator() -> DiGraph:
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


class TestDiGraph(unittest.TestCase):

    def test_v_size(self):
        graph = graph_creator()
        self.assertEqual(7, graph.v_size(), "The number of nodes is incorrect")
        graph.add_node(41)
        self.assertEqual(8, graph.v_size(), "Added a new node")
        graph.add_node(41)
        self.assertEqual(8, graph.v_size(), "No node was supposed to be added")
        graph.remove_node(4)
        self.assertEqual(7, graph.v_size(), "Removed a node")
        graph.remove_edge(0,6)
        self.assertEqual(7,graph.v_size(), "Removed an edge")

    def test_e_size(self):
        graph = graph_creator()
        self.assertEqual(7, graph.e_size(), "The number of edges is incorrect")
        graph.add_edge(4, 0, 1)
        self.assertEqual(8, graph.e_size(), "Added an edge")
        graph.add_edge(4, 0, 12)
        self.assertEqual(8, graph.e_size(), "No edges were supposed to be added")
        graph.add_edge(4, 16, 1)
        self.assertEqual(8, graph.e_size(), "No edges were supposed to be added")
        graph.add_edge(16, 4, 1)
        self.assertEqual(8, graph.e_size(), "No edges were supposed to be added")
        graph.add_edge(10, 16, 1)
        self.assertEqual(8, graph.e_size(), "No edges were supposed to be added")
        graph.add_edge(1, 1, 1)
        self.assertEqual(8, graph.e_size(), "No edges were supposed to be added")
        graph.remove_edge(4,0)
        self.assertEqual(7,graph.e_size(),"Removed an edge")

    def test_get_mc(self):
        graph = graph_creator()
        self.assertEqual(14, graph.get_mc(), "The number of MC is incorrect")
        graph.add_node(0)
        self.assertEqual(14, graph.get_mc(), "No change in MC after adding existing node")
        graph.add_node(7)
        self.assertEqual(15, graph.get_mc(), "The number of MC is incorrect: added a new node")
        graph.add_edge(1, 4, 0.5)
        self.assertEqual(16, graph.get_mc(), "The number of MC is incorrect: added a new edge(0->4)")
        graph.add_edge(1, 4, 17)
        self.assertEqual(16, graph.get_mc(), "The number of MC is incorrect: added an existing edge(0->4)")
        graph.remove_node(4)
        self.assertEqual(18, graph.get_mc(), "The number of MC is incorrect: deleted node 4")
        graph.remove_edge(1, 2)
        self.assertEqual(19, graph.get_mc(), "The number of MC is incorrect: deleted edge (1,2)")
        graph.remove_node(1)
        self.assertEqual(22, graph.get_mc(), "The number of MC is incorrect: deleted node 1")

    def test_add_edge(self):
        graph = graph_creator()

        self.assertEqual(True, graph.add_edge(4, 0, 1), "Added an edge")
        self.assertEqual(False, graph.add_edge(4, 0, 12), "No edges were supposed to be added")
        self.assertEqual(False, graph.add_edge(4, 16, 1), "No edges were supposed to be added")
        self.assertEqual(False, graph.add_edge(16, 4, 1), "No edges were supposed to be added")
        self.assertEqual(False, graph.add_edge(10, 16, 1), "No edges were supposed to be added")
        self.assertEqual(False, graph.add_edge(1, 1, 1), "No edges were supposed to be added")

    def test_add_node(self):
        graph = graph_creator()

        self.assertEqual(True, graph.add_node(41), "Added a new node")
        self.assertEqual(False, graph.add_node(41), "No node was supposed to be added")
        self.assertEqual(False, graph.add_node(4), "No node was supposed to be added")

    def test_remove_node(self):
        graph = graph_creator()

        self.assertEqual(False, graph.remove_node(31), "No node was supposed to be removed")
        self.assertEqual(True, graph.remove_node(1), "Removed node 1")
        self.assertEqual(False, graph.remove_node(1), "Was already removed")

    def test_remove_edge(self):
        graph = graph_creator()

        self.assertEqual(False, graph.remove_edge(4, 0), "No such edge")
        graph.add_edge(4, 0, 1)
        self.assertEqual(True, graph.remove_edge(4, 0), "remove an edge")
        self.assertEqual(False, graph.remove_edge(4, 16), "No edges were supposed to be removed")
        self.assertEqual(False, graph.remove_edge(1, 5), "No edges were supposed to be removed")
        self.assertEqual(True, graph.remove_edge(5, 1), "An edge was supposed to be removed")
        self.assertEqual(False, graph.remove_edge(10, 16), "No edges were supposed to be removed")
        self.assertEqual(False, graph.remove_edge(1, 1), "No edges were supposed to be removed")
