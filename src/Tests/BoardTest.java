package Tests;

import static org.junit.Assert.*;

import org.junit.Test;

import Pieces.*;
import main.Board;
import main.Space;
import main.Game.Team;

public class BoardTest {

	Board board = new Board();
	Space s1 = new Space(1, 1);
	Space s2 = new Space(2, 2);
	Space s3 = new Space(3, 3);
	Space s4 = new Space(4, 4);
	@Test
	public void testCreateBoard() {
		try {
			board.createBoard();
			// check that it creates 64 spaces
			assertTrue(board.getSpaces().size() == 64);
			// check that the first and last elements are correct
			assertTrue(board.getSpaces().elementAt(0).getxCoordinate() == 0);
			assertTrue(board.getSpaces().elementAt(0).getyCoordinate() == 0);
			assertTrue(board.getSpaces().elementAt(63).getxCoordinate() == 7);
			assertTrue(board.getSpaces().elementAt(63).getyCoordinate() == 7);
		} catch(Exception e) {
			fail("Exception throw in test");
		}
	}

	@Test
	public void testSetPiece() throws Exception {
		Board board = new Board();
		board.createBoard();
		assertFalse(board.getSpaces().get(0).hasPiece());
		board.setPiece(0, 0, new Pawn(0, Team.TEAM1));
		assertTrue(board.getSpaces().get(0).hasPiece());
	}
	
	@Test(expected=Exception.class)
	public void testSetPieceException() throws Exception {
		board.setPiece(10, 10, null);
	}

	@Test
	public void testRemovePiece() {
		try {
			board = new Board();
			board.createBoard();
			board.setPiece(5, 5, new Pawn(1, Team.TEAM1));
			assertTrue(board.getSpace(5, 5).hasPiece());
			board.removePiece(5, 5);
			assertFalse(board.getSpace(5, 5).hasPiece());
		} catch(Exception e) {
			fail("Exception throw in test");
		}
	}

	@Test
	public void testGetSpaceIndex() {
		board = new Board();
		board.createBoard();
		assertTrue(board.getSpaceIndex(0, 0) == 0);
		assertTrue(board.getSpaceIndex(7, 7) == 63);
		assertTrue(board.getSpaceIndex(9, 9) == -1);
	}
	
	@Test 
	public void testGetSpace() {
		board = new Board();
		board.createBoard();
		Space sp = board.getSpace(0, 0);
		assertTrue(sp.getxCoordinate() == 0 && sp.getyCoordinate() == 0);
		assertTrue(board.getSpace(8, 8) == null);
	}

	@Test
	public void testClearInRow() {
		try {
			// reset board without pieces
			board = new Board();
			board.createBoard();
			board.setPiece(1, 1, new Pawn(0, Team.TEAM1));
			assertTrue(board.getSpace(1, 1).hasPiece());
			Space s41 = new Space(4, 1);
			assertTrue(board.clearInRow(s1, s41));
			board.setPiece(3, 1, new Pawn(2, Team.TEAM1));
			assertFalse(board.clearInRow(s41, s1));
			assertTrue(board.clearInRow(s1, s3));
		} catch(Exception e) {
			fail("Exception throw in test");
		}
	}

	@Test
	public void testClearInColumn() {
		try {
			// reset board without pieces
			board = new Board();
			board.createBoard();
			board.setPiece(1, 1, new Pawn(0, Team.TEAM1));
			assertTrue(board.getSpace(1, 1).hasPiece());
			Space s14 = new Space(1, 4);
			assertTrue(board.clearInColumn(s14, s1));
			board.setPiece(1, 3, new Pawn(2, Team.TEAM1));
			assertFalse(board.clearInColumn(s1, s14));
			assertTrue(board.clearInColumn(s1, s3));
		} catch(Exception e) {
			fail("Exception throw in test");
		}
	}

	@Test
	public void testClearInDiagonal() {
		try {
			// reset board without pieces
			board = new Board();
			board.createBoard();
			board.setPiece(1, 1, new Pawn(0, Team.TEAM1));
			assertTrue(board.getSpace(1, 1).hasPiece());
			assertTrue(board.clearInDiagonal(s1, s3));
			board.setPiece(2, 2, new Pawn(2, Team.TEAM1));
			assertFalse(board.clearInDiagonal(s1, s3));
			Space s = new Space(1,3);
			assertTrue(board.clearInDiagonal(s1, s));
		} catch(Exception e) {
			fail("Exception throw in test");
		}
	}
	
