package mpjp.shared;

import java.io.Serializable;

import mpjp.shared.geom.Point;

/**
 * The current status of a single puzzle's piece. The piece is identified by an
 * integer, and has a block and a position. A block is a set of connected
 * pieces. The position of a piece is determined by it's center. The center
 * corresponds to a point in the panel of PuzzleStructure.
 */
public class PieceStatus implements HasPoint, Serializable {

	private static final long serialVersionUID = 1L;
	int block;
	int id;
	double rotation;
	Point position;

	/**
	 * An empty instance
	 */
	public PieceStatus() {
	}

	/**
	 * An instance with an Id and a position. Block and rotation can be added after
	 * 
	 * @param id
	 * @param position
	 */
	public PieceStatus(int id, Point position) {
		this.id = id;
		this.position = position;
	}

	/**
	 * An instance with an Id, position, block and rotation
	 * 
	 * @param block
	 * @param id
	 * @param position
	 * @param rotation
	 */
	public PieceStatus(int block, int id, Point position, double rotation) {
		this.block = block;
		this.id = id;
		this.position = position;
		this.rotation = rotation;
	}

	public int getBlock() {
		return block;
	}

	public void setBlock(int block) {
		this.block = block;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	@Override
	public double getX() {
		return position.getX();
	}

	@Override
	public double getY() {
		return position.getY();
	}

}
