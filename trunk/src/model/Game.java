package model;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import model.player.ComputerPlayer;
import model.player.HumanPlayer;
import model.player.NetworkedPlayer;
import model.player.Player;
import model.server.Server;
import core.MoveState;

public class Game {

	private Player player1;
	private Player player2;
	private Player currentPlayer;

	private int width, height;

	private ArrayList<Integer> boatSizes;

	private Server server;

	public Game(String player1name, String player2name, int width, int height,
			ArrayList<Integer> boatSizes) {

		this(player1name, player2name, width, height, boatSizes, false);
	}
	
	public Game(String player1name, String player2name, int width, int height, ArrayList<Integer> boatSizes, boolean vsComputer, boolean hostGame){
		
		if(vsComputer || hostGame){
			Semaphore mutex = new Semaphore(0); //lock to wait for server to start
			int port = 8080;
			this.server = new Server(port, mutex);
			this.server.start(); // start the server for accepting connections
			
			try {
				mutex.acquire(); //wait for server to start
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(vsComputer){
			player1 = new HumanPlayer(this, player1name);
			player2 = new ComputerPlayer(this);
		}else{
			if(hostGame){
				player1 = new HumanPlayer(this, player1name);
				//wait for second player to connect
			}else{
				//player1 = new NetworkedHumanPlayer();
			}
			player2 = new NetworkedPlayer(this, player2name);
		}
		
	}

	public Game(String player1name, String player2name, int width, int height,
			ArrayList<Integer> boatSizes, boolean multiplayer) {

		Semaphore mutex = new Semaphore(0); //lock to wait for server to start
		int port = 8080;
		this.server = new Server(port, mutex);
		this.server.start(); // start the server for accepting connections
		
		try {
			mutex.acquire(); //wait for server to start
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		player1 = new HumanPlayer(this, player1name);
		
		//if playing against another Human, create a NetworkedPlayer
		if (multiplayer) {
			player2 = new NetworkedPlayer(this, player2name);
		} else {
			player2 = new ComputerPlayer(this);
		}

		this.currentPlayer = player1;
		this.boatSizes = boatSizes;
		this.width = width;
		this.height = height;
	}

	public MoveState makeMove(Player movingPlayer, Move move) {
		if (player1.equals(movingPlayer)) {
			return player2.update(move);
		}
		return player1.update(move);
	}
	
	/**
	 * Switches which player's turn it is. This should be used after a Player is finished with their Move.
	 * @return the new current Player
	 */
	public Player switchCurrentPlayer(){
		Player p = null;
		
		if(currentPlayer.equals(player1)){
			p = player2;
		}else if(currentPlayer.equals(player2)){
			p = player1;
		}
		
		return p;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + ((player1 == null) ? 0 : player1.hashCode());
		result = prime * result + ((player2 == null) ? 0 : player2.hashCode());
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (height != other.height)
			return false;
		if (player1 == null) {
			if (other.player1 != null)
				return false;
		} else if (!player1.equals(other.player1))
			return false;
		if (player2 == null) {
			if (other.player2 != null)
				return false;
		} else if (!player2.equals(other.player2))
			return false;
		if (width != other.width)
			return false;
		return true;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public ArrayList<Integer> getBoatSizes() {
		return boatSizes;
	}

	public Player getP1() {
		return player1;
	}

	public Player getP2() {
		return player2;
	}
}
