import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
import javax.swing.border.Border;
import java.util.List;
import java.util.Vector;
import java.awt.Image;

public class DEP_BattleShipView {

    // Static variables for game logic and UI components
    private static Game game = new Game(); // Game logic handler
    private static JButton[][] coordinateGrid; // Grid for game coordinates
    private static JPanel mainMenuPanel; // Panel for the main menu
    private static JFrame frame; // Main application frame

    public static void main(String[] args) {
        coordinateGrid = new JButton[10][10]; // Initialize the coordinate grid
        
        //Create and set up the window.
        JFrame frame = new JFrame("BattleShip"); // Main game window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit application on close
        
        //create Play Button
        JButton playButton = new JButton("Play Game"); // Button to start the game
        playButton.setFont(new java.awt.Font("Arial", Font.BOLD, 48)); // Set font of the play button

        // Background Image Panel (Main Menu)
        JPanel backgroundPanel = new JPanel() {
            // Override the paintComponent method to draw the background image
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon("icon.png"); // Load the background image
                Image image = imageIcon.getImage(); // Get the image from the icon
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this); // Draw the image to fit the panel
            }
        };

        backgroundPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for flexible layout
        backgroundPanel.add(playButton, new GridBagConstraints()); // Add play button to the background panel
        frame.add(backgroundPanel); // Add background panel to the frame

        frame.pack(); // Pack the frame to fit the preferred sizes of its components
        frame.setMinimumSize(new Dimension(800, 800)); // Set minimum size of the frame
        frame.setLocationRelativeTo(null); // Center the window on the screen
        frame.setVisible(true); // Make the frame visible


        // Action listener for play button. Takes from main menu to game screen.
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.player.board.placeShipsRandomly(); // Randomly place ships on the player's board
                game.player.board.printBoard(); // Print the board to the console for debugging

                // Create game window
                JFrame gameFrame = new JFrame("Game Window"); // Frame for the game window
                gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit application on close
                gameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the game window
        
                // Background Image Panel with GridBagLayout for centering
                JPanel backgroundPanel = new JPanel(new GridBagLayout()) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        ImageIcon imageIcon = new ImageIcon("GamePanelBackground.jpg"); // Load the game panel background image
                        Image image = imageIcon.getImage(); // Get the image from the icon
                        g.drawImage(image, 0, 0, getWidth(), getHeight(), this); // Draw the image to fit the panel
                    }
                };

                JPanel shipContainer = intializeShipContainer();

                // Adjust GridBagConstraints for shipContainer
                GridBagConstraints gbcShipContainer = new GridBagConstraints();
                gbcShipContainer.gridx = 0; // Position it in the first column
                gbcShipContainer.gridy = 1; // Start from the first row
                gbcShipContainer.gridheight = GridBagConstraints.REMAINDER; // Span across the height of the window
                
                gbcShipContainer.insets = new Insets(10, 0, 100, 20); // Add some spacing to the right
                backgroundPanel.add(shipContainer, gbcShipContainer); // Add shipContainer to the backgroundPanel
        
                Vector<Ship> ships = game.player.board.getShipList();
                populateShipContainer(shipContainer, ships);

                GridBagConstraints gbc = new GridBagConstraints(); // Constraints for components in GridBagLayout
                gbc.gridx = 1; // Center horizontally
                gbc.gridwidth = GridBagConstraints.REMAINDER; // End row
                gbc.weightx = 0; // Do not allow horizontal expansion
                gbc.weighty = 0; // Do not allow vertical expansion too much
                gbc.fill = GridBagConstraints.NONE; // Do not fill space, respect component's size
                gbc.anchor = GridBagConstraints.CENTER; // Center component
                
                // Set a reasonable fixed size for the boards
                Dimension boardSize = new Dimension(400, 400); // Preferred size for the board panels
        
                // Initialize enemy BoardPanel and set preferred size for centering
                JPanel enemyBoardPanel = initializeEnemyBoardPanel(); // Initialize the enemy board panel
                enemyBoardPanel.setPreferredSize(boardSize); // Set preferred size
                gbc.gridy = 0; // First row for enemy board
                backgroundPanel.add(enemyBoardPanel, gbc); // Add enemy board panel to the background panel
                
                JButton shipPlacementButton = new JButton("Place Ships");
                shipPlacementButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Object[] options = {"Randomly", "Manually"};
                        int choice = JOptionPane.showOptionDialog(gameFrame,
                        "Would you like to place your ships manually?",
                        "Ship Placement",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, options, options[1]);

                        if (choice == JOptionPane.YES_OPTION){
                            
                            // Initialize player BoardPanel and set preferred size for centering
                            JPanel playerBoardPanel = initializeBoardPanel(); // Initialize the player board panel
                            Dimension boardSize = new Dimension(400, 400);
                            playerBoardPanel.setPreferredSize(boardSize);
                            
                            GridBagConstraints gbc = new GridBagConstraints();
                            gbc.gridx = 1;
                            gbc.gridy = 1; // Second row for player board
                            gbc.gridwidth = GridBagConstraints.REMAINDER;
                            gbc.fill = GridBagConstraints.BOTH;
                            gbc.anchor = GridBagConstraints.CENTER;
                            gbc.insets = new Insets(10, 0, 10, 0); // Add insets for spacing
                            backgroundPanel.add(playerBoardPanel, gbc); // Add player board panel to the background panel
                            gameFrame.revalidate();
                            gameFrame.repaint();
                            
                            shipContainer.removeAll();
                            shipContainer.revalidate();
                            shipContainer.repaint();
                        }

                    }
                });


        
                // Adjustments for exit button
                JButton exitButton = new JButton("Exit"); // Button to exit the game window
                exitButton.addActionListener(ev -> {
                    gameFrame.setVisible(false); // Hide the game window
                    frame.setVisible(true); // Show the main window
                });


                GridBagConstraints gbcPlacementButton = new GridBagConstraints(); // Constraints for the ship placement button
                gbcPlacementButton.gridx = 0; // Center horizontally
                gbcPlacementButton.gridy = 3; // Position after the exit button
                gbcPlacementButton.gridwidth = GridBagConstraints.REMAINDER; // End row
                gbcPlacementButton.fill = GridBagConstraints.HORIZONTAL; // Fill space horizontally
                gbcPlacementButton.anchor = GridBagConstraints.PAGE_END; // Anchor to the bottom of the window
                gbcPlacementButton.insets = new Insets(10, 0, 10, 0); // Add some spacing
                backgroundPanel.add(shipPlacementButton, gbcPlacementButton); // Add the button to the background panel

                gbc.gridy = 2; // Third row for the exit button
                gbc.insets = new Insets(10, 0, 0, 0); // Adjust insets for the exit button
                backgroundPanel.add(exitButton, gbc); // Add exit button to the background panel
        
                gameFrame.setContentPane(backgroundPanel); // Set the content pane of the game frame
                gameFrame.pack(); // Pack the game frame to fit the preferred sizes of its components
                gameFrame.setVisible(true); // Make the game frame visible
            }
        });
    }

    private static JPanel intializeShipContainer() {
        JPanel shipContainer = new JPanel();
        shipContainer.setPreferredSize(new Dimension(400, 400)); // Dimensions to match the player board
        shipContainer.setBackground(Color.WHITE);
        shipContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Add a line border, black, 2 pixels thick
    
        return shipContainer;
    }

    private static void populateShipContainer(JPanel shipContainer, Vector<Ship> ships) {
        shipContainer.removeAll(); // Clear existing content if any
        shipContainer.setLayout(new FlowLayout()); // Or any layout that suits your design
        for (Ship ship : ships) {
            JPanel shipPanel = new JPanel(new BorderLayout()) { // Use BorderLayout to manage label positioning
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(Color.GRAY);
                    g.fillRect(0, 0, getWidth(), getHeight()); // Draw a gray rectangle
                }
            };
    
            JLabel shipLabel = new JLabel(ship.getName(), SwingConstants.CENTER); // Center the label text
            shipLabel.setForeground(Color.WHITE); // Set label text color to contrast with the gray background
            shipPanel.add(shipLabel); // Add the label to the ship panel
    
            int cellSize = 40; // The size of each cell in pixels
            shipPanel.setPreferredSize(new Dimension(ship.getSize() * cellSize, cellSize));
            shipPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Optional: add border to visualize the panel
    
            shipContainer.add(shipPanel);
        }
        shipContainer.revalidate();
        shipContainer.repaint();
    }

    
    // Method to initialize the enemy board panel with buttons for each cell
    private static JPanel initializeEnemyBoardPanel() {
        JPanel boardPanel = new JPanel(); // Panel for the enemy board
        boardPanel.setLayout(new GridLayout(10, 10)); // Set layout to grid for the board
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Set border for the panel
        boardPanel.setPreferredSize(new Dimension(400, 400)); // Set preferred size for the panel
        
        // Populate board panel based on board state
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                JButton cellButton = new JButton(); // Button for each cell
                cellButton.setBackground(Color.BLUE); // Set initial background color
                cellButton.setOpaque(true); // Make the button opaque to show background color
                cellButton.setPreferredSize(new Dimension(40, 40)); // Set preferred size for the button
                cellButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // Set border for the button

                // ADDED FOR TESTING
                boolean hasShip = false; // Flag to check if the cell has a ship
                for (Ship ship : game.player.enemyBoard.getShipList()) { // Iterate through ships on the enemy board
                    for (Coordinate coord : ship.getCoordinates()) { // Iterate through coordinates of each ship
                        if (coord.getRow() == row && coord.getColumn() == col) { // Check if the coordinate matches the cell
                            hasShip = true; // Set flag to true if a ship is found
                            break; // Break the inner loop
                        }
                    }
                    if (hasShip) {
                        cellButton.setBackground(Color.BLACK); // Change background color to indicate a ship
                        break; // Break the outer loop
                    }

                }

                // action listener for shots

                final int finalRow = row; // Final row index for use in the action listener
                final int finalCol = col; // Final column index for use in the action listener
                cellButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Coordinate shot = new Coordinate(finalRow, finalCol); // Create a coordinate for the shot
                        Coordinate result = game.shoot(shot); // Shoot at the coordinate and get the result
                        Audio audio = new Audio(); // Audio handler for playing sound effects

                        // update button based on hit or miss
                        if (result.isHit()) { // Check if the shot was a hit
                            audio.setFile("explosion.wav"); // Set audio file to explosion sound
                            audio.play(); // Play the sound
                            cellButton.setBackground(Color.RED); // Change background color to indicate a hit
                            cellButton.paintImmediately(cellButton.getVisibleRect()); // Repaint the button immediately

                        } else {
                            audio.setFile("miss.wav"); // Set audio file to miss sound
                            audio.play(); // Play the sound
                            cellButton.setBackground(Color.WHITE); // Change background color to indicate a miss
                            cellButton.paintImmediately(cellButton.getVisibleRect()); // Repaint the button immediately
                        }
                        if(game.hasWon() || game.hasLost()) { // Check if the game is over
                            JOptionPane.showMessageDialog(null, "Game Over!"); // Show game over message
                        }
                        try{
                            TimeUnit.MILLISECONDS.sleep(1000); // Pause for a moment before proceeding
  
                        }catch(Exception err){
                            // Exception handling if needed
                        }

                        Coordinate incoming_shot=game.getShot(); // Get the incoming shot from the opponent
                        if(incoming_shot.isHit()){ // Check if the incoming shot was a hit
                            audio.setFile("explosion.wav"); // Set audio file to explosion sound
                            audio.play(); // Play the sound
                            coordinateGrid[incoming_shot.row][incoming_shot.column].setBackground(Color.RED); // Change background color of the player's grid to indicate a hit
                            cellButton.paintImmediately(cellButton.getVisibleRect()); // Repaint the button immediately
                        }
                        else{
                            audio.setFile("miss.wav"); // Set audio file to miss sound
                            audio.play(); // Play the sound
                            coordinateGrid[incoming_shot.row][incoming_shot.column].setBackground(Color.WHITE); // Change background color of the player's grid to indicate a miss
                            cellButton.paintImmediately(cellButton.getVisibleRect()); // Repaint the button immediately
                        }  
                        cellButton.removeActionListener(this); // Remove the action listener to prevent further actions on this cell


                    }
                });

                boardPanel.add(cellButton); // Add the cell button to the board panel     
            }
        }
        return boardPanel; // Return the fully populated board panel
    }

    // Method to initialize the player board panel with buttons for each cell
    private static JPanel initializeBoardPanel() {
        JPanel boardPanel = new JPanel(); // Panel for the player board
        boardPanel.setLayout(new GridLayout(10, 10)); // Set layout to grid for the board
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Set border for the panel
        boardPanel.setPreferredSize(new Dimension(400, 400)); // Set preferred size for the panel
        
        //game.player.board.placeShipsRandomly();

        // Populate board panel based on board state
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                JButton cellButton = new JButton(); // Button for each cell
                cellButton.setBackground(Color.BLUE); // Set initial background color
                cellButton.setOpaque(true); // Make the button opaque to show background color
                cellButton.setPreferredSize(new Dimension(40, 40)); // Set preferred size for the button
                cellButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // Set border for the button

                // ADDED FOR TESTING
                boolean hasShip = false; // Flag to check if the cell has a ship
                for (Ship ship : game.player.board.getShipList()) { // Iterate through ships on the player board
                    for (Coordinate coord : ship.getCoordinates()) { // Iterate through coordinates of each ship
                        if (coord.getRow() == row && coord.getColumn() == col) { // Check if the coordinate matches the cell
                            hasShip = true; // Set flag to true if a ship is found
                            cellButton.setBackground(Color.GRAY);
                            break; // Break the inner loop
                        }
                    }
                    if (hasShip) {
                        break; // Break the outer loop if a ship is found
                    }

                }

                boardPanel.add(cellButton); // Add the cell button to the board panel   
                coordinateGrid[row][col]=cellButton; // Store the button in the coordinate grid for later reference   
            }
        }
        return boardPanel; // Return the fully populated board panel

    }
}


