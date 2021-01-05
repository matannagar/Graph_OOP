from DiGraph import DiGraph
from GraphAlgo import GraphAlgo
import time
import pylab as plt
import networkx as nx

if __name__ == '__main__':
    import random

    from DiGraph import DiGraph
    from GraphAlgo import GraphAlgo
    import time


    def main():
        # create the graphAlgo object
        graphAlgo = GraphAlgo()

        # measure load
        star_time = time.time()

        # file_name = "../src/100_25"
        # file_name = "../src/1K_250"
        # file_name = "../src/10K_500"
        file_name = "../src/1M_1K"
        # file_name = "../src/100K_1K"
        # file_name = "../src/1M"


        # graphAlgo.load_from_json(file_name)
        # end_time = time.time()
        # print(file_name, "py_load_time:", end_time - star_time)
        #
        # # save the AlgoGraph into a variable
        # graph = graphAlgo.get_graph()
        # print("nodes:", graph.v_size(), "edges:", graph.e_size())
        # # # ***networkX tests***
        # # print("************NETWORKX GRAPH TESTS********************")
        # # # load time test
        # star_time = time.time()
        # # create DiGraph from the networkx library
        # g = nx.DiGraph()
        # # add nodes and edges to the graph
        # for n in range(graph.v_size()):
        #     g.add_node(n)
        # for e in graph.edges:
        #     w = graph.edges.get(e)
        #     edge = str(e).split('->')
        #     src = int(edge[0])
        #     dest = int(edge[1])
        #     g.add_edge(src, dest, weight=w)
        # end_time = time.time()
        # print("nx_load_graph_time:", end_time - star_time)
        # #
        # print("Nodes No.:", graph.v_size())
        # print("Edges No.:", graph.e_size())
        # #
        # # # test SCC algorithms
        # star_time = time.time()
        # comp= nx.strongly_connected_components(g)
        # sccs = list(comp)
        # sccs.reverse()
        # end_time = time.time()
        # print("nx_connected_components_time:", end_time - star_time, " seconds")
        # #
        # # test shortest path algorithm
        # star_time = time.time()
        # j = 50
        # for i in range(50):
        #     nx.shortest_path_length(g, i, j)
        #     j = j - 1
        #     nx.shortest_path(g, i, 50 - j)
        # end_time = time.time()
        # print("nx_50_shortest_path_time:", end_time - star_time)

        # graphAlgo.plot_graph()
        # measure specific node SCC
        # star_time = time.time()
        # for i in range(50):
        #  graphAlgo.connected_component(i)
        # end_time = time.time()
        # print("py_1/10 Connected_Component_Time:", end_time - star_time, "seconds")

        # # measure all SCC
        star_time = time.time()
        sccs = list(graphAlgo.connected_components())
        # print(sccs)
        end_time = time.time()
        print("py_connected_components_time:", end_time - star_time)
        #
        # # measure shortest Path and dist between random nodes
        # star_time = time.time()
        # j = 50
        # for i in range(50):
        #     graphAlgo.shortest_path(i, j)
        #     j = j - 1
        # end_time = time.time()
        # print("py_50_shortest_path_time: ", end_time - star_time)


    if __name__ == '__main__':
        main()
