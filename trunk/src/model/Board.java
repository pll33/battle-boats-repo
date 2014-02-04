package model;

import java.util.ArrayList;

import core.MoveState;
import core.Orientation;
import core.SquareState;

/**
 * A class representing the Board(s) of each Player.
 *
 */
public class Board {

	/**
	 * A list of boats placed on this Board.
	 */
	private ArrayList<Boat> boats;
	
	/**
	 * The Board tiles represented as SquareStates
	 */
	private ArrayList<ArrayList<SquareState>> boardState;
	
	/**
	 * The width of the board.
	 */
	int width;
	
	/**
	 * The height of the board.
	 */
	int height;
	
	/**
	 * Create an empty Board with the specified width and height.
	 * @param width The width of the board.
	 * @param height The height of the board.
	 */
	public Board(int width, int height){
		this.width = width;
		this.height = height;
		boats = new ArrayList<Boat>();
		reset();
	}
	
	/**
	 * Create a copy of a Board.
	 * @param other The Board to be copied.
	 */
	public Board(Board other){
		this.boats = other.boats;
		this.boardState = other.boardState;
		this.width = other.width;
		this.height = other.height;
	}
	
	/**
	 * Does a move on this Board and updates the Board accordingly.
	 * @param move The Move to execute
	 * @return Whether the move was a Hit or a Miss.
	 */
	public MoveState move(Move move){
		SquareState state = boardState.get(move.x).get(move.y);
		
		if(state == SquareState.HIT){
			boardState.get(move.x).set(move.y, SquareState.HIT);
			return MoveState.HIT;
		}else if(state == SquareState.MISS){
			boardState.get(move.x).set(move.y, SquareState.MISS);
			return MoveState.MISS;
		}else{
			return MoveState.MISS;
		}
		
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
			//Sets every space occupied by boat to boat
			if(!addBoat(boat)){
				reset();
				return false;
			}
		} 
		return true;
	}
	
	/**
	 * Sets the whole board to empty.
	 */
	private void reset(){
		boardState = new ArrayList<ArrayList<SquareState>>(width);
		//Set whole boardState to empty
		for(int i=0; i<width; i++){
			ArrayList<SquareState> temp = new ArrayList<SquareState>(height);
			for(int j=0; j<height;j++){
				temp.add(SquareState.EMPTY);
			}
			boardState.add(temp);
		}
	}

	/**
	 * Determines the valid Orientations for a Boat being placed at the given (x,y) coordinates of the given size.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param size The size of the Boat
	 * @return A List<Orientation> of valid orientations for a Boat being placed at (x,y) of the given size.
	 */
	public ArrayList<Orientation> getValidOrientations(int x, int y, int size){
		ArrayList<Orientation> orientations = new ArrayList<Orientation>(2);
		Boat tempBoat = new Boat(x,y,Orientation.NORTH,size);
		if(addBoat(tempBoat)){
			orientations.add(Orientation.NORTH);
			removeBoat(tempBoat);
		}
		tempBoat = new Boat(x,y,Orientation.SOUTH,size);
		if(addBoat(tempBoat)){
			orientations.add(Orientation.SOUTH);
			removeBoat(tempBoat);
		}
		tempBoat = new Boat(x,y,Orientation.EAST,size);
		if(addBoat(tempBoat)){
			orientations.add(Orientation.EAST);
			removeBoat(tempBoat);
		}
		tempBoat = new Boat(x,y,Orientation.WEST,size);
		if(addBoat(tempBoat)){
			orientations.add(Orientation.WEST);
			removeBoat(tempBoat);
		}
		
		return orientations;
	}
	
	public boolean removeBoat(Boat boat){
		for(Move move : boat.getSquares()){
			try{
				if(boardState.get(move.x).get(move.y) != SquareState.BOAT){
					return false;
				}
			} catch (IndexOutOfBoundsException e){
				return false;
			}
		} for(Move move : boat.getSquares()){
			boardState.get(move.x).set(move.y, SquareState.EMPTY);
		}
		return true;
	}
	
	/**
	 * If possible, places the given Boat on the Board
	 * @param boat The Boat to places
	 * @return true if the Boat was successfully placed, false otherwise.
	 */
	public boolean addBoat(Boat boat) {
		for(Move move:boat.getSquares()){
			try{
				if(boardState.get(move.x).get(move.y) != SquareState.EMPTY){
					return false;
				}
			} catch (IndexOutOfBoundsException e){
				return false;
			}
		}
		for(Move move:boat.getSquares()){
			boardState.get(move.x).set(move.y, SquareState.BOAT);
		}
		this.boats.add(boat);
		return true;
	}
	
}
