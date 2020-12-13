package gameClient;

import api.*;
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

/**
 * This class is responsible for the graphics of our game.
 * A JFrame that includes a JPanel.
 * The game window is resizeable, includes a timer, total score points and
 * agent personal grade.
 *
 * The game includes initial game window that requires a user Id number and a game scenario.
 */

public class myFrame extends JFrame {
    private Arena _ar;
    private Range2Range _w2f;
    private int numGame;
    private float time;
    private Ex2.Login panel;

    public myFrame(String s, int w, int h, int num) {
        super(s);
        this.setSize(new Dimension(w, h));
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        numGame = num;
        this.setVisible(true);
    }

    public void update(Arena ar) {
        this._ar = ar;
        updateFrame();
        this.revalidate();
    }

    /**
     * this function updates the size of the game window
     * which makes it resizeable
     */
    private void updateFrame() {
        Range rx = new Range(20, this.getWidth() - 50);
        Range ry = new Range(this.getHeight() - 150, 150);
        Range2D frame = new Range2D(rx, ry);
        if (this._ar != null) {
            directed_weighted_graph g = _ar.getGraph();
            _w2f = Arena.w2f(g, frame);
            this.revalidate();
            this.setVisible(true);
        }
    }

    public void paint(Graphics g) {
        this.add(new myPanel(this._w2f));
        updateFrame();
        this.revalidate();
    }

    /**
     * this function is responsible for the initial game window
     */
    public void initLogin() {
        panel = new Ex2.Login();
        this.add(panel);
        this.setVisible(true);
        panel.setVisible(true);
    }

    public void closeInitWindow(){
        panel.setVisible(false);
    }

