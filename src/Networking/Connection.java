package Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import main.Game.Team;

public class Connection {

	public BufferedReader in;
	public PrintWriter out;
	private static int PORT = 8889;
	public static String serverAddress = "localhost";
	private Socket socket;
	
	public Connection(String ip) {
		try {
			serverAddress = ip;
			socket = new Socket(serverAddress,PORT);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(),true);
		} catch (Exception e) { 
			System.out.println("Unable to establish connection");
		}
	}
	
	/** Establishes connection with the server. Gets a message from the server telling
	 * whether you are team1 or team2 and when both teams have started 
	 * @throws Exception
	 */
	public Team run() throws Exception{
		Team team = null;
		while(true){
			String message = in.readLine();
			if(message != null && message.startsWith("TEAM")){
				if(message.equals("TEAM1")) 
					team = Team.TEAM1;
				else
					team = Team.TEAM2;
			}else if(message != null && message.startsWith("START")){
				return team;
			}
		}
	}
	
	/** Listens for an opponents move and then returns the servers message for the move **/
	public String listen() throws IOException {
		while(true) {
			String message = in.readLine();
			if(message != null && message.startsWith("MOVE"))
				return message;
		}
	}
	
	/** Sends the move you made to the server which then sends it to the opponent **/
	public void sendMove(String move) {
		out.println("MOVE,"+move);
	}
	
	
}
