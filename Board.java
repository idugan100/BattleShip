import java.util.Vector; // Importing Vector class for dynamic array usage

import javax.swing.ImageIcon; // Importing ImageIcon class for handling images

import java.util.Random; // Importing Random class for generating random numbers

public class Board {

	final int WIDTH=10; // Constant for board width
	final int HEIGHT=10; // Constant for board height
	private Coordinate[][] coordinateList; // 2D array to store coordinates of the board
	private Vector<Ship> shipList; // Dynamic array to store ships
	
	// Constructor to initialize the board
	Board(){
		coordinateList= new Coordinate[HEIGHT][WIDTH]; // Initializing the 2D array with board dimensions
		shipList = new Vector<Ship>(); // Initializing the dynamic array for ships
		initializeCoordinates(); // Calling method to initialize each coordinate
	}
	
	// Method to add a ship to the board
	public boolean addShip(Ship ship) {
		// Iterating through existing ships to check for overlap
		for(int i=0; i<shipList.size();i++){
			for(int j=0; j<ship.getCoordinates().length;j++){
				// If any part of the new ship overlaps with existing ships, return false
				if(shipList.get(i).isShipOnCoordinate(ship.getCoordinates()[j])){
					return false;
				}

			}
		}

		// If no overlap, add the ship to the list and return true
		shipList.add(ship);
		return true;
	}

	// Method to place ships randomly on the board
	public void placeShipsRandomly() {
		Random random = new Random(); // Initialize Random object for generating random positions
		Vector<Ship> tempShipList = new Vector<Ship>(); // Temporary list to hold ships before placement

		// Creating ship objects with their respective sizes and identifiers
		Ship battleShip = new Ship(4, "Battleship","b");
		Ship patrolShip = new Ship(2, "Patrol Boat","p");
		Ship submarine = new Ship(3, "Submarine","s");
		Ship destroyer = new Ship(3, "Destroyer","d");
		Ship carrier = new Ship(5, "Aircraft Carrier","c");
		
		// Adding ships to the temporary list
		tempShipList.add(battleShip);
		tempShipList.add(patrolShip);
		tempShipList.add(submarine);
		tempShipList.add(destroyer);
		tempShipList.add(carrier);

		// Iterating over each ship to place them on the board
		for (Ship ship : tempShipList) {
			boolean placed = false; // Flag to check if the ship has been placed
			while(!placed) { // Keep trying until the ship is placed
				int row = random.nextInt(HEIGHT); // Random row for ship placement
				int col = random.nextInt(WIDTH); // Random column for ship placement
				
				// Check if the ship can be placed horizontally within the board limits
				if (col + ship.getSize() <= WIDTH) {
					Coordinate[] coordinates = new Coordinate[ship.getSize()]; // Array to hold ship coordinates
					// Assigning coordinates to the ship
					for (int i = 0; i < ship.getSize(); i++) {
						coordinates[i] = new Coordinate(row, col + i); // Creating new coordinate for each part of the ship
					}
					ship.placeShip(coordinates); // Placing the ship with its coordinates
					placed=addShip(ship); // Attempt to add the ship to the board and update the placed flag
					printBoard(); // Print the board to visualize the placement
				}
			}
		}
	}
	
	
	// Method to print the current state of the board
	public void printBoard() {
		String[][] output= new String[HEIGHT][WIDTH]; // 2D array to store the board state for printing
	
		// Filling the output array with board coordinates
		for(int i= 0; i<WIDTH; i++) {
			for(int j=0; j<HEIGHT; j++) {
				output[j][i]= " " +coordinateList[j][i].getBoardModifier();
			}
		}

		// Overriding with ship coordinates
		for(int i=0; i<shipList.size();i++){
			Coordinate[] shipCoordinates=shipList.get(i).getCoordinates();
			for(int j=0; j<shipCoordinates.length;j++){
				output[shipCoordinates[j].row][shipCoordinates[j].column]= shipList.get(i).getMarker()+shipCoordinates[j].getShipModifier();
			}
		}

		// Printing the output array to visualize the board
		for(int i= 0; i<HEIGHT; i++) {
			for(int j=0; j<WIDTH; j++) {
				System.out.print(output[i][j]);
			}
			System.out.print("\n");
		}
		System.out.print("\n");

	}
	
	// Method to update a coordinate on the board
	public void updateCoordinate(Coordinate c){
		coordinateList[c.getRow()][c.getColumn()]=c; // Updating the specified coordinate
	}

	// Method to handle a shot at a coordinate
	public Coordinate handleShot(Coordinate c){
		// Checking if the shot hits any ship
		for(int i=0; i<shipList.size();i++){
			if(shipList.get(i).isShipOnCoordinate(c)){
				c.hitCoordinate(); // Marking the coordinate as hit
				shipList.get(i).updateCoordinate(c); // Updating the ship with the hit coordinate
				coordinateList[c.row][c.column].hitCoordinate(); // Updating the board coordinate as hit
				return c; // Returning the updated coordinate

			}
		}
		// If the shot misses, mark the coordinate as missed
		coordinateList[c.getRow()][c.getColumn()].missCoordinate();
		c.missCoordinate();
		return c; // Returning the updated coordinate
	}

	// Method to initialize all coordinates on the board
	private void initializeCoordinates() {
		for(int i= 0; i<WIDTH; i++) {
			for(int j=0; j<HEIGHT; j++) {
				coordinateList[j][i]= new Coordinate(j,i); // Creating a new Coordinate object for each position
			}
		}
	}

	// Method to check if all ships have been sunk
	public boolean allShipsSunk(){
		for(int i=0; i<shipList.size();i++){
			if(!shipList.get(i).isSunk()){ // If any ship is not sunk, return false
				return false;
			}
		}
		return true; // If all ships are sunk, return true
	}

	// Getter method for the list of ships
	public Vector<Ship> getShipList() {
		return shipList; // Returning the list of ships
	}
	
	// Method to count the number of hits on the board
	public int numberOfHits(){
		int total=0; // Counter for total hits
		for(int i=0; i<WIDTH;i++){
			for(int j=0; j<HEIGHT;j++){
				if(coordinateList[i][j].isHit()){ // If the coordinate is hit, increment the counter
					total++;
				}
			}
		}
		return total; // Returning the total number of hits
	}

	// Method to remove a ship from the list
	public boolean removeShip(Ship shipToRemove) {
		return shipList.remove(shipToRemove); // Removing the specified ship and returning the result
	}
}
