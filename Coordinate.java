
public class Coordinate {
	protected int row;
	protected int column;
	protected CoordinateStatus status;
	
	void Coordinate(int row, int column) {
		this.row=row;
		this.column=column;
		this.status=CoordinateStatus.UNTOUCHED;
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
	
	
}
