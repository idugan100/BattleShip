import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.Image;
import java.io.IOException;
import javax.sound.sampled.*;
import java.net.URL;
import javax.imageio.ImageIO;

public class BattleShipView {

    // currently being used for testing
    private static Game game;
    private static JButton[][] coordinateGrid = new JButton[10][10];
    private JFrame mainFrame;
    private JPanel mainMenuPanel;
    private JPanel gamePanel;

    public BattleShipView() {
        game = new Game();
        initializeMainFrame();
    }
    
    public void initializeMainFrame() {
        mainFrame = new JFrame("Battleship");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // set up main menu
        mainMenuPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    Image backgroundImage = ImageIO.read(getClass().getResource("/icon.png"));
                    g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mainMenuPanel.setLayout(new BorderLayout());

        JButton playButton = new JButton("Start Game");
        playButton.setFont(new Font("Arial", Font.BOLD, 48));
        playButton.addActionListener(e -> transitionToGameScreen());

        mainMenuPanel.add(playButton, BorderLayout.CENTER);
        mainFrame.add(mainMenuPanel);

        mainFrame.pack();
        mainFrame.setMinimumSize(new Dimension(800, 800));
        mainFrame.setLocationRelativeTo(null); // centers the window
        mainFrame.setVisible(true);
    }

