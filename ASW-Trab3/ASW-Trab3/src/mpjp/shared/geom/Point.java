package mpjp.shared.geom;

import java.io.Serializable;

import mpjp.shared.HasPoint;

/**
 * A pair or double coordinates in the plane.
 */
public class Point implements Serializable, HasPoint {
	private static final long serialVersionUID = -5955049541125351948L;
	private double x, y;

    /**
     * Create point at origin 
     */ 
	public Point() {
	    
	}
	
	/**
	 * Create point from coordinates
	 * 
	 * @param x
	 * @param y
	 */
	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		double result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return (int) result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}
	public  static int distanceFromOrigin(Point point) {
	    return (int) Math.sqrt(Math.pow(point.getX(), 2) + Math.pow(point.getY(), 2));
	}

}
