package model.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientThread extends Thread {
	
	private final Server server;
	private final int id;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private boolean running;
	
	public ClientThread(final int id, final Socket socket, final Server server){
		
		this.running = true;
		this.server = server;
		this.id = id;
		
		try {
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public int getID(){
		return id;
	}
	
	public void sendMessage(final String message){
		try {
			out.writeObject(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
