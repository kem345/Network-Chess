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
	public void testSetPiece() {
		try {
			board = new Board();
			board.createBoard();
			assertFalse(board.getSpaces().get(0).hasPiece());
			board.setPiece(0, 0, new Pawn(0, Team.TEAM1));
			assertTrue(board.getSpaces().get(0).hasPiece());
		} catch(Exception e) {
			fail("Exception throw in test");
		}
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

}
