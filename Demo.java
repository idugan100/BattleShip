public class Demo{
    public static void main(String[] args){
        Player player = new Player();
        
        player.add_ships_cli();


        //print board
        player.board.printBoard();

        //shoot battleship
        Coordinate shot = new Coordinate(0, 0);
        player.getShotAt(shot);
        player.board.printBoard();

        //Shoot water
        Coordinate shot2 = new Coordinate(1, 0);
        player.getShotAt(shot2);
        player.board.printBoard();

        if(player.has_lost()){
            System.out.println("player has lost game");

        }
        else{
            System.out.println("player has not lost game");
        }
    }
}