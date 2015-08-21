package main;

import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import Pieces.King;
import Pieces.Piece;
import Pieces.Rook;
import main.Game;
import main.Game.Team;

public class BoardGraphics extends Applet implements ActionListener {

	  Label noteLabel;
	  Button undo;
	  Button end;
	  Button startGame;
	  
	  public void init() {
		undo = new Button("Undo");
		//a.setSize(1, 1);
		end = new Button ("End Turn");
		startGame = new Button ("start game");
		noteLabel = new Label("Game Started");
		undo.setBounds(450, 50, 60, 20);
		end.setBounds(450, 80, 60, 20);
		startGame.setBounds(450, 120, 60, 20);
		noteLabel.setBounds(10, 450, 100, 20);
		undo.addActionListener(this);
		end.addActionListener(this);
		startGame.addActionListener(this);
		//a.setPreferredSize(new Dimension(1,1));
		//SMCanvas mainCanvas = new SMCanvas();
		//undo = new Button(mainCanvas, SWT.Push);
	    this.add(undo);
	    this.add(end);
	    this.add(startGame);
	    this.add(noteLabel);
	    setLayout(new BorderLayout());
	    add(new SMCanvas());
	    //label = new Label("Drag rectangle around within the area");
	    //add("South", label);
	  }
	  
	  public void actionPerformed(ActionEvent e) {
		  if (e.getSource() == undo) {
			  //System.out.println("Button undo was pressed");
			  SMCanvas.undo();
		  }else if (e.getSource() == end){
			  //System.out.println("Button end turn was pressed");
			  SMCanvas.endTurn();
		  }else if (e.getSource() == startGame){
			  SMCanvas.startGame();
		  }else{
			//do nothing	
		  }
	  }

	 

	public static void main(String s[]) {
	    
		Frame f = new Frame("Network Chess");
		
	    f.addWindowListener(new WindowAdapter() {

	      public void windowClosing(WindowEvent e) {
	        System.exit(0);
	      }
	    });
	    Applet applet = new BoardGraphics();
	    f.add("Center", applet);
	    applet.init();
	    applet.setSize(new Dimension(1800, 1400));
	    f.pack();
	    f.setSize(new Dimension(1800, 1400));
	    f.setVisible(true);
	  }

	public void main(Team team) {
		Frame f = new Frame("Network Chess");
		
	    f.addWindowListener(new WindowAdapter() {

	      public void windowClosing(WindowEvent e) {
	        System.exit(0);
	      }
	    });
	    Applet applet = new BoardGraphics();
	    f.add("Center", applet);
	    applet.init();
	    SMCanvas.myTeam = team;
	    applet.setSize(new Dimension(1800, 1400));
	    f.pack();
	    f.setSize(new Dimension(1800, 1400));
	    f.setVisible(true);
	}

	static class SMCanvas extends Canvas implements MouseListener, MouseMotionListener {
	  //game logic pieces
	  static Game game1 = new Game();

	  Board gameBoard = new Board();
	  //Variable to determine team of picked piece
	  static String whoseTeam = "";

	  //variable to track team of player
	  static Team myTeam = null;
	  
	  //Keep track of number of captured pieces for later use
	  static int capturedEnemies = 0;
	  static int lastEnemyCaptured = 0;
	  
	  //track if an undo was done
	  //static boolean undoMade = false;
	  
	  
	  //filler to "erase" captured pieces when you undo
	  static Rectangle filler = null;
	  
	  // Holds the coordinates of the user's last mousePressed event.
	  int last_x, last_y;
	  //initialize check
	  boolean firstTime = true;
	  //are we moving a piece
	  boolean pieceSelected = false;
	  //has a move been made this turn?
	  static boolean moveMade = false;
	  static //is it my turn still
	  boolean myTurn = false;
	  //piece we select
	  static int pieceHeld = 0;
	  static int startSquare = 0;
	  static int newSquare = 0;
	  //board graphics
	  static ArrayList<Rectangle> BoardGrid = new ArrayList<Rectangle>();
	  //piece graphics
	  static ArrayList<Rectangle> whiteRectangles = new ArrayList<Rectangle>();
	  static ArrayList<Rectangle> blackRectangles = new ArrayList<Rectangle>();
	  ArrayList<Rectangle> possibleMoves = new ArrayList<Rectangle>();
	  //piece logics
	  Vector<Piece> whitePieces = new Vector<Piece>();
	  Vector<Piece> blackPieces = new Vector<Piece>();
	  //buttons
	  Button endTurn = new Button();
	  Button undo = new Button();
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
	  //locations where you select and move piece
	  static Space startSpace;
	  static Space newSpace;
	  static Piece capturedPiece =  null;
	 // Piece movingPiece;

