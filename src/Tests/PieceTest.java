package Tests;

import static org.junit.Assert.*;

import org.junit.Test;

import Pieces.*;
import main.Board;
import main.Game.Team;
import main.Space;

public class PieceTest {

	Pawn pawn = new Pawn(0, Team.TEAM1);
	Pawn pawn2 = new Pawn(1, Team.TEAM2);
	Knight knight = new Knight(0, Team.TEAM1);
	Bishop bishop = new Bishop(0, Team.TEAM1);
	Rook rook = new Rook(0, Team.TEAM1);
	Queen queen = new Queen(0, Team.TEAM1);
	King king = new King(0, Team.TEAM1);
	
	Board board = new Board();
	
	@Test
	public void testBasic() {
		assertTrue(pawn.getPieceNumber() == 0);
		assertTrue(pawn.getTeam() == Team.TEAM1);
		pawn.setPieceNumber(1);
		pawn.setTeam(Team.TEAM2);
		assertTrue(pawn.getPieceNumber() == 1);
		assertTrue(pawn.getTeam() == Team.TEAM2);
	}
	
	@Test
	public void testMoved() {
		assertTrue(pawn.getMoveCount() == 0);
		pawn.moved();
		assertTrue(pawn.getMoveCount() == 1);
	}

	@Test
	public void testCheckPawnMove() {
		board.createBoard();
		Space s1 = new Space(1, 1);
		Space s2 = new Space(1, 2);
		Space s3 = new Space(1, 3);
		Space s4 = new Space(2,2);
		
		// Normal move
		assertTrue(pawn.checkMove(s1, s2));
		// Initial move 2 spaces
		assertTrue(pawn.checkMove(s1, s3));
		// Capture move
		assertFalse(pawn.checkMove(s1, s4));
		s4.placePiece(new Knight(0, Team.TEAM2));
		assertTrue(pawn.checkMove(s1, s4));
		
		// Same tests for a team 2 piece since they move in a different direction
		// Normal move
		assertTrue(pawn2.checkMove(s2, s1));
		// Initial move 2 spaces
		assertTrue(pawn2.checkMove(s3, s1));
		// Capture move
		assertFalse(pawn2.checkMove(s4, s1));
		s1.placePiece(new Knight(0, Team.TEAM1));
		assertTrue(pawn2.checkMove(s4, s1));
	}
	
	@Test
	public void testKnightMove() {
		Space s11 = new Space(1, 1);
		Space s23 = new Space(2, 3);
		Space s22 = new Space(2, 2);
		Space s44 = new Space(4, 4);
		
		assertTrue(knight.checkMove(s11, s23));
		assertTrue(knight.checkMove(s23, s44));
		assertFalse(knight.checkMove(s11, s22));
	}
	
	@Test
	public void testBishopMove() {
		Space s11 = new Space(1, 1);
		Space s23 = new Space(2, 3);
		Space s44 = new Space(4, 4);
		
		assertTrue(bishop.checkMove(s11, s44));
		assertTrue(bishop.checkMove(s44, s11));
		assertFalse(bishop.checkMove(s11, s23));
	}
	
	@Test
	public void testRookMove() {
		Space s11 = new Space(1, 1);
		Space s14 = new Space(1, 4);
		Space s41 = new Space(4, 1);
		Space s44 = new Space(4, 4);
		
		assertTrue(rook.checkMove(s11, s14));
		assertTrue(rook.checkMove(s41, s11));
		assertFalse(rook.checkMove(s11, s44));
	}
	
	@Test
	public void testQueenMove() {
		Space s11 = new Space(1, 1);
		Space s14 = new Space(1, 4);
		Space s41 = new Space(4, 1);
		Space s44 = new Space(4, 4);
		Space s56 = new Space(5, 6);
		
		assertTrue(queen.checkMove(s11, s14));
		assertTrue(queen.checkMove(s41, s11));
		assertTrue(queen.checkMove(s11, s44));
		assertFalse(queen.checkMove(s11, s56));
	}

	@Test
	public void testKingMove() {
		Space s11 = new Space(1, 1);
		Space s12 = new Space(1, 2);
		Space s21 = new Space(2, 1);
		Space s22 = new Space(2, 2);
		Space s56 = new Space(5, 6);
		
		assertTrue(king.checkMove(s11, s12));
		assertTrue(king.checkMove(s21, s11));
		assertTrue(king.checkMove(s11, s22));
		assertFalse(king.checkMove(s11, s56));
	}
	
	@Test
	public void testEquals() {
		Pawn p = new Pawn(0, Team.TEAM1);
		assertTrue(pawn.equals(p));
		assertFalse(pawn.equals(pawn2));
		assertFalse(pawn.equals(rook));
		Knight k = new Knight(0, Team.TEAM1);
		assertTrue(knight.equals(k));
		assertFalse(knight.equals(rook));
		assertFalse(knight.equals(new Knight(1, Team.TEAM2)));
		Bishop b = new Bishop(0, Team.TEAM1);
		assertTrue(bishop.equals(b));
		assertFalse(bishop.equals(rook));
		assertFalse(bishop.equals(new Bishop(1, Team.TEAM2)));
		Rook r = new Rook(0, Team.TEAM1);
		assertTrue(rook.equals(r));
		assertFalse(rook.equals(pawn));
		assertFalse(rook.equals(new Rook(1, Team.TEAM2)));
		Queen q = new Queen(0, Team.TEAM1);
		assertTrue(queen.equals(q));
		assertFalse(queen.equals(rook));
		assertFalse(queen.equals(new Queen(1, Team.TEAM2)));
		King k2 = new King(0, Team.TEAM1);
		assertTrue(king.equals(k2));
		assertFalse(king.equals(rook));
		assertFalse(king.equals(new King(1, Team.TEAM2)));
	}

}
