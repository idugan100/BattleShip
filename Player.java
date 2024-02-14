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