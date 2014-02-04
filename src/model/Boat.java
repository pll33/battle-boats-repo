package model;

import java.util.ArrayList;

import core.Orientation;

/**
 * A class representing a Boat that gets placed on the Board.
 *
 */
public class Boat {

	/**
	 * The size of the Boat, typically between 2 and 5 inclusive.
	 */
	private int size;
	
	/**
	 * The x coordinate of the Boat (the farthest left part of the Boat).
	 */
	private int x;
	
	/**
	 * The y coordinate of the Boat (the top part of the Boat).
	 */
	private int y;
	
	/**
	 * The orientation (Horizontal or Vertical) of the Boat.
	 */
	private Orientation orientation;
	
	/**
	 * The number of times this Boat has been hit by an enemy attack.
	 */
	private int hitCount;
	
	/**
	 * Whether this Boat has been placed on a Board yet or not.
	 */
	private boolean onBoard;
	
	private ArrayList<Move> squares = null;
	
	/**
	 * Create a Boat.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param orientation The orientation of the boat
	 * @param size the size of the boat.
	 */
	public Boat(int x, int y, Orientation orientation, int size){
		onBoard = false;
		this.x = x;
		this.y = y;
		this.orientation = orientation;
		this.size = size;
		this.hitCount = 0;
	}
	
	/**
	 * Whether this Boat has been sunk
	 * @return true when <code>hitCount == size</code>.
	 */
	public boolean isSunk(){
		return hitCount == size;
	}
	
	/**
	 * 
	 * @return The size of the boat
	 */
	public int getSize(){
		return size;
	}
	
	/**
	 * Gets all the squares (tiles) that this Boat is on.
	 * @return A List<Move> of all the squares (tiles) that the Boat is on.
	 */
	public ArrayList<Move> getSquares(){
		
		if(squares != null){
			return squares;
		}
		squares = new ArrayList<Move>(size);
		squares.add(new Move(x,y));
		try{
			if(orientation == Orientation.NORTH){
				for(int i=1;i<size;i++){
					squares.add(new Move(x,y-i));
				}
			} else if(orientation == Orientation.SOUTH) {
				for(int i=1;i<size;i++){
					squares.add(new Move(x,y+i));
				}
			}
			else if(orientation == Orientation.EAST) {
				for(int i=1;i<size;i++){
					squares.add(new Move(x+i,y));
				}
			} else {
				for(int i=1;i<size;i++){
					squares.add(new Move(x-i,y));
				}
			}
		} catch (IndexOutOfBoundsException e){
			System.err.println("ERROR: Bad Orientation Given");
			e.printStackTrace();
		}
		return squares;
	}
	
	
}
