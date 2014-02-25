package controller;

import java.util.concurrent.Semaphore;

import utils.Logger;
import core.Constants;
import core.GameSettings;
import core.PlayerType;
import model.Game;
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

	public GameController(final boolean hostGame, final GameSettings settings, final String IP) {
		this(hostGame, settings, IP, PlayerType.HUMAN);		
	}
	
	public GameController(final boolean hostGame, final GameSettings settings, final String IP, final PlayerType playerType){
		if (hostGame) {
			this.server = createServer(settings);
			if(settings.isVsComputer() && playerType == PlayerType.HUMAN){
				Logger.log("Creating computer controller", this);
				new ComputerController(settings).start();
			}
		} else {
			this.server = null;
		}
		
		//change IP to not be hard coded
		this.game = new Game(IP, playerType);
	}

	private Server createServer(final GameSettings settings) {
		//create a locked mutex
		Semaphore mutex = new Semaphore(0);
		
		Server server = new Server(mutex, settings);
		server.start();
		
		//being able to acquire the mutex means the Server is accepting connections
		try {
			mutex.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
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
	
	public Game getGame(){
		return this.game;
	}
}
