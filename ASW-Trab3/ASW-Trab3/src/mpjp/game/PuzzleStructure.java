package mpjp.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import mpjp.shared.MPJPException;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.geom.Point;

/**
 * A PuzzleStructure is responsible for connecting neighboring pieces. Puzzle
 * Structure This is different from a physical puzzle where piece connection is
 * given by cutting; in MPJP the cutting is only used to visual cues to the
 * player.
 *
 */
public class PuzzleStructure implements Iterable<Integer>, Serializable {
	private static final long serialVersionUID = -7094316018221724313L;
	private int columns, rows;
	private double width, height;

	/**
	 * Create an instance from raw data
	 * 
	 * @param rows
	 * @param cols
	 * @param width
	 * @param height
	 */
	public PuzzleStructure(int rows, int columns, double width, double height) {
		this.rows = rows;
		this.columns = columns;
		this.width = width;
		this.height = height;
	}

	/**
	 * Create instance from data in PuzzleInfo
	 * 
	 * @param puzzleInfo
	 */
	public PuzzleStructure(PuzzleInfo puzzleInfo) {
		this.rows = puzzleInfo.getRows();
		this.columns = puzzleInfo.getColumns();
		this.width = puzzleInfo.getWidth();
		this.height = puzzleInfo.getHeight();
	}

	/**
	 * The column of given piece ID
	 * 
	 * @param id
	 */
	public int getPieceColumn​(int id) throws MPJPException {
		if (id >= this.getColumns() * this.getRows() || id < 0) {
			throw new MPJPException("Invalid Id :" + id);
		}
		return id % this.getColumns();
	}

	/**
	 * The row of the given piece ID
	 * 
	 * @param id
	 */
	public int getPieceRow​(int id) throws MPJPException {
		if (id >= this.getColumns() * this.getRows() || id < 0) {
			throw new MPJPException("Invalid Id :" + id);
		}
		return (int) Math.ceil(id / this.getColumns());
	}

	/**
	 * Number of pieces in this piece structures (rows × columns)
	 */
	public int getPieceCount() {
		return columns * rows;
	}

