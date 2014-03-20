package controller;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;

import utils.Logger;
import core.Constants;
import core.GameSettings;
import core.MoveState;
import core.PlayerType;
import core.SquareState;
import model.Boat;
import model.Game;
import model.Move;
import model.server.Server;

/**
 * Class that controls the game itself. Handles starting/stopping etc.
 * 
 */
public class GameController extends Thread{

	/**
	 * The game.
	 */
	private Game game;
	private boolean myTurn;
	
	private Server server;

	public GameController(final boolean hostGame, final GameSettings settings, final String IP) throws UnknownHostException, IOException {
		this(hostGame, settings, IP, PlayerType.HUMAN);		
	}
	
	public GameController(final boolean hostGame, final GameSettings settings, final String IP, final PlayerType playerType) throws UnknownHostException, IOException{
		if (hostGame) {
			this.myTurn = true;
			this.server = createServer(settings);
			if(settings.isVsComputer() && playerType == PlayerType.HUMAN){
				Logger.log("Creating computer controller", this);
				new ComputerController(settings).start();
			} 
		} else {
			this.server = null;
			myTurn = false;
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
			//check if won, if so game over
			//get move from UI and pass to this
			if(myTurn){
				Move move;
				while ((move = game.getPlayerMove()) == null){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				game.getPlayer().getMovedBoard().move(move);
				//TODO: submit move here
				SquareState state = game.sendMove(move);
				if(state == SquareState.WIN){
					gameOn = false;
					//we win;
				}
				game.getPlayer().getMovedBoard().setState(move,state);
				//gameOn = !game.win();
				myTurn = false;
			}
			
			//get other player move
			SquareState state = game.getGameBoard().move(game.getOtherPlayerMove());
			if(checkLose()){
				state = SquareState.WIN;
				gameOn = false;
			}
			game.sendState(state);			
			myTurn = true;
		}
	}
	
	private boolean checkLose(){
		for(Boat b: game.getGameBoard().getBoats()){
			if(!b.isSunk()){
				return false;
			}
		}
		return true;
	}

	public Game getGame(){
		return this.game;
	}

	public boolean getTurn() { return myTurn; }
	@Override
	public void run() {
		startGame();
	}
}
