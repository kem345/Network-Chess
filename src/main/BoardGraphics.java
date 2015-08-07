package main;

import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.awt.image.*;
import java.util.ArrayList;

public class BoardGraphics extends Applet {

	  //static protected Label label;

	  public void init() {

	    setLayout(new BorderLayout());
	    add(new SMCanvas());

	    //label = new Label("Drag rectangle around within the area");
	    //add("South", label);
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
	  
	  Rectangle rect = new Rectangle(0, 0, 100, 50);
	  BufferedImage bi;
	  Graphics2D big;
	  // Holds the coordinates of the user's last mousePressed event.
	  int last_x, last_y;
	  boolean firstTime = true;
	  boolean pieceSelected = false;
	  int pieceHeld = 0;
	  int startSquare = 0;
	  int newSquare = 0;
	  //TexturePaint fillPolka, strokePolka;
	  Rectangle area;
	  ArrayList<Rectangle> BoardGrid = new ArrayList<Rectangle>();
	  ArrayList<Rectangle> playerPieces = new ArrayList<Rectangle>();
	  ArrayList<Rectangle> enemyPieces = new ArrayList<Rectangle>();
	  
	  
	  // True if the user pressed, dragged or released the mouse outside of the rectangle; false otherwise.
	  boolean pressOut = false;

	  public SMCanvas() {
	    setBackground(Color.white);
	    addMouseMotionListener(this);
	    addMouseListener(this);

	    // Creates the fill texture paint pattern.
	    /*bi = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
	    big = bi.createGraphics();
	    //big.setColor(Color.pink);
	    //big.fillRect(0, 0, 7, 7);
	    big.setColor(Color.cyan);
	    big.fillOval(0, 0, 3, 3);
	    Rectangle r = new Rectangle(0, 0, 5, 5);
	    r.setLocation(10,10);
	    fillPolka = new TexturePaint(bi, r);
	    big.dispose();
*/

	  }

	  // Handles the event of the user pressing down the mouse button.
	  public void mousePressed(MouseEvent e) {
	    last_x = rect.x - e.getX();
	    last_y = rect.y - e.getY();

	    // Checks whether or not the cursor is inside of the rectangle while the
	    // user is pressing the mouse.
	    if (rect.contains(e.getX(), e.getY())) {
	      pressOut = false;
	      updateLocation(e);
	    } else {
	    	//BoardGraphics.label.setText("First position the cursor on the rectangle "
	        //+ "and then drag.");
	      pressOut = true;
	    }
	  }

	  // Handles the event of a user dragging the mouse while holding down the
	  // mouse button.
	  public void mouseDragged(MouseEvent e) {
	    if (!pressOut) {
	      updateLocation(e);
	    } else {
	    	//BoardGraphics.label.setText("First position the cursor on the rectangle "
	        //+ "and then drag.");
	    }
	  }

	  // Handles the event of a user releasing the mouse button.
	  public void mouseReleased(MouseEvent e) {

	    // Checks whether or not the cursor is inside of the rectangle when the
	    // user releases the mouse button.
		
		if(pieceSelected){
			
			//playerPieces.get(pieceHeld).setLocation(e.getX(), e.getY());
			//repaint place piece was
			//paint new place
			
			pieceSelected = false;
			for (int j=0; j<BoardGrid.size(); j++){
				if(BoardGrid.get(j).contains(e.getX(), e.getY())){
					newSquare = j;
					playerPieces.get(pieceHeld).setLocation((int)BoardGrid.get(j).getX() + 5, (int)BoardGrid.get(j).getY() + 5);
					System.out.println(j);
				}
			}
			
			repaint();
		}else{
			for(int i=0; i<playerPieces.size(); i++){
				if(playerPieces.get(i).contains(e.getX(), e.getY())){
					System.out.println("Got Piece " + i);
					pieceSelected = true;
					pieceHeld = i;
					for (int j=0; j<BoardGrid.size(); j++){
						if(BoardGrid.get(j).contains(playerPieces.get(i))){
							startSquare = j;
						}
					}
				}
			}
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
	    rect.setLocation(last_x + e.getX(), last_y + e.getY());
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
	    Dimension dim = getSize();
	    int w = (int) dim.getWidth();
	    int h = (int) dim.getHeight();
	    g2.setStroke(new BasicStroke(8.0f));
	    
	    
	    if (firstTime) {
	      area = new Rectangle(dim);
	      
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
	    	  enemyPieces.add(piece);
	    	  g2.setPaint(Color.orange);
	    	  g2.draw(piece);
	    	  g2.fill(piece);
	      }
	      
	      for( int i=48; i<64; i++){
	    	  Rectangle piece = new Rectangle();
	    	  piece.setBounds(i*50 % 400 + 15, i/8 * 50 + 15, 30, 30);
	    	  playerPieces.add(piece);
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
	      firstTime = false;
	    }else{
	    	System.out.println(playerPieces.get(pieceHeld).getBounds());
		    g2.setPaint(Color.blue);
		    g2.draw(playerPieces.get(pieceHeld));
		    g2.fill(playerPieces.get(pieceHeld));
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
	    }
	    /*System.out.println(0%8%2);
	    System.out.println(1%8%2);
	    System.out.println(2%8%2);
	    System.out.println(3%8%2);
	    System.out.println(4%8%2);*/
	    
	    /*for(int i=0; i<64; i++){
	    	System.out.println(BoardGrid.get(i).getBounds());
	    }*/
	    
	    
	    
	    // Clears the rectangle that was previously drawn.
	    //g2.setPaint(Color.white);
	    //g2.fillRect(0, 0, w, h);
	    
	    // Draws and fills the newly positioned rectangle.
	    /*g2.setPaint(Color.black);
	    g2.draw(rect);
	    g2.setPaint(Color.blue);
	    g2.fill(rect);*/

	  }

	  /*
	   * Checks if the rectangle is contained within the applet window.  If the rectangle
	   * is not contained withing the applet window, it is redrawn so that it is adjacent
	   * to the edge of the window and just inside the window.
	   */
	  boolean checkRect() {

	    if (area == null) {
	      return false;
	    }

	    if (area.contains(rect.x, rect.y, 100, 50)) {
	      return true;
	    }
	    int new_x = rect.x;
	    int new_y = rect.y;

	    if ((rect.x + 100) > area.getWidth()) {
	      new_x = (int) area.getWidth() - 99;
	    }
	    if (rect.x < 0) {
	      new_x = -1;
	    }
	    if ((rect.y + 50) > area.getHeight()) {
	      new_y = (int) area.getHeight() - 49;
	    }
	    if (rect.y < 0) {
	      new_y = -1;
	    }
	    rect.setLocation(new_x, new_y);
	    return false;
	  }
}
