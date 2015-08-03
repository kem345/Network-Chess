package Pieces;

import main.Game.Team;
import main.Space;

public class Rook extends Piece {
	
	public Rook(int num, Team team) {
		super(num, team);
	}

	@Override
	public boolean checkMove(Space s1, Space s2) {
		// Rook can move horizontally or vertically any number of spaces
		if(s1.isInRow(s2) || s1.isInColumn(s2))
			return true;
		
		return false;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Rook)) {
			return false;
		}
		
		Piece p = (Piece) o;
		if(pieceNumber == p.getPieceNumber() && team.equals(p.getTeam())) {
			return true;
		}
		
		return false;
	}
}
