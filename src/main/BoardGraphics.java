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

import Pieces.Piece;
import main.Game;

public class BoardGraphics extends Applet implements ActionListener {

	  //static protected Label label;
	  Button a;

	  public void init() {
		a = new Button("A");
		a.setSize(1, 1);
		a.setBounds(0, 0, 1, 1);
		a.addActionListener(this);
		a.setPreferredSize(new Dimension(1,1));

	    this.add(a);
	    setLayout(new BorderLayout());
	    add(new SMCanvas());
	    //label = new Label("Drag rectangle around within the area");
	    //add("South", label);
	  }
	  
	  public void actionPerformed(ActionEvent e) {
		  if (e.getSource() == a) 
				System.out.println("Button 1 was pressed");
			else
				System.out.println("Button 2 was pressed");
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
	}

	class SMCanvas extends Canvas implements MouseListener, MouseMotionListener {
	  //game logic pieces
	  Game game1 = new Game();
	  Board gameBoard = new Board();
	  //Variable to determine team of picked piece
	  String whoseTeam = "";
	  
	  //Keep track of number of captured pieces for later use
	  int capturedEnemies = 0;
	  
	  // Holds the coordinates of the user's last mousePressed event.
	  int last_x, last_y;
	  //initialize check
	  boolean firstTime = true;
	  //are we moving a piece
	  boolean pieceSelected = false;
	  //piece we select
	  int pieceHeld = 0;
	  int startSquare = 0;
	  int newSquare = 0;
	  //board graphics
	  ArrayList<Rectangle> BoardGrid = new ArrayList<Rectangle>();
	  //piece graphics
	  ArrayList<Rectangle> playerRectangles = new ArrayList<Rectangle>();
	  ArrayList<Rectangle> enemyRectangles = new ArrayList<Rectangle>();
	  ArrayList<Rectangle> possibleMoves = new ArrayList<Rectangle>();
	  //piece logics
	  Vector<Piece> playerPieces = new Vector<Piece>();
	  Vector<Piece> enemyPieces = new Vector<Piece>();
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
	  Space startSpace;
	  Space newSpace;

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
		if(pieceSelected){
			//playerRectangles.get(pieceHeld).setLocation(e.getX(), e.getY());
			//repaint place piece was
			//paint new place
			Piece movingPiece = startSpace.getPiece();
			
			pieceSelected = false;
			for (int j=0; j<BoardGrid.size(); j++){
				if(BoardGrid.get(j).contains(e.getX(), e.getY())){
					newSquare = j;		
					System.out.println(j);
				}
			}
			
			newSpace = gameBoard.getSpace(newSquare % 8, newSquare / 8);
			//System.out.println("QWE");
			try {
				if(game1.isValidMove(startSpace, newSpace)){
					//logic for capturing pieces
					if(newSpace.hasPiece()){
						for (int j=0; j<enemyRectangles.size(); j++){
							if(BoardGrid.get(newSquare).contains(enemyRectangles.get(j))){
								enemyRectangles.get(j).setLocation(600 + (capturedEnemies/2) * 50, 50 * (capturedEnemies%2));
								capturedEnemies++;
							}
						}
						for (int j=0; j<playerRectangles.size(); j++){
							if(BoardGrid.get(newSquare).contains(playerRectangles.get(j))){
								playerRectangles.get(j).setLocation(600 + (capturedEnemies/2) * 50, 50 * (capturedEnemies%2));
								capturedEnemies++;
							}
						}
					}
					
					game1.makeMove(startSpace, newSpace);
					
					if(whoseTeam == "white"){
						playerRectangles.get(pieceHeld).setLocation((int)BoardGrid.get(newSquare).getX() + 5, (int)BoardGrid.get(newSquare).getY() + 5);	
					}else{
						enemyRectangles.get(pieceHeld).setLocation((int)BoardGrid.get(newSquare).getX() + 5, (int)BoardGrid.get(newSquare).getY() + 5);							
					}
					
					
					
					//newSpace.placePiece(movingPiece);
					//startSpace.removePiece();
					System.out.println("CHECKING CHECK");
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
			
			whoseTeam = "";
			repaint();
		}else{
			for(int i=0; i<playerRectangles.size(); i++){
				if(playerRectangles.get(i).contains(e.getX(), e.getY())){
					System.out.println("Got Piece " + i);
					pieceSelected = true;
					pieceHeld = i;
					for (int j=0; j<BoardGrid.size(); j++){
						if(BoardGrid.get(j).contains(playerRectangles.get(i))){
							startSquare = j;
							//System.out.println(startSquare % 8);
							//System.out.println(startSquare / 8);
						}
					}
					startSpace = gameBoard.getSpace(startSquare % 8, startSquare / 8);
					System.out.println(startSpace.hasPiece());
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
				//DELETE THIS LATER
				if(enemyRectangles.get(i).contains(e.getX(), e.getY())){
					System.out.println("Got Piece " + i);
					pieceSelected = true;
					pieceHeld = i;
					for (int j=0; j<BoardGrid.size(); j++){
						if(BoardGrid.get(j).contains(enemyRectangles.get(i))){
							startSquare = j;
							//System.out.println(startSquare % 8);
							//System.out.println(startSquare / 8);
						}
					}
					startSpace = gameBoard.getSpace(startSquare % 8, startSquare / 8);
					System.out.println(startSpace.hasPiece());
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
			repaint();
			/*for(int i =0; i< gameBoard.getSpaces().size(); i++){
				System.out.println(gameBoard.getSpaces().get(i).hasPiece()
						+ " " + 
						gameBoard.getSpaces().get(i).getxCoordinate() 
						+ " " +
						gameBoard.getSpaces().get(i).getyCoordinate()
						
						);
			}*/
		}
		
	    /*if (rect.contains(e.getX(), e.getY())) {
	      if (!pressOut) {
	    	System.out.println("Inside rect");
	        updateLocation(e);
	      }
	    } else {
	    	System.out.println("out of rect");
	    	//BoardGraphics.label.setText("First position the cursor on the rectangle "
	        //+ "and then drag.");
	    }
	    System.out.println(e.getX() + " " + e.getY());
		System.out.println(rect.getX() + " " + rect.getY());
		rect.setLocation(e.getX(), e.getY());
		//repaint();*/
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
		    	  enemyRectangles.add(piece);
		      }
		      
		      for( int i=48; i<64; i++){
		    	  Rectangle piece = new Rectangle();
		    	  piece.setBounds(i*50 % 400 + 15, i/8 * 50 + 15, 30, 30);
		    	  playerRectangles.add(piece);
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
			    
			    
			    //System.out.println(playerRectangles.get(pieceHeld).getBounds());
			    //g2.setPaint(Color.blue);
			    //g2.draw(playerRectangles.get(pieceHeld));
			    //g2.fill(playerRectangles.get(pieceHeld));
	    }
	    g2.setPaint(Color.yellow);
	    for(int i=0; i<possibleMoves.size(); i++){
	    	g2.draw(possibleMoves.get(i));
		    g2.fill(possibleMoves.get(i));
	    }
	    
	    Rectangle r = null;
	    for(int i=0;i<8;i++){
		    r = playerRectangles.get(i);
		    g2.drawImage(wPawnImg, r.x, r.y, r.width, r.height, null);	    	
	    }
	    for(int i=8;i<16;i++){
		    r = enemyRectangles.get(i);
		    g2.drawImage(bPawnImg, r.x, r.y, r.width, r.height, null);	    	
	    }
	    r = playerRectangles.get(9);
	    g2.drawImage(wKnightImg, r.x, r.y, r.width, r.height, null);
	    r = playerRectangles.get(14);
	    g2.drawImage(wKnightImg, r.x, r.y, r.width, r.height, null);

	    r = enemyRectangles.get(1);
	    g2.drawImage(bKnightImg, r.x, r.y, r.width, r.height, null);
	    r = enemyRectangles.get(6);
	    g2.drawImage(bKnightImg, r.x, r.y, r.width, r.height, null);
	    
	    r = playerRectangles.get(8);
	    g2.drawImage(wRookImg, r.x, r.y, r.width, r.height, null);
	    r = playerRectangles.get(15);
	    g2.drawImage(wRookImg, r.x, r.y, r.width, r.height, null);
	    
	    r = enemyRectangles.get(0);
	    g2.drawImage(bRookImg, r.x, r.y, r.width, r.height, null);
	    r = enemyRectangles.get(7);
	    g2.drawImage(bRookImg, r.x, r.y, r.width, r.height, null);

	    r = playerRectangles.get(10);
	    g2.drawImage(wBishopImg, r.x, r.y, r.width, r.height, null);
	    r = playerRectangles.get(13);
	    g2.drawImage(wBishopImg, r.x, r.y, r.width, r.height, null);
	    
	    r = enemyRectangles.get(2);
	    g2.drawImage(bBishopImg, r.x, r.y, r.width, r.height, null);
	    r = enemyRectangles.get(5);
	    g2.drawImage(bBishopImg, r.x, r.y, r.width, r.height, null);
	    
	    r = playerRectangles.get(11);
	    g2.drawImage(wQueenImg, r.x, r.y, r.width, r.height, null);

	    r = enemyRectangles.get(3);
	    g2.drawImage(bQueenImg, r.x, r.y, r.width, r.height, null);
	    
	    r = playerRectangles.get(12);
	    g2.drawImage(wKingImg, r.x, r.y, r.width, r.height, null);
	    
	    r = enemyRectangles.get(4);
	    g2.drawImage(bKingImg, r.x, r.y, r.width, r.height, null);
	    
	    Rectangle board = new Rectangle(5,5,400,400);
	    g2.setPaint(Color.black);
	    g2.draw(board);
	    
	    
	    /*Rectangle piece = new Rectangle(0,0,30,30);
  	  	piece.setLocation(600, 100);
  	  	g2.setPaint(Color.orange);
  	  	g2.draw(piece);
  	  	g2.fill(piece);*/
	  }
}
