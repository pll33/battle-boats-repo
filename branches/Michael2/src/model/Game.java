package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;

import utils.Logger;
import model.player.ComputerPlayer;
import model.player.HumanPlayer;
import model.player.Player;
import core.Constants;
import core.GameSettings;
import core.PlayerName;
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
	
	private boolean joiningPlayer;

	public Game(final String IP, final PlayerType playerType, final boolean joiningPlayer) {
		this.IP = IP;
		this.settingsMutex = new Semaphore(0);
		this.ready = false;
		this.joiningPlayer = joiningPlayer;
		connectToServer();
		
		//broadcast player name
		
		
		this.settings = recieveGameSettings();
		Logger.log("Recieved Settings", this);
		Logger.log("Player 1 name: " + settings.getPlayer1Name() + " Player 2 name: " + settings.getPlayer2Name(), this);

		gameBoard = new Board(settings.getWidth(), settings.getHeight());

		if (playerType == PlayerType.HUMAN) {
			this.player = new HumanPlayer(joiningPlayer == false ? settings.getPlayer1Name() : settings.getPlayer2Name());
		} else {
			this.player = new ComputerPlayer();
		}

	}
	
	public void sendPlayerName(final String name){
		try {
			out.writeObject(new PlayerName(name));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void waitForAllReady() {

		Logger.log("Waiting for ready", this);
		Object tmp = null;
		do {
			
			try {
				tmp = in.readObject();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}

		} while (!(tmp instanceof ReadyIndicator));

		this.ready = true;
		Logger.log("All Games Ready", this);
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

		Logger.log("Waiting for settings", this);

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
			Logger.log("Attempting to connect to Server", this);
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
		Logger.log("Setting ready for player", this);
		try {
			out.writeObject(new ReadyIndicator());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//waitForAllReady should get called when the "waiting for other play" screen comes up
		//waitForAllReady();
		// this.ready = true;
	}
}
