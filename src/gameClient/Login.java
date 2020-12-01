package gameClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login implements ActionListener {
    static JTextField userText;
    static JTextField userNum;
    static int id=-1;
    static int num=-1;


    public int getNum(){
        return num;
    }
    public int getId(){
        return id;
    }

    public void user() {
        JFrame frame= new JFrame();
        frame.setSize(350,200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel =new JPanel();

        panel.setLayout(null);

        JLabel id= new JLabel("Id");
        id.setBounds(10,20,80,25);
        panel.add(id);

        frame.add(panel);

        userText= new JTextField();
        userText.setBounds(100,20,165,25);
        panel.add(userText);

        JLabel gameKey= new JLabel("KeyGame");
        gameKey.setBounds(10,50,80,25);

        panel.add(gameKey);

        userNum= new JTextField();
        userNum.setBounds(100,50,165,25);
        panel.add(userNum);

        JButton button= new JButton("Start Game");
        button.setBounds(120,100,120,25);
        panel.add(button);

        frame.setVisible(true);

        button.addActionListener(new Login());
//        if(id!=0&&num!=0)
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        id= Integer.valueOf(userText.getText());
        num= Integer.valueOf(userNum.getText());
        ///////////////make sure that the number is valid
        System.exit(0);
    }
}
