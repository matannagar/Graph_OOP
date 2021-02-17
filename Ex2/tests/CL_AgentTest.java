import api.DWGraph_DS;
import api.NodeData;
import api.directed_weighted_graph;
import api.node_data;
import gameClient.CL_Agent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CL_AgentTest {
    private String pokeballs= "{\"Agent\":{\"id\":0,\"value\":0.0,\"src\":4,\"dest\":-1,\"speed\":1.0,\"pos\":\"35.201403996715264,32.105969032285614,0.0\"}}";
    private CL_Agent ag;
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
    void update() {
        ag=new CL_Agent(g1,1);
        ag.update(pokeballs);
        assertEquals(pokeballs,ag.toString());
    }

    @Test
    void getSrcNode() {
        ag=new CL_Agent(g1,1);
        assertEquals(1,ag.getSrcNode());
        ag.update(pokeballs);
        assertEquals(4,ag.getSrcNode());
    }

    @Test
    void setNextNode() {
        ag=new CL_Agent(g1,1);
        ag.update(pokeballs);
        assertEquals(g1.getEdge(4,2)!=null,ag.setNextNode(2));
        assertEquals(g1.getEdge(4,1)!=null,ag.setNextNode(1));
    }

    @Test
    void setCurrNode() {
        ag=new CL_Agent(g1,1);
        ag.setCurrNode(2);
        assertEquals(2,ag.getSrcNode());
    }

    @Test
    void getID() {
        ag=new CL_Agent(g1, 1);
        ag.update(pokeballs);
        assertEquals(0,ag.getID());
    }

    @Test
    void getLocation() {
        ag=new CL_Agent(g1, 1);
        ag.update(pokeballs);
        assertEquals("35.201403996715264,32.105969032285614,0.0",ag.getLocation().toString());

    }

    @Test
    void getValue() {
        ag=new CL_Agent(g1, 1);
        ag.update(pokeballs);
        assertEquals(0,ag.getValue());
    }

    @Test
    void getNextNode() {
        ag=new CL_Agent(g1,1);
        ag.update(pokeballs);
        assertEquals(-1,ag.getNextNode());
    }

    @Test
    void getSpeed() {
        ag=new CL_Agent(g1,1);
        ag.update(pokeballs);
        assertEquals(1,ag.getSpeed());
    }
}