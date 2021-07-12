package mpjp.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import mpjp.shared.geom.PieceShape;
import mpjp.shared.geom.Point;

/**
 * Instances of this class provide the static data required to render a jigsaw
 * puzzle on the client side, namely the workspace dimensions (width and
 * height), puzzle dimensions, the image, the pieces' shape and locations in the
 * complete (all pieces connected) puzzle. It does not include the current
 * position of each place piece. This data will typically be transfered only
 * once, when the user starts solving the jigsaw puzzle.
 */
public class PuzzleView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	double workspaceHeight;
	double pieceWidth;
	double puzzleWidth;
	double workspaceWidth;
	double puzzleHeight;
	double pieceHeight;

	String image;
	Date start;

	Map<Integer, Point> locations;
	Map<Integer, PieceShape> shapes;

	/**
	 * An empty instance
	 */
	public PuzzleView() {
		super();
	}

	/**
	 * An instance with date, width and height for the workspace, puzzle and pieces,
	 * image, shapes and locations
	 * 
	 * @param start           - Date
	 * @param workspaceWidth
	 * @param workspaceHeight
	 * @param puzzleWidth
	 * @param puzzleHeight
	 * @param pieceWidth
	 * @param pieceHeight
	 * @param imageName
	 * @param shapes
	 * @param locations
	 */
	public PuzzleView(Date start, double workspaceWidth, double workspaceHeight, double puzzleWidth,
			double puzzleHeight, double pieceWidth, double pieceHeight, String imageName,
			Map<Integer, PieceShape> shapes, Map<Integer, Point> locations) {

		this.start = start;
		this.workspaceWidth = workspaceWidth;
		this.workspaceHeight = workspaceHeight;
		this.puzzleWidth = puzzleWidth;
		this.puzzleHeight = puzzleHeight;
		this.pieceWidth = pieceWidth;
		this.pieceHeight = pieceHeight;
		this.image = imageName;
		this.shapes = shapes;
		this.locations = locations;
	}

	public String getImage() {
		return image;
	}

	public Map<Integer, Point> getLocations() {
		return locations;
	}

	public double getPieceHeight() {
		return pieceHeight;
	}

	public Map<Integer, PieceShape> getShapes() {
		return shapes;
	}

	public Date getStart() {
		return start;
	}

	public double getWorkspaceHeight() {
		return workspaceHeight;
	}

	public double getPieceWidth() {
		return pieceWidth;
	}

	public double getPuzzleWidth() {
		return puzzleWidth;
	}

	public double getWorkspaceWidth() {
		return workspaceWidth;
	}

	public double getPuzzleHeight() {
		return puzzleHeight;
	}

	public Point getStandardPieceLocation(int i) {
		return locations.get(i);
	}

	public PieceShape getPieceShape(int id) {
		return shapes.get(id);
	}

}
