package Pieces;
import java.lang.Math;

public class Pawn extends Piece{

	public Pawn(int num, int xCoord, int yCoord) {
		super(num, xCoord, yCoord);
	}

	@Override
	public void move(int xPos, int yPos) {
		if(checkMove(xPos, yPos)) {
			this.setxCoordinate(xPos);
			this.setyCoordinate(yPos);
		}
	}
	
	@Override
	public boolean checkMove(int xPos, int yPos) {
		 
		if(Math.abs(xCoordinate - xPos) > 1) {
			return false; 
		}
		 
		return true;
	}
}
