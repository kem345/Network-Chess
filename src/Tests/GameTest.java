package Tests;

import static org.junit.Assert.*;

import org.junit.Test;

import Pieces.Pawn;
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
	public void testStartGame() {
		try {
			game.startGame();
			assertTrue(game.getBoard().getSpaces().size() == 64);
			assertTrue(game.getTeam1CapturedPieces().size() == 0);
			assertTrue(game.getTeam2CapturedPieces().size() == 0);
		} catch(Exception e) {
			fail("Exception throw in test");
		}
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


}
