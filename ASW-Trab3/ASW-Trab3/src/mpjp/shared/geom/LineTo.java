package mpjp.shared.geom;

import java.io.Serializable;

/**
 * A line segment from the previous point to the given one.
 */
public class LineTo implements Serializable, Segment {

	private static final long serialVersionUID = -645577220443284205L;
	private Point endPoint;

    /**
     * An empty instance.
     */
	public LineTo() {

	}
	
    /**
     * A line segment to the given end point.
     * 
     * @param endPoint
     */
	public LineTo(Point endPoint) {
		this.endPoint = endPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}

}
