package model.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Server extends Thread {

	private boolean accepting;
	private int count;
	private int port;
	private List<ClientThread> threads;
	private Semaphore mutex;

	public Server(final int port, final Semaphore mutex) {
		this.accepting = true;
		this.count = 0;
		this.port = port;
		this.threads = new ArrayList<ClientThread>();
		this.mutex = mutex;
	}

	@Override
	public void run() {
		logMessage("Server Started");
		waitForConnections();
	}

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
	
	private void logMessage(final String message){
		System.out.println("Server: " + message);
	}
	
	public void setAllowConnections(final boolean accepting){
		this.accepting = accepting;
	}
	
	public void sendMessageToAll(final int id, final String message){
		
		for(final ClientThread client : threads){
			if(id != client.getID()){
				client.sendMessage(message);
			}
		}
		
	}

	public void tearDown() {
		
		//setAllowConnections(false);
		for(final ClientThread t : threads){
			t.setRunning(false);
		}
		threads.clear();

	}

}