	/**
	 * The ID of the piece facing in the given direction the give piece For
	 * instance, in a puzzle with 2 rows and 3 columns, the IDs are
	 * 
	 * @param direction
	 * @param id
	 */
	public Integer getPieceFacing​(Direction direction, Integer id) {
		try {
			switch (direction) {
			case EAST:
				if (getPieceColumn​(id) + 1 < 0 || getPieceColumn​(id) + 1 >= this.getColumns())
					return null;
				return id + 1;
			case WEST:
				if (getPieceColumn​(id) - 1 < 0 || getPieceColumn​(id) - 1 >= this.getColumns())
					return null;
				return id - 1;
			case NORTH:
				if (getPieceRow​(id) - 1 < 0 || getPieceRow​(id) - 1 >= this.getRows())
					return null;
				return id - this.getColumns();
			case SOUTH:
				if (getPieceRow​(id) + 1 < 0 || getPieceRow​(id) + 1 >= this.getRows())
					return null;
				return id + this.getColumns();

			default:
				throw new IllegalArgumentException("Unexpected value: " + direction);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * A random point in the standard puzzle, with origin (0,0) at the upper left
	 * corner and bottom right corner a (width,height).
	 * 
	 */
	public Point getRandomPointInStandardPuzzle() {
		Random r = new Random();
		double maxX = this.width;
		double maxY = this.height;
		double x = maxX * r.nextDouble();
		double y = maxY * r.nextDouble();
		return (new Point(x, y));
	}

	/**
	 * Locations of given piece in the final position, assuming the origin at (0,0)
	 * on the upper left corner
	 * 
	 */
	public Map<Integer, Point> getStandardLocations() throws MPJPException {
		Map<Integer, Point> map = new TreeMap<>();
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				int id = (i * this.columns) + j;
				map.put(id, getPieceStandardCenter​(id));
			}
		}
		return map;
	}

	/**
	 * Width of a panel cell assigned to a piece.
	 * 
	 */
	public Double getPieceWidth() {
		return this.getWidth() / (this.getColumns());
	}

	/**
	 * Height of a panel cell assigned to a piece.
	 * 
	 */
	public Double getPieceHeight() {
		return this.getHeight() / (this.getRows());
	}

	/**
	 * Checks if an given id is a valid one
	 * 
	 * @param id
	 */
	public boolean isValidId(int id) {
		return !(id < 0 || id >= this.getColumns() * this.getRows());
	}

	/**
	 * Set of pieces where a point might be located in a complete puzzle.
	 * 
	 * @param point
	 */
	public Set<Integer> getPossiblePiecesInStandarFor(Point point) {
		Set<Integer> set = new HashSet<>();
		Set<Integer> setAux = new HashSet<>();
		int pointCol = (int) Math.floor((point.getX() / this.width) * this.columns);
		int pointRow = (int) Math.floor((point.getY() / this.height) * this.rows);
		int id = (pointRow * this.columns) + pointCol;
		set.add(id);
		setAux.add(id - this.getColumns() - 1);
		setAux.add(id - this.getColumns());
		setAux.add(id - this.getColumns() + 1);
		setAux.add(id + 1 + this.getColumns());
		setAux.add(id + this.getColumns());
		setAux.add(id + this.getColumns() - 1);
		setAux.add(id + 1);
		setAux.add(id - 1);
		for (Integer neighborID : setAux) {
			if (isValidId(neighborID)) {
				try {
					if (Math.abs(this.getPieceColumn​(neighborID) - this.getPieceColumn​(id)) <= 1) {
						if (Math.abs(this.getPieceRow​(neighborID) - this.getPieceRow​(id)) <= 1) {
							set.add(neighborID);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}

		return set;

	}

	/**
	 * The center of a matching piece facing in the given direction the piece with
	 * given center For instance, in a puzzle with a piece cell width is 120 and
	 * height is 100, the matching piece facing a piece currently with center
	 * (200,200) to the: EAST is centered at (320,100) SOUTH is centered at
	 * (200,300) WEST is centered at (80,200) NORTH is centered at (200,100)
	 * 
	 * @param direction
	 * @param base      - Point
	 */
	public Point getPieceCenterFacing(Direction direction, Point base) {
		double piece_width = this.getWidth() / this.getColumns();
		double piece_height = this.getHeight() / this.getRows();
		switch (direction) {
		case EAST:
			return new Point(base.getX() + piece_width, base.getY());

		case SOUTH:
			return new Point(base.getX(), base.getY() + piece_height);

		case WEST:
			return new Point(base.getX() - piece_width, base.getY());

		case NORTH:
			return new Point(base.getX(), base.getY() - piece_height);

		default:
			throw new IllegalArgumentException("Unexpected value: " + direction);
		}
	}

	/**
	 * Returns the id of a piece given a point
	 * 
	 * @param i
	 * @param j
	 */
	public int getPieceIdUsingRowAndCol(int i, int j) {
		return (i * this.getColumns()) + j;
	}

	/**
	 * Center of given piece in the final position, assuming the origin at (0,0) on
	 * the upper left corner
	 * 
	 * @param id
	 */
	public Point getPieceStandardCenter​(int id) throws MPJPException {
		double baseX = this.getPieceWidth() * this.getPieceColumn​(id);
		double baseY = this.getPieceHeight() * this.getPieceRow​(id);
		baseX += this.getPieceWidth() / 2;
		baseY += this.getPieceHeight() / 2;
		return new Point(baseX, baseY);
	}

	/**
	 * 
	 * @param rows
	 * @param columns
	 * @param width
	 * @param height
	 */
	void init​(int rows, int columns, double width, double height) {
		this.rows = rows;
		this.columns = columns;
		this.width = width;
		this.height = height;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	@Override
	public String toString() {
		return "PuzzleStructure [columns=" + columns + ", rows=" + rows + ", width=" + width + ", height=" + height
				+ "]";
	}

	@Override
	public Iterator<Integer> iterator() {
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < this.getColumns() * this.getRows(); i++) {
			list.add(i);
		}
		return list.iterator();
	}

}
