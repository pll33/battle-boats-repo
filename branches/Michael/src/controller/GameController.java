package controller;

import java.util.concurrent.Semaphore;

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

	public GameController(final boolean hostGame, final GameSettings settings) {
		this(hostGame, settings, PlayerType.HUMAN);		
	}
	
	public GameController(final boolean hostGame, final GameSettings settings, final PlayerType playerType){
		if (hostGame) {
			this.server = createServer(settings);
		} else {
			this.server = null;
		}
		
		System.out.println(playerType);

		if(settings.isVsComputer() && playerType == PlayerType.HUMAN){
			new ComputerController(settings).start();
		}
		
		//change IP to not be hard coded
		this.game = new Game(Constants.LOCAL_IP, playerType);
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
}
