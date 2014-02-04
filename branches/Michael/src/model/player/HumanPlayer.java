package model.player;

import model.Game_Old;

/**
 * A class representing a local human Player.
 *
 */
public class HumanPlayer extends Player {

	public HumanPlayer(Game_Old game, String playerName) {
		super(game, playerName);
	}
	
	public HumanPlayer(Game_Old game, String playerName, boolean multiplayer) {
		super(game, playerName, multiplayer);
	}
}
