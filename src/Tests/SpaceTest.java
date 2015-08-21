package Tests;

import static org.junit.Assert.*;

import org.junit.Test;

import Pieces.Pawn;
import Pieces.Piece;
import main.Space;
import main.Game.Team;

public class SpaceTest {

	@Test
	public void testGetxCoordinate() {
		
	}

	@Test
	public void testSetValidCoordinates() throws Exception {
		Space space = new Space(0, 0);
		space.setxCoordinate(5);
		space.setyCoordinate(3);
		assertTrue(space.getxCoordinate() == 5 && space.getyCoordinate() == 3);
	}

	@Test(expected=Exception.class)
	public void testSetInvalidxCoordinate1() throws Exception {
		// test that exception is thrown when coord is over
		Space sp = new Space(0,0);
		sp.setxCoordinate(9);	
	}
	
	@Test(expected=Exception.class)
	public void testSetInvalidxCoordinate2() throws Exception {
		// test that exception is thrown when coord is under
		Space sp = new Space(0,0);
		sp.setxCoordinate(8);	
	}
	
	@Test(expected=Exception.class)
	public void testSetInvalidyCoordinate1() throws Exception {
		// test that exception is thrown when coord is over
		Space sp = new Space(0,0);
		sp.setyCoordinate(9);	
	}
	
	@Test(expected=Exception.class)
	public void testSetInvalidyCoordinate2() throws Exception {
		// test that exception is thrown when coord is under
		Space sp = new Space(0,0);
		sp.setyCoordinate(-1);	
	}

	@Test
	public void testPieceOperations() {
		Space sp = new Space(1,1);
		Piece piece = new Pawn(0, Team.TEAM1);
		assertFalse(sp.hasPiece());
		sp.placePiece(piece);
		assertTrue(sp.hasPiece());
		assertEquals(piece, sp.getPiece());
		sp.removePiece();
		assertFalse(sp.hasPiece());
	}

	@Test
	public void testIsInRow() {
		Space sp1 = new Space(1,1);
		Space sp2 = new Space(2,1);
		Space sp3 = new Space(5,5);		
		assertTrue(sp1.isInRow(sp2));
		assertFalse(sp1.isInRow(sp3));
	}

	@Test
	public void testIsInColumn() {
		Space sp1 = new Space(1,1);
		Space sp2 = new Space(1,2);
		Space sp3 = new Space(5,5);		
		assertTrue(sp1.isInColumn(sp2));
		assertFalse(sp1.isInColumn(sp3));
	}

	@Test
	public void testIsInDiagonal() {
		Space sp1 = new Space(1,1);
		Space sp2 = new Space(5,5);
		Space sp3 = new Space(1,5);		
		assertTrue(sp1.isInDiagonal(sp2));
		assertFalse(sp1.isInDiagonal(sp3));
	}
	
	@Test
	public void testEquals() {
		Space s1 = new Space(5, 5);
		Space s2 = new Space(5, 5);
		Space s3 = new Space(1,5);
		Space s4 = new Space(5,1);
		assertTrue(s1.equals(s2));
		assertFalse(s1.equals(s3));
		assertFalse(s1.equals(s4));
		assertFalse(s1.equals(null));
		assertFalse(s1.equals(new Pawn(0, Team.TEAM1)));
	}
	
	@Test
	public void testHashCode() {
		Space s1 = new Space(1,1);
		Space s2 = new Space(1,1);
		
		assertTrue(s1.hashCode() == s2.hashCode());
	}

}
