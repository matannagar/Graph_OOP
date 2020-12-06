package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ReutFrame extends JFrame {

    private Arena _ar;
    private Range2Range _w2f;
    private LinkedList<geo_location> pk = new LinkedList<>();
    private LinkedList<geo_location> ag = new LinkedList<>();
    private boolean flag;
    private MyPanel p = new MyPanel("temp");

    public ReutFrame(String s, int w, int h) {
        super(s);
        newF(w, h);
        flag = false;
    }

    private void newF(int w, int h) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(w, h);
        this.setVisible(true);
    }

    public void update(Arena ar) {
        this._ar = ar;
        updateFrame();
    }

    private void updateFrame() {

        Range rx = new Range(20, this.getWidth() - 20);
        Range ry = new Range(this.getHeight() - 10, 150);
        Range2D frame = new Range2D(rx, ry);
        directed_weighted_graph g = _ar.getGraph();
        _w2f = Arena.w2f(g, frame);
        initMenu();
        this.setVisible(true);
    }

    public void paint(Graphics g) {

        drawGraph(g);
        drawPokemons(g);
        drawAgents(g);
//        drawInfo(g);
        drawGradeAg(g);
        drawGrade(g);
        drawTimer(g);


//        p.setVisible(true);
    }

    public void initLogin() {
        MyPanel panel = new MyPanel("login");
        this.add(panel);
        this.setVisible(true);
        panel.setVisible(true);
        /*Scanner s= new Scanner(System.in);
        int userId = s.nextInt();  // Read userId input
        int keyGame = s.nextInt();  // Read keyGame input
        return (userId+""+keyGame);*/

    }

    private void initMenu() {
        JMenuBar mb = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenu submenu = new JMenu("item");
        JMenuItem defaultId = new JMenuItem("item1");
        JMenuItem defaultNum = new JMenuItem("item2");

        submenu.add(defaultId);
        submenu.add(defaultNum);
        menu.add(submenu);
        mb.add(menu);
        this.setJMenuBar(mb);
    }

    private void initGraph(Graphics g) {
        updateFrame();
        drawGraph(g);
        this.setVisible(true);
    }

    public void timer() {
        GUITimer t = new GUITimer();
    }

    private void initGame() {
        MyPanel panel = new MyPanel("game");
        this.add(panel);
    }

