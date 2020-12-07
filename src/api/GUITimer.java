package api;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class GUITimer extends JFrame implements ItemListener {

    private static final long serialVersionUID = 5924880907001755076L;

    JLabel _win;
    JComboBox<Integer> jcb;
    NumberFormat format;

    public Timer timer;
    public long initial;
    public long ttime2;
    public String init_time;
    public long remaining;

    public GUITimer(game_service game) {
        JPanel timePanel = new JPanel(){
            @Override
            public Dimension getPreferredSize(){
                return new Dimension(200,70);
            }
        };

        _win = new JLabel("01:00");
//        _win.setHorizontalAlignment((int) CENTER_ALIGNMENT);
        _win.setForeground(Color.yellow);
        _win.setBackground(Color.BLACK);
        _win.setOpaque(true);
        _win.setFont(new Font("Arial", Font.CENTER_BASELINE, 50));
        timePanel.add(_win);
       // init_time = "1";
        init_time = (game.timeToEnd())*-1+"";
        getContentPane().add(timePanel, BorderLayout.CENTER);
        updateDisplay();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationByPlatform(true);
        setVisible(true);
    }

  /*  public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUITimer();
            }
        });
    }*/

    //run the timer on screen
    void updateDisplay() {
        Timeclass tc = new Timeclass();
        timer = new Timer(1, tc);
        initial = System.currentTimeMillis();
        timer.start();
    }

    // code that is invoked by swing timer for every second passed
    public class Timeclass implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            remaining = convertTime();
            long current = System.currentTimeMillis();
            long elapsed = current - initial;
            remaining -= elapsed;
            // initial = current;

            format = NumberFormat.getNumberInstance();
            format.setMinimumIntegerDigits(2);

            if (remaining < 0)
                remaining = (long) 0;
            int minutes = (int) (remaining / 60000);
            int seconds = (int) ((remaining % 60000) / 1000);
            _win.setText(format.format(minutes) + ":"
                    + format.format(seconds));

            if (remaining == 0) {
                _win.setText("No More\n Pokemons");
                _win.setFont(new Font("Arial", Font.CENTER_BASELINE, 30));
                timer.stop();
            }
        }
    }
    // get the number of minutes chosen by the user and activate convertTime
    // method
    public void itemStateChanged(ItemEvent ie) {

        init_time = (String) jcb.getSelectedItem().toString();
        convertTime();
    }

    // first need to convert no. of minutes from string to long.
    // then need to convert that to milliseconds.
    public long convertTime() {

        ttime2 = Long.parseLong(init_time);
        long converted = (ttime2 * 60000) + 1000;
        return converted;
    }

}