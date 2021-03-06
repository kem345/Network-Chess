package Networking;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

	public class TestingPart{

	    BufferedReader in;
	    PrintWriter out;
	    JFrame frame = new JFrame("Chatter");
	    JTextField textField = new JTextField(40);
	    JTextArea messageArea = new JTextArea(8, 40);
	    String name;
	    
	    
	    
	    public TestingPart(){
	        textField.setEditable(false);
	        messageArea.setEditable(false);
	        frame.getContentPane().add(textField, "North");
	        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
	        frame.pack();
	        
	        
	        textField.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					out.println(textField.getText());
					textField.setText("");
				}
			});
	    }
	    
	    private void run() throws IOException{
	    	String serverAddress = "localhost";
	    	Socket socket = new Socket(serverAddress, 8889);
	    	in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    	out = new PrintWriter(socket.getOutputStream(), true);
	    	textField.setEditable(true);
	    	while(true){
	    		
	    		String line = in.readLine();
	    		messageArea.append(line + "\n");
	    	}
	    }
	
	public static void main(String[] args) throws Exception{
		TestingPart client = new TestingPart();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
	}
	}

