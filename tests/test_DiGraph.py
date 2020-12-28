import unittest

from src.DiGraph import DiGraph


class TestDiGraph(unittest.TestCase):
    # g = None
    # def setUp(self): TestDiGraph.g = self.graph_creator()

    def test_v_size(self):
        graph = self.graph_creator()
        self.assertEquals(7,graph.v_size(),"The number of nodes is incorrect")

    def test_e_size(self):
        self.fail()

    def test_get_mc(self):
        self.fail()

    def test_add_edge(self):
        self.fail()

    def test_add_node(self):
        self.fail()

    def test_remove_node(self):
        self.fail()

    def test_get_src(self):
        self.fail()

    def test_get_dest(self):
        self.fail()

    def test_remove_edge(self):
        self.fail()

    def graph_creator(self)->DiGraph:
        graph = DiGraph()
        for x in range (7):
            graph.add_node(x)

        graph.add_edge(1,2,2.5)
        graph.add_edge(1, 3, 1.7)
        graph.add_edge(2, 3, 0.3)
        graph.add_edge(3, 2, 5.6)
        graph.add_edge(5, 1, 3.2)
        graph.add_edge(5, 0, 9.8)
        graph.add_edge(0, 6, 3.8)

        return graph

