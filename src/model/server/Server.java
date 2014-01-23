package model.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
	
	private int count;
	private int port;
	private List<ClientThread> threads;
	
	public Server(final int port){
		this.count = 0;
		this.port = port;
		this.threads = new ArrayList<ClientThread>();
	}
	
	public void start() {
		
		try(ServerSocket serverSocket = new ServerSocket(port)){
			
			while(true){
				Socket clientSocket = serverSocket.accept();
				count++;
				threads.add(new ClientThread(count, clientSocket));
				threads.get(threads.size() - 1).start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void tearDown(){
		
	}

}
