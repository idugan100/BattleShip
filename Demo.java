public class Demo{
    public static void main(String[] args){
        Player player = new Player();
        
        //add battleship
        Ship battleShip = new Ship(4, "Battleship","b");
        Coordinate[] list=new Coordinate[4];
        list[0]=new Coordinate(0, 0);
        list[1]=new Coordinate(0, 1);
        list[2]=new Coordinate(0, 2);
        list[3]=new Coordinate(0, 3);
        player.board.addShip(battleShip);
        battleShip.placeShip(list);

        //add patrol boat
        Ship patrol = new Ship(2, "Patrol Boat","p");
        Coordinate[] list2=new Coordinate[2];
        list2[0]=new Coordinate(3, 0);
        list2[1]=new Coordinate(4, 0);
        player.board.addShip(patrol);
        patrol.placeShip(list2);


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