package model;

import java.util.ArrayList;

import core.Orientation;

public class Boat {

	private int size;
	private int x, y;
	private Orientation orientation;
	private int hitCount;
	
	private boolean onBoard;
	
	public Boat(int x, int y, Orientation orientation, int size){
		onBoard = false;
		this.x = x;
		this.y = y;
		this.orientation = orientation;
		this.size = size;
		this.hitCount = 0;
	}
	
	public boolean isSunk(){
		return hitCount == size;
	}
	
	public int getSize(){
		return size;
	}
	
	public ArrayList<Move> getSquares(){
		ArrayList<Move> squares = new ArrayList<Move>(size);
		squares.add(new Move(x,y));
		if(orientation == Orientation.HORIZONTAL){
			for(int i=1;i<size;i++){
				squares.add(new Move(x+i,y));
			}
		} else {
			for(int i=1;i<size;i++){
				squares.add(new Move(x,y+i));
			}
		}
		return squares;
	}
	
	
}
