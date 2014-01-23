package model;

import java.util.ArrayList;

import model.server.Server;
import core.MoveState;

public class Game {

	private Player player1;
	private Player player2;
	
	private int width, height;
	
	private ArrayList<Integer> boatSizes;
	
	private Server server;
	
	public Game(String player1name, String player2name,
				int width, int height, ArrayList<Integer> boatSizes){
		
		this(player1name,player2name,width,height,boatSizes,false);
	}
	
	public Game(String player1name, String player2name, int width, int height, 
				ArrayList<Integer> boatSizes, boolean multiplayer){
		
		int port = 8080;
		this.server = new Server(port);
		this.server.start(); //start the server for accepting connections
		
		this.boatSizes = boatSizes;
		player1 = new HumanPlayer(this,player1name);
		if(multiplayer){
			player2 = new NetworkedPlayer(this,player2name);
		} else {
			player2 = new ComputerPlayer(this);
		}
		this.width = width;
		this.height = height;
	}
	
	public MoveState makeMove(Player movingPlayer, Move move){
		if(player1.equals(movingPlayer)){
			return player2.update(move);
		}
		return player1.update(move);
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
	
	public ArrayList<Integer> getBoatSizes(){
		return boatSizes;
	}
	public Player getP1(){
		return player1;
	}
	public Player getP2(){
		return player2;
	}
}
