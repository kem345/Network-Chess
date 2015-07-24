package main;

public class Space {
	
	int xCoordinate;
	int yCoordinate;
	boolean hasPieceOn = false;
	
	public Space(int xCoordinate, int yCoordinate) {
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
	}

	// Getters and Setters
	
	public int getxCoordinate() {
		return xCoordinate;
	}
	public void setxCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public int getyCoordinate() {
		return yCoordinate;
	}
	public void setyCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}
	
	public boolean isPieceOn() {
		return hasPieceOn;
	}
	
	public void placePiece() {
		hasPieceOn = true;
	}
	
	public void removePiece() {
		hasPieceOn = false;
	}
		
	

	
	boolean isInRow(Space space) {
		if(yCoordinate - space.getyCoordinate() == 0) {
			return true;
		}
		
		return false;
	}
	
	boolean isInColumn(Space space) {
		if(xCoordinate - space.getxCoordinate() == 0) {
			return true;
		}
		
		return false;
	}
	
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Space)) {
			return false;
		}
		
		Space s = (Space) o;
		if(xCoordinate == s.getxCoordinate() && yCoordinate == s.getyCoordinate()) {
			return true;
		}
		
		return false;
	}
		

}
