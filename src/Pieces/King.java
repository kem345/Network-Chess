package Pieces;

import main.Game.Team;
import main.Space;

public class King extends Piece {


	public King(int num, Team team) {
		super(num, team);
	}

	@Override
	public boolean checkMove(Space start, Space end) {
		// A King can move horizontally, vertically or diagonally one space at a time
		
		// Check horizontal
		if(start.isInRow(end) && Math.abs(start.getxCoordinate() - end.getxCoordinate()) == 1)
			return true;
		
		// Check vertical
		if(start.isInColumn(end) && Math.abs(start.getyCoordinate() - end.getyCoordinate()) == 1)
			return true;
		
		// Check diagonal
		if(start.isInDiagonal(end) && Math.abs(start.getxCoordinate() - end.getxCoordinate()) == 1 &&
				Math.abs(start.getyCoordinate() - end.getyCoordinate()) == 1)
			return true;
		
		return false;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof King)) {
			return false;
		}
		
		Piece p = (Piece) o;
		if(pieceNumber == p.getPieceNumber() && team.equals(p.getTeam())) {
			return true;
		}
		
		return false;
	}
}
