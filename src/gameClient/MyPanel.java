package gameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MyPanel extends JPanel {
    public MyPanel(String type) {
        if (type.equals("login")) {
            init();
            login();
        }
        if (type.equals("game")) {
            //game();
        }
    }
    /*public void paint(Graphics g){
        g.clearRect(0,0,this.getWidth(),this.getHeight());
    }*/

    private void init() {
        this.setBackground(Color.pink);
        this.setLayout(null);
    }

    private void login() {
        idANDnum();
        imagePok();
        buttonStart();
    }

    private void idANDnum() {
        JLabel id = new JLabel("          Id");
        id.setBounds(10, 20, 80, 25);
        id.setForeground(Color.RED.darker());
        id.setBackground(Color.gray.brighter());
        id.setOpaque(true);

        add(id);

        JTextField userText = new JTextField();
        userText.setBounds(100, 20, 165, 25);
        this.add(userText);

        userText.addKeyListener(new KeyAdapter() {
                                    public void keyPressed(KeyEvent ke) {
                                        String value = userText.getText();
                                        int l = value.length();
                                        if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') {
                                            userText.setEditable(true);
                                        } else {
                                            userText.setEditable(false);
                                            //userText.setText("");

                                            userText.setText("* Enter only numeric digits(0-9)");

                                        }
                                    }
                                }
        );

        JLabel gameKey = new JLabel("   Key Game");
        gameKey.setBounds(10, 50, 80, 25);
        gameKey.setForeground(Color.RED.darker());
        gameKey.setBackground(Color.gray.brighter());
        gameKey.setOpaque(true);

        this.add(gameKey);

        JTextField userNum = new JTextField();
        userNum.setBounds(100, 50, 165, 25);
        this.add(userNum);

        JButton button = new JButton("Default");
        button.setBounds(150, 100, 100, 25);
        button.setForeground(Color.red.darker());
        button.setBackground(Color.orange);
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

    }

    private void imagePok() {
        JLabel temp = new JLabel();
        temp.setIcon(new ImageIcon("data/pok.png"));
        temp.setBounds(275, 70, 70, 70);
        this.add(temp);
    }

    private void buttonStart() {
        JButton button = new JButton("Start Game");
        button.setBounds(40, 100, 100, 25);
        button.setForeground(Color.red.darker());
        button.setBackground(Color.orange);
        this.add(button);

        /*button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == button) game();
            }
        });*/
    }
    /*private void game(){
        this.repaint();
    }*/
}


