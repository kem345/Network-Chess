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

	  Button startGame;
	  TextField ipAddressBox;
	  
	  public void init() {
		startGame = new Button("Start Game");
		ipAddressBox = new TextField("Enter IP Here");
		
		startGame.setBounds(20, 80, 60, 20);
		ipAddressBox.setBounds(20, 110, 160, 20);
		
		startGame.addActionListener(this);
		ipAddressBox.addActionListener(this);
		
	    this.add(startGame);
	    this.add(ipAddressBox);
	    
	    setLayout(new BorderLayout());
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
