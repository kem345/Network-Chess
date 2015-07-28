package Tests;

import static org.junit.Assert.*;

import org.junit.Test;

import Pieces.Pawn;
import main.Game;

public class GameTest {

	Game game = new Game();
	
	@Test
	public void testCaptureTeam1Piece() {
		Pawn pawn = new Pawn(1, "team1");
		assertFalse(game.getTeam1CapturedPieces().contains(pawn));
		game.captureTeam1Piece(pawn);
		assertTrue(game.getTeam1CapturedPieces().contains(pawn));
	}

	@Test
	public void testCaptureTeam2Piece() {
		Pawn pawn = new Pawn(1, "team2");
		assertFalse(game.getTeam2CapturedPieces().contains(pawn));
		game.captureTeam2Piece(pawn);
		assertTrue(game.getTeam2CapturedPieces().contains(pawn));
	}

	@Test
	public void testStartGame() {
		game.startGame();
		assertTrue(game.getBoard().getSpaces().size() == 64);
		assertTrue(game.getTeam1CapturedPieces().size() == 0);
		assertTrue(game.getTeam2CapturedPieces().size() == 0);
	}


}
