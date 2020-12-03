package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.StyledEditorKit;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph - you are welcome to use this class - yet keep in mind
 * that the code is not well written in order to force you improve the
 * code and not to take it "as is".
 */
public class MyFrame extends JFrame {
    private int _ind;
    private Arena _ar;
    private Range2Range _w2f;

    public MyFrame(String a) {
        super(a);
        int _ind = 0;
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
    }

    public void paint(Graphics g) {
        int w = this.getWidth();
        int h = this.getHeight();
        g.clearRect(0, 0, w, h);
        updateFrame();
        drawPokemons(g);
        drawGraph(g);
        drawAgents(g);
        drawInfo(g);
        drawGradeAg(g);
        drawTimer(g);
    }

    private void drawInfo(Graphics g) {
        List<String> str = _ar.get_info();
        String dt = "none";
        for (int i = 0; i < str.size(); i++) {
            g.drawString(str.get(i) + " dt: " + dt, 100, 60 + i * 20);
        }

    }

    private void drawGraph(Graphics g) {
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
    }

    private void drawPokemons(Graphics g) {
        List<CL_Pokemon> fs = _ar.getPokemons();
        if (fs != null) {
            Iterator<CL_Pokemon> itr = fs.iterator();

            while (itr.hasNext()) {

                CL_Pokemon f = itr.next();
                Point3D c = f.getLocation();
                int r = 10;
                g.setColor(Color.green);
                if (f.getType() < 0) {
                    g.setColor(Color.orange);
                }
                if (c != null) {

                    geo_location fp = this._w2f.world2frame(c);
                    g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
                    /////////////////Matan's code
                    BufferedImage img = null;
                    try {
                        img = ImageIO.read(new File("data/pok.png"));
                    } catch (IOException e) {

                    }
                    ;
//					g.drawImage(img,((getWidth()-img.getWidth()) /3) ,((getHeight()-img.getHeight())/3),this);
                    int k = 25;
                    g.drawImage(img, (int) fp.x() - k, (int) fp.y() - k, this);
                    /////////////////Matan's code
                    //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);

                }
            }
        }
    }

    private void drawAgents(Graphics g) {
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
//				g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
                /////////////////Matan's code
                BufferedImage img = null;
                try {
                    img = ImageIO.read(new File("data/mypac.png"));
                } catch (IOException e) {

                }
                ;
//					g.drawImage(img,((getWidth()-img.getWidth()) /3) ,((getHeight()-img.getHeight())/3),this);
                int k = 25;
                g.drawImage(img, (int) fp.x() - k, (int) fp.y() - k, this);
                /////////////////Matan's code
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
        List<CL_Agent> rs = _ar.getAgents();
        g.setColor(Color.red);

        g.drawLine(815, 50, 950, 50);
        g.drawLine(815, 80 + 20 * rs.size(), 950, 80 + 20 * rs.size());

        g.setColor(Color.blue);
        int i = 0, j = 20;
        while (rs != null && i < rs.size()) {
            g.drawString("Agent- " + rs.get(i).getID() + ": value- " + rs.get(i).getValue(), 830, 80 + i * j);
            i++;
        }
        //Matan's commet:
        //lets add the num_of_scenario + total_points so far + num_of_steps + num_of_agnts ...
        //Matan's commet
    }

    //////////Matan's timer
    public void drawTimer(Graphics g) {
        g.setColor(Color.red);
        g.drawString("Remaining time: 00:00"+" ,Insert timer here", 400, 120);
        /////////Matan's timer
    }
}