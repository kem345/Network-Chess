package Pieces;

import main.Game.Team;
import main.Space;

public class Bishop extends Piece {


	public Bishop(int num, Team team) {
		super(num, team);
	}

	@Override
	public boolean checkMove(Space s1, Space s2) {
		// Bishop can only move in diagonal
		if(s1.isInDiagonal(s2))
			return true;
		
		return false;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Bishop)) {
			return false;
		}
		
		Piece p = (Piece) o;
		if(pieceNumber == p.getPieceNumber() && team.equals(p.getTeam())) {
			return true;
		}
		
		return false;
	}
}
