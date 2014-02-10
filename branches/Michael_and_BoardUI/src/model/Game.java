package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import model.player.ComputerPlayer;
import model.player.HumanPlayer;
import model.player.Player;
import core.Constants;
import core.GameSettings;
import core.PlayerType;

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

	public Game(final String IP, final PlayerType playerType) {
		this.IP = IP;
		connectToServer();
		this.settings = recieveGameSettings();
		System.out.println("Recieved Settings");

		gameBoard = new Board(settings.getWidth(), settings.getHeight());

		if (playerType == PlayerType.HUMAN) {
			this.player = new HumanPlayer("Human");
		} else {
			this.player = new ComputerPlayer();
		}
	}
	
	public GameSettings getGameSettings(){
		return this.settings;
	}

	private GameSettings recieveGameSettings() {
		Object tmp = null;

		System.out.println("Waiting for settings");

		do {
			try {
				tmp = in.readObject();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		} while (!(tmp instanceof GameSettings));

		return (GameSettings) tmp;
	}

	private void connectToServer() {
		try {
			System.out.println("Attempting to connect to Server");
			this.socket = new Socket(IP, Constants.PORT);
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Board getGameBoard(){
		return this.gameBoard;
	}
	
	public Player getPlayer(){
		return this.player;
	}
	
	public Move getPlayerMove(){
		return player.getMove();
	}

}
