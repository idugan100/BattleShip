import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class BattleShipView{

    // currently being used for testing
    private static Game game = new Game();
    private static JButton[][] coordinateGrid;
    private static MyPanel dragAndDrop;

    public static void main(String[] args) {
        coordinateGrid= new JButton[10][10];
        //Create and set up the window.
        JFrame frame = new JFrame("BattleShip");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //create Play Button
        JButton randButton = new JButton("Place Ships Randomly");
        randButton.setFont(new java.awt.Font("Arial", Font.BOLD, 48)); 

        JButton confirmButton = new JButton("Confirm Ship Placement");
        confirmButton.setFont(new java.awt.Font("Arial", Font.BOLD, 48));
        
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Ships Placement Confirmed");
                List<Integer> shipCoordinates = placeShips();
                for (int i = 0; i < shipCoordinates.size(); i++) {
                    System.out.println(shipCoordinates.get(i));
                }
            }
        });

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

        JPanel placementButtonPanel = new JPanel();
        placementButtonPanel.add(randButton);
        placementButtonPanel.add(confirmButton);

        backgroundPanel.setLayout(new BorderLayout()); // Set the layout of the backgroundPanel to BorderLayout
        backgroundPanel.add(placementButtonPanel, BorderLayout.SOUTH);

        // Initialize the dragAndDrop panel with a specific size
        dragAndDrop = new MyPanel();
        dragAndDrop.setPreferredSize(new Dimension(1000, 1000)); // Set preferred size
        
        // Create a container panel with GridBagLayout for more control
        JPanel containerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER; // Center the dragAndDrop panel within the container
        
        // Add the dragAndDrop panel to the container panel
        containerPanel.add(dragAndDrop, gbc);
        
        // Add the container panel to the backgroundPanel at the center
        backgroundPanel.add(containerPanel, BorderLayout.CENTER);

        JButton battleship = new JButton("Rotate Battleship");
        battleship.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dragAndDrop.rotateBattleship();
            }});
        JButton submarine = new JButton("Rotate Submarine");
        submarine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dragAndDrop.rotateSubmarine();
            }});
        JButton patrol = new JButton("Rotate Patrol Boat");
        patrol.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dragAndDrop.rotatePatrol();
            }});
        JButton carrier = new JButton("Rotate Carrier");
        carrier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dragAndDrop.rotateCarrier();
            }});
        JButton destroyer = new JButton("Rotate Destroyer");
        destroyer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dragAndDrop.rotateDestroyer();
            }});


        JPanel buttonPanel = new JPanel();
        buttonPanel.add(battleship);
        buttonPanel.add(submarine);
        buttonPanel.add(patrol);
        buttonPanel.add(carrier);
        buttonPanel.add(destroyer);
        backgroundPanel.add(buttonPanel, BorderLayout.NORTH);

        backgroundPanel.add(containerPanel, BorderLayout.CENTER);
        //backgroundPanel.add(dragAndDrop, BorderLayout.CENTER);
        frame.add(backgroundPanel);
        
        frame.pack();
        frame.setMinimumSize(new Dimension(800, 800));
        frame.setMaximumSize(new Dimension(800, 800));
        frame.setLocationRelativeTo(null); // centers the window
        frame.setResizable(false);
        frame.setVisible(true);



        // Action listener for play button. Takes from main menu to game screen.
        randButton.addActionListener(new ActionListener() {
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


    private static List<Integer> shipCoordinates() {
        List<Integer> shipCoordinates = dragAndDrop.getShipCoordinates();
        List<Integer> shipTemp = new ArrayList<Integer>(shipCoordinates);

        

        return shipTemp;
    }


    // once user confirms they're done with placement this gets called and determines where on the grid the ships will go
    private static List<Integer> placeShips() {
        List<Integer> shipCoordinates = dragAndDrop.getShipCoordinates();
        List<Integer> shipTemp = new ArrayList<Integer>(shipCoordinates);

            int ship1RowStart = (shipCoordinates.get(0)/100);
            System.out.print("SHIP 1 ROW B4: ");
            System.out.println(shipCoordinates.get(0));
            if (ship1RowStart <= 0) {
                ship1RowStart = 0;
                shipTemp.set(0, 0);
            } else {
                ship1RowStart += 1;
                shipTemp.set(0, ship1RowStart);
            }
            
            int ship1ColStart = (shipCoordinates.get(1)/100);
            System.out.print("SHIP 1 COL B4: ");
            System.out.println(shipCoordinates.get(1));
            if(ship1ColStart <= 0) {
                ship1ColStart = 0;
                shipTemp.set(1, 0);
            } else {
                ship1ColStart += 1;
                shipTemp.set(1, ship1ColStart);
            }
            //int ship1RowSpan = (shipCoordinates.get(2)/100);
            //int ship1ColSpan = (shipCoordinates.get(3)/100);

            int ship2RowStart = (shipCoordinates.get(2)/100);
            System.out.print("SHIP 2 ROW B4: ");
            System.out.println(shipCoordinates.get(2));
            if (ship2RowStart <= 0) {
                ship2RowStart = 0;
                shipTemp.set(2, 0);
            } else {
                ship2RowStart += 1;
                shipTemp.set(2, ship2RowStart);
            }

            int ship2ColStart = (shipCoordinates.get(3)/100);
            System.out.print("SHIP 2 COL B4: ");
            System.out.println(shipCoordinates.get(3));
            if (ship2ColStart <= 0) {
                ship2ColStart = 0;
                shipTemp.set(3, 0);
            } else {
                ship2ColStart += 1;
                shipTemp.set(3, ship2ColStart);
            }
            //int ship2RowSpan = (shipCoordinates.get(6)/100);
            //int ship2ColSpan = (shipCoordinates.get(7)/100);

            int ship3RowStart = (shipCoordinates.get(4)/100);
            System.out.print("SHIP 3 ROWB4: ");
            System.out.println(shipCoordinates.get(4));
            if (ship3RowStart <= 0) {
                ship3RowStart = 0;
                shipTemp.set(4, 0);
            } else {
                ship3RowStart += 1;
                shipTemp.set(4, ship3RowStart);
            }

            int ship3ColStart = (shipCoordinates.get(5)/100);
            System.out.print("SHIP 3 COL B4: ");
            System.out.println(shipCoordinates.get(5));
            if (ship3ColStart <= 0) {
                ship3ColStart = 0;
                shipTemp.set(5, 0);
            } else {
                ship3ColStart = (ship3ColStart + 1);
                shipTemp.set(5, ship3ColStart);
            }
            //int ship3RowSpan = (shipCoordinates.get(10)/100);
            //int ship3ColSpan = (shipCoordinates.get(11)/100);

            System.out.print("SHIP 4 ROW B4: ");
            System.out.println(shipCoordinates.get(6));
            int ship4RowStart = (shipCoordinates.get(6)/100);
            if (ship4RowStart <= 0) {
                ship4RowStart = 0;
                shipTemp.set(6, 0);
            } else {
                ship4RowStart += 1;
                shipTemp.set(6, ship4RowStart);
            }
            
            int ship4ColStart = (shipCoordinates.get(7)/100);
            System.out.print("SHIP 4 COL B4: ");
            System.out.println(shipCoordinates.get(7));
            if (ship4ColStart <= 0) {
                ship4ColStart = 0;
                shipTemp.set(7, 0);
            } else {
                ship4ColStart += 1;
                shipTemp.set(7, ship4ColStart);
            }
            //int ship4RowSpan = (shipCoordinates.get(14)/100);
            //int ship4ColSpan = (shipCoordinates.get(15)/100);

            int ship5RowStart = (shipCoordinates.get(8)/100);
            System.out.print("SHIP 5 ROW B4: ");
            System.out.println(shipCoordinates.get(8));
            if (ship5RowStart <= 0) {
                ship5RowStart = 0;
                shipTemp.set(8, 0);
            } else {
                ship5RowStart += 1;
                shipTemp.set(8, ship5RowStart);
            }
            int ship5ColStart = (shipCoordinates.get(9)/100);
            System.out.print("SHIP 5 COL B4: ");
            System.out.println(shipCoordinates.get(9));
            if (ship5ColStart <= 0) {
                ship5ColStart = 0;
                shipTemp.set(9, 0);
            } else {
                ship5ColStart += 1;
                shipTemp.set(9, ship5ColStart);
            }
            //int ship5RowSpan = (shipCoordinates.get(18)/100);
            //int ship5ColSpan = (shipCoordinates.get(19)/100);
            return shipTemp;
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
                ImageIcon water = new ImageIcon("cover.png");
                cellButton.setIcon(water);
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
                        ImageIcon shippng = new ImageIcon("ship.png");
                            cellButton.setIcon(shippng);
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
                            ImageIcon hitIcon = new ImageIcon("hitship.png");
                            cellButton.setIcon(hitIcon);
                            cellButton.paintImmediately(cellButton.getVisibleRect());

                        } else {
                            audio.setFile("miss.wav");
                            audio.play();
                            ImageIcon white = new ImageIcon("white.png");
                            cellButton.setIcon(white);
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
                            ImageIcon hitIcon = new ImageIcon("hitship.png");
                            coordinateGrid[incoming_shot.row][incoming_shot.column].setIcon(hitIcon);;
                            cellButton.paintImmediately(cellButton.getVisibleRect());
                        }
                        else{
                            audio.setFile("miss.wav");
                            audio.play();
                            ImageIcon white = new ImageIcon("white.png");
                            cellButton.setIcon(white);
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
                ImageIcon water = new ImageIcon("cover.png");
                cellButton.setIcon(water);
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
                        ImageIcon shippng = new ImageIcon("ship.png");
                        cellButton.setIcon(shippng);
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
