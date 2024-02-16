import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BattleShipView{

    public static void main(String[] args) {
        //Create and set up the window.
        JFrame frame = new JFrame("BattleShip");
        frame.setMinimumSize(new Dimension(1000, 800));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton playButton = new JButton("Play Game");
        playButton.setVerticalTextPosition(AbstractButton.CENTER);
        playButton.setHorizontalTextPosition(AbstractButton.LEADING);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(playButton);

        // Add the panel with buttons to the frame
        frame.add(buttonPanel);

        frame.pack();
        frame.setVisible(true);
    }
}
