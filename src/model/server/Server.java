package model.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;

import utils.Logger;
import core.Constants;
import core.GameSettings;
import core.ReadyIndicator;

public class Server extends Thread {

	private int port;

	private final int MAX_CONNECTIONS = 2;

	private boolean accepting;

	private int count;

	private List<ClientThread> threads;

	private Semaphore startMutex;
	private Semaphore settingsMutex;

	private GameSettings settings;

	private Boolean[] readyStatus;

	public Server(final Semaphore mutex, final GameSettings settings) {
		this(Constants.PORT, mutex, settings);
	}

	public Server(final int port, final Semaphore startMutex,
			final GameSettings settings) {
		this.readyStatus = new Boolean[2];
		Arrays.fill(readyStatus, Boolean.FALSE);
		this.port = port;
		this.accepting = true;
		this.count = 0;
		this.threads = new ArrayList<ClientThread>();
		this.startMutex = startMutex;
		this.settingsMutex = new Semaphore(0);
		this.settings = settings;
	}

	@Override
	public void run() {
		Logger.log("Server Started", this);
		waitForConnections();
	}

	private void waitForConnections() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {

			Logger.log("Accepting Connections", this);
			startMutex.release(); // server has started

			while (accepting) {
				Socket clientSocket = serverSocket.accept();
				Logger.log("Client Connected", this);

				threads.add(new ClientThread(threads.size() + 1, clientSocket, this, MAX_CONNECTIONS));
				threads.get(threads.size() - 1).start();
				count++;

				Logger.log("Number of clients connected: " + threads.size(), this);

				if (threads.size() == MAX_CONNECTIONS) {
					accepting = false;
					//acquire mutex to ensure gamesettings has been set with both players names
					settingsMutex.acquire();
					broadcastSettings();
				}

			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void broadcastSettings() {
		Logger.log("Broadcasting settings", this);
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

	public boolean setReadyStatus(int id) {
		this.readyStatus[id] = true;

		boolean allReady = true;
		for (Boolean b : readyStatus) {
			if (b == false) {
				allReady = false;
			}
		}
		return allReady;
	}
	
	public Semaphore getSettingsMutex(){
		return this.settingsMutex;
	}
	
	public void setPlayerName(final String name, final int ID){
		//ids are only given to 2 players starting at 1
		if(ID == 1){
			settings.setPlayer1Name(name);
		}else if(ID == 2){
			settings.setPlayer2Name(name);
		}
	}

}
