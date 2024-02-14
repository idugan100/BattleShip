
public class Game{
    public Player player;
    boolean isTurn;
    boolean isOver;

    Game(){
        player = new Player();
        isTurn=false;//will need to be swapped for server vs client
        isOver=false;
    }

    void loop(){
        player.add_ships();
        while(!isOver){
            //get incoming shot from opponent over network
            Coordinate c=new  Coordinate(0, 0);
            c = player.board.handleShot(c);
            //send c back over network so opponent can update their board
            if(player.board.allShipsSunk()){
                //send network signal that game is over
                break;
            }
            player.enemyBoard.printBoard();
            player.board.printBoard();
            //read in new values and shoot
            player.shoot(c);
            player.enemyBoard.printBoard();
            player.board.printBoard();

        }
    }

}