
public class Ship {
	private Coordinate coordinateList[];
	private int size;
	private String name;
	private String marker;
	
	Ship(int size, String name, String marker){
		this.size=size;
		this.name=name;
		this.marker=marker;
	}
	
	boolean isSunk() {
		for(int i=0; i<size; i++) {
			if(!coordinateList[i].isHit()) {
				return false;
			}
		}
		return true;
	}
	 
	Coordinate[] getCoordinates() {
		return coordinateList;
	}
	
	boolean placeShip(Coordinate[] list) {
		if(size!=list.length){
			throw new IllegalArgumentException("Incorrect Number of Coordinates");
		}
		
	//todo add in collision handling when placing a ship
		this.coordinateList=list;
		return true;
	}
	
	String getName() {
		return name;
	}
	
	int getSize() {
		return size;
	}

	String getMarker(){
		return marker;
	}
	
	boolean isShipOnCoordinate(Coordinate c) {
		for(int i=0; i<size; i++) {
			if(coordinateList[i].getRow()==c.getRow() && coordinateList[i].getColumn()==c.getColumn() ) {
				return true;
			}
	}
		return false;
	}
	 
	 
	 
}