	@Test
	public void testClearBetween() {
		board = new Board();
		board.createBoard();
		Space s42 = new Space(4, 2);
		Space s24 = new Space(2, 4);
		Space s76 = new Space(7, 6);
		
		try {
			board.setPiece(4, 4, new Pawn(0, Team.TEAM1));
			// Check clear in row, column and diagonal
			assertTrue(board.clearBetween(s4, s24));
			assertTrue(board.clearBetween(s4, s42));
			assertTrue(board.clearBetween(s4, s2));
			
			// Set pieces between those space and check for false
			board.setPiece(3, 4, new Knight(0, Team.TEAM1));
			board.setPiece(4, 3, new Bishop(0, Team.TEAM1));
			board.setPiece(3, 3, new Rook(0, Team.TEAM1));
			assertFalse(board.clearBetween(s4, s24));
			assertFalse(board.clearBetween(s4, s42));
			assertFalse(board.clearBetween(s4, s2));
			
			// Test spaces that aren't in any line with each other 
			assertFalse(board.clearBetween(s4, s76));
			
		} catch (Exception e) {
			fail("Exception thrown");
		}
	}
	
	@Test
	public void testGetTeamPieces() throws Exception {
		board = new Board();
		board.createBoard();
		Pawn pawn = new Pawn(0, Team.TEAM1);
		Pawn pawn2 = new Pawn(0, Team.TEAM2);
		
		assertEquals(board.getTeamPieces(Team.TEAM1).size(), 0);
		board.setPiece(4, 4, pawn);
		board.setPiece(4, 3, pawn2);
		assertTrue(board.getTeamPieces(Team.TEAM1).size() == 1);
				
	}
	
	@Test
	public void testTeamInCheck() throws Exception {
		board = new Board();
		board.createBoard();
		
		board.setPiece(1, 2, new Pawn(0, Team.TEAM1));
		board.setPiece(4, 4, new King(0, Team.TEAM1));
		assertFalse(board.teamInCheck(Team.TEAM1));
		board.setPiece(1, 1, new Queen(0, Team.TEAM2));
		assertTrue(board.teamInCheck(Team.TEAM1));
		assertFalse(board.teamInCheck(Team.TEAM2));
		
	}
	
	@Test
	public void testTeamInCheckmate() throws Exception {
		board = new Board();  
		board.createBoard();
		
		board.setPiece(3, 0, new King(0, Team.TEAM1));
		board.setPiece(0, 0, new Pawn(0, Team.TEAM2));
		assertFalse(board.teamInCheckmate(Team.TEAM1));
		board.setPiece(6, 0, new Queen(0, Team.TEAM2));
		assertFalse(board.teamInCheckmate(Team.TEAM1));
		board.setPiece(2, 1, new Rook(0, Team.TEAM2));
		assertTrue(board.teamInCheckmate(Team.TEAM1));
	}
	
	@Test
	public void testCastleMove() throws Exception {
		Rook r0 = new Rook(0, Team.TEAM1);
		Rook r1 = new Rook(1, Team.TEAM1);
		King k0 = new King(0, Team.TEAM1);
		Queen q20 = new Queen(0, Team.TEAM2);
		Rook r20 = new Rook(0, Team.TEAM2);
		Pawn p20 = new Pawn(0, Team.TEAM2);
		board = new Board();
		board.createBoard();
		
		// Test false is return for not real rook
		assertFalse(board.canCastle(Team.TEAM1, new Rook(4, Team.TEAM1)));
		board.setPiece(4, 0, k0);
		board.setPiece(0, 0, r0);
		board.setPiece(7, 0, r1);
		// Test that correct castle moves are allowed
		assertTrue(board.canCastle(Team.TEAM1, r0));
		assertTrue(board.canCastle(Team.TEAM1, r1));
		
		// Put king in check and test that castle is not allowed
		board.setPiece(4, 5, q20);
		assertFalse(board.canCastle(Team.TEAM1, r0));
		assertFalse(board.canCastle(Team.TEAM1, r1));
		board.removePiece(4, 5);
		
		// Have the king need to move through check and test that the castle is not allowed
		board.setPiece(3, 4, r20);
		assertFalse(board.canCastle(Team.TEAM1, r0));
		assertTrue(board.canCastle(Team.TEAM1, r1));
		board.removePiece(3, 4);
		board.setPiece(5, 4, r20);
		assertTrue(board.canCastle(Team.TEAM1, r0));
		assertFalse(board.canCastle(Team.TEAM1, r1));
		board.removePiece(5, 4);
		
		// Put a piece between the king and rook and test that the castle is not allowed
		board.setPiece(2, 0, p20);
		assertFalse(board.canCastle(Team.TEAM1, r0));
		assertTrue(board.canCastle(Team.TEAM1, r1));
		board.removePiece(2, 0);
		board.setPiece(6, 0, p20);
		assertTrue(board.canCastle(Team.TEAM1, r0));
		assertFalse(board.canCastle(Team.TEAM1, r1));
		board.removePiece(6, 0);
		
		// Move the king and rook and test that the castle is not allowed
		assertTrue(board.canCastle(Team.TEAM1, r0));
		board.removePiece(4, 0);
		k0.moved();
		board.setPiece(4, 0, k0);
		assertFalse(board.canCastle(Team.TEAM1, r0));
		board.removePiece(4, 0);
		k0 = new King(0, Team.TEAM1);
		board.setPiece(4, 0, k0);
		board.removePiece(0, 0);
		r0.moved();
		board.setPiece(0, 0, r0);
		assertFalse(board.canCastle(Team.TEAM1, r0));
	}
	
