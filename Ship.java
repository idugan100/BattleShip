import java.util.Scanner;

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

	void updateCoordinate(Coordinate c){
		for(int i=0; i<size; i++) {
			if(coordinateList[i].getRow()==c.getRow() && coordinateList[i].getColumn()==c.getColumn() ) {
				coordinateList[i]=c;
				return;
			}
		}
		return;
	}

	void placeShipCli() {
		Scanner scanner = new Scanner(System.in);
		Coordinate[] list = new Coordinate[size];

		for ( int i = 0; i < size; i++ ) {
			boolean validCoordinate = false;

			while(!(validCoordinate)) {
				
				System.out.printf("Enter the row for %s coordiante %d:",name,(i+1));
				int row = scanner.nextInt();
				
				if ( row < 0 || row > 9) {
					System.out.println("Only enter number between 0 and 9");
					continue;
				}
				
				System.out.printf("Enter the column for %s coordiante %d:",name,(i+1));
				int col = scanner.nextInt();

				if ( col < 0 || col > 9) {
					System.out.println("Only enter number between 0 and 9");
					continue;
				}

				if ( i > 0 ) {

					// ensure linear placement by check if the current ship part is in the same row or column as the previous ship part
					// this prevents diagonal  placements by requiring at least one of the coordinates to remain unchaged
					boolean isLinearPlacement = (row == list[i - 1].getRow()) || (col == list[i - 1].getColumn());
					
					// prevents placing ship parts out of order
					boolean isDirectlyAdjacent = Math.abs(row - list[i - 1].getRow()) + Math.abs(col - list[i - 1].getColumn()) == 1;
					if( !isLinearPlacement || !isDirectlyAdjacent) {
						System.out.println("Invalid placement; ships can only be placed directly next to each other, vertically or horizontally.");
						continue;
					} else {
						list[i] = new Coordinate(row, col);
						validCoordinate = true;
					}

				} else {
					// First coordinate, don't have to check diagonal/adjacenecy
					list[i] = new Coordinate(row, col);
					validCoordinate = true;
				}
			}
		}

		this.placeShip(list);
		// scanner.close();
	}
}