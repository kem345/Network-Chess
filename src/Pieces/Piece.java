package Pieces;

import main.Space;
import main.Game.Team;

public abstract class Piece {

	protected int pieceNumber;
	protected int moveCount = 0;
	protected Team team;
	// Constructor 
	
	public Piece(int num, Team team) {
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
	
	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
	
	// Abstract methods to be implemented by each piece's class
	
	abstract public boolean checkMove(Space start, Space end);
	
}
