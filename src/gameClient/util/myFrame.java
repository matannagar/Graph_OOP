package gameClient.util;

import api.*;
import gameClient.Arena;
import gameClient.CL_Agent;
import gameClient.CL_Pokemon;
import gameClient.Ex2;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
    private Login panel;

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
        panel = new Login();
        this.add(panel);
        this.setVisible(true);
        panel.setVisible(true);
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
//            this.revalidate();
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
                for (node_data n : gg.getV()) {
                    g.setColor(Color.gray.darker());
                    drawNode(n, 5, g);
                    for (edge_data e : gg.getE(n.getKey())) {
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
                for (CL_Pokemon f : fs) {
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

    /**
     * this class is responsible for the login screen of the game
     * where one enters his ID number and a chosen scenario for the game.
     */
    public static class Login extends JPanel {

        public Login() {
            super();
            this.setLayout(null);
            idANDnum();
            title();
            imageLogo();
/*
            if(Ex2.numGame!=-1&&Ex2.ID!=-1) Ex2.client.start();
*/
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
            if (Ex2.ID >= 0) userText.setText(Ex2.ID + "");

            this.add(userText);

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
            if (Ex2.numGame >= 0) userNum.setText(Ex2.numGame + "");

            this.add(userNum);

            JButton button = new JButton("Reut");
            button.setBounds(30, 140, 80, 25);
            button.setForeground(Color.red.darker());
            button.setBackground(Color.orange);
            button.setBorder(b);
            button.setFont(new Font("MV Boli", Font.BOLD, 15));

            this.add(button);
            button.addActionListener(e -> {

                if (e.getSource() == button) {
                    // userText.setEditable(false);
                    userText.setText("208196709");
                    userNum.setText("1");
                }
            });

            JButton matan = new JButton("Matan");
            matan.setBounds(210, 140, 80, 25);
            matan.setForeground(Color.red.darker());
            matan.setBackground(Color.orange);
            matan.setBorder(b);
            matan.setFont(new Font("MV Boli", Font.BOLD, 15));

            this.add(matan);
            matan.addActionListener(e -> {

                if (e.getSource() == matan) {
                    // userText.setEditable(false);
                    userText.setText("206240301");
                    userNum.setText("1");
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
                    Ex2.numGame = Integer.parseInt(userNum.getText()));
            button1.addActionListener(e ->
                    Ex2.ID = Integer.parseInt(userText.getText()));
            button1.addActionListener(e -> Ex2.client.start());
            button1.addActionListener(e ->  Ex2._win.setVisible(false));

        }

        private void imageLogo() {
            JLabel temp = new JLabel();
            temp.setIcon(new ImageIcon("data/backround.jpg"));
            temp.setBounds(0, 0, 350, 197);
            this.add(temp);
        }
    }
}
