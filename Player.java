import java.util.Vector;

import javax.swing.ImageIcon;

public class Player{
    public Board board;
    public Board enemyBoard;

    Player(){
        board= new Board();
        enemyBoard = new Board();
    }

    void shoot(Coordinate c){
        //send coordinate to networked fellow and get a coordinate back
        enemyBoard.updateCoordinate(c);
    }

    void getShotAt(Coordinate c){
        board.handleShot(c);
        //send the returned coordinate over the network
    }

    boolean has_lost(){
        return board.allShipsSunk();
    }

    void add_ships_cli(){
       board.printBoard();

       ImageIcon battleShipIcon = new ImageIcon("battleship.png");
       ImageIcon patrolShipIcon = new ImageIcon("patrol.png");
       ImageIcon submarineIcon = new ImageIcon("sub.png");
       ImageIcon destroyerIcon = new ImageIcon("destroyer.png");
       ImageIcon carrierIcon = new ImageIcon("carrier.png");

        Ship battleShip = new Ship(4, "Battleship","b", battleShipIcon);
        place_ship_handle_collisions(battleShip);
        
            board.printBoard();
        Ship patrolShip = new Ship(2, "Patrol Boat","p", patrolShipIcon);
        place_ship_handle_collisions(patrolShip);

        board.printBoard();
        Ship submarine = new Ship(3, "Submarine","s", submarineIcon);
        place_ship_handle_collisions(submarine);

        board.printBoard(); 
        Ship destroyer = new Ship(3, "Destroyer","d", destroyerIcon);
        place_ship_handle_collisions(destroyer);

        board.printBoard();
        Ship carrier = new Ship(5, "Aircraft Carrier","c", carrierIcon);
        place_ship_handle_collisions(carrier);
    }

    void place_ship_handle_collisions(Ship ship){
        Boolean successfully_placed = false;
        ship.placeShipCli();
        successfully_placed = board.addShip(ship);

        while(!successfully_placed){
            System.out.println(" This causes a collision with another ship");
            ship.placeShipCli();
            successfully_placed = board.addShip(ship);
        }

    }

}