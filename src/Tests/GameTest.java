package Tests;

import static org.junit.Assert.*;

import org.junit.Test;

import Pieces.*;
import main.Board;
import main.Game;
import main.Game.Team;

public class GameTest {

	Game game = new Game();
	
	@Test
	public void testCaptureTeam1Piece() throws Exception {
		Pawn pawn = new Pawn(1, Team.TEAM1);
		assertFalse(game.getTeam1CapturedPieces().contains(pawn));
		game.captureTeam1Piece(pawn);
		assertTrue(game.getTeam1CapturedPieces().contains(pawn));
	}

	@Test
	public void testCaptureTeam2Piece() throws Exception {
		Pawn pawn = new Pawn(1, Team.TEAM2);
		assertFalse(game.getTeam2CapturedPieces().contains(pawn));
		game.captureTeam2Piece(pawn);
		assertTrue(game.getTeam2CapturedPieces().contains(pawn));
	}

	@Test
	public void testStartGame() throws Exception {
		game.startGame();
		assertTrue(game.getBoard().getSpaces().size() == 64);
		assertTrue(game.getTeam1CapturedPieces().size() == 0);
		assertTrue(game.getTeam2CapturedPieces().size() == 0);
	}
	
	@Test
	public void testTurn() throws Exception {
		Game g = new Game();
		g.startGame();
		assertTrue(g.getTurn() == Team.TEAM1);
		g.changeTurn();
		assertTrue(g.getTurn() == Team.TEAM2);
		g.changeTurn();
		assertTrue(g.getTurn() == Team.TEAM1);
	}
	
	@Test
	public void testYourTeam() {
		Game g = new Game();
		g.setYourTeam(Team.TEAM1);
		assertTrue(g.getYourTeam().equals(Team.TEAM1));
	}
	
	@Test
	public void testIsValidMove() throws Exception {
		Game g = new Game();
		g.startGame();
		// Test normal move (pawn initial move)
		assertTrue(g.isValidMove(g.getBoard().getSpace(1, 1), g.getBoard().getSpace(1, 2)));
		// Test knight can jump over pieces
		assertTrue(g.isValidMove(g.getBoard().getSpace(1, 0), g.getBoard().getSpace(2, 2)));
		// Test illegal pawn move
		assertFalse(g.isValidMove(g.getBoard().getSpace(0, 1), g.getBoard().getSpace(5, 5)));
		// Test illegal knight move
		assertFalse(g.isValidMove(g.getBoard().getSpace(1, 0), g.getBoard().getSpace(5, 5)));
		
		g.makeMove(g.getBoard().getSpace(1, 0), g.getBoard().getSpace(2, 2));
		// Test pawn can't go through a piece
		assertFalse(g.isValidMove(g.getBoard().getSpace(2, 1), g.getBoard().getSpace(2, 3)));
		// Test a pawn can't land on a piece from the same team
		assertFalse(g.isValidMove(g.getBoard().getSpace(2, 1), g.getBoard().getSpace(2, 2)));
		// Test knight can't land on a piece from the same team
		assertFalse(g.isValidMove(g.getBoard().getSpace(6, 0), g.getBoard().getSpace(4, 1)));
		
		g.makeMove(g.getBoard().getSpace(2, 2), g.getBoard().getSpace(1, 4));
		// Test knight capture
		assertTrue(g.isValidMove(g.getBoard().getSpace(1, 4), g.getBoard().getSpace(0, 6)));
		g.makeMove(g.getBoard().getSpace(1, 4), g.getBoard().getSpace(0, 6));
		assertTrue(g.getBoard().getSpace(0, 6).getPiece() instanceof Knight);
		
		g.makeMove(g.getBoard().getSpace(4, 1), g.getBoard().getSpace(4, 3));
		g.makeMove(g.getBoard().getSpace(5, 6), g.getBoard().getSpace(5, 4));
		// Test pawn capture
		assertTrue(g.isValidMove(g.getBoard().getSpace(5, 4), g.getBoard().getSpace(4, 3)));
		g.makeMove(g.getBoard().getSpace(5, 4), g.getBoard().getSpace(4, 3));
		assertTrue(g.getBoard().getSpace(4, 3).getPiece() instanceof Pawn);
		
		assertFalse(g.getBoard().getSpace(5, 5).hasPiece());
		g.makeMove(g.getBoard().getSpace(5, 5), g.getBoard().getSpace(4, 3));
	}

	@Test
	public void testValidCastleMove() throws Exception {
		Game g = new Game();
		g.startGame();
		assertFalse(g.isValidMove(g.getBoard().getSpace(4, 0), g.getBoard().getSpace(0, 0)));
		// Clear path for castle move
		g.makeMove(g.getBoard().getSpace(3, 1), g.getBoard().getSpace(3, 3));
		g.makeMove(g.getBoard().getSpace(2, 0), g.getBoard().getSpace(4, 2));
		g.makeMove(g.getBoard().getSpace(1, 0), g.getBoard().getSpace(0, 2));
		g.makeMove(g.getBoard().getSpace(3, 0), g.getBoard().getSpace(3, 1));
		assertTrue(g.isValidMove(g.getBoard().getSpace(4, 0), g.getBoard().getSpace(0, 0)));
		// Make the castle move
		g.makeMove(g.getBoard().getSpace(4, 0), g.getBoard().getSpace(0, 0));
		// Test the castle move was made
		assertTrue(g.getBoard().getSpace(2, 0).getPiece() instanceof King);
		
		g.makeMove(g.getBoard().getSpace(4, 6), g.getBoard().getSpace(4, 5));
		g.makeMove(g.getBoard().getSpace(5, 7), g.getBoard().getSpace(3, 5));
		g.makeMove(g.getBoard().getSpace(6, 7), g.getBoard().getSpace(7, 5));
		assertTrue(g.isValidMove(g.getBoard().getSpace(4, 7), g.getBoard().getSpace(7, 7)));
		g.makeMove(g.getBoard().getSpace(4, 7), g.getBoard().getSpace(7, 7));
		assertTrue(g.getBoard().getSpace(6, 7).getPiece() instanceof King);
	}
	
