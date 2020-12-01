package api;

import gameClient.CL_Agent;
import gameClient.CL_Pokemon;

import java.util.ArrayList;
import java.util.Date;

public class GameService implements game_service{
    private directed_weighted_graph g;
    private ArrayList<CL_Agent> agents;
    private ArrayList<CL_Pokemon> pokemons;
    private int status=-1;
    private long definedTime;

    public GameService(directed_weighted_graph g, ArrayList<CL_Agent> agents, ArrayList<CL_Pokemon> pokemons, long definedTime){
        this.g=g;
        this.agents=agents;
        this.pokemons=pokemons;
        this.definedTime=definedTime;
    }

    @Override
    public String getGraph() {
        return g.toString();
    }

    @Override
    public String getPokemons() {
        return pokemons.toString();
    }

    @Override
    public String getAgents() {
        return agents.toString();
    }

    @Override
    public boolean addAgent(int start_node) {
        if(g.getNode(start_node)==null) return false;
        CL_Agent a= new CL_Agent(g,start_node);
        return true;
    }

    @Override
    public long startGame() {
        /////////////////////////////////////why wouldnt the game start ?
        status=1;
        return new Date().getTime();
    }

    @Override
    public boolean isRunning() {
        if(status==0) return false;
        return true;
    }

    @Override
    public long stopGame() {
        status=0;
        return new Date().getTime();
    }

    @Override
    public long chooseNextEdge(int id, int next_node) {
        return 0;
    }

    @Override
    public long timeToEnd() {
        return definedTime-new Date().getTime();
    }

    @Override
    public String move() {
        ////////////////////////////
        return null;
    }

    @Override
    public boolean login(long id) {
        ////////////////////////////
        return false;
    }
}
