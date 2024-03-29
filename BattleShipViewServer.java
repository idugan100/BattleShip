import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;


public class BattleShipViewServer{

    // currently being used for testing
    private static GameServer game = new GameServer();
    private static JButton[][] coordinateGrid;
    private static MyPanel dragAndDrop;
    private static JFrame frame;
    private static JTextArea enemyShipsSunk;
    private static JTextArea playerShipsSunk;
    private static JPanel gameBackgroundPanel;

    public static void main(String[] args) {
        coordinateGrid= new JButton[10][10];
        //Create and set up the window.
        frame = new JFrame("BattleShip");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //create Play Button
        JButton randButton = new JButton("Place Ships Randomly");
        randButton.setFont(new java.awt.Font("Arial", Font.BOLD, 48)); 

        JButton confirmButton = new JButton("Confirm Ship Placement");
        confirmButton.setFont(new java.awt.Font("Arial", Font.BOLD, 48));
        
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                        List<Integer> shipCoordinates = placeShips();
                        boolean check = initializeBoardManually(shipCoordinates);  
                        
                        if(check){
                            System.out.println("Ships Placement Confirmed");
                            game.player.board.printBoard();
            
                            // Create game window
                            JFrame gameFrame = new JFrame("Game Window");
                            gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            gameFrame.setLayout(new BorderLayout()); // Set layout for gameFrame
            
                            // Create components for the game window
                             gameBackgroundPanel = new JPanel();
                            gameBackgroundPanel.setLayout(new BoxLayout(gameBackgroundPanel, BoxLayout.Y_AXIS));
            
                            JPanel psunk = new JPanel(new FlowLayout(FlowLayout.LEFT));
                            playerShipsSunk = new JTextArea("Your Ships Sunk: ");
                            playerShipsSunk.setEditable(false);
                            playerShipsSunk.setColumns(40); // Set preferred width (adjust as needed)
                            psunk.add(playerShipsSunk);
                            JPanel playerBoardPanel = initializeBoardPanel();
                            JPanel targetingBoardPanel = initializeEnemyBoardPanel();
                            JPanel esunk = new JPanel(new FlowLayout(FlowLayout.LEFT));
                            enemyShipsSunk = new JTextArea("Enemy Ships Sunk: ");
                            enemyShipsSunk.setEditable(false);
                            enemyShipsSunk.setColumns(40);
                            enemyShipsSunk.setPreferredSize(new Dimension(390, 25)); // Set preferred size to see background color

                            esunk.add(enemyShipsSunk);
            
            
            
                            // Add boards to game background
                            gameBackgroundPanel.add(esunk);
                            gameBackgroundPanel.add(targetingBoardPanel);
                            gameBackgroundPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                            gameBackgroundPanel.add(playerBoardPanel);
                            gameBackgroundPanel.add(psunk);
            
            
                            // Create and add label and exit button
                            JLabel label = new JLabel("Game Started", SwingConstants.CENTER);            
                            // Add components to gameFrame
                            gameFrame.add(label, BorderLayout.NORTH);
                            gameFrame.add(gameBackgroundPanel, BorderLayout.CENTER);            
            
                            gameFrame.pack();
                            gameFrame.setLocationRelativeTo(null); // center game window
                            gameFrame.setVisible(true);
                            frame.setVisible(false);
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Invalid Placement!");
                            game.player.board.resetBoard();
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
        dragAndDrop.setPreferredSize(new Dimension(600, 600)); // Set preferred size
        
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
                gameBackgroundPanel = new JPanel();
                gameBackgroundPanel.setLayout(new BoxLayout(gameBackgroundPanel, BoxLayout.Y_AXIS));

               // Initialize player and targeting BoardPanel 
               JPanel psunk = new JPanel(new FlowLayout(FlowLayout.LEFT));
               playerShipsSunk = new JTextArea("Your Ships Sunk: ");
               playerShipsSunk.setEditable(false);
               playerShipsSunk.setColumns(40); // Set preferred width (adjust as needed)
               psunk.add(playerShipsSunk);
               JPanel playerBoardPanel = initializeBoardPanel();
               JPanel targetingBoardPanel = initializeEnemyBoardPanel();
               JPanel esunk = new JPanel(new FlowLayout(FlowLayout.LEFT));
               enemyShipsSunk = new JTextArea("Enemy Ships Sunk: ");
               enemyShipsSunk.setEditable(false);
                enemyShipsSunk.setColumns(40);
               esunk.add(enemyShipsSunk);



               // Add boards to game background
               gameBackgroundPanel.add(esunk);
               gameBackgroundPanel.add(targetingBoardPanel);
               gameBackgroundPanel.add(Box.createRigidArea(new Dimension(0, 10)));
               gameBackgroundPanel.add(playerBoardPanel);
               gameBackgroundPanel.add(psunk);


                // Create and add label and exit button
                JLabel label = new JLabel("Game Started", SwingConstants.CENTER);

                // Add components to gameFrame
                gameFrame.add(label, BorderLayout.NORTH);
                gameFrame.add(gameBackgroundPanel, BorderLayout.CENTER);

                gameFrame.pack();
                gameFrame.setLocationRelativeTo(null); // center game window
                gameFrame.setVisible(true);
                frame.setVisible(false);


            }
        });
        
        game.connection.runServer();

    }


    // once user confirms they're done with placement this gets called and determines where on the grid the ships will go
    private static List<Integer> placeShips() {
        List<Integer> shipCoordinates = dragAndDrop.getShipCoordinates();
        List<Integer> shipTemp = new ArrayList<Integer>(shipCoordinates);
    
        // Process each ship
        for (int shipIndex = 0; shipIndex < 5; shipIndex++) {
            int baseIndex = shipIndex * 2; // Each ship has two entries (row, col) in the list
            boolean isHorizontal = dragAndDrop.getHorizontal(shipIndex);

    
            int rowStart = shipCoordinates.get(baseIndex) / 60;

            int colStart = shipCoordinates.get(baseIndex + 1) / 60;

            // Adjust row start based on orientation

            if (rowStart >= 9) {
                rowStart = 9;
            } else if (rowStart <= 0){
                rowStart = 0;
            }
            
            if (colStart >= 9) {
                colStart = 9;
            }

            if (colStart <= 0) {
                colStart = 0;
            }
            
            // Update the temporary list with adjusted values
            shipTemp.set(baseIndex, rowStart);
            shipTemp.set(baseIndex + 1, colStart);
        }
    
        return shipTemp;
    }
    
    private static boolean initializeBoardManually (List<Integer> shipCoordinates) {
    //CARRIER

        int[] carrierStartPoint = new int[]{shipCoordinates.get(0), shipCoordinates.get(1)};
        Coordinate[] carrierList = new Coordinate[5];
        if(dragAndDrop.getHorizontal(0)){
            for(int i=0;i<5;i++){
                ImageIcon carrierIndexImage = new ImageIcon("P_ship1h_" + i + ".png");
                Coordinate c = new Coordinate(carrierStartPoint[0], carrierStartPoint[1]+i, carrierIndexImage);
                carrierList[i]=c;
            }
        }
        else{
            for(int i=0;i<5;i++){
                ImageIcon carrierIndexImage = new ImageIcon("P_ship1_" + i + ".png");
                Coordinate c = new Coordinate(carrierStartPoint[0]+i, carrierStartPoint[1], carrierIndexImage);
                carrierList[i]=c;
            }
        }
        Ship Carrier = new Ship(5, "Carrier", "c");
        Carrier.placeShip(carrierList);
        if(!game.player.board.addShip(Carrier)){
            return false;
        }
    //battleship
        int[] battleshipStartPoint = new int[]{shipCoordinates.get(2), shipCoordinates.get(3)};
        Coordinate[] battleShipList = new Coordinate[4];
        if(dragAndDrop.getHorizontal(1)){
            for(int i=0;i<4;i++){
                ImageIcon battleshipIndexImage = new ImageIcon("P_ship2h_" + i + ".png");
                Coordinate c = new Coordinate(battleshipStartPoint[0], battleshipStartPoint[1]+i, battleshipIndexImage);
                battleShipList[i]=c;
            }
        }
        else{
            for(int i=0;i<4;i++){
                ImageIcon battleshipIndexImage = new ImageIcon("P_ship2_" + i + ".png");
                Coordinate c = new Coordinate(battleshipStartPoint[0]+i, battleshipStartPoint[1], battleshipIndexImage);
                battleShipList[i]=c;

            }
        }
        Ship BattleShip = new Ship(4, "BattleShip", "b");
        BattleShip.placeShip(battleShipList);
        
        if(!game.player.board.addShip(BattleShip)){
            return false;
        }
    //SUBMARINE
        int[] subStartPoint = new int[]{shipCoordinates.get(4), shipCoordinates.get(5)};
        Coordinate[] subShipList = new Coordinate[3];
        if(dragAndDrop.getHorizontal(2)){
            for(int i=0;i<3;i++){
                ImageIcon subIndexImage = new ImageIcon("P_ship3h_" + i + ".png");
                Coordinate c = new Coordinate(subStartPoint[0], subStartPoint[1]+i, subIndexImage);
                subShipList[i]=c;
            }
        }
        else{
            for(int i=0;i<3;i++){
                ImageIcon subIndexImage = new ImageIcon("P_ship3_" + i + ".png");
                Coordinate c = new Coordinate(subStartPoint[0]+i, subStartPoint[1], subIndexImage);
                subShipList[i]=c;

            }
        }
        Ship SubShip = new Ship(3, "Submarine", "s");
        SubShip.placeShip(subShipList);

        if(!game.player.board.addShip(SubShip)){
            return false;
        }
        

    //DESTROYER
        int[] destroyerStartPoint = new int[]{shipCoordinates.get(6), shipCoordinates.get(7)};
        Coordinate[] destroyerShipList = new Coordinate[3];
        if(dragAndDrop.getHorizontal(3)){
            for(int i=0;i<3;i++){
                ImageIcon destroyerIndexImage = new ImageIcon("P_ship4h_" + i + ".png");
                Coordinate c = new Coordinate(destroyerStartPoint[0], destroyerStartPoint[1]+i, destroyerIndexImage);
                destroyerShipList[i]=c;
            }
        }
        else{
            for(int i=0;i<3;i++){
                ImageIcon destroyerIndexImage = new ImageIcon("P_ship4_" + i + ".png");
                Coordinate c = new Coordinate(destroyerStartPoint[0]+i, destroyerStartPoint[1], destroyerIndexImage);
                destroyerShipList[i]=c;

            }
        }
        Ship DestroyerShip = new Ship(3, "Destroyer", "d");
        DestroyerShip.placeShip(destroyerShipList);

        if(!game.player.board.addShip(DestroyerShip)){
            return false;
        }
    // PATROL BOAT
        int[] patrolStartPoint = new int[]{shipCoordinates.get(8), shipCoordinates.get(9)};
        Coordinate[] patrolShipList = new Coordinate[2];
        if(dragAndDrop.getHorizontal(4)){
            for(int i=0;i<2;i++){
                ImageIcon patrolBoatIndexImage = new ImageIcon("P_ship5h_" + i + ".png");
                Coordinate c = new Coordinate(patrolStartPoint[0], patrolStartPoint[1]+i, patrolBoatIndexImage);
                patrolShipList[i]=c;
            }
        }
        else{
            for(int i=0;i<2;i++){
                ImageIcon patrolBoatIndexImage = new ImageIcon("P_ship5_" + i + ".png");
                Coordinate c = new Coordinate(patrolStartPoint[0]+i, patrolStartPoint[1], patrolBoatIndexImage);
                patrolShipList[i]=c;
            }
        }
        Ship PatrolBoat = new Ship(2, "Patrol Boat", "p");
        PatrolBoat.placeShip(patrolShipList);

        if(!game.player.board.addShip(PatrolBoat)){
            return false;
        }
        return true;
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
                        enemyShipsSunk.setText(game.enemySunkList);
                        enemyShipsSunk.paintImmediately(gameBackgroundPanel.getVisibleRect());


                        if(game.hasWon()) {
                            JOptionPane.showMessageDialog(null, "Game Over You Won!");
                            System.exit(0);
                        }
                        else if(game.hasLost()){
                            JOptionPane.showMessageDialog(null, "Game Over You Lost!");
                            System.exit(0);

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
                            coordinateGrid[incoming_shot.row][incoming_shot.column].setIcon(white);
                            coordinateGrid[incoming_shot.row][incoming_shot.column].paintImmediately(coordinateGrid[incoming_shot.row][incoming_shot.column].getVisibleRect());
                        }  
                        playerShipsSunk.setText("Your Ships Sunk: " + game.player.board.getSunkList());

                        if(game.hasWon()) {
                            JOptionPane.showMessageDialog(null, "Game Over You Won!");
                            System.exit(0);

                        }
                        else if(game.hasLost()){
                            JOptionPane.showMessageDialog(null, "Game Over You Lost!");
                            System.exit(0);

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
                            cellButton.setIcon(coord.getImageIcon());
                            break;
                        }
                    }
                    if (hasShip) {
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
