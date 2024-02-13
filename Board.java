import java.util.Vector;

public class Board {

	final int WIDTH=10;
	final int HEIGHT=10;
	private Coordinate[][] coordinateList;
	private Vector<Ship> shipList;
	
	Board(){
		initializeCoordinates();
	}
	
	public void addShip(Ship ship) {
		shipList.add(ship);
	}
	
	public void printBoard() {
		String[][] output= new String[HEIGHT][WIDTH];
		for(int i= 0; i<WIDTH; i++) {
			for(int j=0; j<HEIGHT; j++) {
				output[j][i]= coordinateList[j][i].getBoardModifier();
			}
		}

		for(int i=0; i<shipList.size();i++){
			Coordinate[] shipCoordinates=shipList.get(i).getCoordinates();
			for(int j=0; j<shipCoordinates.length;j++){
				output[shipCoordinates[j].row][shipCoordinates[j].column]= shipList.get(i)+shipCoordinates[j].getShipModifier();
			}
		}

		// format
	}
	
	private void initializeCoordinates() {
		for(int i= 0; i<WIDTH; i++) {
			for(int j=0; j<HEIGHT; j++) {
				coordinateList[j][i]= new Coordinate(j,i);
			}
		}
	}
}
