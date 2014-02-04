package model.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
	
	private int port;
	
	private final int MAX_CONNECTIONS = 4;
	
	private boolean accepting;
	
	private int count;
	
	private List<ClientThread> threads;
	
	public Server(final int port){
		this.port = port;
		this.accepting = true;
		this.count = 0;
		this.threads = new ArrayList<ClientThread>();
	}
	
	@Override
	public void run(){
		logMessage("Server Started");
		waitForConnections();
	}
	
	private void waitForConnections(){
		try (ServerSocket serverSocket = new ServerSocket(port)) {

			logMessage("Accepting Connections");
			//mutex.release(); //server has started
			
			while (accepting) {
				Socket clientSocket = serverSocket.accept();
				logMessage("Client Connected");
				count++;
				threads.add(new ClientThread(count, clientSocket, this));
				threads.get(threads.size() - 1).start();
				
				logMessage("Number of clients connected: " + threads.size());
				
				if(threads.size() == MAX_CONNECTIONS){
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

}
