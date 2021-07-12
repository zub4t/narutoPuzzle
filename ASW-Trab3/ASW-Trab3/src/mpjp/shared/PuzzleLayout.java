package mpjp.shared;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Current puzzle layout, the status (position, rotation) of each piece, and the
 * blocks pieces fit together.
 */
public class PuzzleLayout implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int percentageSolved;
	Map<Integer, List<Integer>> blocks;
	Map<Integer, PieceStatus> pieces;

	/**
	 * An empty instance
	 */
	public PuzzleLayout() {

	}

	/**
	 * An instance with the pieces, blocks and percentage solved
	 * 
	 * @param pieces
	 * @param blocks
	 * @param percentageSolved
	 */
	public PuzzleLayout(Map<Integer, PieceStatus> pieces, Map<Integer, List<Integer>> blocks, int percentageSolved) {
		this.pieces = pieces;
		this.blocks = blocks;
		this.percentageSolved = percentageSolved;
	}

	public Map<Integer, List<Integer>> getBlocks() {
		return blocks;
	}

	public void setBlocks(Map<Integer, List<Integer>> blocks) {
		this.blocks = blocks;
	}

	public Map<Integer, PieceStatus> getPieces() {
		return pieces;
	}

	public void setPieces(Map<Integer, PieceStatus> pieces) {
		this.pieces = pieces;
	}

	public int getPercentageSolved() {
		return (100 * (this.pieces.size() - this.blocks.size()) / (this.pieces.size() - 1));
	}

	public void setPercentageSolved(int percentageSolved) {
		this.percentageSolved = percentageSolved;
	}

	public boolean isSolved() {
		return ((100 * (this.pieces.size() - this.blocks.size()) / (this.pieces.size() - 1)) == 100);
	}

}
