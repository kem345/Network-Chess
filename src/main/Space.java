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
	public void setxCoordinate(int xCoordinate) throws Exception {
		if(xCoordinate >= 0 && xCoordinate <= 7) {
			this.xCoordinate = xCoordinate;
		} else {
			throw new Exception("Coordinate must be between 1 and 8");
		}
	}

	public int getyCoordinate() {
		return yCoordinate;
	}
	public void setyCoordinate(int yCoordinate) throws Exception {
		if(yCoordinate >= 0 && yCoordinate <= 7) {
			this.yCoordinate = yCoordinate;
		} else {
			throw new Exception("Coordinate must be between 1 and 8");
		}
	}
	
	public Piece getPiece() {
		return piece;
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
	
	/** Returns true if the given space is in the same row, false otherwise **/
	public boolean isInRow(Space space) {
		if(yCoordinate == space.getyCoordinate()) {
			return true;
		}
		
		return false;
	}
	
	/** Returns true if the given space is in the same column, false otherwise **/
	public boolean isInColumn(Space space) {
		if(xCoordinate == space.getxCoordinate()) {
			return true;
		}
		
		return false;
	}
	
	/** Returns true if the given space is in a diagonal line 
	 * on the board with this space, false otherwise **/
	public boolean isInDiagonal(Space space) {
		if(Math.abs(xCoordinate - space.getxCoordinate()) ==
				Math.abs(yCoordinate - space.getyCoordinate())) {
			return true;
		}
		
		return false;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + xCoordinate;
		result = prime * result + yCoordinate;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Space other = (Space) obj;
		if (xCoordinate != other.xCoordinate)
			return false;
		if (yCoordinate != other.yCoordinate)
			return false;
		return true;
	}
		
}
