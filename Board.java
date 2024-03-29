import java.util.Vector;
import java.util.Random;
import javax.swing.ImageIcon;

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
		Ship battleShip = new Ship(4, "Battleship","b");
		Ship patrolShip = new Ship(2, "Patrol Boat","p");
		Ship submarine = new Ship(3, "Submarine","s");
		Ship destroyer = new Ship(3, "Destroyer","d");
		Ship carrier = new Ship(5, "Aircraft Carrier","c");
		tempShipList.add(carrier);
		tempShipList.add(battleShip);
		tempShipList.add(submarine);
		tempShipList.add(destroyer);
		tempShipList.add(patrolShip);

		int imageCount = 1;
		for (Ship ship : tempShipList) {
			boolean placed = false;
			while(!placed) {
				int row = random.nextInt(HEIGHT);
				int col = random.nextInt(WIDTH);
				int dir = random.nextInt(2);

				//horizontal
				if(dir==0){
					if ((col + ship.getSize() <= WIDTH) ) {
						Coordinate[] coordinates = new Coordinate[ship.getSize()];
						for (int i = 0; i < ship.getSize(); i++) {
							ImageIcon shipIndexImage = new ImageIcon("P_ship" + imageCount + "h_" + i + ".png");
							coordinates[i] = new Coordinate(row, col + i, shipIndexImage);
						}
						ship.placeShip(coordinates);
						placed=addShip(ship);
						printBoard();
						imageCount++;
					}
				}

				//vertical
				if(dir==1){
					if ((row + ship.getSize() <= HEIGHT) ) {
						Coordinate[] coordinates = new Coordinate[ship.getSize()];
						for (int i = 0; i < ship.getSize(); i++) {
							ImageIcon shipIndexImage = new ImageIcon("P_ship" + imageCount + "_" + i + ".png");
							coordinates[i] = new Coordinate(row+i, col, shipIndexImage);
						}
						ship.placeShip(coordinates);
						placed=addShip(ship);
						printBoard();
					}
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

	public void resetBoard () {
		shipList.clear();
	}

	public String getSunkList(){
		String list ="";
		for(int i=0; i<shipList.size();i++){
			if(shipList.get(i).isSunk()){
				list += shipList.get(i).getName() + ", ";
			}
		}
		return list;

	}
}
