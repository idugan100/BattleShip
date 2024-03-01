import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Image;

public class DEP_BattleShipView {

    // currently being used for testing
    private static Game game = new Game();
    private static JButton[][] coordinateGrid;
    private JPanel mainMenuPanel;
    private JFrame frame;

    public static void main(String[] args) {
        coordinateGrid = new JButton[10][10];
        
        //Create and set up the window.
        JFrame frame = new JFrame("BattleShip");;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //create Play Button
        JButton playButton = new JButton("Play Game");
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
        
                game.player.board.placeShipsRandomly();
                game.player.board.printBoard();
        
                // Create game window
                JFrame gameFrame = new JFrame("Game Window");
                gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                gameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
                // Background Image Panel with GridBagLayout for centering
                JPanel backgroundPanel = new JPanel(new GridBagLayout()) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        ImageIcon imageIcon = new ImageIcon("GamePanelBackground.jpg"); // Replace with your background image path
                        Image image = imageIcon.getImage();
                        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                    }
                };
        
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0; // Center horizontally
                gbc.gridwidth = GridBagConstraints.REMAINDER; // End row
                gbc.weightx = 0; // Do not allow horizontal expansion
                gbc.weighty = 0; // Do not allow vertical expansion too much
                gbc.fill = GridBagConstraints.NONE; // Do not fill space, respect component's size
                gbc.anchor = GridBagConstraints.CENTER; // Center component
        
                // Set a reasonable fixed size for the boards
                Dimension boardSize = new Dimension(400, 400);
        
                // Initialize enemy BoardPanel and set preferred size for centering
                JPanel enemyBoardPanel = initializeEnemyBoardPanel();
                enemyBoardPanel.setPreferredSize(boardSize);
                gbc.gridy = 0; // First row for enemy board
                backgroundPanel.add(enemyBoardPanel, gbc);
        
                // Initialize player BoardPanel and set preferred size for centering
                JPanel playerBoardPanel = initializeBoardPanel();
                playerBoardPanel.setPreferredSize(boardSize);
                gbc.gridy = 1; // Second row for player board
                gbc.insets = new Insets(10, 0, 10, 0);
                backgroundPanel.add(playerBoardPanel, gbc);
        
                // Adjustments for exit button
                JButton exitButton = new JButton("Exit");
                exitButton.addActionListener(ev -> {
                    gameFrame.setVisible(false);
                    frame.setVisible(true);
                });
                gbc.gridy = 2; // Third row for the exit button
                gbc.insets = new Insets(10, 0, 0, 0);
                backgroundPanel.add(exitButton, gbc);
        
                gameFrame.setContentPane(backgroundPanel);
                gameFrame.pack(); // Pack to respect preferred sizes
                gameFrame.setVisible(true);
            }
        });
        
        
        // playButton.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {

        //         game.player.board.placeShipsRandomly();
        //         game.player.board.printBoard();

        //         // Create game window
        //         JFrame gameFrame = new JFrame("Game Window");
        //         gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //         gameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //         gameFrame.setLayout(new BorderLayout()); // Set layout for gameFrame

        //         // Create components for the game window
        //         JPanel gameBackgroundPanel = new JPanel();
        //         gameBackgroundPanel.setLayout(new BoxLayout(gameBackgroundPanel, BoxLayout.Y_AXIS));

        //         // Initialize player and targeting BoardPanel 
        //         JPanel playerBoardPanel = initializeBoardPanel();
        //         JPanel targetingBoardPanel = initializeEnemyBoardPanel();

        //         // Add boards to game background
        //         gameBackgroundPanel.add(targetingBoardPanel);
        //         gameBackgroundPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        //         gameBackgroundPanel.add(playerBoardPanel);


        //         // Create and add label and exit button
        //         JLabel label = new JLabel("Game Started", SwingConstants.CENTER);
        //         JButton exitButton = new JButton("Exit to Main Menu");

        //         // Add components to gameFrame
        //         gameFrame.add(label, BorderLayout.NORTH);
        //         gameFrame.add(gameBackgroundPanel, BorderLayout.CENTER);
        //         gameFrame.add(exitButton, BorderLayout.SOUTH);

        //         // Exit button functionality
        //         exitButton.addActionListener(new ActionListener() {
        //            @Override
        //            public void actionPerformed(ActionEvent e) {
        //             gameFrame.setVisible(false);
        //             frame.setVisible(true); 
        //            } 
        //         });

        //         gameFrame.pack();
        //         gameFrame.setLocationRelativeTo(null); // center game window
        //         gameFrame.setVisible(true);
        //         frame.setVisible(false);

        //     }
        // });
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
                            cellButton.setIcon(ship.getImageIcon());
                            break;
                        }
                    }
                    if (hasShip) {
                        //cellButton.setBackground(Color.BLACK);
                        break;
                    }

                }

                boardPanel.add(cellButton);   
                coordinateGrid[row][col]=cellButton;   
            }
        }
        return boardPanel;

    }

public class DEP_BattleShipView {
    // Your existing fields and methods...

    // Method to initialize and make ships draggable
    private void makeShipsDraggable(JFrame gameFrame) {
        JPanel shipsPanel = new JPanel(new FlowLayout()); // Or any layout that suits your design
        shipsPanel.setPreferredSize(new Dimension(400, 100)); // Example size

        // Example creating ship objects as JLabels
        JLabel shipLabel = new JLabel("Ship");
        shipLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        shipsPanel.add(shipLabel);

        // Attach draggable logic
        DraggableComponent draggable = new DraggableComponent();
        shipLabel.addMouseListener(draggable);
        shipLabel.addMouseMotionListener(draggable);

        // Add shipsPanel to your gameFrame
        gameFrame.add(shipsPanel, BorderLayout.SOUTH);

        // Your method to show the gameFrame...
    }

    // DraggableComponent class
    private class DraggableComponent extends MouseAdapter {
        private Point imageCorner;
        private Point prevPt;

        @Override
        public void mousePressed(MouseEvent e) {
            prevPt = e.getPoint();
            Component comp = (Component) e.getSource();
            imageCorner = comp.getLocation();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Point currentPt = e.getPoint();
            Component comp = (Component) e.getSource();

            imageCorner.translate(
                currentPt.x - prevPt.x,
                currentPt.y - prevPt.y
            );

            comp.setLocation(imageCorner);
            comp.getParent().repaint();
        }
    }

}


