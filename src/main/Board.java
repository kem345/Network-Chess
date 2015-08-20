package main;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import Pieces.*;
import main.Game.Team;

public class Board {
	
	private Vector<Space> spaces = new Vector<Space>();
	
	public Board() { 
		spaces.clear();
		spaces.setSize(64); 
	}

	public Vector<Space> getSpaces() {
		return spaces;
	}
	
	/** Create all 64 spaces for the chess board. Initialize them to have no pieces **/
	public void createBoard() {
		spaces.clear();
		// set y-coordinates
		for(int y=0; y <= 7; y++) {
			// set x-coordinates
			for(int x=0; x <= 7; x++) {
				spaces.addElement(new Space(x, y));
			}
		}
	}
	
	/** Set the given piece on the space specified by the coordinates 
	 * @throws Exception **/
	public void setPiece(int xCoord, int yCoord, Piece piece) throws Exception {
		if(xCoord > 7 || xCoord < 0 || yCoord > 7 || yCoord < 0) {
			throw new Exception("invlaid coordinate");
		}
		int index = getSpaceIndex(xCoord, yCoord);
		if(index > -1 && spaces.get(index) != null) {
			spaces.get(index).placePiece(piece);
		}
	}
	
	/** Remove the piece from the given space on the board if it has one **/
	public void removePiece(int xCoord, int yCoord) {
		int index = getSpaceIndex(xCoord, yCoord);
		if(index > -1 && spaces.get(index).hasPiece()) {
			spaces.get(index).removePiece();
		}
	}
	
	/** Get the index in the spaces vector of the space with the given coordinates **/
	public int getSpaceIndex(int xCoord, int yCoord) {
		int index = 0;
		for(Space sp : spaces) {
			if(sp.getxCoordinate() == xCoord && sp.getyCoordinate() == yCoord) {
				return index;
			}
			index++;
		}
		
		return -1;
	}
	
	public Space getSpace(int xCoord, int yCoord) {
		int index = getSpaceIndex(xCoord, yCoord);
		if(index > -1)
			return spaces.get(index);
		
		return null;
	}
	
	/** Return a vector of all of the spaces between space1 and space2 **/
	private Vector<Space> getSpacesBetween(Space space1, Space space2) {
		Vector<Space> ret = new Vector<Space>();
		int greaterX, greaterY, lessX, lessY;

		// Get the larger and smaller x value
		if(space1.getxCoordinate() > space2.getxCoordinate()) {
			greaterX = space1.getxCoordinate();
			lessX = space2.getxCoordinate();
		} else {
			greaterX = space2.getxCoordinate();
			lessX = space1.getxCoordinate();
		}
		// get the larger and smaller y values
		if(space1.getyCoordinate() > space2.getyCoordinate()) {
			greaterY = space1.getyCoordinate();
			lessY = space2.getyCoordinate();
		} else {
			greaterY = space2.getyCoordinate();
			lessY = space1.getyCoordinate();
		}
		
		// Add all spaces that are between space1 and space2 to the vector
		if(space1.isInRow(space2)) {
			for(Space sp : spaces) {
				if(sp.getyCoordinate() == space1.getyCoordinate() &&
					sp.getxCoordinate() < greaterX && sp.getxCoordinate() > lessX) {
					ret.addElement(sp);
				}
			}
		} else if(space1.isInColumn(space2)) {
			for(Space sp : spaces) {
				if(sp.getxCoordinate() == space1.getxCoordinate() &&
					sp.getyCoordinate() < greaterY && sp.getyCoordinate() > lessY) {
					ret.addElement(sp);
				}
			}
		} else if(space1.isInDiagonal(space2)) {
			for(Space sp : spaces) {
				if(sp.isInDiagonal(space1) && sp.isInDiagonal(space2) && 
						sp.getxCoordinate() < greaterX && sp.getxCoordinate() > lessX &&
						sp.getyCoordinate() < greaterY && sp.getyCoordinate() > lessY) {
					ret.addElement(sp);
				}
			}
		}
		
		return ret;
	}
	
	/** Return true if there are no pieces in the same row between space 1 and space2 **/
	public boolean clearInRow(Space space1, Space space2) {
		// If spaces are not in same row then the row check should pass
		if(!space1.isInRow(space2)) {
			return true;
		}
		
		for(Space sp : getSpacesBetween(space1, space2)) {
			if(sp.hasPiece())
				return false;
		}
		
		return true;
	}
	
	/** Return true if there are not pieces in the same column between space1 and space2 **/
	public boolean clearInColumn(Space space1, Space space2) {
		// If spaces are not in same column then the column check should pass
		if(!space1.isInColumn(space2)) {
			return true;
		}
		
		for(Space sp : getSpacesBetween(space1, space2)) {
			if(sp.hasPiece())
				return false;
		}
		
		return true;
	}
	
