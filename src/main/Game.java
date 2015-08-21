package main;

import java.util.Map.Entry;
import java.util.Vector;

import Pieces.*;
import Networking.Connection;

public class Game {

	public enum Team {
		TEAM1, TEAM2
	}
	
	private Vector<Piece> team1CapturedPieces = new Vector<Piece>();
	private Vector<Piece> team2CapturedPieces = new Vector<Piece>();
	private Board board;
	private Team turn = Team.TEAM1;
	private Team yourTeam;
	private Connection connection;

	// Constructor 
	
	public Game() {
		board = new Board();
	}
	
	public void connect(String ip) {
		connection = new Connection(ip);
	}
	
	public Connection getConnection() {
		return this.connection;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public void setBoard(Board board) {
		this.board = board;
	}

	public Vector<Piece> getTeam1CapturedPieces() {
		return team1CapturedPieces;
	}

	/** Capture the specified team1 piece 
	 * @throws Exception **/
	public void captureTeam1Piece(Piece piece) throws Exception {
		if(piece.getTeam().equals(Team.TEAM1))
			this.team1CapturedPieces.addElement(piece);
	}

	public Vector<Piece> getTeam2CapturedPieces() {
		return team2CapturedPieces;
	}

	/** Capture the specified team2 piece 
	 * @throws Exception **/
	public void captureTeam2Piece(Piece piece) throws Exception {
		if(piece.getTeam().equals(Team.TEAM2)) 
			this.team2CapturedPieces.addElement(piece);
	}

	public Team getTurn() {
		return turn;
	}

	public void changeTurn() {
		if(turn.equals(Team.TEAM1))
			turn = Team.TEAM2;
		else
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

	/** Returns true if the piece on the start space is allowed to move to the end space 
	 * @throws Exception **/
	public boolean isValidMove(Space start, Space end) throws Exception {
		Piece piece = start.getPiece();
		Piece endPiece = end.getPiece();
		
		// Simulate making the move to see if it puts your team in check
		this.board.removePiece(start.getxCoordinate(), start.getyCoordinate());
		this.board.setPiece(end.getxCoordinate(), end.getyCoordinate(), piece);
		// IF the move puts the team in check then it is not valid
		if(board.teamInCheck(piece.getTeam())) {
			start.placePiece(piece);
			end.placePiece(endPiece);
			return false;
		}
		
		// replace the piece to how they were
		start.placePiece(piece);
		end.placePiece(endPiece);
		
		// check castle move
		if(piece instanceof King && end.hasPiece() && end.getPiece() instanceof Rook &&
				board.canCastle(piece.getTeam(), (Rook)end.getPiece()))
			return true;
		
		// check en passant
		if(board.canEnpassant(start, end) && !end.hasPiece())
			return true;
		
		// Check the piece is allowed to move to the new space
		// Only knights can jump over pieces so if it is not a knight, check that the path for the move is clear
		// Check that another piece from the same team is not already on the space
		if(!(piece instanceof Knight) && piece.checkMove(start, end) && 
				board.clearBetween(start, end)) {
			if((!end.hasPiece()) || (end.hasPiece() && (!end.getPiece().getTeam().equals(piece.getTeam()))))
				return true;			
		} else if(piece instanceof Knight && piece.checkMove(start, end)) {
			if((!end.hasPiece()) || (end.hasPiece() && (!end.getPiece().getTeam().equals(piece.getTeam()))))
				return true;
		}
		
		return false;
	}
	
	/** Move the piece on the start space to the end space as long as the move is valid **/
	public void makeMove(Space start, Space end) throws Exception {
		// If the start space does not have a piece then no move can be made
		if(!start.hasPiece()) {
			return;
		}
		
		boolean approveMove = isValidMove(start, end);
		Piece piece = start.getPiece();
		
		// check castle move
		if(piece instanceof King && end.hasPiece() && end.getPiece() instanceof Rook &&
				board.canCastle(piece.getTeam(), (Rook)end.getPiece())) {
			Rook r = (Rook)end.getPiece();
			board.removePiece(start.getxCoordinate(), start.getyCoordinate());
			board.removePiece(end.getxCoordinate(), end.getyCoordinate());
			if(end.getxCoordinate() > start.getxCoordinate()) {	
				board.setPiece(start.getxCoordinate() + 2, start.getyCoordinate(), piece);
				board.setPiece(end.getxCoordinate() - 2, end.getyCoordinate(), r);
			} else if(end.getxCoordinate() < start.getxCoordinate()) {	
				board.setPiece(start.getxCoordinate() - 2, start.getyCoordinate(), piece);
				board.setPiece(end.getxCoordinate() + 3, end.getyCoordinate(), r);
			}
			
			return;
		}
		
		// check en passant
		if(board.canEnpassant(start, end) && !end.hasPiece()) {
			// If it fits en passant requirements and the moving piece in team 1 then 
			// the pawn being captured must be on row 5
			if(piece.getTeam().equals(Team.TEAM1)) {
				// Move the piece from start to finish position
				board.removePiece(start.getxCoordinate(), start.getyCoordinate());
				piece.moved();
				board.setPiece(end.getxCoordinate(), end.getyCoordinate(), piece);
				board.removePiece(end.getxCoordinate(), 4);
			} else {
				// If it fits en passant requirements and the moving piece in team 2 then 
				// the pawn being captured must be on row 3
				// Move the piece from start to finish position
				board.removePiece(start.getxCoordinate(), start.getyCoordinate());
				piece.moved();
				board.setPiece(end.getxCoordinate(), end.getyCoordinate(), piece);
				board.removePiece(end.getxCoordinate(), 3);
			}
			return;
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
			board.removePiece(start.getxCoordinate(), start.getyCoordinate());
			piece.moved();
			board.setPiece(end.getxCoordinate(), end.getyCoordinate(), piece);
			
			changeTurn();
		}
	}

	/** Promote the pawn on the given space to the newPiece **/
	public void promotePawn(Space space, Piece newPiece) {
		board.promotePiece(space, newPiece);
	}
	
	/** Returns true if the given team is in checkmate; false otherwise **/
	public boolean checkMate(Team team) throws Exception {
		// If the king is not in check or it can move itself out of check then return false
		if(!board.kingCheckmateCheck(team))
			return false;
		
		boolean stillCheck = true;
		// Go through each piece on the team and see if it can make a move to get the king out of check
		for(Entry<Space, Piece> entry : board.getTeamPieces(team).entrySet()) {
			// Go through all possible valid moves
			for(Space space : getAllValid(entry.getKey())) {
				// If the end space has a piece, get it so it can be replaced
				Piece p = space.getPiece();
				// Make the move, see if the team is still in check then undo the move
				makeMove(entry.getKey(), space);
				stillCheck = board.teamInCheck(team);
				undoMove(entry.getKey(), space, p);
				// if there is a move to get out of check, return false
				if(!stillCheck)
					return false;
			}
		}
		
		return true;
	}
	
	/** Returns a vector of all of the space that the piece on the start space is allowed to move to 
	 * @throws Exception **/
	private Vector<Space> getAllValid(Space start) throws Exception {
		Vector<Space> moves = new Vector<>();
		// King moves are checked somewhere else
		if(start.getPiece() instanceof King)
			return moves;
		
		for(Space space : board.getSpaces()) {
			if(isValidMove(start, space))
				moves.addElement(space);
		}
		
		return moves;
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
		String[] coords = move.split(",");
		
		if(!move.contains("Promote") && coords.length != 5){
			throw new Exception("Invalid message recieved from server");
		}
		
		// The string should be of the form:
		// MOVE,startx,starty,endx,endy
		int startx = Character.getNumericValue(coords[1].charAt(0));
		int starty = Character.getNumericValue(coords[2].charAt(0));
		int endx = Character.getNumericValue(coords[3].charAt(0));
		int endy = Character.getNumericValue(coords[4].charAt(0));
		
		// Make the move specified by the opponent
		Space start = board.getSpace(startx, starty);
		Space end = board.getSpace(endx, endy);
		Team team = start.getPiece().getTeam();
		makeMove(start, end);
		
		if(move.contains("Promote")) {
			if(coords.length != 8) {
				throw new Exception("Invalid promote message recieved from server");
			}
			String type = coords[6];
			int pieceNum = Integer.parseInt(coords[7]);
			Piece newPiece = null;
			if(type.equals("Knight"))
				newPiece = new Knight(pieceNum, team);
			else if(type.equals("Bishop"))
				newPiece = new Bishop(pieceNum, team);
			else if(type.equals("Rook"))
				newPiece = new Rook(pieceNum, team);
			else if(type.equals("Queen"))
				newPiece = new Queen(pieceNum, team);
			else
				throw new Exception("Invalid promote message recieved from server");
			
			this.promotePawn(end, newPiece);
		}
		
	}
	
	/** Undoes a move by putting the piece on the new space back on the original space **/
	public void undoMove(Space original, Space newSpace, Piece capturedPiece) {
		// The new space has to now have a piece in order to undo
		if(!newSpace.hasPiece())
			return;
		
		// Get the piece that moved and return its move count to what it was before the move
		Piece piece = newSpace.getPiece();
		piece.undoMove();
		
		// Remove from the new and place back on the original
		newSpace.removePiece();
		original.placePiece(piece);
		
		// If a piece was captured then put it back
		if(capturedPiece != null) {
			newSpace.placePiece(capturedPiece);
			if(capturedPiece.getTeam().equals(Team.TEAM1))
				this.team1CapturedPieces.removeElement(capturedPiece);
			else
				this.team2CapturedPieces.removeElement(capturedPiece);
		}
	}

}
