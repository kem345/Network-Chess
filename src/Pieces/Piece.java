package Pieces;

import main.Space;

public abstract class Piece {

	protected int pieceNumber;
	protected int moveCount = 0;
	protected String team;
	// Constructor 
	
	public Piece(int num, String team) {
		pieceNumber = num;
		this.team = team;
	}

	// Getters and Setters
	
	public int getPieceNumber() {
		return pieceNumber;
	}

	public void setPieceNumber(int pieceNumber) {
		this.pieceNumber = pieceNumber;
	}
	
	public int getMoveCount() {
		return moveCount;
	}

	public void moved() {
		moveCount++;
	}
	
	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}
	
	// Abstract methods to be implemented by each piece's class
	
	abstract public boolean checkMove(int xPos, int yPos);
	
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Piece)) {
			return false;
		}
		
		Piece p = (Piece) o;
		if(pieceNumber == p.getPieceNumber() && team.equalsIgnoreCase(p.getTeam())) {
			return true;
		}
		
		return false;
	}
	
}
