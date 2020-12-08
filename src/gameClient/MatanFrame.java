package gameClient;

import javax.swing.*;
import java.awt.*;

public class MatanFrame extends JFrame {
    trythis thrythis = new trythis();
    public static void main(String[] args) {

  ReutPanel panel = new ReutPanel("hello",600,600);
        trythis trythis = new trythis();
        MatanFrame frame = new MatanFrame(trythis);
        System.out.println(frame);
    }

    public MatanFrame(trythis temp) {
        this.setSize(600, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(temp);
        this.setVisible(true);
    }
}
