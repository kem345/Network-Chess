package Pieces;

import main.Game.Team;
import main.Space;

public class Knight extends Piece {

	public Knight(int num, Team team) {
		super(num, team);
	}

	@Override
	public boolean checkMove(Space start, Space end) {
		// Knight can move two spaces horizontally and one space vertically, 
		// or two spaces vertically and one space horizontally
		if(Math.abs(start.getxCoordinate() - end.getxCoordinate()) == 1
				&& Math.abs(start.getyCoordinate() - end.getyCoordinate()) == 2)
			return true;
		
		if(Math.abs(start.getxCoordinate() - end.getxCoordinate()) == 2
				&& Math.abs(start.getyCoordinate() - end.getyCoordinate()) == 1)
			return true;
		
		return false;
	}

	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Knight)) {
			return false;
		}
		
		Piece p = (Piece) o;
		if(pieceNumber == p.getPieceNumber() && team.equals(p.getTeam())) {
			return true;
		}
		
		return false;
	}
}
