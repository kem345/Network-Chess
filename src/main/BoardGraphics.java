package main;

import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;

import Pieces.Bishop;
import Pieces.King;
import Pieces.Knight;
import Pieces.Pawn;
import Pieces.Piece;
import Pieces.Queen;
import Pieces.Rook;
import main.Game;
import main.Game.Team;

public class BoardGraphics extends Applet implements ActionListener {

	  //Label noteLabel;
	  //Button undo;
	  //Button end;
	  //Button startGame;
	  String importedIP;

	  
	  public void init() {
		//undo = new Button("Undo");
		//a.setSize(1, 1);
		//end = new Button ("End Turn");
		//startGame = new Button ("start game");
		//noteLabel = new Label("Game Started");
		//undo.setBounds(450, 50, 60, 20);
		//end.setBounds(450, 80, 60, 20);
		//startGame.setBounds(450, 120, 60, 20);
		//noteLabel.setBounds(10, 450, 100, 20);
		//undo.addActionListener(this);
		//end.addActionListener(this);
		//startGame.addActionListener(this);
		//a.setPreferredSize(new Dimension(1,1));
		//SMCanvas mainCanvas = new SMCanvas();
		//undo = new Button(mainCanvas, SWT.Push);
	    //this.add(undo);
	    //this.add(end);
	    //this.add(startGame);
	    //this.add(noteLabel);
	    setLayout(new BorderLayout());
	    add(new SMCanvas(importedIP));
	    //label = new Label("Drag rectangle around within the area");
	    //add("South", label);
	  }
	  
	  public void actionPerformed(ActionEvent e) {

	  }

	public void main(String s[]) {
	    
		Frame f = new Frame("Network Chess");
		
	    f.addWindowListener(new WindowAdapter() {

	      public void windowClosing(WindowEvent e) {
	        System.exit(0);
	      }
	    });
	    Applet applet = new BoardGraphics();
	    f.add("Center", applet);
	    if(s.length > 0){
		    importedIP = s[0];
	    }else{
		    importedIP = "localhost";
	    }
	    applet.init();
	    applet.setSize(new Dimension(1200, 800));
	    f.pack();
	    f.setSize(new Dimension(1200, 800));
	    f.setVisible(true);
	  }

	class SMCanvas extends Canvas implements MouseListener, MouseMotionListener {
	  //Variables used throughout the Canvas class
	  //game logic pieces
	  Game game1 = new Game();
	  Board gameBoard = new Board();
	  //Variable to determine team of picked piece
	  //useful for simulating opponent's turn
	  String whoseTeam = "";
	  //variable to track team of player
	  Team myTeam = null;
	  Team enemyTeam = null; //needed for promoting other team's pieces
	  
	  //Keep track of number of captured pieces for later use
	  //int capturedEnemies = 0;
	  int lastEnemyCaptured = 0;
	  int capturedWhites = 0;
	  int capturedBlacks = 0;
	  
	  //track if an undo was done
	  //static boolean undoMade = false;
	  
	  //filler to "erase" captured pieces when you undo
	  //Rectangle filler = null;
	  
	  // Holds the coordinates of the user's last mousePressed event.
	  int last_x, last_y;
	  
	  //initialize check
	  boolean firstTime = true;
	  
	  //have we selected a piece to be moved?
	  boolean pieceSelected = false;
	  
	  //has a move been made this turn?
	  boolean moveMade = false;
	  
	  //is it my turn still
	  boolean myTurn = false;
	  
	  //piece we select
	  int pieceHeld = 0;
	  int startSquare = 0;
	  int newSquare = 0;
	  String pieceType = "";
	  
	  //check/checkmate conditionals
	  boolean blackInCheck = false;
	  boolean blackInCheckmate = false;
	  boolean whiteInCheck = false;
	  boolean whiteInCheckmate = false;
	  
	  //board graphics
	  ArrayList<Rectangle> BoardGrid = new ArrayList<Rectangle>();
	  //piece graphics
	  ArrayList<Rectangle> whitePawns = new ArrayList<Rectangle>();
	  ArrayList<Rectangle> blackPawns = new ArrayList<Rectangle>();
	  ArrayList<Rectangle> whiteSpecials = new ArrayList<Rectangle>();
	  ArrayList<Rectangle> blackSpecials = new ArrayList<Rectangle>();
	  //keeps track of types of pieces added through promotions
	  ArrayList<Image> whitePromoResults = new ArrayList<Image>();
	  ArrayList<Image> blackPromoResults = new ArrayList<Image>();
	  
	  //shows possible moves
	  ArrayList<Rectangle> possibleMoves = new ArrayList<Rectangle>();
	  
	  //used to show pieces to choose for promotion
	  ArrayList<Rectangle> promoRects = new ArrayList<Rectangle>();

	  
	  
	  //piece logics
	  Vector<Piece> whitePieces = new Vector<Piece>();
	  Vector<Piece> blackPieces = new Vector<Piece>();

	  //piece images
	  Image wPawnImg = null;
	  Image wRookImg = null;
	  Image wKnightImg = null;
	  Image wBishopImg = null;
	  Image wKingImg = null;
	  Image wQueenImg = null;
	  Image bPawnImg = null;
	  Image bRookImg = null;
	  Image bKnightImg = null;
	  Image bBishopImg = null;
	  Image bKingImg = null;
	  Image bQueenImg = null;
	  
	  //Image undoButtonImg = null;
	  //images to convey game information
	  Image endTurnButtonImg = null;	//end turn button image
	  Image yourTurnImg = null;			//indicates it is your turn
	  Image oppTurnImg = null;			//indicates it is not your turn
	  
	  Image blackCheckImg = null;	  	//black team in check
	  Image blackCheckmateImg = null; 	//black team in checkmate
	  Image teamBlackImg = null;		//indicates you are black team
	  Image teamWhiteImg = null;		//indicates you are white team
	  Image whiteCheckImg = null;		//white team in check
	  Image whiteCheckmateImg = null;	//white team in checkmate
	  Image promoSelectImg = null;		//put over the promotion candidate pieces
	  
	  
	  //Rectangle undoButton = new Rectangle(450, 50, 60, 20);
	  //rectangles for information images
	  Rectangle endTurnButtonRect = new Rectangle(450, 80, 60, 20);
	  