	@Test
	public void testEnPassant() throws Exception {
		board = new Board();
		board.createBoard();
		
		Pawn p01 = new Pawn(0, Team.TEAM1);
		Pawn p11 = new Pawn(1, Team.TEAM1);
		Pawn p02 = new Pawn(0, Team.TEAM2);
		Pawn p12 = new Pawn(1, Team.TEAM2);
		Bishop b1 = new Bishop(0, Team.TEAM1);
		Bishop b2 = new Bishop(0, Team.TEAM2);
		
		p01.moved(); p01.moved(); p11.moved(); 
		board.setPiece(1, 3, p01);
		board.setPiece(3, 3, p11);
		board.setPiece(2, 3, p02);
		board.setPiece(4, 3, p12);
		
		// Test team2 capturing team1
		assertFalse(board.canEnpassant(board.getSpace(2, 3), board.getSpace(1, 2)));
		assertTrue(board.canEnpassant(board.getSpace(4, 3), board.getSpace(3, 2)));
		
		// Reset board
		board = new Board();
		board.createBoard();
		board.setPiece(1, 4, p02);
		board.setPiece(3, 4, p12);
		board.setPiece(2, 4, p01);
		board.setPiece(4, 4, p11);
		p02.moved(); p02.moved(); p12.moved();
		
		// Test team2 capturing team1
		assertFalse(board.canEnpassant(board.getSpace(2, 4), board.getSpace(1, 5)));
		assertTrue(board.canEnpassant(board.getSpace(4, 4), board.getSpace(3, 5)));
		
		board = new Board();
		board.createBoard();
		board.setPiece(2, 3, b1);
		board.setPiece(3, 3, p02);
		board.setPiece(5, 4, b2);
		board.setPiece(6, 4, p01);
		// bishop can't be captured by en passant
		assertFalse(board.canEnpassant(board.getSpace(3, 3), board.getSpace(2, 2)));
		assertFalse(board.canEnpassant(board.getSpace(6, 4), board.getSpace(5, 5)));
		
		// Bishop can't do en passant move
		board.setPiece(3, 3, b1);
		board.setPiece(2, 3, p02);
		assertFalse(board.canEnpassant(board.getSpace(3, 3), board.getSpace(2, 2)));
		
		board.setPiece(2, 2, p11);
		board.setPiece(3, 2, p12);
		board.setPiece(4, 2, p01);
		board.setPiece(5, 2, p02);
		// Cant use enpassant at wrong space
		assertFalse(board.canEnpassant(board.getSpace(3, 2), board.getSpace(2, 1)));
		assertFalse(board.canEnpassant(board.getSpace(4, 2), board.getSpace(5, 1)));
	}
	
	@Test
	public void testPromote() throws Exception {
		board = new Board();
		board.createBoard();
		
		Pawn p0 = new Pawn(0, Team.TEAM1);
		Pawn p1 = new Pawn(1, Team.TEAM1);
		Pawn p2 = new Pawn(2, Team.TEAM1);
		Knight k0 = new Knight(0, Team.TEAM1);
		
		board.setPiece(5, 7, p0);
		board.setPiece(4, 6, p1);
		board.setPiece(3, 0, p2);
		board.setPiece(1, 7, k0);
		board.promotePiece(board.getSpace(5, 7), new Queen(1, Team.TEAM1));
		assertTrue(board.getSpace(5, 7).getPiece() instanceof Queen);
		board.promotePiece(board.getSpace(4, 6), new Queen(2, Team.TEAM1));
		assertTrue(board.getSpace(4, 6).getPiece() instanceof Pawn);
		board.promotePiece(board.getSpace(1, 7), new Queen(2, Team.TEAM1));
		assertTrue(board.getSpace(1, 7).getPiece() instanceof Knight);
		board.promotePiece(board.getSpace(0, 0), new Queen(2, Team.TEAM1));
		assertFalse(board.getSpace(0, 0).hasPiece());
		board.promotePiece(board.getSpace(3, 0), new Queen(2, Team.TEAM2));
		assertTrue(board.getSpace(3, 0).getPiece() instanceof Pawn);
	}

}
