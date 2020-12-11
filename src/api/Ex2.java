package api;

import GUI.myFrame;
import Server.Game_Server_Ex2;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gameClient.*;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;


public class Ex2 implements Runnable {
    private static myFrame _win;
    private static Arena _ar;
    private static int ID = -1;
    private static int numGame = -1;
    private static ArrayList<Integer> pair = new ArrayList<>();
    private static HashMap<Integer, Integer> edges = new HashMap<>();
    private static Thread client = new Thread(new Ex2());
    private static long dt;
    private static HashMap<String, Double> dij = new HashMap<>();

    public static void main(String[] args) {


        //Value of terminal
        if (args.length == 2) {
            ID = Integer.valueOf(args[0]);
            numGame = Integer.valueOf(args[1]);
        }

        _win = new myFrame("Login game", 350, 220, numGame);
        _win.initLogin();
    }

    @Override
    public void run() {
            game_service game = Game_Server_Ex2.getServer(numGame);
//        game.login(ID);
        directed_weighted_graph gg = loadGraph(game.getGraph());
        init(game);
        game.startGame();
//        _win.setTitle("");
        int ind = 1;
        dt = 100;

//fix music location
        /*SimplePlayer player= new SimplePlayer("data/music.mp3");
        Thread playerThread= new Thread(player);
        playerThread.start();*/

        while (game.isRunning()) {
            moveAgents(game, gg);
            try {
                if (ind % 1 == 0) {
                    _win.repaint();
                    _win.setTimeToEnd(game.timeToEnd() / 10);
                }
                Thread.sleep(dt);
                ind++;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (game.timeToEnd() < 20000) dt = 90;
            //   if (game.timeToEnd() < 20000) dt = 80;
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
            for (int i = 0; i < g.getV().size(); i++) {
                edges.put(i, -1);
            }
            return g;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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
            System.out.println(info);
            System.out.println(game.getPokemons());
            ArrayList<CL_Pokemon> pksArr = Arena.json2Pokemons(game.getPokemons());
            for (int i = 0; i < pksArr.size(); i++) {
                Arena.updateEdge(pksArr.get(i), gg);
            }

            //if we want set agents at src edge with the max pok value- bubbleSort
            //////////////here Reut Change
             bubbleSort(pksArr);

            //decides where we should place the agent
            //on an edge that is accessible to the pokemon
            for (int i = 0; i < rs; i++) {
                CL_Pokemon c = pksArr.get(i);
                System.out.println(c.get_edge()+" value: "+c.getValue());
//                int nn = c.get_edge().getDest();
//                if (c.getType() < 0) {
                 int  nn = c.get_edge().getSrc();
//                }
                game.addAgent(nn);
                pair.add(-1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //sort the poks order to their value
    //after that we can set agents at best pos to catch poks
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
     * Moves each of the agents along the edge,
     *
     * @param game
     * @param gg
     * @param
     */
//    private static void moveAgents(game_service game, directed_weighted_graph gg) {
//        String lg = game.move();
//        List<CL_Agent> ageA = Arena.getAgents(lg, gg);
//        _ar.setAgents(ageA);
//        String pk = game.getPokemons();
//        _ar.setPokemons(Arena.json2Pokemons(pk));
//        dw_graph_algorithms algo = new DWGraph_Algo();
//        algo.init(gg);
//        for (int i = 0; i < ageA.size(); i++) {
//            CL_Agent ag = ageA.get(i);
//            int id = ag.getID();
//            int dest = ag.getNextNode();
//            int src = ag.getSrcNode();
//
//            double v = ag.getValue();
//            double sp = ag.getSpeed();
//            if (dest == -1) {
//                pair.set(id, -1);
//                List<node_data> l = path(gg, src, id, sp, game);
//                Iterator<node_data> it = l.iterator();
//                while (it.hasNext()) {
//                    node_data temp = it.next();
//                    game.chooseNextEdge(ag.getID(), temp.getKey());
//                    System.out.println("Agent: " + id + ", speed: " + sp + ", val: " + v + "  turned to node: " + temp.getKey());
//                }
//
////                for (int j = 0; j < ageA.size(); j++) {
////                    ag = ageA.get(j);
////                    id = ag.getID();
////                    dest = ag.getNextNode();
////                    src = ag.getSrcNode();
////
////                    v = ag.getValue();
////                    sp = ag.getSpeed();
////
////                    if (i != j ) {
////                        //if (algo.shortestPathDist(src, pair.get(id)) > 8) {
////                            pair.set(id, -1);
////                            l = path(gg, src, id, sp, game);
////                            it = l.iterator();
////                            while (it.hasNext()) {
////                                node_data temp = it.next();
////                                game.chooseNextEdge(ag.getID(), temp.getKey());
////                                System.out.println("Agent: " + id + ", speed: " + sp + ", val: " + v + "  turned to node: " + temp.getKey());
////
////                            }
//////                        }
////                    }
////                }
//            }
//        }
//    }

    //pairs each agent with closest pokemon
//    public static List<node_data> path(directed_weighted_graph g, int src, int id, double sp, game_service game) {
////        if (sp <= 2) dt = 100;
////        else dt = 90;
//
//        dw_graph_algorithms algo = new DWGraph_Algo();
//        algo.init(g);
//        // System.out.println(id + ": " + pair);
//
//        //updates pokemon edges
//        double minD = Double.MAX_VALUE, val;
//        boolean flag = true;
//        int index = 0, srcP, po = 0, PmaxV = 0, pk = 0, destP;
//
//        for (int i = 0; i < _ar.getPokemons().size(); i++) {
//            Arena.updateEdge(_ar.getPokemons().get(i), g);
////            srcP = _ar.getPokemons().get(i).get_edge().getSrc();
//            destP = _ar.getPokemons().get(i).get_edge().getDest();
//
//
//            if (flag) {
//                //check if someone else go to this pok
//                for (int j = 0; j < pair.size(); j++) {
////                    if (pair.get(j) != -1)
////                     if ((pair.get(j) == srcP) || (algo.shortestPathDist(pair.get(j), srcP) < 2)) flag = false;
////                    if (pair.get(j) == srcP) flag = false;
//                    if (pair.get(j) == destP) flag = false;
//                }
//            }
//            if (flag) {
//                val = _ar.getPokemons().get(i).getValue();
//                if (val > PmaxV) {
////                        PmaxV = srcP;
//                    PmaxV = destP;
//                    pk = i;
//                }
//            }
//            if (flag) {
//                double tempD;
//                if (!(dij.containsKey(((DWGraph_DS) g).getPair(src, destP)))) {
////                    double tempD = algo.shortestPathDist(src, destP);
//                    tempD = algo.shortestPathDist(src, destP);
//                    dij.put(((DWGraph_DS) g).getPair(src, destP), tempD);
//                } else {
//                    tempD = dij.get(((DWGraph_DS) g).getPair(src, destP));
//                }
//                if (tempD <= minD) {
//                    minD = tempD;
////                        index = srcP;
//                    index = destP;
//                    po = i;
//
//                }
//            }
//
//            flag = true;
//        }
//
//        double shortes;
//        if (!(dij.containsKey(((DWGraph_DS) g).getPair(src, PmaxV)))) {
//            shortes = algo.shortestPathDist(src, PmaxV);
//            dij.put(((DWGraph_DS) g).getPair(src, PmaxV), shortes);
//        } else {
//            shortes = dij.get(((DWGraph_DS) g).getPair(src, PmaxV));
//        }
//        if (Math.abs(shortes - minD) < 5) {
//            index = PmaxV;
//            po = pk;
//        }
//
//       /* if (index == -1)
//            index = random(g, src);*/
//
//        srcP = _ar.getPokemons().get(po).get_edge().getSrc();
////        List<node_data> path = algo.shortestPath(src, index);
//        List<node_data> path = algo.shortestPath(src, srcP);
//
//        //     int d = _ar.getPokemons().get(po).get_edge().getDest();
//
//        pair.set(id, index);
////        path.add(g.getNode(d));
//        path.add(g.getNode(index));
////        if (_ar.getPokemons().get(po).get_edge().getWeight() < 1.5 && sp >= 5 && game.timeToEnd() < 30000) dt = 70;
//        return path;
//    }


    private static void moveAgents(game_service game, directed_weighted_graph gg) {

        String lg = game.move();
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


//    //pairs each agent with closest pokemon
//    public static List<node_data> path(directed_weighted_graph g, int src, int id, double sp, long timeToEnd) {
//        dt=100;
//        if (timeToEnd < 20000) dt = 90;
//        dw_graph_algorithms algo = new DWGraph_Algo();
//        algo.init(g);
//        System.out.println(id + ": " + pair);
//
//        //updates pokemon edges
//        double minD = Double.MAX_VALUE;
//        boolean flag = true;
////        int index = -1;
//        int index = 0;
//        int srcP;
//        int po = 0;
//        double val=0;
//        int PmaxV=0;
//        int destP;
//        int pk = 0;
//
//        for (int i = 0; i < _ar.getPokemons().size(); i++) {
//            Arena.updateEdge(_ar.getPokemons().get(i), g);
//            srcP = _ar.getPokemons().get(i).get_edge().getSrc();
////            destP = _ar.getPokemons().get(i).get_edge().getDest();
//
//            if(flag) {
//                //check if someone else go to this pok
//                for (int j = 0; j < pair.size(); j++) {
//                    if (pair.get(j) == srcP) flag = false;
////                    if (pair.get(j) == destP) flag = false;
//                }
//            }
//
//            if (flag){
//                val= _ar.getPokemons().get(i).getValue();
//                if(val>PmaxV) {
//                    PmaxV=srcP;
////                    PmaxV = destP;
//                    pk=i;
//                }
//            }
//
//            if (flag) {
////                double tempD = algo.shortestPathDist(src, destP);
//                double tempD = algo.shortestPathDist(src, srcP);
//                if (tempD <= minD) {
//                    minD = tempD;
//                    index = srcP;
////                    index = destP;
//                    po = i;
//                }
//            }
//            flag = true;
//
//        }
//
//        if(Math.abs(algo.shortestPathDist(src, PmaxV)-minD)<4){
//            index=PmaxV;
//            po=pk;
//        }
//
//        srcP = _ar.getPokemons().get(po).get_edge().getSrc();
//        List<node_data> path = algo.shortestPath(src, index);
////        List<node_data> path = algo.shortestPath(src, srcP);
//        int d = _ar.getPokemons().get(po).get_edge().getDest();
//        pair.set(id, index);
//        path.add(g.getNode(d));
////        path.add(g.getNode(index));
//        if (_ar.getPokemons().get(po).get_edge().getWeight() < 0.5 && sp >= 5 && timeToEnd < 30000) dt = 50;
//        return path;
//    }

    public static List<node_data> path(directed_weighted_graph g, int mySrc, int id, double sp, long timeToEnd) {
        dt=100;
        if (timeToEnd < 20000) dt = 90;
        dw_graph_algorithms algo = new DWGraph_Algo();
        algo.init(g);
//        System.out.println(id + ": " + pair);

        //updates pokemon edges
        double minDistance = Double.MAX_VALUE;
        boolean flag = true;
        int nodeDest = -1;
        int pokSrc;
        int PMVSrc=0;
        int minDindex = 0;
        double val;
        double PmaxV=0;
        int PmaxVindex = 0;

        for (int i = 0; i < _ar.getPokemons().size(); i++) {
            Arena.updateEdge(_ar.getPokemons().get(i), g);
            pokSrc = _ar.getPokemons().get(i).get_edge().getSrc();
            //if might not be critical
            if(flag) {
                //check if someone else goes to this pok
                for (int j = 0; j < pair.size(); j++) {
                    if (pair.get(j) == pokSrc) flag = false;
                }
            }

            /*if (flag){
                //temporary value of each pokemon in this for
                //PmaxVindex is the index of the pokemon with the heighest value
                //sound redundant because a pokemon with heighest value can be very far from me
                //and there might be one closer to me with the same value;
                //we should switch the order of functions
                val= _ar.getPokemons().get(i).getValue();
                if(val>PmaxV) {
                    PmaxV=val;
                    PmaxVindex=i;
                    PMVSrc = pokSrc;

                }
            }*/

            if (flag) {
                //save the index of the pokemon that is closest to the agent
                double tempDistance = algo.shortestPathDist(mySrc, pokSrc);
                if (tempDistance < minDistance) {
                    minDistance = tempDistance;
                    minDindex = i;
                    nodeDest = pokSrc;
                }
            }
            flag = true;

        }
        //if the distance between the agent and PmaxValuePokemon equals to distance
        //between agent and minDistantPokemon
      /*  if(Math.abs(algo.shortestPathDist(mySrc, PMVSrc)-minDistance)<5){
            nodeDest=PMVSrc;
            minDindex=PmaxVindex;
        }*/

        List<node_data> path = algo.shortestPath(mySrc, nodeDest);
        int d = _ar.getPokemons().get(minDindex).get_edge().getDest();
        pair.set(id, nodeDest);
        path.add(g.getNode(d));
        if (_ar.getPokemons().get(minDindex).get_edge().getWeight() < 0.5 && sp >= 5 && timeToEnd < 30000) dt = 50;
        return path;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static class Login extends JPanel {

        public Login() {
            super();
            this.setLayout(null);
            idANDnum();
            title();
            imageLogo();
        }

        private void title() {
            JLabel t = new JLabel("Catch Them All");
            t.setFont(new Font("MV Boli", Font.BOLD, 30));
            t.setForeground(Color.white);
            t.setBounds(40, 20, 350, 40);
            add(t);
            t = new JLabel("Catch Them All");
            t.setFont(new Font("MV Boli", Font.BOLD, 30));
            t.setForeground(Color.red.darker());
            t.setBounds(42, 22, 350, 40);
            add(t);
        }

        private void idANDnum() {
            JLabel id = new JLabel("     Id");
            id.setBounds(10, 70, 80, 25);
            Border b = BorderFactory.createLineBorder(Color.RED.darker(), 2);
            id.setForeground(Color.RED.darker());
            id.setBackground(Color.gray.brighter());
            id.setFont(new Font("MV Boli", Font.BOLD, 13));
            id.setBorder(b);
            id.setOpaque(true);

            add(id);

            JTextField userText = new JTextField();
            userText.setBounds(100, 70, 165, 25);
            userText.setFont(new Font("MV Boli", Font.BOLD, 13));
            userText.setForeground(Color.RED.darker());
            if (ID >= 0) userText.setText(ID + "");

            this.add(userText);

//            userText.addKeyListener(new KeyAdapter() {
//                                        public void keyPressed(KeyEvent ke) {
//                                            String value = userText.getText();
//                                            int l = value.length();
//                                            if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') {
//                                                userText.setEditable(true);
//                                            } else {
//                                                userText.setEditable(false);
//                                                //userText.setText("");
//
//                                                userText.setText("* Enter only numeric digits(0-9)");
//
//                                            }
//                                        }
//                                    }
//            );

            JLabel gameKey = new JLabel(" Key Game");
            gameKey.setBounds(10, 100, 80, 25);
            gameKey.setForeground(Color.RED.darker());
            gameKey.setBackground(Color.gray.brighter());
            gameKey.setFont(new Font("MV Boli", Font.BOLD, 13));
            gameKey.setBorder(b);
            gameKey.setOpaque(true);

            this.add(gameKey);

            JTextField userNum = new JTextField();
            userNum.setBounds(100, 100, 165, 25);
            userNum.setFont(new Font("MV Boli", Font.BOLD, 13));
            userNum.setForeground(Color.RED.darker());
            if (numGame >= 0) userNum.setText(numGame + "");

            this.add(userNum);

            JButton button = new JButton("Reut");
            button.setBounds(30, 140, 80, 25);
            button.setForeground(Color.red.darker());
            button.setBackground(Color.orange);
            button.setBorder(b);
            button.setFont(new Font("MV Boli", Font.BOLD, 15));

            this.add(button);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    if (e.getSource() == button) {
                        // userText.setEditable(false);
                        userText.setText("208196709");
                        userNum.setText("1");
                    }
                }

            });

            JButton matan = new JButton("Matan");
            matan.setBounds(210, 140, 80, 25);
            matan.setForeground(Color.red.darker());
            matan.setBackground(Color.orange);
            matan.setBorder(b);
            matan.setFont(new Font("MV Boli", Font.BOLD, 15));

            this.add(matan);
            matan.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    if (e.getSource() == matan) {
                        // userText.setEditable(false);
                        userText.setText("206240301");
                        userNum.setText("1");
                    }
                }

            });

            JButton button1 = new JButton("Start");
            button1.setBounds(120, 140, 80, 25);
            button1.setForeground(Color.red.darker());
            button1.setBackground(Color.orange);
            button1.setFont(new Font("MV Boli", Font.BOLD, 15));
            button1.setBorder(b);
            this.add(button1);
            button1.addActionListener(e ->
                    numGame = Integer.valueOf(userNum.getText()));
            button1.addActionListener(e ->
                    ID = Integer.valueOf(userText.getText()));
            button1.addActionListener(e -> client.start());

        }

        private void imageLogo() {
            JLabel temp = new JLabel();
            temp.setIcon(new ImageIcon("data/backround.jpg"));
            temp.setBounds(0, 0, 350, 197);
            this.add(temp);
        }
    }
}

