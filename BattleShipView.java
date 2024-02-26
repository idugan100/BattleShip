import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Image;

public class BattleShipView{

    // currently being used for testing
    private static Board board = new Board();
    private static Player player = new Player();

    public static void main(String[] args) {
        //Create and set up the window.
        JFrame frame = new JFrame("BattleShip");
        //frame.setMinimumSize(new Dimension(800, 800));
        //frame.setMaximumSize(new Dimension(1000,1200));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //create Play Button
        JButton playButton = new JButton("Play Game");
        playButton.setFont(new java.awt.Font("Arial", Font.BOLD, 48)); 

        /*playButton.setVerticalTextPosition(AbstractButton.CENTER);
        playButton.setHorizontalTextPosition(AbstractButton.CENTER);
        playButton.setFont(new java.awt.Font("Arial", Font.BOLD, 48));*/

        // Background Image Panel (Main Menu)
        JPanel backgroundPanel = new JPanel() {
            // Override the paintComponent method to draw the background image
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon("icon.png"); // Replace "background.jpg" with your image file path
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());
        backgroundPanel.add(playButton, new GridBagConstraints());
        frame.add(backgroundPanel);

        frame.pack();
        frame.setMinimumSize(new Dimension(800, 800));
        frame.setLocationRelativeTo(null); // centers the window
        frame.setVisible(true);

        // Action listener for play button. Takes from main menu to game screen.
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.add_ships();
                player.board.placeShipsRandomly();

                // Create game window
                JFrame gameFrame = new JFrame("Game Window");
                gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                gameFrame.setLayout(new BorderLayout()); // Set layout for gameFrame

                // Create components for the game window
                JPanel gameBackgroundPanel = new JPanel();
                //gameBackgroundPanel.setLayout(new GridLayout(2, 1)); //  layout to arrange the two boards
                gameBackgroundPanel.setLayout(new BoxLayout(gameBackgroundPanel, BoxLayout.Y_AXIS));

                // Initialize player and targeting BoardPanel 
                JPanel playerBoardPanel = initializeBoardPanel();
                JPanel targetingBoardPanel = initializeBoardPanel();

                // Add boards to game background
                gameBackgroundPanel.add(playerBoardPanel);
                gameBackgroundPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                gameBackgroundPanel.add(targetingBoardPanel);

                // Create and add label and exit button
                JLabel label = new JLabel("Game Started", SwingConstants.CENTER);
                JButton exitButton = new JButton("Exit to Main Menu");

                // Add components to gameFrame
                gameFrame.add(label, BorderLayout.NORTH);
                gameFrame.add(gameBackgroundPanel, BorderLayout.CENTER);
                gameFrame.add(exitButton, BorderLayout.SOUTH);

                // Exit button functionality
                exitButton.addActionListener(new ActionListener() {
                   @Override
                   public void actionPerformed(ActionEvent e) {
                    gameFrame.setVisible(false);
                    frame.setVisible(true); 
                   } 
                });

                gameFrame.pack();
                gameFrame.setLocationRelativeTo(null); // center game window
                gameFrame.setVisible(true);
                frame.setVisible(false);

            }
        });
    }

    private static JPanel initializeBoardPanel() {
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(10, 10));
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        boardPanel.setPreferredSize(new Dimension(400, 400));
        
        // Populate board panel based on board state
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                JButton cellButton = new JButton();
                cellButton.setBackground(Color.BLUE);
                cellButton.setOpaque(true);
                cellButton.setPreferredSize(new Dimension(40, 40));
                cellButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

                // ADDED FOR TESTING
                boolean hasShip = false;
                for (Ship ship : player.board.getShipList()) {
                    for (Coordinate coord : ship.getCoordinates()) {
                        if (coord.getRow() == row && coord.getColumn() == col) {
                            hasShip = true;
                            break;
                        }
                    }
                    if (hasShip) {
                        cellButton.setBackground(Color.RED);
                        break;
                    }

                }

                // action listener for shots

                final int finalRow = row;
                final int finalCol = col;
                cellButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Coordinate shot = new Coordinate(finalRow, finalCol);
                        Coordinate result = player.board.handleShot(shot);

                        // update button based on hit or miss
                        if (result.isHit()) {
                            cellButton.setBackground(Color.BLACK);

                        } else {
                            cellButton.setBackground(Color.WHITE); // miss shot

                        }
                        cellButton.setEnabled(false);

                        if(player.board.allShipsSunk()) {
                            JOptionPane.showMessageDialog(null, "Game Over!");
                        }
                    }
                });

                boardPanel.add(cellButton);      
            }
        }
        return boardPanel;

    }
}