    private void transitionToGameScreen() {
        // remove the main menu panel
        mainFrame.remove(mainMenuPanel);

        // create and set up game panel
        gamePanel = new JPanel(new BorderLayout());
        gamePanel.setPreferredSize(new Dimension(800, 800));
        // initialize board panels
        JPanel playerBoardPanel = initializeBoardPanel();
        JPanel enemyBoardPanel = initializeEnemyBoardPanel();

        // initialize the ship icons for dragging
        JPanel shipSelectionPanel = initializeShipIcons();

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 800));


        int totalWidth = 800;
        int boardWidth = 400;
        int gap = (totalWidth-(2*boardWidth)) / 3;

        playerBoardPanel.setBounds(gap, 400, 400, 400);
        enemyBoardPanel.setBounds(gap*2+boardWidth, 400, 400, 400);
        shipSelectionPanel.setBounds(0, 0, 800, 400);

        layeredPane.add(playerBoardPanel, Integer.valueOf(JLayeredPane.DEFAULT_LAYER));
        layeredPane.add(enemyBoardPanel, Integer.valueOf(JLayeredPane.DEFAULT_LAYER));
        layeredPane.add(shipSelectionPanel, Integer.valueOf(JLayeredPane.PALETTE_LAYER));
    
        gamePanel.add(layeredPane, BorderLayout.CENTER);

        // Add an exit button to return to main menu
        JButton exitButton = new JButton("Exit to Main Menu");
        exitButton.addActionListener(ev -> {
            mainFrame.remove(gamePanel);
            mainFrame.add(mainMenuPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        });
        gamePanel.add(exitButton, BorderLayout.SOUTH);

        mainFrame.add(gamePanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    // Helper method to create draggable ship labels
    private JLabel createDraggableShipLabel(Ship ship) {
        JLabel label = new JLabel(ship.getImageIcon());
        
        MouseAdapter mouseAdapter = new MouseAdapter() {
            private Point initialClickPoint = null;

            @Override
            public void mousePressed(MouseEvent e) {             
                initialClickPoint = SwingUtilities.convertPoint(label, e.getPoint(), label.getParent());
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int dx = e.getX() - initialClickPoint.x;
                int dy = e.getY() - initialClickPoint.y;
                Component component = e.getComponent();
                Point componentStartLocation = component.getLocation();
                component.setLocation(componentStartLocation.x + dx, componentStartLocation.y + dy);
                initialClickPoint = e.getPoint(); // Update initial point to the current point after moving
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                initialClickPoint = null;
            }
        };
        label.addMouseListener(mouseAdapter);
        label.addMouseMotionListener(mouseAdapter);
        return label;
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
    
    public static void Main(String[] args) {
        SwingUtilities.invokeLater(DEP_BattleShipView::new);
    }

    private JPanel initializeShipIcons() {
        JPanel shipPanel = new JPanel(null);

        ImageIcon battleShipIcon = new ImageIcon("battleship.png");
        ImageIcon patrolShipIcon = new ImageIcon("patrol.png");
        ImageIcon submarineIcon = new ImageIcon("sub.png");
        ImageIcon destroyerIcon = new ImageIcon("destroyer.png");
        ImageIcon carrierIcon = new ImageIcon("carrier.png");
        
        Ship battleShip = new Ship(4, "Battleship", "b", battleShipIcon);
        Ship patrolShip = new Ship(2, "Patrol Boat", "p", patrolShipIcon);
        Ship submarine = new Ship(3, "Submarine", "s", submarineIcon);
        Ship destroyer = new Ship(3, "Destroyer", "d", destroyerIcon);
        Ship carrier = new Ship(5, "Aircraft Carrier", "c", carrierIcon);

        JLabel battleShipLabel = createDraggableShipLabel(battleShip);
        JLabel patrolShipLabel = createDraggableShipLabel(patrolShip);
        JLabel submarineLabel = createDraggableShipLabel(submarine);
        JLabel destroyerLabel = createDraggableShipLabel(destroyer);
        JLabel carrierLabel = createDraggableShipLabel(carrier);

        // Adjusted setBounds for each label to prevent stacking
        battleShipLabel.setBounds(20, 20, battleShipIcon.getIconWidth(), battleShipIcon.getIconHeight());
        patrolShipLabel.setBounds(20, 100, patrolShipIcon.getIconWidth(), patrolShipIcon.getIconHeight());
        submarineLabel.setBounds(20, 180, submarineIcon.getIconWidth(), submarineIcon.getIconHeight()); 
        destroyerLabel.setBounds(20, 260, destroyerIcon.getIconWidth(), destroyerIcon.getIconHeight()); 
        carrierLabel.setBounds(20, 340, carrierIcon.getIconWidth(), carrierIcon.getIconHeight());        
        
        shipPanel.add(battleShipLabel);
        shipPanel.add(patrolShipLabel);
        shipPanel.add(submarineLabel);
        shipPanel.add(destroyerLabel);
        shipPanel.add(carrierLabel);

        return shipPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BattleShipView();
            }
        });
    }

    

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
/*     public static void main(String[] args) {
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

    private void initializeShipIcons() {
        // initialize each ship with an ImageIcon
        Ship battleShip = new Ship(4, "Battleship", "b", loadIcon("/Assets/ShipIcons/battleship.png"));
        Ship patrolShip = new Ship(2, "Patrol Boat", "p", loadIcon("/Assets/ShipIcons/patrol.png"));
        Ship submarine = new Ship(3, "Submarine", "s", loadIcon("/Assets/ShipIcons/sub.png"));
        Ship destroyer = new Ship(3, "Destroyer", "d", loadIcon("/Assets/ShipIcons/destroyer.png"));
        Ship carrier = new Ship(5, "Aircraft Carrier", "c", loadIcon("/Assets/ShipIcons/carrier.png"));

        // Create the JLabels for the ships
        JLabel battleshipLabel = createDraggableShipLabel(battleShip);
        JLabel patrolShipLabel = createDraggableShipLabel(patrolShip);
        JLabel submarineLabel = createDraggableShipLabel(submarine);
        JLabel destroyerLabel = createDraggableShipLabel(destroyer);
        JLabel carrierLabel = createDraggableShipLabel(carrier);

        // Add teh ships to a JPanel
        JPanel shipPanel = new JPanel();
        shipPanel.add(battleshipLabel);
        shipPanel.add(patrolShipLabel);
        shipPanel.add(submarineLabel);
        shipPanel.add(destroyerLabel);
        shipPanel.add(carrierLabel);

        // add ship panel to game window
        frame.add(shipPanel, BorderLayout.WEST);
        return shipPanel;
    }

    // Helper method to create draggable ship labels
    private JLabel createDraggableShipLabel(Ship ship) {
        JLabel label = new JLabel(ship.getImageIcon());
        label.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e) {
                JComponent comp = (JComponent) e.getSource();
                TransferHandler handler = comp.getTransferHandler();
                handler.exportAsDrag(comp, e, TransferHandler.MOVE);
            }
        });
        label.setTransferHandler(new TransferHandler("ship", ship));
        return label;
    } */



}

