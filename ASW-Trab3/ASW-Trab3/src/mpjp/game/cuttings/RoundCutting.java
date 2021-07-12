package mpjp.game.cuttings;

import java.util.Map;
import java.util.TreeMap;

import mpjp.game.Direction;
import mpjp.game.PuzzleStructure;
import mpjp.shared.geom.PieceShape;

/**
 * This is a simple cutting where each side of a piece is a quadratic curve
 * segment.
 */
public class RoundCutting implements Cutting {

	/**
	 * An empty instance
	 */
	public RoundCutting() {
	}

	/**
	 * Produce a map of piece ID (Integer) to PieceShape
	 * 
	 * @param structure
	 */
	public Map<Integer, PieceShape> getShapes​(PuzzleStructure structure) {
		Map<Integer, PieceShape> shapes = new TreeMap<>();
		int rows = structure.getRows();
		int cols = structure.getColumns();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Map<Direction, Integer> cuts = new TreeMap<>();
				Draw.doPatternDraw(i, j, cols, rows, cuts);
				shapes.put((i * structure.getColumns()) + j, Draw.drawRound(structure, cuts));
			}
		}
		return shapes;
	}

}