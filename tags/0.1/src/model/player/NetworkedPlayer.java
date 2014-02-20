package model.player;

import model.Game;

/**
 * A class to represent a second human player being played over the network. If the game is set to multiplayer, this class will be used.
 *
 */
public class NetworkedPlayer extends Player {

	public NetworkedPlayer(final Game game, final String name) {
		super(game, name, false);
		this.playerLocalToServer = false;
	}

}