//    private void drawInfo(Graphics g) {
//        java.util.List<String> str = _ar.get_info();
//        String dt = "none";
//        for (int i = 0; i < str.size(); i++) {
//            g.drawString(str.get(i) + " dt: " + dt, 100, 60 + i * 20);
//        }
//
//    }

    private void drawGraph(Graphics g) {
        if (flag == false) {
            if (_ar != null) {
                ///
                this.add(p);
                p.setVisible(false);
                p.temp(g);
                ////
                directed_weighted_graph gg = _ar.getGraph();
                Iterator<node_data> iter = gg.getV().iterator();
                while (iter.hasNext()) {
                    node_data n = iter.next();
                    g.setColor(Color.DARK_GRAY);
                    drawNode(n, 5, g);
                    Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
                    while (itr.hasNext()) {
                        edge_data e = itr.next();
                        g.setColor(Color.gray);
                        drawEdge(e, g);
                    }
                }
                flag = true;
            }
        }
    }

    private void drawPokemons(Graphics g) {
        int k = 25;
//        for(geo_location p: pk) {
//        g.clearRect((int) p.x() - k, (int) p.y() - k, 70, 70);
//        }
        pk.clear();

        //***** check why _ar could be NULL
        if (_ar != null) {
            List<CL_Pokemon> fs = _ar.getPokemons();
            if (fs != null) {

                Iterator<CL_Pokemon> itr = fs.iterator();
                while (itr.hasNext()) {
                    CL_Pokemon f = itr.next();
                    Point3D c = f.getLocation();

                /*for(geo_location p: pk){
                    if(!(p.x()==c.x()&&p.y()==c.y()))
                        g.clearRect((int) p.x() - k, (int) p.y() - k, 70, 70);
                }*/

                    int r = 10;
                    g.setColor(Color.green);
                    if (f.getType() < 0) {
                        g.setColor(Color.orange);
                    }
                    if (c != null) {

                        geo_location fp = this._w2f.world2frame(c);
                        pk.add(fp);
                        //g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
                        /////////////////Matan's code
                    }
                    for (geo_location p : pk) {
                        BufferedImage img = null;
                        try {
                            img = ImageIO.read(new File("data/pok.png"));

                        } catch (IOException e) {

                        }
                        ;
//					g.drawImage(img,((getWidth()-img.getWidth()) /3) ,((getHeight()-img.getHeight())/3),this);

                        g.drawImage(img, (int) p.x() - k, (int) p.y() - k, this);

                        /////////////////Matan's code
                        //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);

                    }
                }
            }
        }
    }

    private void drawAgents(Graphics g) {
        int k = 25;
//        for(geo_location p: ag) {
//            g.clearRect((int) p.x() - k, (int) p.y() - k, 30, 30);
//        }
        ag.clear();
        //**** find out why _ar could be null
        if (_ar != null) {
            List<CL_Agent> rs = _ar.getAgents();
            //	Iterator<OOP_Point3D> itr = rs.iterator();
            g.setColor(Color.red);
            int i = 0;
            while (rs != null && i < rs.size()) {
                geo_location c = rs.get(i).getLocation();
                int r = 8;
                i++;
                if (c != null) {

                    geo_location fp = this._w2f.world2frame(c);
                    ag.add(fp);
//				g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
                    /////////////////Matan's code
                }
                for (geo_location p : ag) {
                    BufferedImage img = null;
                    try {
                        img = ImageIO.read(new File("data/mypac.png"));

                    } catch (IOException e) {

                    }
                    ;
//					g.drawImage(img,((getWidth()-img.getWidth()) /3) ,((getHeight()-img.getHeight())/3),this);

                    g.drawImage(img, (int) p.x() - k, (int) p.y() - k, this);
                    /////////////////Matan's code
                }
            }
        }
    }

    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
        g.setColor(Color.red);
        //matans code
        g.setFont(new Font("David", Font.PLAIN, 15));
        //matans code
        g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 4 * r);

    }

    private void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        //////Matan's
//		this.paintComponents(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        g2.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
        ///Matan's
        g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
        //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
    }

    public void drawGradeAg(Graphics g) {
        //*****find out why _ar could be NULL
        if (_ar != null) {
            List<CL_Agent> rs = _ar.getAgents();
            g.setColor(Color.pink);

            // g.clearRect(815,50,100,50);

            g.drawLine(815, 50, 970, 50);
            g.drawLine(815, 80 + 20 * rs.size(), 970, 80 + 20 * rs.size());

            g.setColor(Color.blue.darker().darker());
            Font f1 = new Font(Font.DIALOG, Font.BOLD, 15);
            g.setFont(f1);
            int i = 0, j = 20;
            while (rs != null && i < rs.size()) {
                //  g.drawString("Agent- " + rs.get(i).getID() + ": value- " + rs.get(i).getValue(), 820, 80 + i * j);
                g.drawString("Agent- " + rs.get(i).getID() + ": value- ", 820, 80 + i * j);
                i++;
            }
            //Matan's commet:
            //lets add the num_of_scenario + total_points so far + num_of_steps + num_of_agnts ...
            //Matan's commet
        }
    }

    public void drawGrade(Graphics g) {
        g.clearRect(940, 60, 50, 70);
        ///*** find out why _ar could be NULL
        if (_ar != null) {
            List<CL_Agent> rs = _ar.getAgents();

            g.setColor(Color.orange.darker().darker());
            Font f1 = new Font(Font.DIALOG, Font.BOLD, 15);
            g.setFont(f1);
            int i = 0, j = 20;
            while (rs != null && i < rs.size()) {
                g.drawString("" + rs.get(i).getValue(), 940, 80 + i * j);
                i++;
            }
        }
    }

    public void drawTimer(Graphics g) {
        g.setColor(Color.orange.darker().darker());
        g.drawString("Remaining time: 00:00" + " ,Insert timer here", 300, 100);
    }
}
