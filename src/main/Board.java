package main;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import Pieces.*;

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
		for(int y=1; y <= 8; y++) {
			// set x-coordinates
			for(int x=1; x <= 8; x++) {
				spaces.addElement(new Space(x, y));
			}
		}
	}
	
	/** Set the given piece on the space specified by the coordinates 
	 * @throws Exception **/
	public void setPiece(int xCoord, int yCoord, Piece piece) throws Exception {
		if(xCoord > 8 || xCoord < 1 || yCoord > 8 || yCoord < 1) {
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
	
	public boolean clearBetween(Space s1, Space s2) {
		if(s1.isInRow(s2) && clearInRow(s1, s2))
			return true;
		
		if(s1.isInColumn(s2) && clearInColumn(s1, s2))
			return true;
		
		if(s1.isInDiagonal(s2) && clearInDiagonal(s1, s2))
			return true;
		
		return false;
	}
	
	public Map<Space, Piece> getTeamPieces(String team) {
		Map<Space, Piece> map = new HashMap<Space, Piece>();
		for(Space sp : spaces) {
			if(sp.hasPiece() && sp.getPiece().getTeam().equals(team)) {
				map.put(sp, sp.getPiece());
			}
		}
			
		return map; 
	}
	
	/** Return true if the team's king is in check **/
	public boolean teamInCheck(String team) {
		Map<Space,Piece> pieces = getTeamPieces(team);
		// get the team's king location
		Space kingSpace = new Space(0, 0);
		for(Entry<Space, Piece> entry : pieces.entrySet()) {
			if(entry.getValue() instanceof King) {
				kingSpace = entry.getKey();
			}
		}
		
		// Check if each piece can move to the King's space if it can then it is in check
		for(Entry<Space, Piece> entry : pieces.entrySet()) {
			if((!(entry.getValue() instanceof King)) && entry.getValue().checkMove(entry.getKey(), kingSpace) &&
					clearBetween(entry.getKey(), kingSpace)) {
				return true;
			}
		}
		
		return false;
	}
}
