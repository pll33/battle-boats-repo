package controller;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;

import utils.Logger;
import view.ThreadedDialog;
import core.Constants;
import core.GameSettings;
import core.MoveState;
import core.PlayerType;
import core.SquareState;
import model.Board;
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
	private boolean win;
	private boolean lose;
	
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
		win = false;
		lose = false;
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
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public void startGame() throws ClassNotFoundException, IOException {
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
				//game.getPlayer().getMovedBoard().move(move);
				SquareState state = game.sendMove(move);
				if(state == SquareState.WIN){
					gameOn = false;
					System.out.println("A WINNER IS YOU");
					//we win;
					win = true;
					ThreadedDialog td = new ThreadedDialog("YOU WIN");
					td.start();
				}
				game.getPlayer().getMovedBoard().setState(move,state);

				myTurn = false;
			}
			
			//get other player move
			SquareState state = game.getGameBoard().move(game.getOtherPlayerMove());
			if(checkLose()){
				state = SquareState.WIN;
				gameOn = false;
				lose = true;

				System.out.println("A WINNER IS THEM");
				ThreadedDialog td = new ThreadedDialog("YOU LOSE");
				td.start();
			}
			// send win state to other player
			game.sendState(state);			
			myTurn = true;
		}
	}
	
	private boolean checkLose(){
		return game.getGameBoard().allSunk();
	}

	public Game getGame(){
		return this.game;
	}

	public boolean getWin(){
		return win;
	}
	
	public boolean getLose(){
		return lose;
	}
	
	public boolean getTurn() { return myTurn; }
	
	@Override
	public void run() {
		try {
			startGame();
		} catch (ClassNotFoundException | IOException e) {
			win = true;
			//System.exit(0);
			//e.printStackTrace();
		}
	}
	public void setGameBoard(Board b){
		game.setGameBoard(b);
	}
	
}
