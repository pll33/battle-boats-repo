package utils;

import java.util.Random;

import model.Board;
import model.Move;
import core.Orientation;
import core.SquareState;

public class RandomHelper {

	public static Orientation getRandomOrientation(Random rand) {
		int randInt = rand.nextInt(4);
		if (randInt == 0) {
			return Orientation.NORTH;
		} else if (randInt == 1) {
			return Orientation.SOUTH;
		} else if (randInt == 2) {
			return Orientation.EAST;
		} else {
			return Orientation.WEST;
		}
	}

	public static Move getRandomValidMove(final Random rand, final Board board) {

		int randX;
		int randY;
		SquareState ss;

		do {
			randX = rand.nextInt(board.getWidth());
			randY = rand.nextInt(board.getHeight());
			ss = board.getSquareState(randX, randY);
		} while (ss == SquareState.BOAT || ss == SquareState.HIT);

		return new Move(randX, randY);
	}
}
