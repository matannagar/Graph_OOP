package GUI;

import api.*;
import gameClient.Arena;
import gameClient.CL_Agent;
import gameClient.CL_Pokemon;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class myFrame extends JFrame {
    private Arena _ar;
    private Range2Range _w2f;
    private boolean flag;

    public myFrame(String s, int w, int h) {
        super(s);
        this.setSize(new Dimension(w,h));
       // this.setSize(w, h);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        flag = false;
    }

    public void update(Arena ar) {
        this._ar = ar;
        updateFrame();
        this.revalidate();
    }

    private void updateFrame() {

        Range rx = new Range(20, this.getWidth() - 20);
        Range ry = new Range(this.getHeight() - 10, 150);
        Range2D frame = new Range2D(rx, ry);
        directed_weighted_graph g = _ar.getGraph();
        _w2f = Arena.w2f(g, frame);
        initMenu();
        this.revalidate();
//        this.repaint();
        this.setVisible(true);
    }

    public void paint(Graphics g) {

       this.add(new myPanel(this._w2f));

//        this.getContentPane().add(new myPanel(this._w2f),BorderLayout.CENTER);
//        setLocationByPlatform(true);
        if (flag != true) {
            GUITimer t = new GUITimer();
        }
        this.revalidate();
    }

    public void initLogin() {
        Login panel = new Login("login");
        this.add(panel);
        this.setVisible(true);
        panel.setVisible(true);
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

    public void drawTimer(Graphics g) {
        g.drawString("Remaining time:" + " ,Insert timer here", 300, 100);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class myPanel extends JPanel {
        private Range2Range _w2f;

        public myPanel(Range2Range _w2f) {
            if (_w2f != null)
                this._w2f = _w2f;
            this.revalidate();
        }

        public void paintComponent(Graphics g) {
            Graphics2D gg = (Graphics2D) g;
            super.paintComponent(g);
            drawGraph(gg);
            drawPokemons(gg);
            drawAgents(gg);
//            drawGradeAg(gg);
            drawGrade(gg);
            drawTimer(gg);

            this.revalidate();
            this.setVisible(true);
        }

        private void drawEdge(edge_data e, Graphics g) {
            directed_weighted_graph gg = _ar.getGraph();
            geo_location s = gg.getNode(e.getSrc()).getLocation();
            geo_location d = gg.getNode(e.getDest()).getLocation();
            if (this._w2f != null) {
                geo_location s0 = this._w2f.world2frame(s);
                geo_location d0 = this._w2f.world2frame(d);
                this.paintComponents(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(3));
                g2.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
                g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
                this.revalidate();
            }
        }

        private void drawNode(node_data n, int r, Graphics g) {
            geo_location pos = n.getLocation();
            if (pos != null && this._w2f != null) {
                geo_location fp = this._w2f.world2frame(pos);

                g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);

                g.setColor(Color.red);
                g.setFont(new Font("David", Font.PLAIN, 15));
                g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 2 * r);
            }
        }

        private void drawAgents(Graphics g) {
            int k = 25;
            //CHECKTHIS find out why _ar could be null
            if (_ar != null) {
                List<CL_Agent> rs = _ar.getAgents();
                int i = 0;
                while (rs != null && i < rs.size()) {
                    geo_location c = rs.get(i).getLocation();
                    i++;
                    if (c != null) {
                        //CHECKTHIS _w2f PLASTER
                        if (this._w2f != null) {
                            geo_location fp = this._w2f.world2frame(c);
                            BufferedImage img = null;
                            try {
                                img = ImageIO.read(new File("data/mypac.png"));

                            } catch (IOException e) {

                            }
                            g.drawImage(img, (int) fp.x() - k, (int) fp.y() - k, this);
                        }
                    }
                }
            }
            this.revalidate();
        }
        public void drawGrade(Graphics g) {
            //CHECKTHIS find out why _ar could be NULL
            if (_ar != null) {
                List<CL_Agent> rs = _ar.getAgents();
                g.setColor(Color.pink);
                //CHECKTHIS
                if (rs != null) {
                    g.drawLine(815, 50, 970, 50);
                    g.drawLine(815, 80 + 20 * rs.size(), 970, 80 + 20 * rs.size());
                }
                g.setColor(Color.blue.darker().darker());
                g.setFont(new Font("MV Boli", Font.BOLD, 15));
                int i = 0, j = 20;
                while (rs != null && i < rs.size()) {
                    g.drawString("Agent " + rs.get(i).getID(), 820, 80 + i * j);
                    g.drawString("value  " , 887, 80 + i * j);
                    g.drawString("" + rs.get(i).getValue(), 940, 80 + i * j);
                    i++;
                }
            }
        }

        private void drawGraph(Graphics g) {
            myPanel reut = new myPanel(_w2f);
            if (_ar != null) {
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
                // CHECKTHIS
                flag = true;
            }
        }

        private void drawPokemons(Graphics g) {

            int k = 25;
            BufferedImage img = null;
            //CHECKTHIS check why _ar could be NULL
            if (_ar != null) {
                List<CL_Pokemon> fs = _ar.getPokemons();

                if (fs != null) {
                    Iterator<CL_Pokemon> itr = fs.iterator();
                    //CHECKTHIS throws exceptions
                    while (itr.hasNext()) {
                        CL_Pokemon f = itr.next();
                        Point3D c = f.getLocation();

                        if (f.getType() < 0) {
                            try {
                                img = ImageIO.read(new File("data/pok.png"));

                            } catch (IOException e) {

                            }
                        } else if (f.getType() > 0) {
                            try {
                                img = ImageIO.read(new File("data/balb.png"));
                            } catch (IOException e) {

                            }
                        }

                        if (c != null) {
                            //CHECKTHIS check condition
                            if (this._w2f != null) {
                                geo_location fp = this._w2f.world2frame(c);
                                g.drawImage(img, (int) fp.x() - k, (int) fp.y() - k, this);
                            }
                        }
                    }
                }
            }
        }
    }
}
