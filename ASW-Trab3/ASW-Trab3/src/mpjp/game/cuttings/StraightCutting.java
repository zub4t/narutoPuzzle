package mpjp.game.cuttings;

import java.util.Map;
import java.util.TreeMap;

import mpjp.game.PuzzleStructure;
import mpjp.shared.geom.LineTo;
import mpjp.shared.geom.PieceShape;
import mpjp.shared.geom.Point;

/**
 * This is a very simple cutting where each piece is just a rectangle wit its
 * center at the origin (0,0). Hence, all shapes are equal, independently of the
 * piece id. They all have an initial point and 4 (straight) lines. To represent
 * lines the shape should use lines LineTo.
 */
public class StraightCutting implements Cutting {

	/**
	 * An empty instance
	 */
	public StraightCutting() {
	}

	/**
	 * Produce a map of piece ID (Integer) to PieceShape
	 * 
	 * @param structure
	 */
	public Map<Integer, PieceShape> getShapes​(PuzzleStructure structure) {
		Map<Integer, PieceShape> shapes = new TreeMap<>();
		double piece_width = structure.getPieceWidth();
		double piece_height = structure.getPieceHeight();
		for (int i = 0; i < structure.getRows(); i++) {
			for (int j = 0; j < structure.getColumns(); j++) {
				PieceShape shape = new PieceShape(new Point(piece_width / 2, piece_height / 2))
						.addSegment(new LineTo(new Point(piece_width / 2, -piece_height / 2)))
						.addSegment(new LineTo(new Point(-piece_width / 2, -piece_height / 2)))
						.addSegment(new LineTo(new Point(-piece_width / 2, piece_height / 2)))
						.addSegment(new LineTo(new Point(piece_width / 2, piece_height / 2)));
				shapes.put((i * structure.getColumns()) + j, shape);
			}
		}
		return shapes;
	}
}