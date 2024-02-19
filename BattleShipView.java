import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Image;

public class BattleShipView{

    public static void main(String[] args) {
        //Create and set up the window.
        JFrame frame = new JFrame("BattleShip");
        frame.setMinimumSize(new Dimension(800, 800));
        frame.setMaximumSize(new Dimension(1000,1200));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //create play button
        JButton playButton = new JButton("Play Game");
        playButton.setVerticalTextPosition(AbstractButton.CENTER);
        playButton.setHorizontalTextPosition(AbstractButton.CENTER);
        playButton.setFont(new java.awt.Font("Arial", Font.BOLD, 48));



        //create panel with radar background image
        JPanel panel = new JPanel() {
            // Override the paintComponent method to draw the background image
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon("icon.png"); // Replace "background.jpg" with your image file path
                Image image = imageIcon.getImage();

                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new GridBagLayout());

        //compose layouts
        panel.add(playButton, new GridBagConstraints());
        frame.add(panel);

        frame.pack();
        frame.setVisible(true);

        // Action listener for play button. Takes from main menu to game screen.
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create the new frame (game window)
                JFrame gameFrame = new JFrame("Game Window");
                gameFrame.setMinimumSize(new Dimension(800, 800));
                gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JLabel label = new JLabel("Game Started", SwingConstants.CENTER);
                gameFrame.add(label);

                JButton exitButton = new JButton("Exit to Main Menu");
                exitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // hide game window
                        gameFrame.setVisible(false);
                        // show main menu
                        frame.setVisible(true);
                    }
                });

                // Exit button
                gameFrame.setLayout(new FlowLayout()); // layout manager
                gameFrame.add(exitButton);

                // make game window visible
                gameFrame.pack();
                gameFrame.setVisible(true);

                // hide main menu frame
                frame.setVisible(false);
            }
        });
    }
}
