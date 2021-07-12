package mpjp.game;

import mpjp.shared.HasPoint;
import mpjp.shared.geom.Point;

/**
 * A class that hold an id and a point. This class is used do represent a piece
 * in a quadTree
 * 
 */
public class Tuplo implements HasPoint, Comparable<Tuplo> {
	Point point;
	public int peiceId;

	public Tuplo(int peiceId, Point point) {
		this.peiceId = peiceId;
		this.point = point;
	}

	@Override
	public double getX() {
		return this.point.getX();
	}

	@Override
	public double getY() {
		return this.point.getY();
	}

	@Override
	public String toString() {
		return "Tuplo [point=" + point + ", id=" + peiceId + "]";
	}

	@Override
	public int compareTo(Tuplo o) {
		Point p = (Point) o.point;
		if (Point.distanceFromOrigin(p) > Point.distanceFromOrigin(this.point)) {
			return -1;
		}
		if (Point.distanceFromOrigin(p) < Point.distanceFromOrigin(this.point)) {
			return 1;
		}
		return 0;
	}

}
