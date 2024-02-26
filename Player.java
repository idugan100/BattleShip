import java.util.Vector;

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
       Ship battleShip = new Ship(4, "Battleship","b");
       place_ship_handle_collisions(battleShip);
      
        board.printBoard();
       Ship patrolShip = new Ship(2, "Patrol Boat","p");
       place_ship_handle_collisions(patrolShip);

       board.printBoard();
       Ship submarine = new Ship(3, "Submarine","s");
       place_ship_handle_collisions(submarine);

       board.printBoard(); 
       Ship destroyer = new Ship(3, "Destroyer","d");
       place_ship_handle_collisions(destroyer);

       board.printBoard();
       Ship carrier = new Ship(5, "Aircraft Carrier","c");
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

    void add_ships(){
            Ship battleShip = new Ship(4, "Battleship","b");
            Ship patrolShip = new Ship(2, "Patrol Boat","p");
            Ship submarine = new Ship(3, "Submarine","s");
            Ship destroyer = new Ship(3, "Destroyer","d");
            Ship carrier = new Ship(5, "Aircraft Carrier","c");
     
            board.addShip(battleShip);
            board.addShip(patrolShip);
            board.addShip(submarine);
            board.addShip(destroyer);
            board.addShip(carrier);
    }
}