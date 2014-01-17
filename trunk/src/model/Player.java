package model;

import java.util.ArrayList;

import core.MoveState;

public abstract class Player {
	
	protected Board gameBoard;
	protected Board movedBoard;
	protected Game game;
	
	public Player(Game game){
		movedBoard = new Board(game.getWidth(), game.getHeight());
		gameBoard = new Board(game.getWidth(), game.getHeight());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((game == null) ? 0 : game.hashCode());
		result = prime * result
				+ ((gameBoard == null) ? 0 : gameBoard.hashCode());
		result = prime * result
				+ ((movedBoard == null) ? 0 : movedBoard.hashCode());
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
		Player other = (Player) obj;
		if (game == null) {
			if (other.game != null)
				return false;
		} else if (!game.equals(other.game))
			return false;
		if (gameBoard == null) {
			if (other.gameBoard != null)
				return false;
		} else if (!gameBoard.equals(other.gameBoard))
			return false;
		if (movedBoard == null) {
			if (other.movedBoard != null)
				return false;
		} else if (!movedBoard.equals(other.movedBoard))
			return false;
		return true;
	}

	public boolean makeMove(Move move) {
		MoveState state = game.makeMove(this,move);
		if(state == MoveState.INVALID){
			return false;
		}
		
		return false;
	}
	
	public MoveState update(Move move){
		return gameBoard.move(move);
	}
	
	public Board getGameBoard(){
		return gameBoard;
	}
	
	public ArrayList<MoveState> updatePosition(ArrayList<Move> moves){
		ArrayList<MoveState> list = new ArrayList<MoveState>();
		for (Move move: moves){
			MoveState state = gameBoard.move(move);
			list.add(state);
		}
		return list;
	}
	
	
}
