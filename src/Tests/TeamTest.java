package Tests;

import static org.junit.Assert.*;
import org.junit.Test;

import Pieces.Pawn;
import main.Team;

public class TeamTest {

	Team team = new Team("team1");
	
	@Test
	public void testAddPiece() {
		assertTrue(team.getPieces().isEmpty());
		team.addPiece(new Pawn(0, 1, 1));
		assertEquals(team.getPieces().size(), 1);
	}
	
	@Test
	public void testAddCapturePiece() {
		assertTrue(team.getCapturedPieces().isEmpty());
		team.addCapturedPiece(new Pawn(0, 1, 1));
		assertEquals(team.getCapturedPieces().size(), 1);
	}
	
	@Test
	public void testClearPieces() {
		team.addPiece(new Pawn(0, 1, 1));
		assertFalse(team.getPieces().isEmpty());
		team.clearPieces();
		assertTrue(team.getPieces().isEmpty());
	}
	
	@Test
	public void testClearCapturePieces() {
		team.addCapturedPiece(new Pawn(0, 1, 1));
		assertFalse(team.getCapturedPieces().isEmpty());
		team.clearCapturedPieces();
		assertTrue(team.getCapturedPieces().isEmpty());
	}

}
