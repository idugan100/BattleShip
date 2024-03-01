import java.util.Vector;

import javax.swing.ImageIcon;

import java.util.Random;

public class Board {

	final int WIDTH=10;
	final int HEIGHT=10;
	private Coordinate[][] coordinateList;
	private Vector<Ship> shipList;
	
	Board(){
		coordinateList= new Coordinate[HEIGHT][WIDTH];
		shipList = new Vector<Ship>();
		initializeCoordinates();
	}
	
	public boolean addShip(Ship ship) {
		for(int i=0; i<shipList.size();i++){
			for(int j=0; j<ship.getCoordinates().length;j++){
				if(shipList.get(i).isShipOnCoordinate(ship.getCoordinates()[j])){
					return false;
				}

			}
		}

		shipList.add(ship);
		return true;
	}

	public void placeShipsRandomly() {
		Random random = new Random(); // making random object
		Vector<Ship> tempShipList = new Vector<Ship>();

		ImageIcon battleShipIcon = new ImageIcon("battleship.png");
		ImageIcon patrolShipIcon = new ImageIcon("patrol.png");
		ImageIcon submarineIcon = new ImageIcon("sub.png");
		ImageIcon destroyerIcon = new ImageIcon("destroyer.png");
		ImageIcon carrierIcon = new ImageIcon("carrier.png");

		Ship battleShip = new Ship(4, "Battleship","b", battleShipIcon);
		Ship patrolShip = new Ship(2, "Patrol Boat","p", patrolShipIcon);
		Ship submarine = new Ship(3, "Submarine","s", submarineIcon);
		Ship destroyer = new Ship(3, "Destroyer","d", destroyerIcon);
		Ship carrier = new Ship(5, "Aircraft Carrier","c", carrierIcon);
		tempShipList.add(battleShip);
		tempShipList.add(patrolShip);
		tempShipList.add(submarine);
		tempShipList.add(destroyer);
		tempShipList.add(carrier);

		for (Ship ship : tempShipList) {
			boolean placed = false;
			while(!placed) {
				int row = random.nextInt(HEIGHT);
				int col = random.nextInt(WIDTH);
				// only horizontal right now
				// check if ship fits
				if (col + ship.getSize() <= WIDTH) {
					Coordinate[] coordinates = new Coordinate[ship.getSize()];
					for (int i = 0; i < ship.getSize(); i++) {
						coordinates[i] = new Coordinate(row, col + i);
					}
					ship.placeShip(coordinates);
					placed=addShip(ship);
					printBoard();
				}
			}
		}
	}
	
	
	public void printBoard() {
		String[][] output= new String[HEIGHT][WIDTH];
	
		//add board coordinates
		for(int i= 0; i<WIDTH; i++) {
			for(int j=0; j<HEIGHT; j++) {
				output[j][i]= " " +coordinateList[j][i].getBoardModifier();
			}
		}

		//override with ship coordinates
		for(int i=0; i<shipList.size();i++){
			Coordinate[] shipCoordinates=shipList.get(i).getCoordinates();
			for(int j=0; j<shipCoordinates.length;j++){
				output[shipCoordinates[j].row][shipCoordinates[j].column]= shipList.get(i).getMarker()+shipCoordinates[j].getShipModifier();
			}
		}

		//print output array
		for(int i= 0; i<HEIGHT; i++) {
			for(int j=0; j<WIDTH; j++) {
				System.out.print(output[i][j]);
			}
			System.out.print("\n");
		}
		System.out.print("\n");

	}
	
	public void updateCoordinate(Coordinate c){
		coordinateList[c.getRow()][c.getColumn()]=c;
	}

	public Coordinate handleShot(Coordinate c){
		for(int i=0; i<shipList.size();i++){
			if(shipList.get(i).isShipOnCoordinate(c)){
				c.hitCoordinate();
				shipList.get(i).updateCoordinate(c);
				coordinateList[c.row][c.column].hitCoordinate();
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

	public Vector<Ship> getShipList() {
		return shipList;
	}
	
	public int numberOfHits(){
		int total=0;
		for(int i=0; i<WIDTH;i++){
			for(int j=0; j<HEIGHT;j++){
				if(coordinateList[i][j].isHit()){
					total++;
				}
			}
		}
		return total;
	}

	public boolean removeShip(Ship shipToRemove) {
		return shipList.remove(shipToRemove);
	}
}
