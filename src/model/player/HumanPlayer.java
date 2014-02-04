package model.player;

import core.PlayerType;

/**
 * A class representing a local human Player.
 *
 */
public class HumanPlayer extends Player {

	public HumanPlayer(String playerName) {
		super(playerName, PlayerType.HUMAN);
	}
}
