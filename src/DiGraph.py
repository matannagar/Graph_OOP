from GraphInterface import GraphInterface


class Node:

    def __init__(self, key: int = None, src=None, dest=None, pos: tuple = None, tag: int = 0):
        if dest is None:
            dest = {}
        if src is None:
            src = {}
        self.id = key
        self.src = src
        self.dest = dest
        self.pos = pos
        self.tag = tag
        self.info = True
        if pos is None:
            self.info = False

    def __str__(self):
        return "Node: {:d}".format(self.id)

    def __repr__(self):
        return str(self)

    def __eq__(self, other):
        return self.id is other.id

    def __hash__(self):
        return hash(self.id)


class DiGraph(GraphInterface):
    def __init__(self):
        self.mc = 0
        self.nodes = {}
        self.edges = {}

    def v_size(self) -> int:
        return len(self.nodes)

    def e_size(self) -> int:
        return len(self.edges)

    def get_mc(self) -> int:
        return self.mc

    def add_edge(self, id1: int, id2: int, weight: float) -> bool:
        if not (str(id1) in self.nodes and str(id2) in self.nodes): return False
        if id1 == id2:
            return False
        if weight < 0:
            return False
        if str(id1) + '->' + str(id2) in self.edges: return False

        n1 = self.nodes.get(str(id1))
        n1.src[str(id2)] = weight
        n2 = self.nodes.get(str(id2))
        n2.dest[str(id1)] = weight

        self.edges[str(id1) + '->' + str(id2)] = weight
        self.mc = self.mc + 1
        return True

    def add_node(self, node_id: int, pos: tuple = None) -> bool:
        if str(node_id) in self.nodes: return False
        node = Node(key=node_id, pos=pos)
        self.nodes[str(node_id)] = node
        self.mc = self.mc + 1
        return True

    def remove_node(self, node_id: int) -> bool:
        if str(node_id) not in self.nodes:
            return False

        iterator = iter(self.get_src(node_id).keys())
        while True:
            try:
                n = next(iterator)
                self.remove_edge(node_id1=node_id, node_id2=n)
                iterator = iter(self.get_src(node_id).keys())
            except StopIteration:
                break

        iterator = iter(self.get_dest(node_id).keys())
        while True:
            try:
                n = next(iterator)
                self.remove_edge(node_id1=n, node_id2=node_id)
                iterator = iter(self.get_dest(node_id).keys())
            except StopIteration:
                break

        self.mc = self.mc + 1
        self.nodes.get(str(node_id)).src.clean()
        del self.nodes[str(node_id)]

        return True

    def get_src(self, id1: int) -> dict:
        return self.nodes.get(str(id1)).src

    def get_dest(self, id1: int) -> dict:
        return self.nodes.get(str(id1)).dest

    def remove_edge(self, node_id1: int, node_id2: int) -> bool:
        if str(node_id1) not in self.nodes or str(node_id2) not in self.nodes: return False
        s = str(node_id1) + '->' + str(node_id2)
        if s not in self.edges: return False

        n1 = self.nodes.get(str(node_id1))
        n2 = self.nodes.get(str(node_id2))
        del n1.src[str(node_id2)]
        del n2.dest[str(node_id1)]
        del self.edges[s]

        self.mc = self.mc + 1
        return True

    def get_node(self, id: int) -> Node:
        return self.nodes.get(str(id))

    def __str__(self):
        s = "Graph: |V|={:d} , |E|={:d}\n\tNodes: ".format(self.v_size(), self.e_size())
        for key in self.nodes.keys():
            s += '{} '.format(key)
        s += "\n\tEdges: "
        for key, weight in self.edges.items():
            s += ('{} : {} \t '.format(key, weight))
        return s

    def __repr__(self):
        return str(self)

    def get_all_v(self) -> dict:
        return self.nodes

    def all_in_edges_of_node(self, id1: int) -> dict:
        return self.get_all_v().get(str(id1)).dest

    def all_out_edges_of_node(self, id1: int) -> dict:
        return self.get_all_v().get(str(id1)).src
