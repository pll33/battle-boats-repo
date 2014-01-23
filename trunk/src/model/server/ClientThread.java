package model.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
	
	private final Server server;
	private final int id;
	private PrintWriter out;
	private BufferedReader in;
	private boolean running;
	
	public ClientThread(final int id, final Socket socket, final Server server){
		
		this.running = true;
		this.server = server;
		this.id = id;
		try {
			this.out = new PrintWriter(socket.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public int getID(){
		return id;
	}
	
	public void sendMessage(final String message){
		out.println(message);
	}
	
	public void setRunning(final boolean running){
		this.running = running;
	}
	
	@Override
	public void run(){
		String input; //what the client sends in		
		try {			
			while(running == true && (input = in.readLine()) != null){
				server.sendMessageToAll(id, input);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