    public void setTimeToEnd(long timeTo) {
        time = (float) timeTo / 100;
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class myPanel extends JPanel {
        private Range2Range _w2f;

        public myPanel(Range2Range _w2f) {
            if (_w2f != null)
                this._w2f = _w2f;
            ImageIcon img = new ImageIcon("data/icon.png");
            setIconImage(img.getImage());
            this.setBackground(new Color(255, 252, 221));
            this.revalidate();
        }

        public void paintComponent(Graphics g) {
            if (_ar != null && this._w2f != null) {
                Graphics2D gg = (Graphics2D) g;
                super.paintComponent(g);
                drawAsh(gg);
                drawGraph(gg);
                drawPokemons(gg);
                drawAgents(gg);
                drawGrade(gg);
                numGame(gg);
                drawTimer(gg);

                this.revalidate();
                this.setVisible(true);
            }
        }

        private void drawTimer(Graphics g) {
            g.setFont(new Font("MV Boli", Font.PLAIN, 30));
            g.setColor(Color.magenta.darker().darker().darker());
            g.drawString("Remaining time:  ", 10, 110);
            g.setColor(Color.blue.darker().darker().darker());
            g.setFont(new Font("MV Boli", Font.PLAIN, 23));
            g.drawString("" + time, 250, 110);
        }

        private void drawAsh(Graphics g) {
            Image img = null;
            try {
                img = ImageIO.read(new File("data/ash.png"));
            } catch (IOException e) {

            }
            g.drawImage(img, 780, 120, this);
            try {
                img = ImageIO.read(new File("data/misty.png"));
            } catch (IOException e) {

            }
            g.drawImage(img, 0, 120, this);
            try {
                img = ImageIO.read(new File("data/bigLogo.png"));
            } catch (IOException e) {

            }
            g.drawImage(img, 350, 5, this);
        }

        private void drawEdge(edge_data e, Graphics g) {
            directed_weighted_graph gg = _ar.getGraph();
            geo_location s = gg.getNode(e.getSrc()).getLocation();
            geo_location d = gg.getNode(e.getDest()).getLocation();
            geo_location s0 = this._w2f.world2frame(s);
            geo_location d0 = this._w2f.world2frame(d);
            this.paintComponents(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));
            g2.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
            g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
            this.revalidate();
        }

        private void drawNode(node_data n, int r, Graphics g) {
            geo_location pos = n.getLocation();
            if (pos != null) {
                geo_location fp = this._w2f.world2frame(pos);

                g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);

                g.setColor(Color.red);
                g.setFont(new Font("MV Boli", Font.PLAIN, 15));
                g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 2 * r);
            }
        }

        private void drawAgents(Graphics g) {
            int k = 15;
            List<CL_Agent> rs = _ar.getAgents();
            int i = 0;
            double grade = 0;
            while (rs != null && i < rs.size()) {
                geo_location c = rs.get(i).getLocation();

                if (c != null) {

                    geo_location fp = this._w2f.world2frame(c);
                    grade = grade + rs.get(i).getValue();

                    BufferedImage img = null;
                    if (rs.get(i).getID() % 3 == 0) {
                        try {
                            img = ImageIO.read(new File("data/re.png"));

                        } catch (IOException e) {

                        }
                    } else if (rs.get(i).getID() % 3 == 1) {
                        try {
                            img = ImageIO.read(new File("data/pu.png"));

                        } catch (IOException e) {

                        }
                    } else {
                        try {
                            img = ImageIO.read(new File("data/yel.png"));

                        } catch (IOException e) {

                        }
                    }
                    g.drawImage(img, (int) fp.x() - k, (int) fp.y() - k, this);
                    //HERE YOU CAN ADD INFO REGARDING AGENT
                    g.setFont(new Font("MV Boli", Font.BOLD, 15));
                    g.drawString("" + grade, (int) fp.x() - k, (int) fp.y() - k);
                    grade = 0;
                }
                i++;
            }
            this.revalidate();
        }

        public void numGame(Graphics g) {
            g.setColor(Color.magenta.darker().darker().darker());
            g.setFont(new Font("MV Boli", Font.PLAIN, 40));
            g.drawString("Game: ", 70, 30);
            g.drawString("Score: ", 70, 70);
            g.setColor(Color.blue.darker().darker().darker());
            g.setFont(new Font("MV Boli", Font.PLAIN, 30));
            g.drawString("" + numGame, 200, 30);
        }

        public void drawGrade(Graphics g) {
            int grade = 0;
            List<CL_Agent> rs = _ar.getAgents();
            g.setColor(Color.pink);

            int i = 0, j = 20;
            while (rs != null && i < rs.size()) {
                BufferedImage img = null;
                if (rs.get(i).getID() % 3 == 0) {
                    g.setColor(new Color(251, 61, 50));
                    try {
                        img = ImageIO.read(new File("data/re.png"));

                    } catch (IOException e) {

                    }
                } else if (rs.get(i).getID() % 3 == 1) {
                    g.setColor(new Color(124, 54, 126));
                    try {
                        img = ImageIO.read(new File("data/pu.png"));

                    } catch (IOException e) {

                    }
                } else {
                    g.setColor(new Color(227, 212, 23));
                    try {
                        img = ImageIO.read(new File("data/yel.png"));

                    } catch (IOException e) {

                    }
                }
                g.drawImage(img, 730, 22 + i * j, this);
                g.setFont(new Font("MV Boli", Font.BOLD, 15));
                g.drawString("Agent " + rs.get(i).getID(), 760, 40 + i * j);
                g.drawString("value  " + rs.get(i).getValue(), 840, 40 + i * j);

                grade += rs.get(i).getValue();
                i++;
            }
            Graphics2D gg = (Graphics2D) g;
            gg.setColor(Color.blue.darker().darker().darker());
            gg.setFont(new Font("MV Boli", Font.PLAIN, 30));
            gg.drawString("" + grade, 200, 70);
        }

        private void drawGraph(Graphics g) {
            if (_ar != null) {
                directed_weighted_graph gg = _ar.getGraph();
                Iterator<node_data> iter = gg.getV().iterator();
                while (iter.hasNext()) {
                    node_data n = iter.next();
                    g.setColor(Color.gray.darker());
                    drawNode(n, 5, g);
                    Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
                    while (itr.hasNext()) {
                        edge_data e = itr.next();
                        g.setColor(Color.gray.darker());
                        drawEdge(e, g);
                    }
                }
            }
        }

        private void drawPokemons(Graphics g) {
            int k = 20;
            BufferedImage img = null;
            List<CL_Pokemon> fs = _ar.getPokemons();

            if (fs != null) {
                Iterator<CL_Pokemon> itr = fs.iterator();
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
                        geo_location fp = this._w2f.world2frame(c);
                        g.drawImage(img, (int) fp.x() - k, (int) fp.y() - k, this);
                    }
                }
            }
        }
    }
}
