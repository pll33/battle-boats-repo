package model.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import model.player.Player;

/**
 * A Thread spawned by Server to handle communication between multiple clients.
 *
 */
public class ClientThread extends Thread {
	
	/**
	 * The Server that spawned this thread.
	 */
	private final Server server;
	
	/**
	 * The ID of this thread
	 */
	private final int id;
	
	/**
	 * A stream used for sending messages (Objects) to this client.
	 */
	private ObjectOutputStream out;
	
	/**
	 * A steam used for receiving messages (Objects) from other clients.
	 */
	private ObjectInputStream in;
	
	/**
	 * A boolean indicating if this Thread is running.
	 */
	private boolean running;
	
	/**
	 * A boolean indicating whether the Client this Thread handles is local to the server.
	 */
	private Boolean clientLocalToServer;
	
	/**
	 * Construct a new ClientThread.
	 * @param id The ID of the Thread
	 * @param socket The socket created for this Thread.
	 * @param server The Server instance.
	 */
	public ClientThread(final int id, final Socket socket, final Server server){
		
		this.running = true;
		this.server = server;
		this.id = id;
		this.clientLocalToServer = null;
		
		try {
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public Boolean isClientLocalToServer(){
		return clientLocalToServer;
	}
	
	/**
	 * 
	 * @return The ID of the Thread
	 */
	public int getID(){
		return id;
	}
	
	/**
	 * Sends a message to this Client.
	 * @param message The message to send.
	 */
	public void sendMessage(final Object message){
		try {
			out.writeObject(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Change if this Thread is running.
	 * @param running
	 */
	public void setRunning(final boolean running){
		this.running = running;
	}
	
	/**
	 * The Threads main run method.
	 */
	@Override	
	public void run(){
		Object input; //what the client sends in		
		try {			
			while(running == true && (input = in.readObject()) != null){
				
				if(input instanceof Player){
					clientLocalToServer = ((Player)input).isPlayerLocalToServer();
				}else{
					//server.sendMessageToAll(id, input);
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
