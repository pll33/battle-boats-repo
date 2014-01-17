package model;

import core.Orientation;

public class Boat {

	private int size;
	private int x, y;
	private Orientation orientation;
	
	private boolean onBoard;
	
	public Boat(int x, int y, Orientation orientation, int size){
		onBoard = false;
		this.x = x;
		this.y = y;
		this.orientation = orientation;
		this.size = size;
	}
	
}
