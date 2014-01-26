package model.player;

import model.Game;


public class HumanPlayer extends Player {

	public HumanPlayer(Game game, String playerName) {
		super(game, playerName);
	}
	
	public HumanPlayer(Game game, String playerName, boolean multiplayer) {
		super(game, playerName, multiplayer);
	}
}
