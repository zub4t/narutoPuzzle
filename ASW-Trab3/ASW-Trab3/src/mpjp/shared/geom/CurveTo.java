package mpjp.shared.geom;

import java.io.Serializable;

/**
 * A segment of a cubic (Bezier) curve from the previous point to the given one.
 * The given control points defines 2 lines, one with the starting point and the
 * first control point, an the other with second control point and the end 
 * point. These lines are tangents to the cubic curve, respectively on the 
 * starting and end points.
 */
public class CurveTo implements Segment, Serializable {

	private static final long serialVersionUID = -7139429628577137964L;
	private Point controlPoint1;
	private Point controlPoint2;
	private Point endPoint;

    /**
     * An empty instance
     */
	public CurveTo() {

	}
    
    /**
     * A Bezier segment with given control points and end point
     * 
     * @param controlPoint1
     * @param controlPoint2
     * @param endPoint
     */
	public CurveTo(Point controlPoint1, Point controlPoint2, Point endPoint) {
		this.controlPoint1 = controlPoint1;
		this.controlPoint2 = controlPoint2;
		this.endPoint = endPoint;
	}

	public Point getControlPoint1() {
		return controlPoint1;
	}

	public void setControlPoint1(Point controlPoint1) {
		this.controlPoint1 = controlPoint1;
	}

	public Point getControlPoint2() {
		return controlPoint2;
	}

	public void setControlPoint2(Point controlPoint2) {
		this.controlPoint2 = controlPoint2;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}

}
