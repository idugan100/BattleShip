public class TestBattleShip {
    public static void main(String[] args) {
        Board board = new Board();
        board.placeShipsRandomly();
        board.printBoard();

        // simulate shots
        board.handleShot(new Coordinate(0, 0));
        board.printBoard();

        if (board.allShipsSunk()) {
            System.out.println("All ships sunk!");
        } else {
            System.out.println("Ships still afloat");
        }
    }
}
