package Pieces;

import main.Game.Team;
import main.Space;

public class Queen extends Piece {

	public Queen(int num, Team team) {
		super(num, team);
	}

	@Override
	public boolean checkMove(Space start, Space end) {
		// A queen can move horizontally, vertically or diagonally any number of spaces
		if(start.isInRow(end) || start.isInColumn(end) || start.isInDiagonal(end))
			return true;
		
		return false;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Queen)) {
			return false;
		}
		
		Piece p = (Piece) o;
		if(pieceNumber == p.getPieceNumber() && team.equals(p.getTeam())) {
			return true;
		}
		
		return false;
	}
}
