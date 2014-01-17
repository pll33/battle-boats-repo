package model;

import core.MoveState;

public class Game {

	private Player player1;
	private Player player2;
	
	private int width, height;
	
	public Game(int width, int height){
		this(width,height,false);
		
	}
	public Game(int width, int height, boolean multiplayer){
		player1 = new HumanPlayer(this);
		if(multiplayer){
			player2 = new HumanPlayer(this);
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
}
