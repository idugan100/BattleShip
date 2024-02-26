import java.io.IOException;

public class Game{
    public Player player;
    boolean isTurn;
    boolean isOver;
    Client connection;

    Game(){
        player = new Player();
        isTurn=false;//will need to be swapped for server vs client
        isOver=false;
        connection = new Client( "127.0.0.1" );
        try{
            connection.connectToServer(); // create a Socket to make connection
            connection.getStreams();  
            connection.input.readObject();
        }
        catch(Exception e){

        }

    }

    void shoot(int row, int col){
        Coordinate c=new  Coordinate(row, col);
        //send coordiante over network
        //read response
        player.shoot(c);

    }

    void getShot(){
        String row_string="";
        String col_string="";
        try{

            row_string = ( String ) connection.input.readObject();
            System.out.println(row_string);

            col_string = ( String ) connection.input.readObject();
            System.out.println(col_string);

        }
        catch(Exception exception){
            System.out.println("error in get shot networking");
        }

        Coordinate c=new  Coordinate(Integer.parseInt(row_string), Integer.parseInt(col_string));

        c = player.board.handleShot(c);
        if(c.isHit()){
            connection.sendData("hit");

        }
        else{
            connection.sendData("miss");

        }
    }

    boolean hasWon(){
        return player.enemyBoard.numberOfHits() == 17;
    }

    boolean hasLost(){
        return player.board.allShipsSunk();
    }
}