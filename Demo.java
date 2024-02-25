public class Demo{
    public static void main(String[] args){
        Game game=new  Game();
        game.player.add_ships_cli();
        
        while(!game.hasWon() && !game.hasLost()){
            game.getShot();
            game.player.board.printBoard();
        }
        
    }
}