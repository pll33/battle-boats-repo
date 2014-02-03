package model;

import java.io.Serializable;

/**
 * A class representing a move (x and y coordinates) on a Board. This class is also Serializable so it can be sent over a network.
 *
 */
public class Move implements Serializable {

	private static final long serialVersionUID = -3552137844787073900L;
	
	/**
	 * The x coordinate
	 */
	public int x;
	
	/**
	 * the y coordinate
	 */
	public int y;
	
	/**
	 * Create a move
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public Move(int x, int y){
		this.x = x;
		this.y = y;
	}
}
