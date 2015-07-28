package Pieces;
import java.lang.Math;

public class Pawn extends Piece{

	

	public Pawn(int num, String team) {
		super(num, team);
	}

	
	@Override
	public boolean checkMove(int xPos, int yPos) {
		 
		return true;
	}
}
