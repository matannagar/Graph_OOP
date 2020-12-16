import api.*;
import gameClient.CL_Pokemon;
import gameClient.util.Point3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CL_PokemonTest {
    private directed_weighted_graph g1;
    private CL_Pokemon pok;

    @BeforeEach
    void generateGraph() {
        dw_graph_algorithms algo = new DWGraph_Algo();
        algo.load("data/A0");
        g1 = algo.getGraph();
    }

    @Test
    void get_edge() {
        Point3D p = new Point3D(g1.getNode(9).getLocation().x(),g1.getNode(9).getLocation().y(),0);
        pok = new CL_Pokemon(p,-1,13, g1.getEdge(9,8));
        assertEquals(g1.getEdge(9,8), pok.get_edge());

    }

    @Test
    void set_edge() {
        Point3D p = new Point3D(g1.getNode(9).getLocation().x(),g1.getNode(9).getLocation().y(),0);
        pok = new CL_Pokemon(p,-1,13, g1.getEdge(9,8));
        pok.set_edge(g1.getEdge(0,1));
        assertEquals(g1.getEdge(0,1), pok.get_edge());
    }

    @Test
    void getLocation() {
        Point3D p = new Point3D(g1.getNode(9).getLocation().x(),g1.getNode(9).getLocation().y(),0);
        pok = new CL_Pokemon(p,-1,13, g1.getEdge(9,8));
        assertEquals(p, pok.getLocation());
    }

    @Test
    void getType() {
        Point3D p = new Point3D(g1.getNode(9).getLocation().x(),g1.getNode(9).getLocation().y(),0);
        pok = new CL_Pokemon(p,-1,13, g1.getEdge(9,8));
        assertEquals(-1, pok.getType());
    }

    @Test
    void getValue() {
        Point3D p = new Point3D(g1.getNode(9).getLocation().x(),g1.getNode(9).getLocation().y(),0);
        pok = new CL_Pokemon(p,-1,13, g1.getEdge(9,8));
        assertEquals(13, pok.getValue());
    }
}