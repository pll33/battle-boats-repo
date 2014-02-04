package model.player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import model.Board;
import model.Boat;
import model.Game;
import model.Move;
import core.MoveState;

/**
 * An abstract class to represent a Player.
 * 
 */
public abstract class Player {

	/**
	 * The board on which this Player has their pieces placed.
	 */
	protected Board gameBoard;

	/**
	 * The board used to keep track of this Players moves (shots fired).
	 */
	protected Board movedBoard;

	/**
	 * The game in which this player is playing.
	 */
	protected Game game;
	
	/**
	 * The socket used to communicated with the Server
	 */
	protected Socket socket;
	
	/**
	 * Stream used for sending messages (Objects) to the server
	 */
	protected ObjectOutputStream out;
	
	/**
	 * Stream used for receiving messages (Objects) from the server.
	 */
	protected ObjectInputStream in;
	
	protected boolean playerLocalToServer;

	private String playerName;

	/**
	 * Abstract constructor for Player that has
	 * <code>connectToServer = true</code>.
	 * 
	 * @param game
	 *            The game that the player is playing in
	 * @param name
	 *            The name of the player
	 */
	public Player(final Game game, final String name) {
		this(game, name, true);
	}

	/**
	 * Abstract constructor for Player.
	 * 
	 * @param game
	 *            The game that the player is playing in
	 * @param name
	 *            The name of the player
	 * @param If
	 *            set to true, the player will attempt to connect to the Battle
	 *            Boats server for the game.
	 */
	public Player(final Game game, final String name,
			final boolean connectToServer) {
		movedBoard = new Board(game.getWidth(), game.getHeight());
		gameBoard = new Board(game.getWidth(), game.getHeight());
		playerName = name;
		this.playerLocalToServer = true;

		if (connectToServer == true) {
			logMessage("attempting to connect to server");
			// will have to change this from being hardcoded
			String ip = "127.0.0.1";
			int port = 8080;

			try {
				this.socket = new Socket(ip, port);
				this.out = new ObjectOutputStream(socket.getOutputStream());
				this.in = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Utility function for logging from Player class
	 * 
	 * @param message
	 *            The message to be logged
	 */
	protected void logMessage(final String message) {
		System.out.println(playerName + ": " + message);
	}

	public boolean isPlayerLocalToServer(){
		return playerLocalToServer;
	}
	
	/**
	 * Places the list of boats on the game board
	 * 
	 * @param boats
	 *            The list of boats
	 * @return true if all boats were successfully places on the board.
	 */
	public boolean placeBoats(final ArrayList<Boat> boats) {
		return gameBoard.addBoats(boats);
	}

	/**
	 * Attempts to make the specified move on the gameboard by asking Game if
	 * the move is valid.
	 * 
	 * @param move
	 *            The Move to be made
	 * @return true if a valid move, otherwise false.
	 */
	public boolean makeMove(Move move) {
		MoveState state = game.makeMove(this, move);
		if (state == MoveState.INVALID) {
			return false;
		}

		return true;
	}

	/**
	 * Updates the gameboard with the specified Move
	 * 
	 * @param move
	 *            The Move used to updated the board
	 * @return The results of the Move (Hit, Miss, Invalid)
	 */
	public MoveState update(Move move) {
		return gameBoard.move(move);
	}

	/**
	 * 
	 * @return The players GameBoard
	 */
	public Board getGameBoard() {
		return gameBoard;
	}

	/**
	 * Attempts to update the GameBoard with the specified list of Moves
	 * 
	 * @param moves
	 *            A List<Move> to attempt
	 * @return A List<MoveState> of the results, in order, of each Move
	 *         attempted.
	 */
	public ArrayList<MoveState> updatePosition(ArrayList<Move> moves) {
		ArrayList<MoveState> list = new ArrayList<MoveState>();
		for (Move move : moves) {
			MoveState state = gameBoard.move(move);
			list.add(state);
		}
		return list;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((game == null) ? 0 : game.hashCode());
		result = prime * result
				+ ((gameBoard == null) ? 0 : gameBoard.hashCode());
		result = prime * result
				+ ((movedBoard == null) ? 0 : movedBoard.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (game == null) {
			if (other.game != null)
				return false;
		} else if (!game.equals(other.game))
			return false;
		if (gameBoard == null) {
			if (other.gameBoard != null)
				return false;
		} else if (!gameBoard.equals(other.gameBoard))
			return false;
		if (movedBoard == null) {
			if (other.movedBoard != null)
				return false;
		} else if (!movedBoard.equals(other.movedBoard))
			return false;
		return true;
	}

}
