package mpjp.game;

import java.io.Serializable;

/**
 * Directions in the plane. Each direction can be translated to a signal
 * variation in both the X and Y axis, using the getSignalX() and getSignalY().
 * The methods return a value in { -1, 0, 1}.
 *
 */
public enum Direction implements Serializable, Comparable<Direction> {

	EAST, NORTH, SOUTH, WEST;

	public int getSignalX() {
		switch (this) {
		case EAST:
			return 1;
		case SOUTH:
			return 0;
		case WEST:
			return -1;
		case NORTH:
			return 0;
		default:
			throw new IllegalArgumentException("Unexpected value: " + this);
		}
	}
	public int getSignalY() {
		switch (this) {
		case EAST:
			return 0;
		case SOUTH:
			return 1;
		case WEST:
			return 0;
		case NORTH:
			return -1;
		default:
			throw new IllegalArgumentException("Unexpected value: " + this);
		}
	}
	public static Direction valueOf​(String name) {
		switch (name) {
		case "EAST":
			return EAST;
		case "SOUTH":
			return SOUTH;
		case "WEST":
			return WEST;
		case "NORTH":
			return NORTH;
		default:
			throw new IllegalArgumentException("Unexpected value: " + name);
		}

	}
}
