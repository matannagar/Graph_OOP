package GUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Login extends JPanel {
    public Login(String type) {
        super();
        this.setLayout(null);
        if (type.equals("login")) {
            login();
        }
        if (type.equals("game")) {
            //game();
        }
    }
    /*public void paint(Graphics g){
        g.clearRect(0,0,this.getWidth(),this.getHeight());
    }*/

    /*private void temp(){
        JFrame frame= new JFrame();
        JLabel t = new JLabel("Catch Them All");
        t.setFont(new Font("MV Boli", Font.BOLD, 30));
        t.setForeground(Color.red.darker());
        t.setVerticalAlignment(JLabel.CENTER);
        frame.add(t);

        frame.setVisible(true);
        frame.pack();
    }*/


  /*  private void init() {
        //this.setBackground(Color.pink);
        this.setLayout(null);
    }*/

    private void login() {
        idANDnum();
        title();
        // imagePok();
        imageLogo();
        //buttonStart();
    }
    private void title() {
        JLabel t = new JLabel("Catch Them All");

        t.setFont(new Font("MV Boli", Font.BOLD, 30));
        t.setForeground(Color.white);

        t.setBounds(40, 20, 350, 40);

        // t.setOpaque(true);
        add(t);
        t = new JLabel("Catch Them All");
        t.setFont(new Font("MV Boli", Font.BOLD, 30));
        t.setForeground(Color.red.darker());
        // t.setVerticalAlignment(JLabel.CENTER);

        t.setBounds(42, 22, 350, 40);

        // t.setOpaque(true);
        add(t);
    }

    private void idANDnum() {
        JLabel id = new JLabel("     Id");
        id.setBounds(10, 70, 80, 25);
        Border b= BorderFactory.createLineBorder(Color.RED.darker(),2);
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
        this.add(userNum);

        JButton button = new JButton("Default");
        button.setBounds(150, 140, 100, 25);
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

        JButton button1 = new JButton("Start");
        button1.setBounds(40, 140, 100, 25);
        button1.setForeground(Color.red.darker());
        button1.setBackground(Color.orange);
        button1.setFont(new Font("MV Boli", Font.BOLD, 15));
        button1.setBorder(b);
        this.add(button1);

    }

    private void imagePok() {
        JLabel temp = new JLabel();
        temp.setIcon(new ImageIcon("data/pok.png"));
        temp.setBounds(275, 70, 70, 70);
        this.add(temp);
    }

    private void imageLogo() {
        JLabel temp = new JLabel();


        temp.setIcon(new ImageIcon("data/backround.jpg"));
        temp.setBounds(0, 0, 350, 197);
        this.add(temp);
    }


    private void buttonStart() {
        JButton button = new JButton("Start");
        button.setBounds(40, 140, 100, 25);
        Border b= BorderFactory.createLineBorder(Color.RED.darker(),2);
        button.setForeground(Color.red.darker());
        button.setBackground(Color.orange);
        button.setFont(new Font("MV Boli", Font.BOLD, 15));
        button.setBorder(b);
        this.add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //  if (e.getSource() == button) game();
            }
        });
    }
   /* public void paint(Graphics g){
       g.drawRect(100,200,56,80);
    }*/
//
//    private void game(){
//        setBounds(0,0,1000, 700);
//    }

}