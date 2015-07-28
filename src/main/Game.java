package main;

import java.util.Vector;

import Pieces.*;

public class Game {

	private Vector<Piece> team1CapturedPieces = new Vector<Piece>();
	private Vector<Piece> team2CapturedPieces = new Vector<Piece>();
	private Board board;

	public Game() {
		board = new Board();
	}
	
	public Board getBoard() {
		return board;
	}

	public Vector<Piece> getTeam1CapturedPieces() {
		return team1CapturedPieces;
	}

	public void captureTeam1Piece(Piece piece) {
		this.team1CapturedPieces.addElement(piece);
	}

	public Vector<Piece> getTeam2CapturedPieces() {
		return team2CapturedPieces;
	}

	public void captureTeam2Piece(Piece piece) {
		this.team2CapturedPieces.addElement(piece);
	}

	/** Setup the game in its intial state **/
	public void startGame() {
		// initially clear all pieces
		team1CapturedPieces.clear();
		team2CapturedPieces.clear();

		board.createBoard();
		initializePieces();

	}

	/**
	 * Initialize the teams pieces to the way they should be at the start of a
	 * game
	 **/
	private void initializePieces() {

		// Give each team 8 pawns at the correct starting positions
		for (int i = 1; i <= 8; i++) {
			board.setPiece(i, 2, new Pawn(i, "team1"));
			board.setPiece(i, 7, new Pawn(i, "team2"));
		}

		// Give both teams 2 knights in the correct starting positions
		board.setPiece(2, 1, new Knight(0, "team1"));
		board.setPiece(7, 1, new Knight(1, "team1"));
		board.setPiece(2, 8, new Knight(0, "team2"));
		board.setPiece(7, 8, new Knight(0, "team2"));

		// Give both teams 2 bishops in the correct starting positions
		board.setPiece(3, 1, new Bishop(0, "team1"));
		board.setPiece(6, 1, new Bishop(1, "team1"));
		board.setPiece(3, 8, new Bishop(0, "team2"));
		board.setPiece(6, 8, new Bishop(1, "team2"));

		// Give both teams 2 rooks in the correct starting positions
		board.setPiece(1, 1, new Rook(0, "team1"));
		board.setPiece(8, 1, new Rook(1, "team1"));
		board.setPiece(1, 8, new Rook(0, "team2"));
		board.setPiece(8, 8, new Rook(1, "team2"));

		// Give both teams a Queen in the correct starting position
		board.setPiece(4, 1, new Queen(0, "team1"));
		board.setPiece(4, 8, new Queen(0, "team2"));

		// Give both teams a King in the correct starting position
		board.setPiece(5, 1, new King(0, "team1"));
		board.setPiece(5, 8, new King(0, "team2"));
	}

}
