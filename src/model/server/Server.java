package model.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import core.Constants;
import core.GameSettings;

public class Server extends Thread {

	private int port;

	private final int MAX_CONNECTIONS = 2;

	private boolean accepting;

	private int count;

	private List<ClientThread> threads;

	private Semaphore mutex;

	private GameSettings settings;

	public Server(final Semaphore mutex, final GameSettings settings) {
		this(Constants.PORT, mutex, settings);
	}

	public Server(final int port, final Semaphore mutex,
			final GameSettings settings) {
		this.port = port;
		this.accepting = true;
		this.count = 0;
		this.threads = new ArrayList<ClientThread>();
		this.mutex = mutex;
		this.settings = settings;
	}

	@Override
	public void run() {
		logMessage("Server Started");
		waitForConnections();
	}

	private void waitForConnections() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {

			logMessage("Accepting Connections");
			mutex.release(); // server has started

			while (accepting) {
				Socket clientSocket = serverSocket.accept();
				logMessage("Client Connected");
				count++;
				threads.add(new ClientThread(count, clientSocket, this));
				threads.get(threads.size() - 1).start();

				logMessage("Number of clients connected: " + threads.size());

				if (threads.size() == MAX_CONNECTIONS) {
					accepting = false;
					broadcastSettings();
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void broadcastSettings() {
		sendMessageToAll(settings);
	}

	public void sendMessageToAll(final Object message) {
		sendMessageToAll(-1, message);
	}

	public void sendMessageToAll(final int id, final Object message) {
		for (final ClientThread ct : threads) {
			if (ct.getID() != id) {
				ct.sendMessage(message);
			}
		}
	}

	/**
	 * A utility function to log messages from the Server.
	 * 
	 * @param message
	 *            The message to log.
	 */
	private void logMessage(final String message) {
		System.out.println("Server: " + message);
	}

}
