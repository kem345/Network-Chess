package main;

import java.util.Vector;

import Pieces.Piece;

public class Team {

	private String name = new String();
	private Vector<Piece> pieces = new Vector<Piece>();
	private Vector<Piece> capturedPieces = new Vector<Piece>();
	
	public Team(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Vector<Piece> getPieces() {
		return pieces;
	}
	public void addPiece(Piece piece) {
		this.pieces.addElement(piece);
	}
	public void clearPieces() {
		this.pieces.clear();
	}
	
	public Vector<Piece> getCapturedPieces() {
		return capturedPieces;
	}
	public void addCapturedPiece(Piece capturedPiece) {
		this.capturedPieces.addElement(capturedPiece);
	}
	public void clearCapturedPieces() {
		this.capturedPieces.clear();
	}
	
	
}
