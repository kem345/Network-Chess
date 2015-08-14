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
		wRookImg = new ImageIcon("../images/whitePawn.png").getImage();
		wKnightImg = new ImageIcon("../images/whitePawn.png").getImage();
		wBishopImg = new ImageIcon("../images/whitePawn.png").getImage();
		wKingImg = new ImageIcon("../images/whitePawn.png").getImage();
		wQueenImg = new ImageIcon("../images/whitePawn.png").getImage();
		bPawnImg = new ImageIcon("../images/blackPawn.png").getImage();
		bRookImg = new ImageIcon("../images/whitePawn.png").getImage();
		bKnightImg = new ImageIcon("../images/whitePawn.png").getImage();
		bBishopImg = new ImageIcon("../images/whitePawn.png").getImage();
		bKingImg = new ImageIcon("../images/whitePawn.png").getImage();
		bQueenImg = new ImageIcon("../images/whitePawn.png").getImage();
		
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
			if(movingPiece.makeMove(startSpace, newSpace)){
				playerRectangles.get(pieceHeld).setLocation((int)BoardGrid.get(newSquare).getX() + 5, (int)BoardGrid.get(newSquare).getY() + 5);
				newSpace.placePiece(movingPiece);
				startSpace.removePiece();
			}
			
			
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
						Piece movingPiece = startSpace.getPiece();
						if(movingPiece.makeMove(startSpace, newSpace)){
							possibleMoves.add(BoardGrid.get(k));
						}
					}
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
	    	  g2.setPaint(Color.orange);
	    	  g2.draw(piece);
	    	  g2.fill(piece);
	      }
	      
	      for( int i=48; i<64; i++){
	    	  Rectangle piece = new Rectangle();
	    	  piece.setBounds(i*50 % 400 + 15, i/8 * 50 + 15, 30, 30);
	    	  playerRectangles.add(piece);
	    	  g2.setPaint(Color.blue);
	    	  g2.draw(piece);
	    	  g2.fill(piece);
	      }
	     // for(int i=0; i<64; i++){
	    //	  System.out.println(BoardGrid.get(i).x);
	    //  }
	      Rectangle board = new Rectangle(5,5,400,400);
	      g2.setPaint(Color.black);
	      g2.draw(board);
	      //rect.setBounds(15, 15, 30, 30);

		    
		  //Rectangle r = playerRectangles.get(0);
		    //g2.setPaint(Color.yellow);
		    //g2.draw(playerRectangles.get(0));
		    //g2.fill(playerRectangles.get(0));
		    //System.out.println(img.);
		  //g2.drawImage(img, r.x, r.y, r.width, r.height, null);
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
		    
		    
		    System.out.println(playerRectangles.get(pieceHeld).getBounds());
		    g2.setPaint(Color.blue);
		    g2.draw(playerRectangles.get(pieceHeld));
		    g2.fill(playerRectangles.get(pieceHeld));
	    }
	    g2.setPaint(Color.yellow);
	    for(int i=0; i<possibleMoves.size(); i++){
	    	g2.draw(possibleMoves.get(i));
		    g2.fill(possibleMoves.get(i));
	    }
	    
	    Rectangle r = playerRectangles.get(0);
	    g2.drawImage(wPawnImg, r.x, r.y, r.width, r.height, null);

	    r = playerRectangles.get(1);
	    g2.drawImage(bPawnImg, r.x, r.y, r.width, r.height, null);
	  }

	  /*
	   * Checks if the rectangle is contained within the applet window.  If the rectangle
	   * is not contained withing the applet window, it is redrawn so that it is adjacent
	   * to the edge of the window and just inside the window.
	   */
	  
}
