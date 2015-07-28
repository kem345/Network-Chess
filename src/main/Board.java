package main;

import java.util.Iterator;
import java.util.Vector;

import Pieces.Piece;

public class Board {
	
	private Vector<Space> spaces = new Vector<Space>();
	
	public Board() { 
		spaces.setSize(64); 
	}

	public Vector<Space> getSpaces() {
		return spaces;
	}
	
	// Create all 64 spaces for the chessboard. Initialize them to have no pieces
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
	
	public void setPiece(int xCoord, int yCoord, Piece piece) {
		int index = getSpaceIndex(xCoord, yCoord);
		if(index > -1 && spaces.get(index) != null) {
			spaces.get(index).placePiece(piece);
		}
	}
	
	public int getSpaceIndex(int xCoord, int yCoord) {
		int index = 0;
		Iterator<Space> sp = spaces.iterator();
		while(sp.hasNext()) {
			Space tempSpace = sp.next();
			if(tempSpace.getxCoordinate() == xCoord && tempSpace.getyCoordinate() == yCoord) {
				return index;
			}
			index++;
		}
		
		return -1;
	}
	
	
	
}
