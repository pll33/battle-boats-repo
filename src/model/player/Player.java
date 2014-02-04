package model.player;

import core.PlayerType;
import model.Board;

/**
 * An abstract class to represent a Player.
 * 
 */
public abstract class Player {

	/**
	 * The board used to keep track of this Players moves (shots fired).
	 */
	protected Board movedBoard;

	protected String playerName;
	
	protected PlayerType playerType;
	
	public Player(final String playerName, final PlayerType playerType){
		this.playerName = playerName;
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

}
