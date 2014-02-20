package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;

import model.player.ComputerPlayer;
import model.player.HumanPlayer;
import model.player.Player;
import core.Constants;
import core.GameSettings;
import core.PlayerType;
import core.ReadyIndicator;

public class Game {

	private GameSettings settings;
	private String IP;

	/**
	 * The board on which the players Boats are placed
	 */
	private Board gameBoard;

	/**
	 * The socket used to communicated with the Server
	 */
	private Socket socket;

	/**
	 * Stream used for sending messages (Objects) to the server
	 */
	private ObjectOutputStream out;

	/**
	 * Stream used for receiving messages (Objects) from the server.
	 */
	private ObjectInputStream in;

	private Player player;

	private Semaphore settingsMutex;

	private boolean ready;

	public Game(final String IP, final PlayerType playerType) {
		this.IP = IP;
		this.settingsMutex = new Semaphore(0);
		this.ready = false;
		connectToServer();
		this.settings = recieveGameSettings();
		logMessage("Recieved Settings");

		gameBoard = new Board(settings.getWidth(), settings.getHeight());

		if (playerType == PlayerType.HUMAN) {
			this.player = new HumanPlayer("Human");
		} else {
			this.player = new ComputerPlayer();
		}

	}
	
	private void logMessage(final String message){
		System.out.println("Game: " + message);
	}

	private void waitForAllReady() {

		logMessage("Waiting for ready");
		Object tmp = null;
		do {
			
			try {
				tmp = in.readObject();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}

		} while (!(tmp instanceof ReadyIndicator));

		this.ready = true;
		logMessage("All Games Ready");
	}

	public GameSettings getGameSettings() {
		final GameSettings settings;
		try {
			settingsMutex.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		settings = this.settings;
		settingsMutex.release();
		return settings;
	}

	private GameSettings recieveGameSettings() {
		Object tmp = null;

		logMessage("Waiting for settings");

		do {
			try {
				tmp = in.readObject();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		} while (!(tmp instanceof GameSettings));

		settingsMutex.release();
		return (GameSettings) tmp;
	}

	private void connectToServer() {
		try {
			logMessage("Attempting to connect to Server");
			this.socket = new Socket(IP, Constants.PORT);
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Board getGameBoard() {
		return this.gameBoard;
	}

	public Player getPlayer() {
		return this.player;
	}

	public Move getPlayerMove() {
		return player.getMove();
	}

	public void setReady() {
		logMessage("Setting ready for player");
		try {
			out.writeObject(new ReadyIndicator());
		} catch (IOException e) {
			e.printStackTrace();
		}
		waitForAllReady();
		// this.ready = true;
	}
}
