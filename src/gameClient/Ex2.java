package gameClient;

import api.*;
import gameClient.util.myFrame;
import Server.Game_Server_Ex2;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.List;

/**
 * Ex2 class is responsible for initiating and starting the game.
 */

public class Ex2 implements Runnable {
    public static myFrame _win;
    private static Arena _ar;
    public static int ID = -1, numGame = -1;
    private static ArrayList<Integer> pair = new ArrayList<>();
    private static HashMap<Integer, Integer> edges = new HashMap<>();
    public static Thread client = new Thread(new Ex2());
    private static HashMap<String ,Double> dij= new HashMap<>();
    private static long timeToSleep;
    private static long timeOfGame;

    public static void main(String[] args) {

        //Value of terminal
        if (args.length == 2) {
            ID = Integer.valueOf(args[0]);
            numGame = Integer.valueOf(args[1]);
            client.start();
        }
        else {
            _win = new myFrame("Login game", 350, 220, numGame);
            _win.initLogin();
        }
    }

    /**
     * starts the game and counter, connect the game results to the server.
     */
    @Override
    public void run() {
        game_service game = Game_Server_Ex2.getServer(numGame);
//        game.login(ID);
        directed_weighted_graph gg = loadGraph(game.getGraph());
        init(game);

        game.startGame();
        timeOfGame= game.timeToEnd();
//        int ind = 1;
        timeToSleep = 100;

        while (game.isRunning()) {
            moveAgents(game, gg);
            try {
//                if (ind % 1 == 0) {
                    _win.repaint();
                    _win.setTimeToEnd(game.timeToEnd() / 10);
//                }
                Thread.sleep(timeToSleep);
//                ind++;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (game.timeToEnd() < 0.333*timeOfGame) timeToSleep = 90;
        }
        String res = game.toString();
        System.out.println(res);
        System.exit(0);
    }

    /**
     * loads the picked scenario to create the game arena
     */
    private directed_weighted_graph loadGraph(String s) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(DWGraph_DS.class, new DWGraphJsonDeserializer());
        Gson gson = builder.create();
        try {
            directed_weighted_graph g = gson.fromJson(s, DWGraph_DS.class);
            for (int i = 0; i < g.getV().size(); i++) {
                edges.put(i, -1);
            }
            return g;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * initialize the game by creating a customized arena;
     * the arena includes a list of agents and pokemons that will appear in the game.
     * this method also calls for the myFrame class that creates the graphic game.
     *
     * @param game
     */
    private void init(game_service game) {

        _win = new myFrame("Catch Them All", 1000, 900, numGame);
        String pks = game.getPokemons();
        directed_weighted_graph gg = loadGraph(game.getGraph());

        _ar = new Arena();
        _ar.setGraph(gg);
        _ar.setPokemons(Arena.json2Pokemons(pks));
        _win.update(_ar);

        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject gameData = line.getJSONObject("GameServer");
            int rs = gameData.getInt("agents");
            System.out.println("Welcome to our pokemon game!\n");
            System.out.println("We wish you the best of luck catching them all :)\n");
            System.out.println(info + "\n");
            ArrayList<CL_Pokemon> pksArr = Arena.json2Pokemons(game.getPokemons());
            for (int i = 0; i < pksArr.size(); i++) {
                Arena.updateEdge(pksArr.get(i), gg);
            }

            bubbleSort(pksArr);

            //decides where we should place the agent
            //on an edge that is accessible to the pokemon
            for (int i = 0; i < rs; i++) {
                CL_Pokemon c = pksArr.get(i);
//                int nn = c.get_edge().getDest();
//                if (c.getType() < 0) {
                   int nn = c.get_edge().getSrc();
//                }
                System.out.println(c.get_edge());
                System.out.println(nn);
                game.addAgent(nn);
                pair.add(-1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * sort the pokemons by the order of their value
     * after that we can set agents at best positions to catch the pokemons
     */
    private static void bubbleSort(List<CL_Pokemon> cl) {
        int n = cl.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (cl.get(j).getValue() < cl.get(j + 1).getValue()) {
                    CL_Pokemon temp = cl.get(j);
                    cl.set(j, cl.get(j + 1));
                    cl.set(j + 1, temp);
                }
            }
        }
    }

    /**
     * Moves each of the agents along the edge
     *
     * @param game
     * @param gg
     * @param
     */
    private static void moveAgents(game_service game, directed_weighted_graph gg) {

        String lg = game.move();
        ///check this- why are we creating endless objects of agents.
        List<CL_Agent> ageA = Arena.getAgents(lg, gg);
        _ar.setAgents(ageA);
        String pk = game.getPokemons();
        _ar.setPokemons(Arena.json2Pokemons(pk));

        for (int i = 0; i < _ar.getAgents().size(); i++) {
            CL_Agent ag = _ar.getAgents().get(i);
            int id = ag.getID();
            int dest = ag.getNextNode();
            int src = ag.getSrcNode();
            double v = ag.getValue();
            double sp = ag.getSpeed();
            if (dest == -1) {
                pair.set(id, -1);
                List<node_data> l = path(gg, src, id, sp, game.timeToEnd());
                Iterator<node_data> it = l.iterator();
                while (it.hasNext()) {
                    node_data temp = it.next();
                    game.chooseNextEdge(ag.getID(), temp.getKey());
                    System.out.println("Agent: " + id + ", speed: " + sp + ", val: " + v + "   turned to node: " + temp.getKey());
                }
            }
        }
    }

    /**
     * this functions picks the shortest path from an unoccupied agent to a pokemon.
     *
     * @param g
     * @param mySrc
     * @param id
     * @param sp
     * @param timeToEnd
     * @return
     */
    public static List<node_data> path(directed_weighted_graph g, int mySrc, int id, double sp, long timeToEnd) {
        timeToSleep = 100;
        if (timeToEnd < 0.333*timeOfGame) timeToSleep = 90;
        dw_graph_algorithms algo = new DWGraph_Algo();
        algo.init(g);

        //updates pokemon edges
        double minDistance = Double.MAX_VALUE;
        boolean flag = true;
        int nodeDest = -1;
        int pokSrc;
        int minDindex = 0;


        for (int i = 0; i < _ar.getPokemons().size(); i++) {
            Arena.updateEdge(_ar.getPokemons().get(i), g);
            pokSrc = _ar.getPokemons().get(i).get_edge().getSrc();

            if (flag) {
                //check if some other agent is heading to this pokemon
                for (int j = 0; j < pair.size(); j++) {
                    if (pair.get(j) == pokSrc) flag = false;
                }
            }
            //chooses the closest pokemon as a destination
            if (flag) {
                //save the index of the pokemon that is closest to the agent
                String s= mySrc+","+pokSrc;
                double tempDistance;
                if(!(dij.containsKey(s))){
                    tempDistance = algo.shortestPathDist(mySrc, pokSrc);
                    dij.put(s,tempDistance);
                }
                else tempDistance= dij.get(s);
                if (tempDistance < minDistance) {
                    minDistance = tempDistance;
                    minDindex = i;
                    nodeDest = pokSrc;
                }
            }
            flag = true;

        }

        List<node_data> path = algo.shortestPath(mySrc, nodeDest);
        int d = _ar.getPokemons().get(minDindex).get_edge().getDest();
        pair.set(id, nodeDest);
        path.add(g.getNode(d));
        // if an agent has maximun speed, is on a short edge and there are only 30 seconds to the game
        //get more moves.
        if (_ar.getPokemons().get(minDindex).get_edge().getWeight() < 0.5 && sp >= 5 && timeToEnd < 0.5*timeOfGame)
            timeToSleep = 50;
        return path;
    }
}

