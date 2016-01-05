package unice.display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by gillot on 05/01/16.
 */

/**
 * @author  Thomas gillot, Loic Rose
 * Setup the beginning : the user can enter the number of steps and the neighbourhood type
 */

public class Begining extends JFrame {

    private String neighbourhood;
    private int steps;
    private boolean simuation_begin;

    private JPanel container = new JPanel();
    private JLabel label0 = new JLabel("                       Enter your settings:                     ");
    private JTextField jtf = new JTextField();
    private JLabel label = new JLabel("Step Number:");
    private JComboBox combo = new JComboBox();
    private JLabel label2 = new JLabel("Neighbourhood type:");

    public Begining() {
        /**
         * Setup the frame
         */
        simuation_begin = false;

        setTitle("Welcome to the Epidemic Simulator !");
        this.setLocationRelativeTo(null);
        setSize(400, 250);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        container.setLayout(new BorderLayout());

        JPanel top = new JPanel();
        Font police = new Font("Arial",Font.BOLD,14);

        label0.setFont(new Font("Arial",Font.BOLD,20));
        top.add(label0);

        jtf.setFont(police);
        jtf.setPreferredSize(new Dimension(150,30));
        jtf.setText("500");
        jtf.addKeyListener(new ClavierListener());
        top.add(label);
        top.add(jtf);
        steps = 500;

        JPanel back = new JPanel();
        combo.setPreferredSize(new Dimension(150,30));
        combo.addItem("FOUR");
        combo.addItem("HEIGHT");
        combo.setSelectedIndex(0);
        neighbourhood = "FOUR";

        top.add(label2);
        top.add(combo);

        JButton button = new JButton("Enter");
        button.addActionListener(new BoutonListener());
        top.add(button);

        this.setContentPane(container);
        this.setContentPane(top);
        this.setVisible(true);
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public int getSteps() {
        return steps;
    }

    public boolean isSimuation_begin() {
        return simuation_begin;
    }

    public void close(){
        this.dispose();
    }


    private class BoutonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {

            simuation_begin = true;
            neighbourhood = combo.getSelectedItem().toString();
            steps = Integer.parseInt(jtf.getText());
        }
    }

    /**
     * This class doesn't allow to write any character no numeric
     * Only allow character in [0-9]
     */
    private class ClavierListener implements KeyListener{
        /**
         * Source : openClassRoom : Controle du clavier : l'interface KeyListener
         * @param e
         */
        public void keyReleased(KeyEvent e){
            if(!isNumeric(e.getKeyChar())){
                jtf.setText(jtf.getText().replace(String.valueOf(e.getKeyChar()),""));
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {}

        @Override
        public void keyTyped(KeyEvent e) {}

        private boolean isNumeric(char car){
            try{
                Integer.parseInt(String.valueOf(car));
            }catch(NumberFormatException e){
                return false;
            }
            return true;
        }
    }
}
