package main;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainMenu extends Applet implements ActionListener {

	  Label noteLabel;
	  //Button undo;
	  //Button end;
	  Button startGame;
	  TextField ipAddressBox;
	  
	  public void init() {
		//undo = new Button("Undo");
		//a.setSize(1, 1);
		//end = new Button ("End Turn");
		startGame = new Button("Start Game");
		
		ipAddressBox = new TextField("Enter IP Here");
		
		//noteLabel = new Label("Game Started");
		//undo.setBounds(20, 20, 60, 20);
		//end.setBounds(20, 50, 60, 20);

		startGame.setBounds(20, 80, 60, 20);
		ipAddressBox.setBounds(20, 110, 160, 20);
		//noteLabel.setBounds(10, 450, 100, 20);
		//undo.addActionListener(this);
		//end.addActionListener(this);
		startGame.addActionListener(this);
		ipAddressBox.addActionListener(this);
		
		//a.setPreferredSize(new Dimension(1,1));
		//SMCanvas mainCanvas = new SMCanvas();
		//undo = new Button(mainCanvas, SWT.Push);
	   // this.add(undo);
	    //this.add(end);
	    this.add(startGame);
	    this.add(ipAddressBox);
	    //this.add(noteLabel);
	    setLayout(new BorderLayout());
	    //add(new SMCanvas());
	    //label = new Label("Drag rectangle around within the area");
	    //add("South", label);
	  }
	  
	  public void actionPerformed(ActionEvent e) {
		  if (e.getSource() == startGame) {
			  BoardGraphics game = new BoardGraphics();
			  String[] args = new String[1];
			  args[0] = ipAddressBox.getText();
			  game.main(args);
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
	
}
