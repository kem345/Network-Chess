

package Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

import javax.swing.JFrame;



public class ChessServer{

	private static final int PORT = 8889;
	JFrame frame = new JFrame("Server is Running");
	
	public ChessServer(){
		
	}
	
	public static void main(String[] args) throws Exception{
		ChessServer cs = new ChessServer();
		cs.frame.setBounds(100, 100, 500, 160); 
		cs.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cs.frame.setVisible(true);
		ServerSocket listener = new ServerSocket(PORT);

		try {
			while(true){
				Match game = new Match();
				Match.Player TEAM1 = game.new Player(listener.accept(), "TEAM1");
				Match.Player TEAM2 = game.new Player(listener.accept(), "TEAM2");
				TEAM1.setOpponent(TEAM2);
				TEAM2.setOpponent(TEAM1);
				TEAM1.output.println("START");
				TEAM2.output.println("START");
				TEAM1.start();
				TEAM2.start();
			}
		} finally {
			listener.close();
		}
	}
}


class Match{
	
	class Player extends Thread{
		String mark;
		Player opponent;
		Socket socket;
		BufferedReader input;
		PrintWriter output;
		
		
		
		public Player(Socket socket, String mark) throws Exception{
			this.socket = socket;
			this.mark = mark;
			try{
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream(),true);
			output.println(mark);
			}catch(IOException e){
				System.out.println("Error: " + e);
			}
		}
		
		public void setOpponent(Player opponent){
			this.opponent = opponent;
		}
		
		
		public synchronized void Move(Player player,String move)
		{
			player.opponent.output.println(move);
		}
		
		public void run(){
			try {			
			
				while(true){
						String command = input.readLine();
						Move(this,command);
						}				
			} catch (Exception e) {
			}
		}
		}
	}	