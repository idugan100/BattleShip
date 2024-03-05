import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Image;

public class BattleShipView{

    // currently being used for testing
    private static Game game = new Game();
    private static JButton[][] coordinateGrid;


    public static void main(String[] args) {
        coordinateGrid= new JButton[10][10];
        //Create and set up the window.
        JFrame frame = new JFrame("BattleShip");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //create Play Button
        JButton playButton = new JButton("Place Ships Randomly");
        playButton.setFont(new java.awt.Font("Arial", Font.BOLD, 48)); 

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

        backgroundPanel.setLayout(new BorderLayout()); // Set the layout of the backgroundPanel to BorderLayout
        backgroundPanel.add(playButton, BorderLayout.SOUTH);


        JButton battleship = new JButton("batttle ship");
        JButton submarine = new JButton("sub ship");
        JButton patrol = new JButton("patrol ship");
        JButton carrier = new JButton("carrier ship");
        JButton destroyer = new JButton("destroyer ship");


        JPanel buttonPanel = new JPanel();
        buttonPanel.add(battleship);
        buttonPanel.add(submarine);
        buttonPanel.add(patrol);
        buttonPanel.add(carrier);
        buttonPanel.add(destroyer);
        backgroundPanel.add(buttonPanel, BorderLayout.NORTH);

        MyPanel dragAndDrop = new MyPanel();
        
        backgroundPanel.add(dragAndDrop, BorderLayout.CENTER);

        frame.add(backgroundPanel);
        
        frame.pack();
        frame.setMinimumSize(new Dimension(800, 800));
        frame.setLocationRelativeTo(null); // centers the window
        frame.setVisible(true);

        // Action listener for play button. Takes from main menu to game screen.
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.player.board.placeShipsRandomly();
                game.player.board.printBoard();

                // Create game window
                JFrame gameFrame = new JFrame("Game Window");
                gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                gameFrame.setLayout(new BorderLayout()); // Set layout for gameFrame

                // Create components for the game window
                JPanel gameBackgroundPanel = new JPanel();
                gameBackgroundPanel.setLayout(new BoxLayout(gameBackgroundPanel, BoxLayout.Y_AXIS));

                // Initialize player and targeting BoardPanel 
                JPanel playerBoardPanel = initializeBoardPanel();
                JPanel targetingBoardPanel = initializeEnemyBoardPanel();

                // Add boards to game background
                gameBackgroundPanel.add(targetingBoardPanel);
                gameBackgroundPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                gameBackgroundPanel.add(playerBoardPanel);


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

    private static JPanel initializeEnemyBoardPanel() {
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
                for (Ship ship : game.player.enemyBoard.getShipList()) {
                    for (Coordinate coord : ship.getCoordinates()) {
                        if (coord.getRow() == row && coord.getColumn() == col) {
                            hasShip = true;
                            break;
                        }
                    }
                    if (hasShip) {
                        cellButton.setBackground(Color.BLACK);
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
                        Coordinate result = game.shoot(shot);
                        Audio audio = new Audio();

                        // update button based on hit or miss
                        if (result.isHit()) {
                            audio.setFile("explosion.wav");
                            audio.play();
                            cellButton.setBackground(Color.RED);
                            cellButton.paintImmediately(cellButton.getVisibleRect());

                        } else {
                            audio.setFile("miss.wav");
                            audio.play();
                            cellButton.setBackground(Color.WHITE);
                            cellButton.paintImmediately(cellButton.getVisibleRect()); 
                        }
                        if(game.hasWon() || game.hasLost()) {
                            JOptionPane.showMessageDialog(null, "Game Over!");
                        }
                        try{
                            TimeUnit.MILLISECONDS.sleep(1000);
  
                        }catch(Exception err){

                        }

                        Coordinate incoming_shot=game.getShot();
                        if(incoming_shot.isHit()){
                            audio.setFile("explosion.wav");
                            audio.play();
                            coordinateGrid[incoming_shot.row][incoming_shot.column].setBackground(Color.RED);
                            cellButton.paintImmediately(cellButton.getVisibleRect());
                        }
                        else{
                            audio.setFile("miss.wav");
                            audio.play();
                            coordinateGrid[incoming_shot.row][incoming_shot.column].setBackground(Color.WHITE);
                            cellButton.paintImmediately(cellButton.getVisibleRect());
                        }  
                        cellButton.removeActionListener(this); 


                    }
                });

                boardPanel.add(cellButton);      
            }
        }
        return boardPanel;

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
                for (Ship ship : game.player.board.getShipList()) {
                    for (Coordinate coord : ship.getCoordinates()) {
                        if (coord.getRow() == row && coord.getColumn() == col) {
                            hasShip = true;
                            break;
                        }
                    }
                    if (hasShip) {
                        cellButton.setBackground(Color.BLACK);
                        break;
                    }

                }

                boardPanel.add(cellButton);   
                coordinateGrid[row][col]=cellButton;   
            }
        }
        return boardPanel;

    }

}
