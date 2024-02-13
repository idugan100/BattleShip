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
				output[shipCoordinates[j].row][shipCoordinates[j].column]= shipList.get(i).getMarker()+shipCoordinates[j].getShipModifier();
			}
		}

		// format
	}
	
	public void updateCoordinate(Coordinate c){
		coordinateList[c.getRow()][c.getColumn()]=c;
	}

	public Coordinate handleShot(Coordinate c){
		for(int i=0; i<shipList.size();i++){
			if(shipList.get(i).isShipOnCoordinate(c)){
				c.hitCoordinate();
				coordinateList[c.getRow()][c.getColumn()].hitCoordinate();
				shipList.get(i).updateCoordinate(c);
				return c;

			}
		}
		coordinateList[c.getRow()][c.getColumn()].missCoordinate();
		c.missCoordinate();
		return c;
	}

	private void initializeCoordinates() {
		for(int i= 0; i<WIDTH; i++) {
			for(int j=0; j<HEIGHT; j++) {
				coordinateList[j][i]= new Coordinate(j,i);
			}
		}
	}

	public boolean allShipsSunk(){
		for(int i=0; i<shipList.size();i++){
			if(!shipList.get(i).isSunk()){
				return false;
			}
		}
		return true;
	}
}
