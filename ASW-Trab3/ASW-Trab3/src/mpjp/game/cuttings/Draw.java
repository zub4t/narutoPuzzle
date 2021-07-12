package mpjp.game.cuttings;

import java.util.Map;

import mpjp.game.Direction;
import mpjp.game.PuzzleStructure;
import mpjp.shared.geom.CurveTo;
import mpjp.shared.geom.LineTo;
import mpjp.shared.geom.PieceShape;
import mpjp.shared.geom.Point;
import mpjp.shared.geom.QuadTo;

/**
 * This class draws the shapes for all pieces using segments
 */
public class Draw {

	/**
	 * cut1 is out right, left and in top, bottom
	 * 
	 * @param cuts
	 */
	static void doCut1(Map<Direction, Integer> cuts) {
		cuts.put(Direction.EAST, 1);
		cuts.put(Direction.WEST, 1);
		cuts.put(Direction.SOUTH, 2);
		cuts.put(Direction.NORTH, 2);
	}

	/**
	 * cut2 is out top, bottom and in right, left
	 * 
	 * @param cuts
	 */
	static void doCut2(Map<Direction, Integer> cuts) {
		cuts.put(Direction.EAST, 2);
		cuts.put(Direction.WEST, 2);
		cuts.put(Direction.SOUTH, 1);
		cuts.put(Direction.NORTH, 1);
	}

	/**
	 * This returns the pieces at borders
	 * 
	 * @param cuts
	 * @param i
	 * @param j
	 * @param col
	 * @param rows
	 */
	static void defaultCuts(Map<Direction, Integer> cuts, int i, int j, int col, int rows) {
		if (i == 0) {
			cuts.put(Direction.NORTH, 0);
		}
		if (i == rows - 1) {
			cuts.put(Direction.SOUTH, 0);
		}
		if (j == 0) {
			cuts.put(Direction.WEST, 0);
		}
		if (j == col - 1) {
			cuts.put(Direction.EAST, 0);
		}
	}

	/**
	 * this method returns a triangular PieceShape, using 8 LineTo segments
	 * 
	 * @param structure
	 * @param cuts
	 */
	static PieceShape drawTriangular(PuzzleStructure structure, Map<Direction, Integer> cuts) {
		double pieceWidth = structure.getPieceWidth();
		double pieceHeight = structure.getPieceHeight();
		double limitY = pieceHeight / 2;
		double limitX = pieceWidth / 2;
		double offsetY = limitY * 0.2;
		double offsetX = limitX * 0.2;
		PieceShape shape = new PieceShape(new Point(-limitX, -limitY));
		if (cuts.get(Direction.NORTH) == 0) {
			shape.addSegment(new LineTo(new Point(0, -limitY))).addSegment(new LineTo(new Point(limitX, -limitY)));
		} else if (cuts.get(Direction.NORTH) == 1) {
			shape.addSegment(new LineTo(new Point(0, -limitY * 2 + offsetY)))
					.addSegment(new LineTo(new Point(limitX, -limitY)));
		} else {
			shape.addSegment(new LineTo(new Point(0, 0 - offsetY))).addSegment(new LineTo(new Point(limitX, -limitY)));
		}

		if (cuts.get(Direction.EAST) == 0) {
			shape.addSegment(new LineTo(new Point(limitX, 0))).addSegment(new LineTo(new Point(limitX, limitY)));
		} else if (cuts.get(Direction.EAST) == 1) {
			shape.addSegment(new LineTo(new Point(limitX * 2 - offsetX, 0)))
					.addSegment(new LineTo(new Point(limitX, limitY)));
		} else {
			shape.addSegment(new LineTo(new Point(0 + offsetX, 0))).addSegment(new LineTo(new Point(limitX, limitY)));
		}

		if (cuts.get(Direction.SOUTH) == 0) {
			shape.addSegment(new LineTo(new Point(0, limitY))).addSegment(new LineTo(new Point(-limitX, limitY)));
		} else if (cuts.get(Direction.SOUTH) == 1) {
			shape.addSegment(new LineTo(new Point(0, limitY * 2 - offsetY)))
					.addSegment(new LineTo(new Point(-limitX, limitY)));
		} else {
			shape.addSegment(new LineTo(new Point(0, 0 + offsetY))).addSegment(new LineTo(new Point(-limitX, limitY)));
		}

		if (cuts.get(Direction.WEST) == 0) {
			shape.addSegment(new LineTo(new Point(-limitX, 0))).addSegment(new LineTo(new Point(-limitX, -limitY)));
		} else if (cuts.get(Direction.WEST) == 1) {
			shape.addSegment(new LineTo(new Point(-limitX * 2 + offsetX, 0)))
					.addSegment(new LineTo(new Point(-limitX, -limitY)));
		} else {
			shape.addSegment(new LineTo(new Point(0 - offsetX, 0))).addSegment(new LineTo(new Point(-limitX, -limitY)));
		}

		return shape;
	}

