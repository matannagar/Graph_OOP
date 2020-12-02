package api;

import Server.Game_Server_Ex2;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gameClient.*;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.*;


public class Ex2 implements Runnable {
    private static MyFrame _win;
    private static Arena _ar;
    private static int id;
    private static int numGame;
    private static ArrayList<Integer> pair = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        Login l = new Login();
        Thread login = new Thread(l);

        login.start();
        while (!l.exit()) {
            System.out.print("");
        }
        id= l.getId();
        numGame=l.getNum();

        Thread client = new Thread(new Ex2());
        client.start();
    }

    @Override
    public void run() {
        /*while(numGame==-1 || id==-1) {
            System.out.println(numGame+" "+id);
        }*/

        game_service game = Game_Server_Ex2.getServer(numGame);

        //game.login(id);
        directed_weighted_graph gg = loadGraph(game.getGraph());
        init(game);

        game.startGame();

        _win.setTitle("Ex2 " + game.toString());
        int ind = 0;
        long dt = 100;
//fix music location
        /*SimplePlayer player= new SimplePlayer("data/music.mp3");
        Thread playerThread= new Thread(player);
        playerThread.start();*/

        while (game.isRunning()) {
            moveAgents(game, gg);
            _win.setTitle("Ex2 " + game.toString());
            try {
                if (ind % 2 == 0) {
                    _win.repaint();
                }
                Thread.sleep(dt);
                ind++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();

        System.out.println(res);
        System.exit(0);
    }

    private directed_weighted_graph loadGraph(String s) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(DWGraph_DS.class, new DWGraphJsonDeserializer());
        Gson gson = builder.create();
        try {
            directed_weighted_graph g = gson.fromJson(s, DWGraph_DS.class);
            return g;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void init(game_service game) {
        String pks = game.getPokemons();
        directed_weighted_graph gg = loadGraph(game.getGraph());
        _ar = new Arena();
        _ar.setGraph(gg);
        _ar.setPokemons(Arena.json2Pokemons(pks));

        _win = new MyFrame("Catch Them All");
        _win.setSize(1000, 700);
        _win.update(_ar);

        _win.show();
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject gameData = line.getJSONObject("GameServer");
            int rs = gameData.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            ArrayList<CL_Pokemon> pksArr = Arena.json2Pokemons(game.getPokemons());
            for (int i = 0; i < pksArr.size(); i++) {
                Arena.updateEdge(pksArr.get(i), gg);
            }
            //decides where we should place the agent
            //on an edge that is accessible to the pokemon
            for (int i = 0; i < rs; i++) {
                CL_Pokemon c = pksArr.get(i % pksArr.size());
                int nn = c.get_edge().getDest();
                if (c.getType() < 0) {
                    nn = c.get_edge().getSrc();
                }
                game.addAgent(nn);
                pair.add(-1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Moves each of the agents along the edge,
     * in case the agent is on a node the next destination (next edge) is chosen (randomly).
     *
     * @param game
     * @param gg
     * @param
     */
    private static void moveAgents(game_service game, directed_weighted_graph gg) {
        String lg = game.move();
        List<CL_Agent> ageA = Arena.getAgents(lg, gg);
        _ar.setAgents(ageA);
        String pk = game.getPokemons();
        _ar.setPokemons(Arena.json2Pokemons(pk));

        for (int i = 0; i < ageA.size(); i++) {
            CL_Agent ag = ageA.get(i);
            int id = ag.getID();
            int dest = ag.getNextNode();
            int src = ag.getSrcNode();
            double v = ag.getValue();
            if (dest == -1) {
                pair.set(id, -1);
                List<node_data> l = path(gg, src, id);
                Iterator<node_data> it = l.iterator();
                while (it.hasNext()) {
                    node_data temp = it.next();
                    game.chooseNextEdge(ag.getID(), temp.getKey());
                    System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + temp.getKey());
                }
            }
        }
    }

    //pairs each agent with closest pokemon
    public static List<node_data> path(directed_weighted_graph g, int src, int id) {
        dw_graph_algorithms algo = new DWGraph_Algo();
        algo.init(g);
        System.out.println(id + ": " + pair);

        //updates pokemon edges
        double minD = Double.MAX_VALUE;
        boolean flag = true;
        int index = -1;
        int dest = 0;
        int po = 0;
        for (int i = 0; i < _ar.getPokemons().size(); i++) {
            Arena.updateEdge(_ar.getPokemons().get(i), g);
            for (int j = 0; j < pair.size(); j++) {
                dest = _ar.getPokemons().get(i).get_edge().getSrc();
                if (pair.get(j) == dest) flag = false;
            }
            // dest = _ar.getPokemons().get(i).get_edge().getSrc();
            double tempD = algo.shortestPathDist(src, dest);
            if (tempD < minD && flag) {
                minD = tempD;
                index = dest;
                po = i;
            }
            flag = true;
        }

        List<node_data> path = algo.shortestPath(src, index);
        int d = _ar.getPokemons().get(po).get_edge().getDest();
        pair.set(id, index);
        path.add(g.getNode(d));
        return path;
    }

}

