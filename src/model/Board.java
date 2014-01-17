package model;

import java.util.ArrayList;

import core.MoveState;
import core.SquareState;

public class Board {

	private ArrayList<Boat> boats;
	private ArrayList<ArrayList<SquareState>> boardState;
	
	public Board(int width, int height){
		boardState = new ArrayList<ArrayList<SquareState>>(width);
		for(int i=0; i<width; i++){
			ArrayList<SquareState> temp = new ArrayList<SquareState>(height);
			for(int j=0; j<height;j++){
				temp.set(j,SquareState.EMPTY);
			}
			boardState.add(temp);
		}
	}
	
	public MoveState move(Move move){
		SquareState state = boardState.get(move.x).get(move.y);
		if(state == SquareState.HIT || state == SquareState.MISS){
			return MoveState.INVALID;
		}
		if(state == SquareState.BOAT){
			boardState.get(move.x).set(move.y, SquareState.HIT);
			return MoveState.HIT;
		}
		boardState.get(move.x).set(move.y, SquareState.MISS);
		return MoveState.MISS;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((boardState == null) ? 0 : boardState.hashCode());
		result = prime * result + ((boats == null) ? 0 : boats.hashCode());
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
		Board other = (Board) obj;
		if (boardState == null) {
			if (other.boardState != null)
				return false;
		} else if (!boardState.equals(other.boardState))
			return false;
		if (boats == null) {
			if (other.boats != null)
				return false;
		} else if (!boats.equals(other.boats))
			return false;
		return true;
	}

	public boolean addBoats(ArrayList<Boat> boats){
		for(Boat boat:boats){
			this.boats.add(boat);
			//TODO: update SquareState in board based on boat placement
		} 
		return true;
	}
	
}
