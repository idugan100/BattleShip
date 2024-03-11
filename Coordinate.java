import javax.swing.ImageIcon;

public class Coordinate {
	
	protected int row;
	protected int column;
	protected CoordinateStatus status;
	private ImageIcon image;
	
	Coordinate(int row, int column) {
		this.row=row;
		this.column=column;
		this.status=CoordinateStatus.UNTOUCHED;
	}

	Coordinate(int row, int column, ImageIcon image) {
		this.row=row;
		this.column=column;
		this.status=CoordinateStatus.UNTOUCHED;
		this.image = image;
	}

	public ImageIcon getImageIcon () {
		return image;
	}

	boolean isHit() {
		return CoordinateStatus.HIT==status;
	}
	
	boolean isUntouched() {
		return CoordinateStatus.UNTOUCHED==status;

	}
	
	boolean isMissed() {
		return CoordinateStatus.MISSED==status;
	}
	
	void hitCoordinate() {
		status=CoordinateStatus.HIT;
	}
	
	void missCoordinate() {
		status=CoordinateStatus.MISSED;
	}
	
	void resetCoordinate() {
		status=CoordinateStatus.UNTOUCHED;
	}
	
	int getRow() {
		return row;
	}
	
	int getColumn() {
		return column;
	}

	String getShipModifier(){
		if(this.isHit()){
			return "x";
		}
		else if (this.isMissed()){
			return "o";
		}
		else{
			return " ";
		}
	}

	String getBoardModifier(){
		if(this.isHit()){
			return "x";
		}
		else if (this.isMissed()){
			return "o";
		}
		else{
			return ".";
		}
	}
	
	
}
