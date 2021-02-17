import api.DWGraph_DS;
import api.NodeData;
import api.directed_weighted_graph;
import api.node_data;
import gameClient.Arena;
import gameClient.CL_Agent;
import gameClient.CL_Pokemon;
import gameClient.util.Point3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArenaTest {

    private Arena ar= new Arena();
    private directed_weighted_graph g1;

    @BeforeEach
    void generateGraph() {
        g1 = new DWGraph_DS();
        for (int i = 1; i < 5; i++){
            node_data n= new NodeData(i);
            g1.addNode(n);
        }
        g1.connect(1, 2, 0.1);
        g1.connect(2,1,3.7);
        g1.connect(3, 4, 1.2);
        g1.connect(4, 1, 2.3);
        g1.connect(3, 1, 3.4);
    }

    @Test
    void setAndgetPokemons() {
        List<CL_Pokemon> l= new ArrayList<>();
        for(int i=0; i<4; i++){
            int x=i, y=i+1, z=0;
            CL_Pokemon pok= new CL_Pokemon(new Point3D(x,y,z),1, i+3, g1.getEdge(i+1,i));
            l.add(pok);
        }
        ar.setPokemons(l);
        assertEquals(l, ar.getPokemons());
    }

    @Test
    void setAndgetAgents() {
        List<CL_Agent> l= new ArrayList<>();
        for(int i=1; i<4; i++){
            CL_Agent ag= new CL_Agent(g1,i);
            l.add(ag);
        }
        ar.setAgents(l);
        assertEquals(l, ar.getAgents());
    }

    @Test
    void setAndgetGraph() {
        ar.setGraph(g1);
        assertEquals(g1, ar.getGraph());
    }

}