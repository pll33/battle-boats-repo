package model.player;

import model.Game;

/**
 * A class representing a local human Player.
 *
 */
public class HumanPlayer extends Player {

	public HumanPlayer(Game game, String playerName) {
		super(game, playerName);
	}
	
	public HumanPlayer(Game game, String playerName, boolean multiplayer) {
		super(game, playerName, multiplayer);
	}
}
