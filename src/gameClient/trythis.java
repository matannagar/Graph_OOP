package gameClient;

import javax.swing.*;
import java.awt.*;

public class trythis extends JPanel {

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.black);
        Graphics gg = (Graphics2D) g;
        gg.setColor(Color.red);
        gg.drawLine(0, 0, 400, 400);
    }

}
