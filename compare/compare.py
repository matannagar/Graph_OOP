import random
from GraphAlgo import GraphAlgo
import time


def main():
    graphAlgo = GraphAlgo()

    star_time = time.time()
    graphAlgo.load_from_json("10_2V")
    end_time = time.time()
    print("load 10_2V:", end_time - star_time)
    rand=random.randrange(0, graphAlgo.get_graph().v_size())
    star_time = time.time()
    graphAlgo.connected_component(rand)
    end_time = time.time()
    print("connected_component",rand,":", end_time - star_time)
    star_time = time.time()
    graphAlgo.connected_components()
    end_time = time.time()
    print("connected_components:", end_time - star_time)
    rand1 = random.randrange(0, graphAlgo.get_graph().v_size())
    rand2 = random.randrange(0, graphAlgo.get_graph().v_size())
    star_time = time.time()
    graphAlgo.shortest_path(rand1,rand2)
    end_time = time.time()
    print("shortest_path", rand1, rand2, ":", end_time - star_time)

if __name__ == '__main__':
    main()
