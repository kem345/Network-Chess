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
	
	public void undoMove() {
		moveCount = moveCount - 1;
	}
	
	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
	
	// Abstract methods to be implemented by each piece's class
	
	abstract public boolean checkMove(Space start, Space end);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + pieceNumber;
		result = prime * result + ((team == null) ? 0 : team.hashCode());
		return result;
	}

}
