package main;

import Pieces.Piece;

public class Space {
	
	int xCoordinate;
	int yCoordinate;
	Piece piece = null;
	
	public Space(int xCoordinate, int yCoordinate) {
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
	}

	// Getters and Setters
	
	public int getxCoordinate() {
		return xCoordinate;
	}
	public void setxCoordinate(int xCoordinate) {
		if(xCoordinate > 0 && xCoordinate <= 64)
		this.xCoordinate = xCoordinate;
	}

	public int getyCoordinate() {
		return yCoordinate;
	}
	public void setyCoordinate(int yCoordinate) {
		if(yCoordinate > 0 && yCoordinate <= 64)
		this.yCoordinate = yCoordinate;
	}
	
	/** If the space has a piece on it return true else return false **/
	public boolean hasPiece() {
		if(piece == null)
			return false;
		
		return true;
	}
	
	public void placePiece(Piece piece) {
		this.piece = piece;
	}
	
	public void removePiece() {
		this.piece = null;
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
