package Pieces;
import java.lang.Math;

import main.Game.Team;
import main.Space;

public class Pawn extends Piece{

	

	public Pawn(int num, Team team) {
		super(num, team);
	}

	
	@Override
	public boolean checkMove(Space start, Space end) {
		 
		// If it is the pawn's first move then it can move forward
		// two spaces as long as the path is clear
		if(moveCount == 0 && !end.hasPiece() && start.getxCoordinate() == end.getxCoordinate() 
				&& team.equals(Team.TEAM1) && (start.getyCoordinate() + 2) == end.getyCoordinate())
			return true;
		
		// If it is the pawn's first move then it can move forward
		// two spaces as long as the path is clear
		if(moveCount == 0 && !end.hasPiece() && start.getxCoordinate() == end.getxCoordinate() 
				&& team.equals(Team.TEAM2) && (start.getyCoordinate() - 2) == end.getyCoordinate())
			return true;
		
		// Check if normal move (no capture) if team1
		if((!end.hasPiece()) && start.getxCoordinate() == end.getxCoordinate() && team.equals(Team.TEAM1) 
				&& (start.getyCoordinate() + 1) == end.getyCoordinate())
			return true;
		
		// Check if normal move (no capture) if team2
		if(!end.hasPiece() && start.getxCoordinate() == end.getxCoordinate() && team.equals(Team.TEAM2) 
				&& (start.getyCoordinate() - 1) == end.getyCoordinate())
			return true;
		
		// Check if attack move team 1 
		if(end.hasPiece() && Math.abs(start.getxCoordinate() - end.getxCoordinate()) == 1 
				&& team.equals(Team.TEAM1) && (start.getyCoordinate() + 1) == end.getyCoordinate())
			return true;
		
		// Check if attack move team2
		if(end.hasPiece() && Math.abs(start.getxCoordinate() - end.getxCoordinate()) == 1 
				&& team.equals(Team.TEAM2) && (start.getyCoordinate() - 1) == end.getyCoordinate())
			return true;		
		
		return false;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Pawn)) {
			return false;
		}
		
		Piece p = (Piece) o;
		if(pieceNumber == p.getPieceNumber() && team.equals(p.getTeam())) {
			return true;
		}
		
		return false;
	}
}
