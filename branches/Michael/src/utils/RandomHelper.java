package utils;

import java.util.Random;

import core.Orientation;

public class RandomHelper {

	public static Orientation getRandomOrientation(Random rand){
		int randInt = rand.nextInt(4);
		if(randInt == 0){
			return Orientation.NORTH;
		} else if(randInt == 1) {
			return Orientation.SOUTH;
		} else if(randInt == 2) {
			return Orientation.EAST;
		} else {
			return Orientation.WEST;
		}
	}	
}
