public class Demo{
    public static void main(String[] args){
        Player player = new Player();
        
        //add battleship
        Ship battleShip = new Ship(4, "Battleship","b");
        player.board.addShip(battleShip);
        battleShip.placeShipCli();

        //add patrol boat
        Ship patrol = new Ship(2, "Patrol Boat","p");
        player.board.addShip(patrol);
        patrol.placeShipCli();


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

        if(player.board.allShipsSunk()){
            System.out.println("Ships all sunk");

        }
        else{
            System.out.println("Ships still afloat");
        }
    }
}