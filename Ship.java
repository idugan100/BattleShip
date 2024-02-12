
public class Ship {
	private Coordinate coordinateList[];
	private int size;
	private String name;
	
	Ship(int size, String name){
		this.size=size;
		this.name=name;
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
	 
	 void placeShip(Coordinate[] list) {
		 this.coordinateList=list;
	 }
	 
	 String getName() {
		 return name;
	 }
	 
	 int getSize() {
		 return size;
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
