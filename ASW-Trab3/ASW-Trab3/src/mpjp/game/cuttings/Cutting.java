package mpjp.game.cuttings;

import java.util.Map;

import mpjp.game.PuzzleStructure;
import mpjp.shared.geom.PieceShape;

/**
 * A Cutting provides a method to trace the boundaries o jigsaw puzzle pieces. 
 * These boundaries are computed from a puzzle structure - specifying the 
 * puzzle's dimensions (width and height) and the number of rows and columns. 
 * A specific cutting implements a strategy for tracing piece boundaries.
 */
public interface Cutting {
	public Map<Integer,PieceShape> getShapes​(PuzzleStructure structure);
}