	  Rectangle teamBlackRect = new Rectangle(450,110,160,30);
	  Rectangle teamWhiteRect = new Rectangle(450,110,160,30);
	  
	  Rectangle yourTurnRect = new Rectangle(450,140,100,30);
	  Rectangle oppTurnRect = new Rectangle(450,140,160,40);
	  
	  Rectangle blackCheckRect = new Rectangle(450, 180, 160, 30);
	  Rectangle blackCheckmateRect = new Rectangle(450,180,160,60);
	  Rectangle whiteCheckRect = new Rectangle(450,180,160,30);
	  Rectangle whiteCheckmateRect = new Rectangle(450,180,160,60);
	  
	  
	  Rectangle promoSelectRect = new Rectangle(600, 350, 200, 30);
	  //600 + 50*(i%2), 400 + 50*(i/2), 30, 30	promo rect params
	  //locations where you select and move piece
	  Space startSpace;
	  Space newSpace;
	  Piece capturedPiece =  null;
	 // Piece movingPiece;
	  
	  //if a pawn is going through promotion we'll show possible pieces
	  boolean promotion = false;
	  boolean promotionHappened = false;
	  String promoType = "";
	 
	  //for connecting to game over network
	  String ip = ""; //ip of host of game
	  boolean gameStarted = false; //has a game been found
	  String latestOpponentMove = ""; //message about opponent's latest move from network
	  
	  //keeps track of if a team has won
	  String winner = "";
	  
	  public SMCanvas() {
		  
	  }
	  
