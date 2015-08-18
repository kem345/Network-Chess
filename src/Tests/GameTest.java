package Tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import Pieces.King;
import Pieces.Knight;
import Pieces.Pawn;
import main.Game;
import main.Game.Team;

public class GameTest {

	@Test
	public void testCaptureTeam1Piece() throws Exception {
		Game game = new Game();
		Pawn pawn = new Pawn(1, Team.TEAM1);
		assertFalse(game.getTeam1CapturedPieces().contains(pawn));
		game.captureTeam1Piece(pawn);
		assertTrue(game.getTeam1CapturedPieces().contains(pawn));
	}

	@Test
	public void testCaptureTeam2Piece() throws Exception {
		Game game = new Game();
		Pawn pawn = new Pawn(1, Team.TEAM2);
		assertFalse(game.getTeam2CapturedPieces().contains(pawn));
		game.captureTeam2Piece(pawn);
		assertTrue(game.getTeam2CapturedPieces().contains(pawn));
	}

	@Test
	public void testStartGame() throws Exception {
		Game game = new Game();
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
	public void testYourTeam() throws IOException {
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
	public void testOpponentsMove() throws Exception {
		Game g = new Game();
		g.startGame();
		assertTrue(g.getBoard().getSpace(0, 1).hasPiece());
		assertFalse(g.getBoard().getSpace(0, 2).hasPiece());
		g.updateOpponentsMove("0,1,0,2");
		assertTrue(g.getBoard().getSpace(0, 2).hasPiece());
	}
	
	@Test(expected=Exception.class)
	public void testInvalidUpdateString() throws Exception {
		Game game = new Game();
		game.updateOpponentsMove("0,1");
	}
}
