package model;

import java.io.Serializable;

public class Move implements Serializable {

	private static final long serialVersionUID = -3552137844787073900L;
	public int x;
	public int y;
	public Move(int x, int y){
		this.x = x;
		this.y = y;
	}
}
