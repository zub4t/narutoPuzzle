package mpjp.game.cuttings;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import mpjp.game.Direction;
import mpjp.game.PuzzleStructure;
import mpjp.shared.geom.CurveTo;
import mpjp.shared.geom.PieceShape;
import mpjp.shared.geom.Point;

/**
 * This is a typical cutting where each piece side that is not border is either
 * a peg or a hole. Opposing pegs and holes must be complementary but every two
 * pairs of pegs and holes should be slightly different. To represent pegs and
 * holes the shape should use Bezier lines CurveTo.
 */
public class StandardCutting implements Cutting {
	Map<Integer, PieceShape> shapes;
	Map<Integer, Map<Direction, CurveTo>> idToControlPoints;
	Random r = new Random();

	/**
	 * Returns an empty treeMap for shapes and another for controlPoints
	 */
	public StandardCutting() {
		shapes = new TreeMap<>();
		idToControlPoints = new TreeMap<>();
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
				shapes.put((i * structure.getColumns()) + j, Draw.drawStandard(structure, cuts));
			}
		}
		return shapes;
	}

	Point getEndControlPoint1​(int id, Direction direction) {
		return idToControlPoints.get(id).get(direction).getControlPoint1();

	}

	Point getStartControlPoint1​(int id, Direction direction) {
		return idToControlPoints.get(id).get(direction).getControlPoint1();
	}

	Point getStartControlPoint2​(int id, Direction direction) {
		return idToControlPoints.get(id).get(direction).getControlPoint2();
	}

	Point getEndControlPoint2(int id, Direction direction) {
		return idToControlPoints.get(id).get(direction).getControlPoint2();

	}

	Point getMiddlePoint​(int id, Direction direction) {
		return null;
	}

}