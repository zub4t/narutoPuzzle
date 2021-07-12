package mpjp.shared;

import java.io.Serializable;
import java.util.Date;

/**
 * Information required to select a puzzle currently being solved. Most of the
 * information is the same used for workspace creation. The extra information is
 * the start date and the percentage of the puzzle that is solved.
 */
public class PuzzleSelectInfo extends PuzzleInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int percentageSolved;
	Date start;

	/**
	 * Creates an instance with the date
	 */
	public PuzzleSelectInfo() {
		this.start = new Date();
	}

	/**
	 * An instance with PuzzleInfo, date and percentageSolved
	 * 
	 * @param info             - PuzzleInfo
	 * @param start            - Date
	 * @param percentageSolved
	 */
	public PuzzleSelectInfo(PuzzleInfo info, Date start, int percentageSolved) {
		super(info.getOwner(), info.getImageName(), info.getCuttingName(), info.getRows(), info.getColumns(),
				info.getWidth(), info.getHeight());
		this.start = start;
		this.percentageSolved = percentageSolved;
	}

	public int getPercentageSolved() {
		return percentageSolved;
	}

	public void setPercentageSolved(int percentageSolved) {
		this.percentageSolved = percentageSolved;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(java.util.Date start) {
		this.start = start;
	}

}