	/**
	 * this method returns a round PieceShape, using 4 QuadTo segments
	 * 
	 * @param structure
	 * @param cuts
	 */
	static PieceShape drawRound(PuzzleStructure structure, Map<Direction, Integer> cuts) {
		double pieceWidth = structure.getPieceWidth();
		double pieceHeight = structure.getPieceHeight();
		PieceShape shape = new PieceShape(new Point(-pieceWidth / 2, -pieceHeight / 2));
		if (cuts.get(Direction.NORTH) == 0) {
			shape.addSegment(new QuadTo(new Point(pieceWidth / 2, -pieceHeight / 2),
					new Point(pieceWidth / 2, -pieceHeight / 2)));
		} else if (cuts.get(Direction.NORTH) == 1) {
			shape.addSegment(new QuadTo(new Point(0, -pieceHeight), new Point(pieceWidth / 2, -pieceHeight / 2)));
		} else {
			shape.addSegment(new QuadTo(new Point(0, 0), new Point(pieceWidth / 2, -pieceHeight / 2)));
		}

		if (cuts.get(Direction.EAST) == 0) {
			shape.addSegment(
					new QuadTo(new Point(pieceWidth / 2, pieceHeight / 2), new Point(pieceWidth / 2, pieceHeight / 2)));
		} else if (cuts.get(Direction.EAST) == 1) {
			shape.addSegment(new QuadTo(new Point(pieceWidth, 0), new Point(pieceWidth / 2, pieceHeight / 2)));
		} else {
			shape.addSegment(new QuadTo(new Point(0, 0), new Point(pieceWidth / 2, pieceHeight / 2)));
		}

		if (cuts.get(Direction.SOUTH) == 0) {
			shape.addSegment(new QuadTo(new Point(-pieceWidth / 2, pieceHeight / 2),
					new Point(-pieceWidth / 2, pieceHeight / 2)));
		} else if (cuts.get(Direction.SOUTH) == 1) {
			shape.addSegment(new QuadTo(new Point(0, pieceHeight), new Point(-pieceWidth / 2, pieceHeight / 2)));
		} else {
			shape.addSegment(new QuadTo(new Point(0, 0), new Point(-pieceWidth / 2, pieceHeight / 2)));
		}

		if (cuts.get(Direction.WEST) == 0) {
			shape.addSegment(new QuadTo(new Point(-pieceWidth / 2, -pieceHeight / 2),
					new Point(-pieceWidth / 2, -pieceHeight / 2)));
		} else if (cuts.get(Direction.WEST) == 1) {
			shape.addSegment(new QuadTo(new Point(-pieceWidth, 0), new Point(-pieceWidth / 2, -pieceHeight / 2)));
		} else {
			shape.addSegment(new QuadTo(new Point(0, 0), new Point(-pieceWidth / 2, -pieceHeight / 2)));
		}

		return shape;
	}

