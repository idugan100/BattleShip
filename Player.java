
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
}