package model.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * A Thread used to recieve connections from Clients and spawn Threads to handle their requests.
 *
 */
public class Server extends Thread {

	/**
	 * Whether the Server is accepting new connections
	 */
	private boolean accepting;
	
	/**
	 * The count of how many Clients have connected since the Server has started
	 */
	private int count;
	
	/**
	 * The port the Server will utilize.
	 */
	private int port;
	
	/**
	 * A List of all the active ClientThreads.
	 */
	private List<ClientThread> threads;
	
	/**
	 * A lock that gets released once the Server can recieve connections.
	 */
	private Semaphore mutex;

	public Server(final int port, final Semaphore mutex) {
		this.accepting = true;
		this.count = 0;
		this.port = port;
		this.threads = new ArrayList<ClientThread>();
		this.mutex = mutex;
	}

	@Override
	/**
	 * The Threads main run method
	 */
	public void run() {
		logMessage("Server Started");
		waitForConnections();
	}

	/**
	 * Waits for connections from clients while <code>accepting = true</code>. <code>Accepting</code> becomes false in this method when 2 Clients have connected.
	 */
	public void waitForConnections() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {

			logMessage("Accepting Connections");
			mutex.release(); //server has started
			
			while (accepting) {
				Socket clientSocket = serverSocket.accept();
				logMessage("Client Connected");
				count++;
				threads.add(new ClientThread(count, clientSocket, this));
				threads.get(threads.size() - 1).start();
				
				logMessage("Number of clients connected: " + threads.size());
				//two players have joined, so stop accepting connections
				if(threads.size() == 2){
					accepting = false;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A utility function to log messages from the Server.
	 * @param message The message to log.
	 */
	private void logMessage(final String message){
		System.out.println("Server: " + message);
	}
	
	/**
	 * Change if the Server is accepting connections.
	 * @param accepting
	 */
	public void setAllowConnections(final boolean accepting){
		this.accepting = accepting;
	}
	
	/**
	 * Sends a message to all connected clients, except the client who originated the message. This method is useful for one client to send a message to all other clients.
	 * @param id The ID of the client that originated the message.
	 * @param message The message to send
	 */
	public void sendMessageToAll(final int id, final String message){
		
		for(final ClientThread client : threads){
			if(id != client.getID()){
				client.sendMessage(message);
			}
		}
		
	}

	/**
	 * The Server's deconstructor
	 */
	public void tearDown() {
		
		//setAllowConnections(false);
		for(final ClientThread t : threads){
			t.setRunning(false);
		}
		threads.clear();

	}

}
