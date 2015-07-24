package Pieces;

import main.Space;

public abstract class Piece {

	protected int pieceNumber;
	protected int xCoordinate;
	protected int yCoordinate;
	protected int moveCount = 0;
	
	// Constructor 
	
	public Piece(int num, int xCoord, int yCoord) {
		pieceNumber = num;
		xCoordinate = xCoord;
		yCoordinate = yCoord;
	}

	// Getters and Setters
	
	public int getPieceNumber() {
		return pieceNumber;
	}

	public void setPieceNumber(int pieceNumber) {
		this.pieceNumber = pieceNumber;
	}

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
	
	public int getMoveCount() {
		return moveCount;
	}

	public void moved() {
		moveCount++;
	}
	
	
	// Abstract methods to be implemented by each piece's class
	
	abstract public void move(int xPos, int yPos);
	
	abstract public boolean checkMove(int xPos, int yPos);
	
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Piece)) {
			return false;
		}
		
		Piece p = (Piece) o;
		if(pieceNumber == p.getPieceNumber() && xCoordinate == p.getxCoordinate()
				&& yCoordinate == p.getyCoordinate()) {
			return true;
		}
		
		return false;
	}
	
}
