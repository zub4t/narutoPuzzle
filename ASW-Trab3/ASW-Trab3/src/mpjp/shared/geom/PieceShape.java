package mpjp.shared.geom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of the boundary of a piece as an initial Point followed by a 
 * list of Segment.
 * Shapes are typically created using chaining using addSegment(Segment). 
 * This method returns the instance itself and thus can be used as context 
 * to another invocation of the same method.
 */
public class PieceShape implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Segment> segments = new ArrayList<>();
	private Point startPoint;

     /**
     * Create an empty piece shape.
     */
	public PieceShape() {
	}
    
     /**
     * Create a piece shape with an initial point. Other segments can be 
     * subsequently added.
     * 
     * @param startPoint
     */
	public PieceShape(Point startPoint) {
		this.startPoint = startPoint;
	}

     /**
     * Create a complete shape from an initial point and a list of segments.
     * 
     * @param startPoint
     * @param segments
     */
	public PieceShape(Point startPoint, List<Segment> segments) {
		this.startPoint = startPoint;
		this.segments = segments;
	}

	public List<Segment> getSegments() {
		return this.segments;
	}

	public void setSegments(List<Segment> segments) {
		this.segments = segments;
	}

	public Point getStartPoint() {
		return this.startPoint;
	}

	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	public PieceShape addSegment(Segment segment) {
		this.segments.add(segment);
		return this;
	}

}