	/**
	 * this method returns a standard PieceShape, using 8 CurveTo segments
	 * 
	 * @param structure
	 * @param cuts
	 */
	static PieceShape drawStandard(PuzzleStructure structure, Map<Direction, Integer> cuts) {
		double pieceWidth = structure.getPieceWidth();
		double pieceHeight = structure.getPieceHeight();
		double limitY = pieceHeight / 2;
		double limitX = pieceWidth / 2;
		double offsetY = limitY * 0.2;
		double offsetX = limitX * 0.2;
		PieceShape shape = new PieceShape(new Point(-limitX, -limitY));

		if (cuts.get(Direction.NORTH) == 0) {
			shape.addSegment(new CurveTo(new Point(-limitX, -limitY), new Point(0, -limitY), new Point(0, -limitY)))
					.addSegment(new CurveTo(new Point(0, -limitY), new Point(limitX / 2, -limitY),
							new Point(limitX, -limitY)));
		} else if (cuts.get(Direction.NORTH) == 1) {
			shape.addSegment(new CurveTo(new Point(limitX * 0.5, -limitY), new Point(-limitX, -limitY * 2 + offsetY),
					new Point(0, -limitY * 2 + offsetY)))
					.addSegment(new CurveTo(new Point(limitX, -limitY * 2 + offsetY), new Point(-limitX * 0.5, -limitY),
							new Point(limitX, -limitY)));
		} else {
			shape.addSegment(new CurveTo(new Point(limitX * 0.5, -limitY), new Point(-limitX, 0 - offsetY),
					new Point(0, 0 - offsetY)))
					.addSegment(new CurveTo(new Point(limitX, 0 - offsetY), new Point(-limitX * 0.5, -limitY),
							new Point(limitX, -limitY)));
		}

		if (cuts.get(Direction.EAST) == 0) {
			shape.addSegment(new CurveTo(new Point(limitX, -limitY), new Point(limitX, 0), new Point(limitX, 0)))
					.addSegment(new CurveTo(new Point(limitX, 0), new Point(limitX, limitY * 0.5),
							new Point(limitX, limitY)));
		} else if (cuts.get(Direction.EAST) == 1) {
			shape.addSegment(new CurveTo(new Point(limitX, limitY * 0.5), new Point(limitX * 2 - offsetX, -limitY),
					new Point(limitX * 2 - offsetX, 0)))
					.addSegment(new CurveTo(new Point(limitX * 2 - offsetX, limitY), new Point(limitX, -limitY * 0.5),
							new Point(limitX, limitY)));
		} else {
			shape.addSegment(new CurveTo(new Point(limitX, limitY * 0.5), new Point(0 + offsetX, -limitY),
					new Point(0 + offsetX, 0)))
					.addSegment(new CurveTo(new Point(0 + offsetX, limitY), new Point(limitX, -limitY * 0.5),
							new Point(limitX, limitY)));
		}

		if (cuts.get(Direction.SOUTH) == 0) {
			shape.addSegment(
					new CurveTo(new Point(limitX, limitY), new Point(limitX * 0.5, limitY), new Point(0, limitY)))
					.addSegment(new CurveTo(new Point(-limitX * 0.5, limitY), new Point(-limitX, limitY),
							new Point(-limitX, limitY)));
		} else if (cuts.get(Direction.SOUTH) == 1) {
			shape.addSegment(new CurveTo(new Point(-limitX * 0.5, limitY), new Point(limitX, limitY * 2 - offsetY),
					new Point(0, limitY * 2 - offsetY)))
					.addSegment(new CurveTo(new Point(-limitX, limitY * 2 - offsetY), new Point(limitX * 0.5, limitY),
							new Point(-limitX, limitY)));
		} else {
			shape.addSegment(new CurveTo(new Point(-limitX * 0.5, limitY), new Point(limitX, 0 + offsetY),
					new Point(0, 0 + offsetY)))
					.addSegment(new CurveTo(new Point(-limitX, 0 + offsetY), new Point(limitX * 0.5, limitY),
							new Point(-limitX, limitY)));
		}

		if (cuts.get(Direction.WEST) == 0) {
			shape.addSegment(new CurveTo(new Point(-limitX, -limitY), new Point(-limitX, 0), new Point(-limitX, 0)))
					.addSegment(new CurveTo(new Point(-limitX, 0), new Point(-limitX, limitY * 0.5),
							new Point(-limitX, limitY)));
		} else if (cuts.get(Direction.WEST) == 1) {
			shape.addSegment(new CurveTo(new Point(-limitX, -limitY * 0.5), new Point(-limitX * 2 + offsetX, limitY),
					new Point(-limitX * 2 + offsetX, 0)))
					.addSegment(new CurveTo(new Point(-limitX * 2 + offsetX, -limitY), new Point(-limitX, limitY * 0.5),
							new Point(-limitX, -limitY)));
		} else {
			shape.addSegment(new CurveTo(new Point(-limitX, -limitY * 0.5), new Point(0 - offsetX, limitY),
					new Point(0 - offsetX, 0)))
					.addSegment(new CurveTo(new Point(0 - offsetX, -limitY), new Point(-limitX, limitY * 0.5),
							new Point(-limitX, -limitY)));
		}

		return shape;
	}

	/**
	 * this method makes sure that pieces that should connect are complementary to
	 * each other
	 * 
	 * @param i
	 * @param j
	 * @param cols
	 * @param rows
	 * @param cuts
	 */
	static void doPatternDraw(int i, int j, int cols, int rows, Map<Direction, Integer> cuts) {
		if (i % 2 == 0) {
			if (j % 2 == 1) {
				Draw.doCut1(cuts);
			}
			if (j % 2 == 0) {
				Draw.doCut2(cuts);
			}
		} else {
			if (j % 2 == 1) {
				Draw.doCut2(cuts);
			}

			if (j % 2 == 0) {
				Draw.doCut1(cuts);
			}
		}
		Draw.defaultCuts(cuts, i, j, cols, rows);
	}
}
