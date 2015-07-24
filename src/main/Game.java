package main;

import Pieces.*;

public class Game {

	private Team team1;
	private Team team2;
	private String saveCode;

	public Team getTeam1() {
		return team1;
	}

	public void setTeam1(Team team1) {
		this.team1 = team1;
	}

	public Team getTeam2() {
		return team2;
	}

	public void setTeam2(Team team2) {
		this.team2 = team2;
	}

	public String getSaveCode() {
		return saveCode;
	}

	public void setSaveCode(String saveCode) {
		this.saveCode = saveCode;
	}

	public void startGame() {
		// initially clear all pieces
		team1.clearCapturedPieces();
		team1.clearPieces();
		team2.clearCapturedPieces();
		team2.clearPieces();
		
		initializePieces();
		
	}
	
	
	
	/**
	 * Initialize the teams pieces to the way they should be at the start of a
	 * game
	 **/
	public void initializePieces() {

		// Give each team 8 pawns at the correct starting positions
		for (int i = 0; i < 8; i++) {
			team1.addPiece(new Pawn(i, i + 1, 2));
			team2.addPiece(new Pawn(i, i + 1, 7));
		}

		// Give both teams 2 knights in the correct starting positions
		team1.addPiece(new Knight(0, 2, 1));
		team1.addPiece(new Knight(1, 7, 1));
		team2.addPiece(new Knight(0, 2, 8));
		team2.addPiece(new Knight(1, 7, 8));

		// Give both teams 2 bishops in the correct starting positions
		team1.addPiece(new Bishop(0, 3, 1));
		team1.addPiece(new Bishop(1, 6, 1));
		team2.addPiece(new Bishop(0, 3, 8));
		team2.addPiece(new Bishop(1, 6, 8));

		// Give both teams a Queen in the correct starting position
		team1.addPiece(new Queen(0, 4, 1));
		team1.addPiece(new Queen(0, 4, 8));

		// Give both teams a King in the correct starting position
		team1.addPiece(new King(0, 5, 1));
		team1.addPiece(new King(0, 5, 8));

	}

}
