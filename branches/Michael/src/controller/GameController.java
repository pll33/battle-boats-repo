package controller;

import java.util.ArrayList;
import java.util.List;

import core.Constants;
import core.GameSettings;
import model.Game;
import model.Game_Old;
import model.server.Server;
import model.server.Server;

/**
 * Class that controls the game itself. Handles starting/stopping etc.
 * 
 */
public class GameController {

	/**
	 * The game.
	 */
	private Game game;

	private Server server;

	public GameController(final boolean hostGame, final String player1Name,
			final String player2Name, final GameSettings settings) {
		
		if (hostGame) {
			this.server = createServer();
		} else {
			this.server = null;
		}
		
		this.game = new Game(settings, Constants.LOCAL_IP);
		
	}

	private Server createServer() {
		Server server = new Server(Constants.PORT);
		server.start();
		
		return server;
	}

	/**
	 * The main game loop
	 */
	public void startGame() {
		boolean gameOn = true;
		while (gameOn) {

		}
	}
}