	/** Return true if there are not pieces in the diagonal spaces between space1 and space2 **/
	public boolean clearInDiagonal(Space space1, Space space2) {
		// If space are not in a diagonal line on the board then this check should pass
		if(!space1.isInDiagonal(space2)) {
			return true;
		}
		
		for(Space sp : getSpacesBetween(space1, space2)) {
			if(sp.hasPiece())
				return false;
		}
		
		return true;
	}
	
	/** Checks if there are pieces in the spaces between the given spaces **/
	public boolean clearBetween(Space s1, Space s2) {
		if(s1.isInRow(s2) && clearInRow(s1, s2))
			return true;
		
		if(s1.isInColumn(s2) && clearInColumn(s1, s2))
			return true;
		
		if(s1.isInDiagonal(s2) && clearInDiagonal(s1, s2))
			return true;
		
		return false;
	}
	
	/** Return a map containing all of pieces for the given team and the space that the piece is currently on **/
	public Map<Space, Piece> getTeamPieces(Team team) {
		Map<Space, Piece> map = new HashMap<Space, Piece>();
		for(Space sp : spaces) {
			if(sp.hasPiece() && sp.getPiece().getTeam().equals(team)) {
				map.put(sp, sp.getPiece());
			}
		}
			
		return map; 
	}
	
	/** Checks if a king would be in check if it was on the given space **/
	public boolean spaceInCheck(Space space, Team team) {
		Map<Space,Piece> opponentPieces = new HashMap<Space,Piece>();
		if(team.equals(Team.TEAM1))
			opponentPieces = getTeamPieces(Team.TEAM2);
		else
			opponentPieces = getTeamPieces(Team.TEAM1);
		
		// Check if each opponent piece can move to the given space if it can then it is in check
		for(Entry<Space, Piece> entry : opponentPieces.entrySet()) {
			if((!(entry.getValue() instanceof King)) && entry.getValue().checkMove(entry.getKey(), space) &&
					clearBetween(entry.getKey(), space)) {
				return true;
			}
		}
		
		return false;
	}
	
	public Space getTeamKingSpace(Team team) {
		Map<Space,Piece> pieces = getTeamPieces(team);
		// get the team's king location
		Space kingSpace = new Space(0, 0);
		for(Entry<Space, Piece> entry : pieces.entrySet()) {
			if(entry.getValue() instanceof King) {
				kingSpace = entry.getKey();
			}
		}
		
		return kingSpace;
	}
	
	/** Return true if the team's king is in check **/
	public boolean teamInCheck(Team team) {
		// get the team's king location
		Space kingSpace = getTeamKingSpace(team);
		
		return spaceInCheck(kingSpace, team);
	}
	
	/** Return all spaces that a King can move to 
	 * @throws Exception **/
	private Vector<Space> getKingMoves(Space start) {
		Vector<Space> temp = new Vector<Space>();		
		// Get all space a king would be allowed to move to and add them to the temp vector
		temp.addElement(new Space(start.getxCoordinate(), start.getyCoordinate() + 1));
		temp.addElement(new Space(start.getxCoordinate(), start.getyCoordinate() - 1));
		temp.addElement(new Space(start.getxCoordinate() + 1, start.getyCoordinate()));
		temp.addElement(new Space(start.getxCoordinate() - 1, start.getyCoordinate()));
		temp.addElement(new Space(start.getxCoordinate() + 1, start.getyCoordinate() + 1));
		temp.addElement(new Space(start.getxCoordinate() - 1, start.getyCoordinate() - 1));
		temp.addElement(new Space(start.getxCoordinate() + 1, start.getyCoordinate() - 1));
		temp.addElement(new Space(start.getxCoordinate() - 1, start.getyCoordinate() + 1));
		
		// Remove any spaces that go beyond the dimensions of the board
		Vector<Space> retVec = new Vector<Space>();
		for(Space sp : temp) {
			if(!(sp.getxCoordinate() > 7 || sp.getxCoordinate() < 0 || sp.getyCoordinate() > 7 || sp.getyCoordinate() < 0))
				retVec.addElement(sp);
		}
		
		return retVec;
		
	}
	
