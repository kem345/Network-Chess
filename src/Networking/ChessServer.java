package Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;


public class ChessServer {

	private static final int PORT = 8888;
	
	
	
	
	public static void main(String[] args) throws Exception{
		ServerSocket listener = new ServerSocket(PORT);
		System.out.println("Online chess server is running!");
		try {
			while(true){
				Game game = new Game();
				Game.Player TEAM1 = game.new Player(listener.accept(), "TEAM1");
				Game.Player TEAM2 = game.new Player(listener.accept(), "TEAM2");
				TEAM1.setOpponent(TEAM2);
				TEAM2.setOpponent(TEAM1);
				TEAM1.output.println("START");
				TEAM2.output.println("START");
				game.currentPlayer = TEAM1;
				TEAM1.start();
				TEAM2.start();
			}
		} finally {
			listener.close();
		}
	}
}


class Game{
	Player currentPlayer;
	
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
		
		
		public synchronized boolean isTurnToMove(Player player,String move)
		{
			if(this == currentPlayer)
			{
				currentPlayer = currentPlayer.opponent; //Switch current player.
				currentPlayer.output.println(move);
				return true;
			}		
			return false;
		}
		
		public void run(){
			try {			
			
				while(true){
					if(this == currentPlayer)
					{
						String command = input.readLine();
							if(isTurnToMove(this,command)){
								
							}
						}
					}					
			} catch (Exception e) {
			}
		}
		}
	}	