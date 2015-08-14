package main;

import java.util.Vector;

import Pieces.*;

public class Game {

	public enum Team {
		TEAM1, TEAM2
	}
	
	private Vector<Piece> team1CapturedPieces = new Vector<Piece>();
	private Vector<Piece> team2CapturedPieces = new Vector<Piece>();
	private Board board;
	private Team turn;
	private Team yourTeam;

	// Constructor 
	
	public Game() {
		board = new Board();
	}
	
	public Board getBoard() {
		return board;
	}

	public Vector<Piece> getTeam1CapturedPieces() {
		return team1CapturedPieces;
	}

	/** Capture the specified team1 piece 
	 * @throws Exception **/
	public void captureTeam1Piece(Piece piece) throws Exception {
		if(piece.getTeam().equals(Team.TEAM1)) {
			this.team1CapturedPieces.addElement(piece);
		} else {
			throw new Exception("A team 1 piece must be provided to this method");
		}
	}

	public Vector<Piece> getTeam2CapturedPieces() {
		return team2CapturedPieces;
	}

	public void captureTeam2Piece(Piece piece) throws Exception {
		if(piece.getTeam().equals(Team.TEAM2)) {
			this.team2CapturedPieces.addElement(piece);
		} else {
			throw new Exception("A team 2 piece must be provided to this method");
		}
	}

	public Team getTurn() {
		return turn;
	}

	public void changeTurn() {
		if(turn.equals(Team.TEAM1))
			turn = Team.TEAM2;
		else if(turn.equals(Team.TEAM2))
			turn = Team.TEAM1;
	}

	public Team getYourTeam() {
		return yourTeam;
	}

	public void setYourTeam(Team yourTeam) {
		this.yourTeam = yourTeam;
	}

	/** Setup the game in its initial state 
	 * @throws Exception **/
	public void startGame() throws Exception {
		// initially clear all pieces
		team1CapturedPieces.clear();
		team2CapturedPieces.clear();
		// Create the board and initialize all of the pieces		
		board.createBoard();
		initializePieces();
		turn = Team.TEAM1;		
		
	}
	
	// TODO: plan-- The clickable graphics will give coordinates of start and end spaces
	// TODO: Make sure move doesn't put you in check
	public boolean makeMove(Space start, Space end) throws Exception {
		// If the start space does not have a piece then no move can be made
		if(!start.hasPiece()) {
			throw new Exception("Start space does not have a piece to move");
		}
		Piece piece = start.getPiece();
		boolean approveMove = false;
		
		// Check the piece is allowed to move to the new space
		// Only knights can jump over pieces so if it is not a knight, check that the path for the move is clear
		// Check that another piece from the same team is not already on the space
		if(!(piece instanceof Knight) && piece.checkMove(start, end) && 
				board.clearBetween(start, end)) {
			if((!end.hasPiece()) || (end.hasPiece() && 
					(!end.getPiece().getTeam().equals(piece.getTeam()))))
				approveMove = true;				
		} else if(piece instanceof Knight && piece.checkMove(start, end)) {
			if((!end.hasPiece()) || (end.hasPiece() && 
					(!end.getPiece().getTeam().equals(piece.getTeam()))))
				approveMove = true;
		}
		
		// If the piece is allowed to move to the new space then allow it
		if(approveMove) {
			// If there is a piece in the new spot then capture it
			if(end.hasPiece()) {
				Piece endPiece = end.getPiece();
				if(endPiece.getTeam().equals(Team.TEAM1)) {
					captureTeam1Piece(endPiece);
					end.removePiece();
				} else if(endPiece.getTeam().equals(Team.TEAM2)) {
					captureTeam2Piece(endPiece);
					end.removePiece();
				}
			}
			// Move the piece from start to finish position
			start.removePiece();
			end.placePiece(piece);
			
			changeTurn();
			return true;
		}
		
		return false;
	}
	
	/**
	 * Initialize the teams pieces to the way they should be at the start of a
	 * game
	 * @throws Exception 
	 **/
	private void initializePieces() throws Exception {

		// Give each team 8 pawns at the correct starting positions
		for (int i = 0; i <= 7; i++) {
			board.setPiece(i, 1, new Pawn(i, Team.TEAM1));
			board.setPiece(i, 6, new Pawn(i, Team.TEAM2));
		}

		// Give both teams 2 knights in the correct starting positions
		board.setPiece(1, 0, new Knight(0, Team.TEAM1));
		board.setPiece(6, 0, new Knight(1, Team.TEAM1));
		board.setPiece(1, 7, new Knight(0, Team.TEAM2));
		board.setPiece(6, 7, new Knight(0, Team.TEAM2));

		// Give both teams 2 bishops in the correct starting positions
		board.setPiece(2, 0, new Bishop(0, Team.TEAM1));
		board.setPiece(5, 0, new Bishop(1, Team.TEAM1));
		board.setPiece(2, 7, new Bishop(0, Team.TEAM2));
		board.setPiece(5, 7, new Bishop(1, Team.TEAM2));

		// Give both teams 2 rooks in the correct starting positions
		board.setPiece(0, 0, new Rook(0, Team.TEAM1));
		board.setPiece(7, 0, new Rook(1, Team.TEAM1));
		board.setPiece(0, 7, new Rook(0, Team.TEAM2));
		board.setPiece(7, 7, new Rook(1, Team.TEAM2));

		// Give both teams a Queen in the correct starting position
		board.setPiece(3, 0, new Queen(0, Team.TEAM1));
		board.setPiece(3, 7, new Queen(0, Team.TEAM2));

		// Give both teams a King in the correct starting position
		board.setPiece(4, 0, new King(0, Team.TEAM1));
		board.setPiece(4, 7, new King(0, Team.TEAM2));
	}
	
	
	/** Update the board based on the opponent's move that has been received from the server 
	 * @throws Exception **/
	public void updateOpponentsMove(String move) throws Exception {
		if(move.length() != 4){
			throw new Exception("Invalid message recieved from server");
		}
		
		int startx = Character.getNumericValue(move.charAt(0));
		int starty = Character.getNumericValue(move.charAt(1));
		int endx = Character.getNumericValue(move.charAt(2));
		int endy = Character.getNumericValue(move.charAt(3));
		
		Space start = board.getSpace(startx, starty);
		Piece p = start.getPiece();
		
		board.removePiece(startx, starty);
		board.setPiece(endx, endy, p);
	}

}