	/** Return true if team is in checkmate 
	 * @throws Exception **/
	public boolean kingCheckmateCheck(Team team) throws Exception {
		// A team can't be in checkmate if it is not in check
		if(!teamInCheck(team))
			return false;
		
		Space kingSpace = getTeamKingSpace(team);
		// get the king object
		King king = (King) kingSpace.getPiece();
		// Get all moves the king can make
		Vector<Space> moves = getKingMoves(kingSpace);
		for(Space sp : moves) {
			// If there is a space for the king to move then it is not in checkmate
			if((!(sp.hasPiece() && sp.getPiece().getTeam().equals(king.getTeam()))) && 
					(!spaceInCheck(sp, team)) && king.checkMove(kingSpace, sp)) {
				return false;
			}
		}
		
		//TODO: Check if move can take you out of check
		return true;
	}
	
	/** Check if the given team is allowed to make a castle move **/
	public boolean canCastle(Team team, Rook rook) {		
		Space kingSpace = getTeamKingSpace(team);
		// A team cannot castle out of check
		if(spaceInCheck(kingSpace, team))
			return false;
		
		Space rookSpace = new Space(-1,-1);
		// get all of the pieces for the given team and the spaces they are on
		Map<Space, Piece> teamPieces = getTeamPieces(team);
		// Loop through all of the space/piece pairs and look for the given rook and king
		Rook teamRook = new Rook(0, team);
		King teamKing = new King(0, team);
		for(Entry<Space, Piece> ent : teamPieces.entrySet()) {
			if(ent.getValue().equals(rook)) {
				rookSpace = ent.getKey();
				teamRook = (Rook) ent.getValue();
			}
			if(ent.getValue() instanceof King)
				teamKing = (King) ent.getValue();
		}
		
		// If the given rook could not be found then do not approve the move
		if(rookSpace.getxCoordinate() == -1 && rookSpace.getyCoordinate() == -1)
			return false;
		
		// test the spaces the king will move through in order to castle
		// if the king will need to move through check then the move is not allowed
		boolean moveThroughCheck = false;
		if(rookSpace.getxCoordinate() < kingSpace.getxCoordinate()) {
			if(spaceInCheck(new Space(kingSpace.getxCoordinate() - 1, kingSpace.getyCoordinate()), team) ||
					spaceInCheck(new Space(kingSpace.getxCoordinate() - 2, kingSpace.getyCoordinate()), team))
					moveThroughCheck = true;
		} else {
			if(spaceInCheck(new Space(kingSpace.getxCoordinate() + 1, kingSpace.getyCoordinate()), team) ||
					spaceInCheck(new Space(kingSpace.getxCoordinate() + 2, kingSpace.getyCoordinate()), team))
					moveThroughCheck = true;
		}
		
		// Check that the king does not have to move through check
		// Check that there are no pieces between the king and rook
		// Check that neither the king or rook have been moved yet
		if(moveThroughCheck || teamKing.getMoveCount() > 0 || teamRook.getMoveCount() > 0 ||
				!clearBetween(kingSpace, rookSpace)) {
			return false;
		}
		
		return true;
	}
	
	/** Check if the given team can execute an En Passant move to the given space **/
	public boolean canEnpassant(Space space, Space end) {
		Piece enP = space.getPiece();
		
		// Only pawns can perform en passant moves
		if(enP instanceof Pawn) {
			// if the piece moving is team 2 then it needs to be moving to row 2
			// and capturing a pawn in row 3
			if(enP.getTeam().equals(Team.TEAM2) && end.getyCoordinate() == 2 && getSpace(space.getxCoordinate(), 3).hasPiece()) {
				Piece piece = getSpace(end.getxCoordinate(), 3).getPiece();
				if(piece instanceof Pawn && !piece.getTeam().equals(enP.getTeam()) && piece.getMoveCount() == 1) {
					return true;
				}
			} else if(enP.getTeam().equals(Team.TEAM1) && end.getyCoordinate() == 5 && getSpace(space.getxCoordinate(), 4).hasPiece()) {
				// if the piece moving is team 1 then it needs to be moving to row 5
				// and capturing a pawn in row 4
				Piece piece = getSpace(end.getxCoordinate(), 4).getPiece();
				if(piece instanceof Pawn && !piece.getTeam().equals(enP.getTeam()) && piece.getMoveCount() == 1) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/** Promote the pawn on the given space to the newPiece **/
	public void promotePiece(Space space, Piece newPiece) {
		// A pawn can only be promoted if it is in the last row
		if(space.getyCoordinate() != 0 && space.getyCoordinate() != 7) {
			return;
		}
		
		// Only pawns can be promoted and they must be promoted to a Piece for the same team
		if(!space.hasPiece() || !(space.getPiece() instanceof Pawn) ||
				!space.getPiece().getTeam().equals(newPiece.getTeam()))
			return;
		
		// Replace the pawn with the new piece
		space.removePiece();
		space.placePiece(newPiece);
	}

}
