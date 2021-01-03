import networkx as nx
import random
from GraphAlgo import GraphAlgo
import time


def main():
    algo = GraphAlgo()
    algo.load_from_json("10_2V")
    graph = algo.get_graph()

    g = nx.DiGraph()
    g.add_nodes_from(graph.nodes)
    for e in graph.edges:
        w = graph.edges.get(e)
        edge = str(e).split('->')
        src = int(edge[0])
        dest = int(edge[1])
        g.add_edge(src,dest, weight=w)
    print(g.nodes())
    print(g.edges)

    print(nx.is_strongly_connected(g))
    print(nx.number_strongly_connected_components(g))
    print(nx.strongly_connected_components(g))



if __name__ == '__main__':
    main()