	  public SMCanvas(String ipAddress) {
		//start logic stuff
		try {
			game1.startGame();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gameBoard = game1.getBoard();
		
		//define images with sources
		wPawnImg = new ImageIcon("../images/whitePawn.png").getImage();
		wRookImg = new ImageIcon("../images/whiteRook.png").getImage();
		wKnightImg = new ImageIcon("../images/whiteKnight.png").getImage();
		wBishopImg = new ImageIcon("../images/whiteBishop.png").getImage();
		wKingImg = new ImageIcon("../images/whiteKing.png").getImage();
		wQueenImg = new ImageIcon("../images/whiteQueen.png").getImage();
		bPawnImg = new ImageIcon("../images/blackPawn.png").getImage();
		bRookImg = new ImageIcon("../images/blackRook.png").getImage();
		bKnightImg = new ImageIcon("../images/blackKnight.png").getImage();
		bBishopImg = new ImageIcon("../images/blackBishop.png").getImage();
		bKingImg = new ImageIcon("../images/blackKing.png").getImage();
		bQueenImg = new ImageIcon("../images/blackQueen.png").getImage();
		//undoButtonImg = new ImageIcon("../images/undoButton.png").getImage();
		endTurnButtonImg = new ImageIcon("../images/endTurnButton.png").getImage();
		yourTurnImg = new ImageIcon("../images/yourTurn.png").getImage(); 
		oppTurnImg = new ImageIcon("../images/oppTurn.png").getImage();
		blackCheckImg = new ImageIcon("../images/blackCheckImg.png").getImage();
		blackCheckmateImg = new ImageIcon("../images/blackCheckmateImg.png").getImage();
		teamBlackImg = new ImageIcon("../images/teamBlackImg.png").getImage();
		teamWhiteImg = new ImageIcon("../images/teamWhiteImg.png").getImage();
		whiteCheckImg = new ImageIcon("../images/whiteCheckImg.png").getImage();
		whiteCheckmateImg = new ImageIcon("../images/whiteCheckmateImg.png").getImage();
		promoSelectImg = new ImageIcon("../images/promoSelectImg.png").getImage();
		
		//set background
	    setBackground(Color.white);
	    //set listeners
	    addMouseMotionListener(this);
	    addMouseListener(this);
	    
	    //start game
	    ip = ipAddress;
		game1.connect(ip);
	    startGame();
	  }

	  // Handles the event of the user pressing down the mouse button.
	  public void mousePressed(MouseEvent e) {
		  
	  }

	  // Handles the event of a user dragging the mouse while holding down the
	  // mouse button.
	  public void mouseDragged(MouseEvent e) {
	    
	  }

	  // Handles the event of a user releasing the mouse button.
	  public void mouseReleased(MouseEvent e) {
		//  System.out.println(ip);
	    // Checks whether or not the cursor is inside of the rectangle when the
	    // user releases the mouse button.
		//possibleMoves.clear();
		//if(isTurn)
		
		//if(e!= null && undoButton.contains(e.getX(), e.getY())){
			//undo();}
		if(e!= null && endTurnButtonRect.contains(e.getX(), e.getY()) && myTurn && moveMade && !promotion){
			endTurn();
			repaint();
		}
		
		if(gameStarted && winner.equals("")){
			if(myTurn){
				if(pieceSelected){
					//System.out.println("MAking my own move");
					pieceSelected = false;
					newSquare = -1;
					for (int j=0; j<BoardGrid.size(); j++){
						if(BoardGrid.get(j).contains(e.getX(), e.getY()) && possibleMoves.contains(BoardGrid.get(j))){
							newSquare = j;
						}
					}
					if(newSquare > -1){
						//System.out.println("MAKING MOVE CONFROM");
						makeMove();
						moveMade = true;
						if(!promotion){
							System.out.println("Waiting for opponent1");
							endTurn();
						}
						
						//repaint();
					}else{
						//System.out.println("MAKING MOVE UNDONE");
						possibleMoves.clear();
						pieceSelected = false;
					}
					//System.out.println("MAking move");
					
					
					//myTurn = false;
				}else if (!moveMade){
					possibleMoves.clear();
					//System.out.println("Selecting my own piece");
					for (int j=0; j<BoardGrid.size(); j++){
						if(BoardGrid.get(j).contains(e.getX(), e.getY())){
							startSquare = j;
						}
					}
					if(myTeam.equals(Team.TEAM2)){	
						selectWhitePiece(startSquare);
					}else{
						selectBlackPiece(startSquare);
					}
					if(pieceSelected){
						
						for(int k=0; k<64; k++){
							newSpace = gameBoard.getSpace(k % 8, k / 8);
							//Piece movingPiece = startSpace.getPiece();
							try {
								if(game1.isValidMove(startSpace, newSpace)){
									possibleMoves.add(BoardGrid.get(k));
								}
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						/*if(possibleMoves.isEmpty()){
							possibleMoves.add(BoardGrid.get(startSquare));
						}*/
					}
				}else if (promotion){
					System.out.println("promoting");
					selectPromoPiece(e);
					if(promotionHappened){
						System.out.println("Waiting for opponent2");
						endTurn();	
					}
				}/*else{
					System.out.println("FAILING TO DO ANYTHING ON MY TURN");
				}*/
				//System.out.println("ONE TRUE REPAINT");
				repaint();
				/*if(moveMade && !myTurn){
					mouseReleased(null);
				}*/
			}else{
				System.out.println("Waiting for opponent3");
				//repaint();
				//you've made your move so wait for opponent's turn
				try {
					latestOpponentMove = game1.getConnection().listen();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//System.out.println("MAKING OPP's MOVE");
				replicateOpponentMove(latestOpponentMove);
				repaint();
				moveMade = false;
			}
		}
	}
	  
	  public void selectPromoPiece(MouseEvent e){
		  for(int i=0; i<promoRects.size(); i++){
				if(promoRects.get(i).contains(e.getX(), e.getY())){
					if(whoseTeam.equals("white")){
						  if(i==0){
							  whitePromoResults.add(wKnightImg);
							  Knight newKnight = new Knight(whiteSpecials.size() + 1, myTeam);
							  game1.promotePawn(newSpace, newKnight);
							  promoType = "Knight";
						  }else if(i==1){
							  whitePromoResults.add(wRookImg);
							  Rook newRook = new Rook(whiteSpecials.size() + 1, myTeam);
							  game1.promotePawn(newSpace, newRook);
							  promoType = "Rook";
						  }else if(i==2){
							  whitePromoResults.add(wBishopImg);
							  Bishop newBishop = new Bishop(whiteSpecials.size() + 1, myTeam);
							  game1.promotePawn(newSpace, newBishop);
							  promoType = "Bishop";
						  }else if(i==3){
							  Queen newQueen = new Queen(whiteSpecials.size() + 1, myTeam);
							  game1.promotePawn(newSpace, newQueen);
							  whitePromoResults.add(wQueenImg);
							  promoType = "Queen";
						  }
						  whiteSpecials.add(whitePawns.get(pieceHeld));
						  whitePawns.remove(pieceHeld);

						  promotion = false;
						  promotionHappened = true;
						  System.out.println("Promoted to a " + promoType);
					}else{
						if(i==0){
							  blackPromoResults.add(bKnightImg);
							  Knight newKnight = new Knight(blackSpecials.size() + 1, myTeam);
							  game1.promotePawn(newSpace, newKnight);
							  promoType = "Knight";
						}else if(i==1){
							  blackPromoResults.add(bRookImg);
							  Rook newRook = new Rook(blackSpecials.size() + 1, myTeam);
							  game1.promotePawn(newSpace, newRook);
							  promoType = "Rook";
						}else if(i==2){
							  blackPromoResults.add(bBishopImg);
							  Bishop newBishop = new Bishop(blackSpecials.size() + 1, myTeam);
							  game1.promotePawn(newSpace, newBishop);
							  promoType = "Bishop";
						}else if(i==3){
							  blackPromoResults.add(bQueenImg);
							  Queen newQueen = new Queen(blackSpecials.size() + 1, myTeam);
							  game1.promotePawn(newSpace, newQueen);
							  promoType = "Queen";
						}
						  blackSpecials.add(blackPawns.get(pieceHeld));
						  blackPawns.remove(pieceHeld);
	
						  promotion = false;
						  promotionHappened = true;
						  System.out.println("Promoted to a " + promoType);
					}
				}
		  }
	  }
	  
	  public void selectWhitePiece(int startSquare){
		  for(int i=0; i<whitePawns.size(); i++){
				if(BoardGrid.get(startSquare).contains(whitePawns.get(i))){
					pieceSelected = true;
					pieceHeld = i;
					startSpace = gameBoard.getSpace(startSquare % 8, startSquare / 8);

					whoseTeam = "white";
					pieceType = "pawn";
				}
		  }
		  for(int i=0; i<whiteSpecials.size(); i++){
				if(BoardGrid.get(startSquare).contains(whiteSpecials.get(i))){
					pieceSelected = true;
					pieceHeld = i;
					startSpace = gameBoard.getSpace(startSquare % 8, startSquare / 8);

					whoseTeam = "white";
					pieceType = "special";
				}
		  }
	  }
	  
	  public void selectBlackPiece(int startSquare){
		  for(int i=0; i<blackPawns.size(); i++){
			  if(BoardGrid.get(startSquare).contains(blackPawns.get(i))){
					pieceSelected = true;
					pieceHeld = i;
					startSpace = gameBoard.getSpace(startSquare % 8, startSquare / 8);
					whoseTeam = "black";
					pieceType = "pawn";
				}
		  }
		  for(int i=0; i<blackSpecials.size(); i++){
			  if(BoardGrid.get(startSquare).contains(blackSpecials.get(i))){
					pieceSelected = true;
					pieceHeld = i;
					startSpace = gameBoard.getSpace(startSquare % 8, startSquare / 8);
					whoseTeam = "black";
					pieceType = "special";
				}
		  }
	  }
	  
	  public void makeMove(){

			newSpace = gameBoard.getSpace(newSquare % 8, newSquare / 8);
			//Moving Pieces logic
			try {
				if(game1.isValidMove(startSpace, newSpace)){
					//castling
					if(startSpace.getPiece() instanceof King &&
							newSpace.hasPiece() && 
							newSpace.getPiece() instanceof Rook &&
							game1.getBoard().canCastle(startSpace.getPiece().getTeam(), (Rook)newSpace.getPiece())){
						int pieceMovingTo = 0;
						for(int i=0; i<whitePawns.size(); i++){
							if(BoardGrid.get(newSquare).contains(whitePawns.get(i))){
								pieceMovingTo = i;
							}
						}
						for(int i=0; i<whiteSpecials.size(); i++){
							if(BoardGrid.get(newSquare).contains(whiteSpecials.get(i))){
								pieceMovingTo = i;
							}
						}
						for(int i=0; i<blackPawns.size(); i++){
							if(BoardGrid.get(newSquare).contains(blackPawns.get(i))){
								pieceMovingTo = i;
							}
						}
						for(int i=0; i<blackSpecials.size(); i++){
							if(BoardGrid.get(newSquare).contains(blackSpecials.get(i))){
								pieceMovingTo = i;
							}
						}
						
						//System.out.println(startSquare + "qwe q" + newSquare);
						if(startSquare < newSquare){
							if(whoseTeam.equals("white")){
								whiteSpecials.get(pieceHeld).setLocation((int)BoardGrid.get(startSquare + 2).getX() + 5, (int)BoardGrid.get(startSquare + 2).getY() + 5);
								whiteSpecials.get(pieceMovingTo).setLocation((int)BoardGrid.get(startSquare + 1).getX() + 5, (int)BoardGrid.get(startSquare + 1).getY() + 5);	
							}else{
								blackSpecials.get(pieceHeld).setLocation((int)BoardGrid.get(startSquare + 2).getX() + 5, (int)BoardGrid.get(startSquare + 2).getY() + 5);
								blackSpecials.get(pieceMovingTo).setLocation((int)BoardGrid.get(startSquare + 1).getX() + 5, (int)BoardGrid.get(startSquare + 1).getY() + 5);								
							}
						}else{
							if(whoseTeam.equals("white")){
								whiteSpecials.get(pieceHeld).setLocation((int)BoardGrid.get(startSquare - 2).getX() + 5, (int)BoardGrid.get(startSquare - 2).getY() + 5);
								whiteSpecials.get(pieceMovingTo).setLocation((int)BoardGrid.get(startSquare - 1).getX() + 5, (int)BoardGrid.get(startSquare - 1).getY() + 5);	
							}else{
								blackSpecials.get(pieceHeld).setLocation((int)BoardGrid.get(startSquare - 2).getX() + 5, (int)BoardGrid.get(startSquare - 2).getY() + 5);	
								blackSpecials.get(pieceMovingTo).setLocation((int)BoardGrid.get(startSquare - 1).getX() + 5, (int)BoardGrid.get(startSquare - 1).getY() + 5);							
							}
						}
						//castling
						game1.makeMove(startSpace, newSpace);
						
					//en passant
					}else if(game1.getBoard().canEnpassant(startSpace, newSpace) && !newSpace.hasPiece()){
						//System.out.println("PASSANT");
						
						int passantSquare = 0;
						if(whoseTeam.equals("white")){
							passantSquare = newSquare + 8;
						}else{
							passantSquare = newSquare - 8;
						}
						Space passantSpace = gameBoard.getSpace(passantSquare % 8, passantSquare / 8);
						//System.out.println(passantSquare);
						//System.out.println(newSquare);
						
						capturedPiece = passantSpace.getPiece();
						for (int j=0; j<blackPawns.size(); j++){
							if(BoardGrid.get(passantSquare).contains(blackPawns.get(j))){
								MoveRectToJail(blackPawns.get(j));
								capturedBlacks++;
								lastEnemyCaptured = j;
							}
						}
						for (int j=0; j<blackSpecials.size(); j++){
							if(BoardGrid.get(passantSquare).contains(blackSpecials.get(j))){
								MoveRectToJail(blackSpecials.get(j));
								capturedBlacks++;
								lastEnemyCaptured = j;
							}
						}
						for (int j=0; j<whitePawns.size(); j++){
							if(BoardGrid.get(passantSquare).contains(whitePawns.get(j))){
								MoveRectToJail(whitePawns.get(j));
								capturedWhites++;
								lastEnemyCaptured = j;
							}
						}
						for (int j=0; j<whiteSpecials.size(); j++){
							if(BoardGrid.get(passantSquare).contains(whiteSpecials.get(j))){
								MoveRectToJail(whiteSpecials.get(j));
								capturedWhites++;
								lastEnemyCaptured = j;
							}
						}
						game1.makeMove(startSpace, newSpace);
						
						if(whoseTeam.equals("white")){
							moveRectToSquare(whitePawns.get(pieceHeld), newSquare);
							//whiteRectangles.get(pieceHeld).setLocation((int)BoardGrid.get(newSquare).getX() + 5, (int)BoardGrid.get(newSquare).getY() + 5);	
						}else{
							moveRectToSquare(blackPawns.get(pieceHeld), newSquare);
							//blackRectangles.get(pieceHeld).setLocation((int)BoardGrid.get(newSquare).getX() + 5, (int)BoardGrid.get(newSquare).getY() + 5);							
						}
						
					//logic for normal capturing pieces. Made to else if to avoid other special moves
					}else if(newSpace.hasPiece()){
						capturedPiece = newSpace.getPiece();
						for (int j=0; j<blackPawns.size(); j++){
							if(BoardGrid.get(newSquare).contains(blackPawns.get(j))){
								MoveRectToJail(blackPawns.get(j));
								capturedBlacks++;
								lastEnemyCaptured = j;
							}
						}
						for (int j=0; j<blackSpecials.size(); j++){
							if(BoardGrid.get(newSquare).contains(blackSpecials.get(j))){
								MoveRectToJail(blackSpecials.get(j));
								capturedBlacks++;
								lastEnemyCaptured = j;
							}
						}
						for (int j=0; j<whitePawns.size(); j++){
							if(BoardGrid.get(newSquare).contains(whitePawns.get(j))){
								MoveRectToJail(whitePawns.get(j));
								capturedWhites++;
								lastEnemyCaptured = j;
							}
						}
						for (int j=0; j<whiteSpecials.size(); j++){
							if(BoardGrid.get(newSquare).contains(whiteSpecials.get(j))){
								MoveRectToJail(whiteSpecials.get(j));
								capturedWhites++;
								lastEnemyCaptured = j;
							}
						}
						game1.makeMove(startSpace, newSpace);
					
						if(whoseTeam.equals("white") && pieceType.equals("pawn")){
							moveRectToSquare(whitePawns.get(pieceHeld), newSquare);
							//whiteRectangles.get(pieceHeld).setLocation((int)BoardGrid.get(newSquare).getX() + 5, (int)BoardGrid.get(newSquare).getY() + 5);	
						}else if(whoseTeam.equals("white") && pieceType.equals("special")){
							moveRectToSquare(whiteSpecials.get(pieceHeld), newSquare);
						}else if(whoseTeam.equals("black") && pieceType.equals("pawn")){
							moveRectToSquare(blackPawns.get(pieceHeld), newSquare);
						}else{
							moveRectToSquare(blackSpecials.get(pieceHeld), newSquare);
							//blackRectangles.get(pieceHeld).setLocation((int)BoardGrid.get(newSquare).getX() + 5, (int)BoardGrid.get(newSquare).getY() + 5);							
						}
					}else{
						//vanilla move
						game1.makeMove(startSpace, newSpace);
						
						if(whoseTeam.equals("white") && pieceType.equals("pawn")){
							moveRectToSquare(whitePawns.get(pieceHeld), newSquare);
							//whiteRectangles.get(pieceHeld).setLocation((int)BoardGrid.get(newSquare).getX() + 5, (int)BoardGrid.get(newSquare).getY() + 5);	
						}else if(whoseTeam.equals("white") && pieceType.equals("special")){
							moveRectToSquare(whiteSpecials.get(pieceHeld), newSquare);
						}else if(whoseTeam.equals("black") && pieceType.equals("pawn")){
							moveRectToSquare(blackPawns.get(pieceHeld), newSquare);
						}else{
							moveRectToSquare(blackSpecials.get(pieceHeld), newSquare);
							//blackRectangles.get(pieceHeld).setLocation((int)BoardGrid.get(newSquare).getX() + 5, (int)BoardGrid.get(newSquare).getY() + 5);							
						}
					}
					//Now check for black promotion 
					if(newSpace.getPiece() instanceof Pawn && whoseTeam=="black" && newSpace.getyCoordinate() == 7){
						promotion = true;
						repaint();
						//do black promotion
					//else if white promotion
					}else if(newSpace.getPiece() instanceof Pawn && whoseTeam.equals("white") && newSpace.getyCoordinate() == 0){
						//do white promotion
						promotion = true;
						repaint();
					}
					//newSpace.placePiece(movingPiece);
					//startSpace.removePiece();
					//System.out.println("CHECKING CHECK");
					
					
					if(game1.checkMate(Game.Team.TEAM1)){
						//System.out.println("Team 1 has been put in CheckMate");
						blackInCheckmate = true;
						winner = "white";
					}else if(game1.checkMate(Game.Team.TEAM2)){
						//System.out.println("Team 2 has been put in CheckMate");
						whiteInCheckmate = true;
						winner = "black";
					}else if(game1.getBoard().teamInCheck(Game.Team.TEAM2)){
						//System.out.println("Team 2 is in check");
						whiteInCheck = true;
					}else if(game1.getBoard().teamInCheck(Game.Team.TEAM1)){
						//System.out.println("Team 1 is in check");	
						blackInCheck = true;
					}else{
						blackInCheckmate = false;
						whiteInCheckmate = false;
						blackInCheck = false;
						whiteInCheck = false;
					}
					
				}
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//whoseTeam = "";
			possibleMoves.clear();
			repaint();
			
	  }
	  
	  public void replicateOpponentMove(String move){
		  String[] coords = move.split(",");
		  
		  int startX = Integer.parseInt(coords[1]);
		  int startY = Integer.parseInt(coords[2]);
		  //startSquare % 8, startSquare / 8
		  startSquare = (startX + startY * 8);
		  //System.out.println("Start x interpret as: " + startX);
		  if(myTeam.equals(Team.TEAM1)){
			  selectWhitePiece(startSquare);
		  }else{
			  selectBlackPiece(startSquare);
		  }
		  //longer message => promotion happened
		  
		  //System.out.println("start Square after select " + startSquare);
		  
		  int endX = Integer.parseInt(coords[3]);
		  int endY = Integer.parseInt(coords[4]);
		  newSquare = (endX + endY * 8);
		  /*for (int j=0; j<BoardGrid.size(); j++){
			  if(BoardGrid.get(j).contains(endX, endY)){
				  newSquare = j;
			  }
		  }*/
		  //System.out.println(startSquare + "  " + newSquare);
		  makeMove();
		  
		  if(coords.length > 6){
			  for(int i=0; i<coords.length; i++){
				  System.out.println(coords[i]);  
			  }
			  //System.out.println(coords);
			  
			  //get what piece was promoted to
			  String pType = coords[6];
			  System.out.println(pType);
			  if(whoseTeam.equals("white")){
				  if(pType.equals("Knight")){
					  whitePromoResults.add(wKnightImg);
					  Knight newKnight = new Knight(whiteSpecials.size() + 1, enemyTeam);
					  game1.promotePawn(newSpace, newKnight);
				  }else if(pType.equals("Rook")){
					  whitePromoResults.add(wRookImg);
					  Rook newRook = new Rook(whiteSpecials.size() + 1, enemyTeam);
					  game1.promotePawn(newSpace, newRook);
				  }else if(pType.equals("Bishop")){
					  whitePromoResults.add(wBishopImg);
					  Bishop newBishop = new Bishop(whiteSpecials.size() + 1, enemyTeam);
					  game1.promotePawn(newSpace, newBishop);
				  }else{
					  Queen newQueen = new Queen(whiteSpecials.size() + 1, enemyTeam);
					  game1.promotePawn(newSpace, newQueen);
					  whitePromoResults.add(wQueenImg);
				  }
				  whiteSpecials.add(whitePawns.get(pieceHeld));
				  whitePawns.remove(pieceHeld);
			  }else{
				  if(pType.equals("Knight")){
					  blackPromoResults.add(bKnightImg);
					  Knight newKnight = new Knight(blackSpecials.size() + 1, enemyTeam);
					  game1.promotePawn(newSpace, newKnight);
				  }else if(pType.equals("Rook")){
					  blackPromoResults.add(bRookImg);
					  Rook newRook = new Rook(blackSpecials.size() + 1, enemyTeam);
					  game1.promotePawn(newSpace, newRook);
				  }else if(pType.equals("Bishop")){
					  blackPromoResults.add(bBishopImg);
					  Bishop newBishop = new Bishop(blackSpecials.size() + 1, enemyTeam);
					  game1.promotePawn(newSpace, newBishop);
				  }else{
					  blackPromoResults.add(bQueenImg);
					  Queen newQueen = new Queen(blackSpecials.size() + 1, enemyTeam);
					  game1.promotePawn(newSpace, newQueen);
				  }
				  blackSpecials.add(blackPawns.get(pieceHeld));
				  blackPawns.remove(pieceHeld);
			  }
			  //pieceType = "special";
		  }
		  
		  myTurn = true;
		  pieceSelected = false;
		  promotion = false;
		  repaint();
	  }
	  
	  // This method required by MouseListener.

	  public void mouseMoved(MouseEvent e) {
	  }

	  // These methods are required by MouseMotionListener.
	  public void mouseClicked(MouseEvent e) {
		  
	  }

	  public void mouseExited(MouseEvent e) {
	  }

	  public void mouseEntered(MouseEvent e) {
		  //repaint();
	  }

	  // Updates the coordinates representing the location of the current rectangle.
	  public void updateLocation(MouseEvent e) {
	    /*
	     * Updates the label to reflect the location of the
	     * current rectangle
	     * if checkRect returns true; otherwise, returns error message.
	     */
	    /*if (checkRect()) {
	    	BoardGraphics.label.setText("Rectangle located at "
	              + rect.getX() + ", "
	              + rect.getY());
	    } else {
	    	BoardGraphics.label.setText("Please don't try to "
	              + " drag outside the area.");
	    }*/

	    //repaint();
		
	  }

	  public void paint(Graphics g) {
	    update(g);
	  }
	  
	  public void moveRectToSquare(Rectangle r, int square){
		  r.setLocation((int)BoardGrid.get(newSquare).getX() + 5, (int)BoardGrid.get(newSquare).getY() + 5);	
	  }
	  
	  public void MoveRectToJail(Rectangle r){
		  if(whoseTeam.equals("white")){
			  r.setLocation(630 + (capturedBlacks/2) * 50, 50 * (capturedBlacks%2));
		  }else{
			  r.setLocation(630 + (capturedWhites/2) * 50, 50 * (capturedWhites%2) + 110);
		  }
	  }
	  
	  public void undo(){
		  /*System.out.println("undoing start");
		  System.out.println(whoseTeam);
		  if(moveMade){
			  //System.out.println("made it");
			  Piece movedPiece = newSpace.getPiece();
			  movedPiece.undoMove();
			  startSpace.placePiece(movedPiece);
			  if(capturedPiece != null){
				  System.out.println("free meh");
				  newSpace.placePiece(capturedPiece);
				  //capturedPiece.undoMove();
				  if(whoseTeam == "black"){
					  moveRectToSquare(whiteRectangles.get(lastEnemyCaptured), newSquare);
					  //whiteRectangles.get(lastEnemyCaptured).setLocation((int)BoardGrid.get(newSquare).getX() + 5, (int)BoardGrid.get(newSquare).getY() + 5);	
				  }else{
					  moveRectToSquare(blackRectangles.get(lastEnemyCaptured), newSquare);
					  //blackRectangles.get(lastEnemyCaptured).setLocation((int)BoardGrid.get(newSquare).getX() + 5, (int)BoardGrid.get(newSquare).getY() + 5);							
				  }
				  capturedEnemies--;
				  //ready a rect to wipe the captured pieces area
				  filler = new Rectangle(600 + (capturedEnemies/2) * 50, 50 * (capturedEnemies%2), 30, 30);
			  }
			  if(whoseTeam == "white"){
				  moveRectToSquare(whiteRectangles.get(pieceHeld), newSquare);
				  //whiteRectangles.get(pieceHeld).setLocation((int)BoardGrid.get(startSquare).getX() + 5, (int)BoardGrid.get(startSquare).getY() + 5);	
			  }else{
				  moveRectToSquare(blackRectangles.get(pieceHeld), newSquare);
				  //blackRectangles.get(pieceHeld).setLocation((int)BoardGrid.get(startSquare).getX() + 5, (int)BoardGrid.get(startSquare).getY() + 5);							
			  }
			  moveMade = false;
			  //myTurn = true;
			  //update(g2);
		  }else{
			  //nothing to do
			  System.out.println("Nothing doing undo");
		  }*/
	  }
	  
	  public void endTurn(){
		  
		  int x1 = startSquare % 8;//startSpace.getxCoordinate();
		  int y1 = startSquare / 8; //startSpace.getyCoordinate();
		  int x2 = newSquare % 8;//newSpace.getxCoordinate();
		  int y2 = newSquare / 8;//newSpace.getyCoordinate();
		  if(promotionHappened){
			  game1.getConnection().sendMove(x1 + "," + y1 + "," + x2 + "," + y2 + ",Promote," + promoType + "," + blackSpecials.size());  
		  }else{
			  game1.getConnection().sendMove(x1 + "," + y1 + "," + x2 + "," + y2);
		  }
		  myTurn = false;
		  promotionHappened = false;
		  //System.out.println("EndTurn");
		  //update(g2);
		  repaint();
		  //System.out.println("SIGN UPDATED");
		  //moveMade = false;//Change this for networking
	  }
	  
	  public void startGame(){
		  System.out.println("Looking for game");
		  Team a = null;
		  try {
			a = game1.getConnection().run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("game start fail");
			e.printStackTrace();
		}
		  game1.setYourTeam(a);
		  //moveMade = false;//Change this for networking
		  Team team = null;
		  //System.out.println("Waiting for game");
		  while(true){
			  if(game1.getYourTeam() != null){
				  team = game1.getYourTeam();
				  break;
			  }
		  }
		  
		  System.out.println("found game" + " " + team);
		  myTeam = team;
		  gameStarted = true;
		  if(team.equals(Team.TEAM2)){
			  myTurn = true;
			  enemyTeam = Team.TEAM1;
		  }else{
			  enemyTeam = Team.TEAM2;
		  }
	  }
	  
	  

	  public void update(Graphics g) {
	    Graphics2D g2 = (Graphics2D) g;
	    g2.setStroke(new BasicStroke(8.0f));
	    for(int i=0; i<64; i+=2){
	    	  Rectangle whiteSpace = new Rectangle();
	    	  Rectangle blackSpace = new Rectangle();
	    	  	if((i/8) %2 == 0){
					whiteSpace.setBounds((i*50) % 400 + 10,i/8 * 50 + 10,40,40);
					blackSpace.setBounds(((i+1)*50) % 400 + 10,(i+1)/8 * 50 + 10,40,40);
	    	  	}else{
	    	  		whiteSpace.setBounds(((i+1)*50) % 400 + 10,i/8 * 50 + 10,40,40);
	    	  		blackSpace.setBounds((i*50) % 400 + 10,(i+1)/8 * 50 + 10,40,40);
	    	  	}
				g2.setPaint(Color.black);
				g2.draw(blackSpace);
				g2.fill(blackSpace);
				g2.setPaint(Color.white);
				g2.draw(whiteSpace);
				g2.fill(whiteSpace);
	      }
	    if (firstTime) {
			for(int i=0; i<64; i+=2){
		    	  Rectangle whiteSpace = new Rectangle();
		    	  Rectangle blackSpace = new Rectangle();
		    	  	if((i/8) %2 == 0){
						whiteSpace.setBounds((i*50) % 400 + 10,i/8 * 50 + 10,40,40);
						blackSpace.setBounds(((i+1)*50) % 400 + 10,(i+1)/8 * 50 + 10,40,40);
						BoardGrid.add(whiteSpace);
						BoardGrid.add(blackSpace);
		    	  	}else{
		    	  		whiteSpace.setBounds(((i+1)*50) % 400 + 10,i/8 * 50 + 10,40,40);
		    	  		blackSpace.setBounds((i*50) % 400 + 10,(i+1)/8 * 50 + 10,40,40);
						BoardGrid.add(blackSpace);
						BoardGrid.add(whiteSpace);
		    	  	}
					g2.setPaint(Color.black);
					g2.draw(blackSpace);
					g2.fill(blackSpace);
					g2.setPaint(Color.white);
					g2.draw(whiteSpace);
		      }
		      for( int i=0; i<8; i++){
		    	  Rectangle piece = new Rectangle();
		    	  piece.setBounds(i*50 % 400 + 15, i/8 * 50 + 15, 30, 30);
		    	  blackSpecials.add(piece);
		      }
		      for( int i=8; i<16; i++){
		    	  Rectangle piece = new Rectangle();
		    	  piece.setBounds(i*50 % 400 + 15, i/8 * 50 + 15, 30, 30);
		    	  blackPawns.add(piece);
		      }
		      
		      for( int i=48; i<56; i++){
		    	  Rectangle piece = new Rectangle();
		    	  piece.setBounds(i*50 % 400 + 15, i/8 * 50 + 15, 30, 30);
		    	  whitePawns.add(piece);
		      }
		      for( int i=56; i<64; i++){
		    	  Rectangle piece = new Rectangle();
		    	  piece.setBounds(i*50 % 400 + 15, i/8 * 50 + 15, 30, 30);
		    	  whiteSpecials.add(piece);
		      }
		      
		      for(int i=0; i<7; i++){
		    	  Rectangle promoPiece = new Rectangle(600 + 50*(i%2), 400 + 50*(i/2), 30, 30);
		    	  promoRects.add(promoPiece);
		      }
		      /*for(int i=0; i<7; i++){
		    	  //Rectangle promoPiece = new Rectangle(600 + 50*(i%2), 400 + 50*(i/2), 30, 30);
		    	  blackPromos.add(promoPiece);
		      }*/
		      
		      //Rectangle board = new Rectangle(5,5,400,400);
		      //g2.setPaint(Color.black);
		      //g2.draw(board);
		      firstTime = false;
	    }else{
		    	
			    //row even
			    if((startSquare/8) %2 == 0){
			    	//column even
			    	if(startSquare%8 % 2 == 0){
			    		//System.out.println("Even row, even column, black");
			    		g2.setPaint(Color.white);
			    	}else{//column odd
			    		//System.out.println("Even row, odd column, white");
			    		g2.setPaint(Color.black);
			    	}
			    }else{ // row odd
			    	//column even
			    	if(startSquare%8 % 2 == 0){
			    		//System.out.println("Odd row, even column, white");
			    		g2.setPaint(Color.black);
			    	}else{//column odd
			    		//System.out.println("Odd row, odd column, black");
			    		g2.setPaint(Color.white);
			    	}
			    }
			    g2.draw(BoardGrid.get(startSquare));
			    g2.fill(BoardGrid.get(startSquare));
			    
			    
			    //System.out.println(whiteRectangles.get(pieceHeld).getBounds());
			    //g2.setPaint(Color.blue);
			    //g2.draw(whiteRectangles.get(pieceHeld));
			    //g2.fill(whiteRectangles.get(pieceHeld));
	    }
	    g2.setPaint(Color.yellow);
	    for(int i=0; i<possibleMoves.size(); i++){
	    	g2.draw(possibleMoves.get(i));
		    g2.fill(possibleMoves.get(i));
	    }
	    
	    Rectangle r = null;
	    for(int i=0;i<whitePawns.size();i++){
		    r = whitePawns.get(i);
		    g2.drawImage(wPawnImg, r.x, r.y, r.width, r.height, null);	    	
	    }
	    for(int i=0;i<blackPawns.size();i++){
		    r = blackPawns.get(i);
		    g2.drawImage(bPawnImg, r.x, r.y, r.width, r.height, null);	    	
	    }
	    r = whiteSpecials.get(1);
	    g2.drawImage(wKnightImg, r.x, r.y, r.width, r.height, null);
	    r = whiteSpecials.get(6);
	    g2.drawImage(wKnightImg, r.x, r.y, r.width, r.height, null);

	    r = blackSpecials.get(1);
	    g2.drawImage(bKnightImg, r.x, r.y, r.width, r.height, null);
	    r = blackSpecials.get(6);
	    g2.drawImage(bKnightImg, r.x, r.y, r.width, r.height, null);
	    
	    r = whiteSpecials.get(0);
	    g2.drawImage(wRookImg, r.x, r.y, r.width, r.height, null);
	    r = whiteSpecials.get(7);
	    g2.drawImage(wRookImg, r.x, r.y, r.width, r.height, null);
	    
	    r = blackSpecials.get(0);
	    g2.drawImage(bRookImg, r.x, r.y, r.width, r.height, null);
	    r = blackSpecials.get(7);
	    g2.drawImage(bRookImg, r.x, r.y, r.width, r.height, null);

	    r = whiteSpecials.get(2);
	    g2.drawImage(wBishopImg, r.x, r.y, r.width, r.height, null);
	    r = whiteSpecials.get(5);
	    g2.drawImage(wBishopImg, r.x, r.y, r.width, r.height, null);
	    
	    r = blackSpecials.get(2);
	    g2.drawImage(bBishopImg, r.x, r.y, r.width, r.height, null);
	    r = blackSpecials.get(5);
	    g2.drawImage(bBishopImg, r.x, r.y, r.width, r.height, null);
	    
	    r = whiteSpecials.get(3);
	    g2.drawImage(wQueenImg, r.x, r.y, r.width, r.height, null);

	    r = blackSpecials.get(3);
	    g2.drawImage(bQueenImg, r.x, r.y, r.width, r.height, null);
	    
	    r = whiteSpecials.get(4);
	    g2.drawImage(wKingImg, r.x, r.y, r.width, r.height, null);
	    
	    r = blackSpecials.get(4);
	    g2.drawImage(bKingImg, r.x, r.y, r.width, r.height, null);
	    
	    //System.out.println("PROMO RESULTS:");
	    
	    for(int i=0; i<whitePromoResults.size(); i++){
	    	r = whiteSpecials.get(i+8);
		    g2.drawImage(whitePromoResults.get(i), r.x, r.y, r.width, r.height, null);
		    //System.out.println(whitePromoResults.get(i).toString());
	    }
	    for(int i=0; i<blackPromoResults.size(); i++){
	    	r = blackSpecials.get(i+8);
		    g2.drawImage(blackPromoResults.get(i), r.x, r.y, r.width, r.height, null);
		    //System.out.println(blackPromoResults.get(i).toString());
	    }
	    
	    Rectangle board = new Rectangle(5,5,400,400);
	    g2.setPaint(Color.black);
	    g2.draw(board);
	    
	    //g2.drawImage(undoButtonImg, undoButton.x, undoButton.y, undoButton.width, undoButton.height, null);
	    r = endTurnButtonRect;
	    g2.drawImage(endTurnButtonImg, r.x, r.y, r.width, r.height, null);
	    
	    if(myTurn){
	    	//erase
	    	g2.setPaint(Color.white);
	    	r = oppTurnRect;
	    	g2.draw(r);
	    	g2.fill(r);
	    	//fill
	    	r = yourTurnRect;
	    	g2.drawImage(yourTurnImg, r.x, r.y, r.width, r.height, null);
	    	//System.out.println("MY TURN sign");
	    }else{
	    	//erase
	    	g2.setPaint(Color.white);
	    	r = yourTurnRect;
	    	g2.draw(r);
	    	g2.fill(r);
	    	//fill
	    	r = oppTurnRect;
	    	//System.out.println("OPP TURN sign");
	    	g2.drawImage(oppTurnImg, r.x, r.y, r.width, r.height, null);
	    }
	    
	    if(myTeam.equals(Team.TEAM1)){
	    	//erase
	    	g2.setPaint(Color.white);
	    	r = teamWhiteRect;
	    	g2.draw(r);
	    	g2.fill(r);
	    	//fill
	    	r = teamBlackRect;
	    	g2.drawImage(teamBlackImg, r.x, r.y, r.width, r.height, null);
	    }else{
	    	//erase
	    	g2.setPaint(Color.white);
	    	r = teamBlackRect;
	    	g2.draw(r);
	    	g2.fill(r);
	    	//fill
	    	r = teamWhiteRect;
	    	g2.drawImage(teamWhiteImg, r.x, r.y, r.width, r.height, null);
	    }
	    /*if(filler != null){
		    g2.setPaint(Color.white);
		    g2.draw(filler);
		    g2.fill(filler);
		    filler = null;
	    }*/
	    
	    //show possible promotion pieces
	    //black
	    if(promotion && whoseTeam.equals("black")){
	    	r = promoSelectRect;
		    g2.drawImage(promoSelectImg, r.x, r.y, r.width, r.height, null);
		    r = promoRects.get(0);
		    g2.drawImage(bKnightImg, r.x, r.y, r.width, r.height, null);
		    r = promoRects.get(1);
		    g2.drawImage(bRookImg, r.x, r.y, r.width, r.height, null);
		    r = promoRects.get(2);
		    g2.drawImage(bBishopImg, r.x, r.y, r.width, r.height, null);
		    r = promoRects.get(3);
		    g2.drawImage(bQueenImg, r.x, r.y, r.width, r.height, null);
		//white
	    }else if(promotion && whoseTeam.equals("white")){
	    	r = promoSelectRect;
		    g2.drawImage(promoSelectImg, r.x, r.y, r.width, r.height, null);
	    	r = promoRects.get(0);
		    g2.drawImage(wKnightImg, r.x, r.y, r.width, r.height, null);
		    r = promoRects.get(1);
		    g2.drawImage(wRookImg, r.x, r.y, r.width, r.height, null);
		    r = promoRects.get(2);
		    g2.drawImage(wBishopImg, r.x, r.y, r.width, r.height, null);
		    r = promoRects.get(3);
		    g2.drawImage(wQueenImg, r.x, r.y, r.width, r.height, null);
	    }else{
	    	g2.setPaint(Color.white);
	    	r = promoSelectRect;
	    	g2.draw(r);
	    	g2.fill(r);
	    	r = promoRects.get(0);
	    	g2.draw(r);
		    g2.fill(r);
		    r = promoRects.get(1);
		    g2.draw(r);
		    g2.fill(r);
		    r = promoRects.get(2);
		    g2.draw(r);
		    g2.fill(r);
		    r = promoRects.get(3);
		    g2.draw(r);
		    g2.fill(r);
	    }
	    
	    //show if a team is in check
	    if(blackInCheck){
	    	r = blackCheckRect;
		    g2.drawImage(blackCheckImg, r.x, r.y, r.width, r.height, null);
	    }else if(blackInCheckmate){
	    	r = blackCheckmateRect;
		    g2.drawImage(blackCheckmateImg, r.x, r.y, r.width, r.height, null);
	    }else if(whiteInCheck){
	    	r = whiteCheckRect;
		    g2.drawImage(whiteCheckImg, r.x, r.y, r.width, r.height, null);
	    }else if(whiteInCheckmate){
	    	r = whiteCheckmateRect;
		    g2.drawImage(whiteCheckmateImg, r.x, r.y, r.width, r.height, null);
	    }else{
	    	//erase
	    	g2.setPaint(Color.white);
	    	r = whiteCheckmateRect;
	    	g2.draw(r);
	    	g2.fill(r);
	    }
	    
	    /*Rectangle piece = new Rectangle(0,0,30,30);
  	  	piece.setLocation(600, 100);
  	  	g2.setPaint(Color.orange);
  	  	g2.draw(piece);
  	  	g2.fill(piece);*/
	  }
	}
}
