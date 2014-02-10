package utils;

import java.awt.Point;

import core.Orientation;

public class OrientationHelper {

	public static Orientation getOrientationFromOrigin(Point origin, Point square) {
		if (square.equals(origin)) {
			return null;
		} else if (square.x == origin.x) {
			return (square.y < origin.y) ? Orientation.NORTH : Orientation.SOUTH;
		} else if (square.y == origin.y) {
			return (square.x < origin.x) ? Orientation.WEST : Orientation.EAST;
		} else {
			return null;
		}
	}
}