	@Test
	public void testEnPassantMove() throws Exception {
		Game g = new Game();
		g.startGame();
		
		// Test team2 make en passant move
		g.makeMove(g.getBoard().getSpace(1, 1), g.getBoard().getSpace(1, 3));
		g.makeMove(g.getBoard().getSpace(2, 6), g.getBoard().getSpace(2, 4));
		g.makeMove(g.getBoard().getSpace(2, 4), g.getBoard().getSpace(2, 3));
		g.makeMove(g.getBoard().getSpace(2, 3), g.getBoard().getSpace(1, 2));
		assertFalse(g.getBoard().getSpace(1, 3).hasPiece());
		assertTrue(g.getBoard().getSpace(1, 2).hasPiece());
		
		// Test team1 make en passant move
		g.makeMove(g.getBoard().getSpace(4, 1), g.getBoard().getSpace(4, 3));
		g.makeMove(g.getBoard().getSpace(4, 3), g.getBoard().getSpace(4, 4));
		g.makeMove(g.getBoard().getSpace(3, 6), g.getBoard().getSpace(3, 4));
		g.makeMove(g.getBoard().getSpace(4, 4), g.getBoard().getSpace(3, 5));
		assertFalse(g.getBoard().getSpace(3, 4).hasPiece());
		assertTrue(g.getBoard().getSpace(3, 5).hasPiece());
	}
	
	@Test
	public void testOpponentsMove() throws Exception {
		Game g = new Game();
		g.startGame();
		assertTrue(g.getBoard().getSpace(0, 1).hasPiece());
		assertFalse(g.getBoard().getSpace(0, 2).hasPiece());
		g.updateOpponentsMove("MOVE,0,1,0,2");
		assertTrue(g.getBoard().getSpace(0, 2).hasPiece());
	}
	
	@Test(expected=Exception.class)
	public void testInvalidUpdateString() throws Exception {
		game.updateOpponentsMove("0,1");
	}
	
	@Test
	public void testUndoMove() throws Exception {
		Game g = new Game();
		g.startGame();
		
		// Test undo normal move
		g.makeMove(g.getBoard().getSpace(0, 1), g.getBoard().getSpace(0, 2));
		assertFalse(g.getBoard().getSpace(0, 1).hasPiece());
		g.undoMove(g.getBoard().getSpace(0, 1), g.getBoard().getSpace(0, 2), null);
		assertTrue(g.getBoard().getSpace(0, 1).hasPiece());
		
		//Test undo team2 captured
		g.makeMove(g.getBoard().getSpace(1, 1), g.getBoard().getSpace(1, 3));
		Pawn p1 = (Pawn) g.getBoard().getSpace(1, 3).getPiece();
		Pawn p2 = (Pawn) g.getBoard().getSpace(2, 6).getPiece();
		g.makeMove(g.getBoard().getSpace(2, 6), g.getBoard().getSpace(2, 4));
		g.makeMove(g.getBoard().getSpace(1, 3), g.getBoard().getSpace(2, 4));
		assertTrue(g.getTeam2CapturedPieces().size() == 1);
		g.undoMove(g.getBoard().getSpace(1, 3), g.getBoard().getSpace(2, 4), p2);
		assertTrue(g.getTeam2CapturedPieces().size() == 0);
		
		// Test undo team1 captured
		g.makeMove(g.getBoard().getSpace(2, 4), g.getBoard().getSpace(1, 3));
		assertTrue(g.getTeam1CapturedPieces().size() == 1);
		g.undoMove(g.getBoard().getSpace(2, 4), g.getBoard().getSpace(1, 3), p1);
		assertTrue(g.getTeam1CapturedPieces().size() == 0);
		
		// Test undo give a space without a piece
		assertFalse(g.getBoard().getSpace(6,4).hasPiece());
		g.undoMove(g.getBoard().getSpace(6, 4), g.getBoard().getSpace(5, 5), null);
		assertFalse(g.getBoard().getSpace(6,4).hasPiece());
	}
	
	@Test
	public void testCheckMate() throws Exception {
		Game g = new Game();
		Board b = new Board();
		b.createBoard();
		b.setPiece(4, 7, new King(0, Team.TEAM2));
		g.setBoard(b);
		assertFalse(g.checkMate(Team.TEAM2));
		b.setPiece(1, 7, new Queen(0, Team.TEAM1));
		b.setPiece(2, 6, new Rook(0, Team.TEAM1));
		// Test without any other team2 pieces
		assertTrue(g.checkMate(Team.TEAM2));
		
		// Test with Team2 piece that can't help
		b.setPiece(7, 5, new Pawn(0, Team.TEAM2));
		g.setBoard(b);
		assertTrue(g.checkMate(Team.TEAM2));
		
		// Test with team2 piece that can help by capturing the piece putting king in check
		b.setPiece(1, 3, new Queen(0, Team.TEAM2));
		g.setBoard(b);
		assertFalse(g.checkMate(Team.TEAM2));
		
		// Test with team2 piece that can get in the way
		b.removePiece(1, 3);
		b.setPiece(3, 3, new Rook(0, Team.TEAM2));
		g.setBoard(b);
		assertFalse(g.checkMate(Team.TEAM2));
		
		
	}
}