	  static boolean gameStarted = false;
	  String latestOpponentMove = "";
	  
	  public SMCanvas() {
		//start logic stuff
		try {
			game1.startGame();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gameBoard = game1.getBoard();
		
		//define piece images
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
		
		//background
	    setBackground(Color.white);
	    //listeners
	    addMouseMotionListener(this);
	    addMouseListener(this);
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

	    // Checks whether or not the cursor is inside of the rectangle when the
	    // user releases the mouse button.

		possibleMoves.clear();
		//if(isTurn)
		if(gameStarted){
			if(myTurn){
				if(pieceSelected){
					//whiteRectangles.get(pieceHeld).setLocation(e.getX(), e.getY());
					//repaint place piece was
					//paint new place
					//movingPiece = startSpace.getPiece();
					
					pieceSelected = false;
					for (int j=0; j<BoardGrid.size(); j++){
						if(BoardGrid.get(j).contains(e.getX(), e.getY())){
							newSquare = j;		
							//System.out.println(j);
						}
					}
					
					newSpace = gameBoard.getSpace(newSquare % 8, newSquare / 8);
					//Moving Pieces logic
					try {
						if(game1.isValidMove(startSpace, newSpace)){
							if(startSpace.getPiece() instanceof King &&
									newSpace.hasPiece() && 
									newSpace.getPiece() instanceof Rook &&
									game1.getBoard().canCastle(startSpace.getPiece().getTeam(), (Rook)newSpace.getPiece())){
								int pieceMovingTo = 0;
								for(int i=0; i<whiteRectangles.size(); i++){
									if(BoardGrid.get(newSquare).contains(whiteRectangles.get(i))){
										pieceMovingTo = i;
									}
								}
								for(int i=0; i<blackRectangles.size(); i++){
									if(BoardGrid.get(newSquare).contains(blackRectangles.get(i))){
										pieceMovingTo = i;
									}
								}
		
								System.out.println(startSquare + "qwe q" + newSquare);
								if(startSquare < newSquare){
									if(whoseTeam == "white"){
										whiteRectangles.get(pieceHeld).setLocation((int)BoardGrid.get(startSquare + 2).getX() + 5, (int)BoardGrid.get(startSquare + 2).getY() + 5);
										whiteRectangles.get(pieceMovingTo).setLocation((int)BoardGrid.get(startSquare + 1).getX() + 5, (int)BoardGrid.get(startSquare + 1).getY() + 5);	
									}else{
										blackRectangles.get(pieceHeld).setLocation((int)BoardGrid.get(startSquare + 2).getX() + 5, (int)BoardGrid.get(startSquare + 2).getY() + 5);
										blackRectangles.get(pieceMovingTo).setLocation((int)BoardGrid.get(startSquare + 1).getX() + 5, (int)BoardGrid.get(startSquare + 1).getY() + 5);								
									}
								}else{
									if(whoseTeam == "white"){
										whiteRectangles.get(pieceHeld).setLocation((int)BoardGrid.get(startSquare - 2).getX() + 5, (int)BoardGrid.get(startSquare - 2).getY() + 5);
										whiteRectangles.get(pieceMovingTo).setLocation((int)BoardGrid.get(startSquare - 1).getX() + 5, (int)BoardGrid.get(startSquare - 1).getY() + 5);	
									}else{
										blackRectangles.get(pieceHeld).setLocation((int)BoardGrid.get(startSquare - 2).getX() + 5, (int)BoardGrid.get(startSquare - 2).getY() + 5);	
										blackRectangles.get(pieceMovingTo).setLocation((int)BoardGrid.get(startSquare - 1).getX() + 5, (int)BoardGrid.get(startSquare - 1).getY() + 5);							
									}
								}
								//castling
								game1.makeMove(startSpace, newSpace);
								
							//logic for capturing pieces. Made to else if so we don't capture on castle
							}else if(newSpace.hasPiece()){
								capturedPiece = newSpace.getPiece();
								for (int j=0; j<blackRectangles.size(); j++){
									if(BoardGrid.get(newSquare).contains(blackRectangles.get(j))){
										//blackRectangles.get(j).setLocation(600 + (capturedEnemies/2) * 50, 50 * (capturedEnemies%2));
										MoveRectToJail(blackRectangles.get(j));
										capturedEnemies++;
										lastEnemyCaptured = j;
									}
								}
								for (int j=0; j<whiteRectangles.size(); j++){
									if(BoardGrid.get(newSquare).contains(whiteRectangles.get(j))){
										//whiteRectangles.get(j).setLocation(600 + (capturedEnemies/2) * 50, 50 * (capturedEnemies%2));
										MoveRectToJail(whiteRectangles.get(j));
										capturedEnemies++;
										lastEnemyCaptured = j;
									}
								}
							//}
							//capturing
							game1.makeMove(startSpace, newSpace);
							
							if(whoseTeam == "white"){
								moveRectToSquare(whiteRectangles.get(pieceHeld), newSquare);
								//whiteRectangles.get(pieceHeld).setLocation((int)BoardGrid.get(newSquare).getX() + 5, (int)BoardGrid.get(newSquare).getY() + 5);	
							}else{
								moveRectToSquare(blackRectangles.get(pieceHeld), newSquare);
								//blackRectangles.get(pieceHeld).setLocation((int)BoardGrid.get(newSquare).getX() + 5, (int)BoardGrid.get(newSquare).getY() + 5);							
							}
						}else{
							//vanilla move
							game1.makeMove(startSpace, newSpace);
							
							if(whoseTeam == "white"){
								moveRectToSquare(whiteRectangles.get(pieceHeld), newSquare);
								//whiteRectangles.get(pieceHeld).setLocation((int)BoardGrid.get(newSquare).getX() + 5, (int)BoardGrid.get(newSquare).getY() + 5);	
							}else{
								moveRectToSquare(blackRectangles.get(pieceHeld), newSquare);
								//blackRectangles.get(pieceHeld).setLocation((int)BoardGrid.get(newSquare).getX() + 5, (int)BoardGrid.get(newSquare).getY() + 5);							
							}
						}
							int x1 = startSpace.getxCoordinate();
							int y1 = startSpace.getyCoordinate();
							int x2 = newSpace.getxCoordinate();
							int y2 = newSpace.getyCoordinate();
							game1.sendMove(x1 + "," + y1 + "," + x2 + "," + y2);
							
							moveMade = true;
							myTurn = false;
							//newSpace.placePiece(movingPiece);
							//startSpace.removePiece();
							//System.out.println("CHECKING CHECK");
							if(game1.getBoard().teamInCheck(Game.Team.TEAM2)){
								System.out.println("Team 2 is in check");
							}else if(game1.getBoard().teamInCheck(Game.Team.TEAM1)){
								System.out.println("Team 1 is in check");	
							}
							
						}
						
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					//whoseTeam = "";
					repaint();
				}else if (!moveMade){
					if(myTeam == Team.TEAM2){
						for(int i=0; i<whiteRectangles.size(); i++){
							if(whiteRectangles.get(i).contains(e.getX(), e.getY())){
								//System.out.println("Got Piece " + i);
								pieceSelected = true;
								pieceHeld = i;
								for (int j=0; j<BoardGrid.size(); j++){
									if(BoardGrid.get(j).contains(whiteRectangles.get(i))){
										startSquare = j;
										//System.out.println(startSquare % 8);
										//System.out.println(startSquare / 8);
									}
								}
								startSpace = gameBoard.getSpace(startSquare % 8, startSquare / 8);
								//System.out.println(startSpace.hasPiece());
								//possible move highlighter
								possibleMoves.clear();
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
								if(possibleMoves.isEmpty()){
									possibleMoves.add(BoardGrid.get(startSquare));
								}
								whoseTeam = "white";
							}
						}
					}else{
						for(int i=0; i<blackRectangles.size(); i++){
							if(blackRectangles.get(i).contains(e.getX(), e.getY())){
								//System.out.println("Got Piece " + i);
								pieceSelected = true;
								pieceHeld = i;
								for (int j=0; j<BoardGrid.size(); j++){
									if(BoardGrid.get(j).contains(blackRectangles.get(i))){
										startSquare = j;
										//System.out.println(startSquare % 8);
										//System.out.println(startSquare / 8);
									}
								}
								startSpace = gameBoard.getSpace(startSquare % 8, startSquare / 8);
								//System.out.println(startSpace.hasPiece());
								//possible move highlighter
								possibleMoves.clear();
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
								if(possibleMoves.isEmpty()){
									possibleMoves.add(BoardGrid.get(startSquare));
								}
								whoseTeam = "black";
							}
						}
					}
				}
				repaint();

				}else{
					//you've made your move so wait for opponent's turn
					try {
						latestOpponentMove = game1.listen();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					System.out.println(latestOpponentMove);
					replicateOpponentMove(latestOpponentMove);
				}
			}
		}
	  
	  public void replicateOpponentMove(String move){
		  
		  
		  
		  
		  myTurn = true;
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
		  repaint();
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
	  
	  public static void moveRectToSquare(Rectangle r, int square){
		  r.setLocation((int)BoardGrid.get(newSquare).getX() + 5, (int)BoardGrid.get(newSquare).getY() + 5);	
	  }
	  
	  public static void MoveRectToJail(Rectangle r){
		  if(whoseTeam == "white"){
			  r.setLocation(600 + (capturedEnemies/2) * 50, 50 * (capturedEnemies%2));
		  }else{
			  r.setLocation(600 + (capturedEnemies/2) * 50, 50 * (capturedEnemies%2) + 110);
		  }
	  }
	  
	  public static void undo(){
		  System.out.println("undoing start");
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
			  //update(g2);
		  }else{
			  //nothing to do
			  System.out.println("Nothing doing undo");
		  }
	  }
	  
	  public static void endTurn(){
		  moveMade = false;//Change this for networking
	  }
	  
	  public static void startGame(){
		  System.out.println("MADEIT satarat game");
		  try {
			game1.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("game start fail");
			e.printStackTrace();
		}
		  
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
		  if(team == Team.TEAM1){
			  myTurn = true;
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
		      for( int i=0; i<16; i++){
		    	  Rectangle piece = new Rectangle();
		    	  piece.setBounds(i*50 % 400 + 15, i/8 * 50 + 15, 30, 30);
		    	  blackRectangles.add(piece);
		      }
		      
		      for( int i=48; i<64; i++){
		    	  Rectangle piece = new Rectangle();
		    	  piece.setBounds(i*50 % 400 + 15, i/8 * 50 + 15, 30, 30);
		    	  whiteRectangles.add(piece);
		    	  //g2.setPaint(Color.blue);
		    	  //g2.draw(piece);
		    	  //g2.fill(piece);
		      }
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
	    for(int i=0;i<8;i++){
		    r = whiteRectangles.get(i);
		    g2.drawImage(wPawnImg, r.x, r.y, r.width, r.height, null);	    	
	    }
	    for(int i=8;i<16;i++){
		    r = blackRectangles.get(i);
		    g2.drawImage(bPawnImg, r.x, r.y, r.width, r.height, null);	    	
	    }
	    r = whiteRectangles.get(9);
	    g2.drawImage(wKnightImg, r.x, r.y, r.width, r.height, null);
	    r = whiteRectangles.get(14);
	    g2.drawImage(wKnightImg, r.x, r.y, r.width, r.height, null);

	    r = blackRectangles.get(1);
	    g2.drawImage(bKnightImg, r.x, r.y, r.width, r.height, null);
	    r = blackRectangles.get(6);
	    g2.drawImage(bKnightImg, r.x, r.y, r.width, r.height, null);
	    
	    r = whiteRectangles.get(8);
	    g2.drawImage(wRookImg, r.x, r.y, r.width, r.height, null);
	    r = whiteRectangles.get(15);
	    g2.drawImage(wRookImg, r.x, r.y, r.width, r.height, null);
	    
	    r = blackRectangles.get(0);
	    g2.drawImage(bRookImg, r.x, r.y, r.width, r.height, null);
	    r = blackRectangles.get(7);
	    g2.drawImage(bRookImg, r.x, r.y, r.width, r.height, null);

	    r = whiteRectangles.get(10);
	    g2.drawImage(wBishopImg, r.x, r.y, r.width, r.height, null);
	    r = whiteRectangles.get(13);
	    g2.drawImage(wBishopImg, r.x, r.y, r.width, r.height, null);
	    
	    r = blackRectangles.get(2);
	    g2.drawImage(bBishopImg, r.x, r.y, r.width, r.height, null);
	    r = blackRectangles.get(5);
	    g2.drawImage(bBishopImg, r.x, r.y, r.width, r.height, null);
	    
	    r = whiteRectangles.get(11);
	    g2.drawImage(wQueenImg, r.x, r.y, r.width, r.height, null);

	    r = blackRectangles.get(3);
	    g2.drawImage(bQueenImg, r.x, r.y, r.width, r.height, null);
	    
	    r = whiteRectangles.get(12);
	    g2.drawImage(wKingImg, r.x, r.y, r.width, r.height, null);
	    
	    r = blackRectangles.get(4);
	    g2.drawImage(bKingImg, r.x, r.y, r.width, r.height, null);
	    
	    Rectangle board = new Rectangle(5,5,400,400);
	    g2.setPaint(Color.black);
	    g2.draw(board);
	    
	    if(filler != null){
		    g2.setPaint(Color.white);
		    g2.draw(filler);
		    g2.fill(filler);
		    filler = null;
	    }
	    /*Rectangle piece = new Rectangle(0,0,30,30);
  	  	piece.setLocation(600, 100);
  	  	g2.setPaint(Color.orange);
  	  	g2.draw(piece);
  	  	g2.fill(piece);*/
	  }
	}
}
