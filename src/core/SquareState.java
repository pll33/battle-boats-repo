package core;

import java.io.Serializable;

/**
 * An enum of the states that any square (tile) on the board may be in.
 *
 */
public enum SquareState implements Serializable{

	EMPTY,BOAT,MISS,HIT,WIN
	
}
