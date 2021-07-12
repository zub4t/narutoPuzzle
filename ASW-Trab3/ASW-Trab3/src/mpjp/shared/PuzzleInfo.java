package mpjp.shared;

/**
 * Information required to create a jigsaw puzzle.
 */
public class PuzzleInfo implements java.io.Serializable {

	private static final long serialVersionUID = 6578429301820260708L;
	int columns;
	int rows;
	double height;
	double width;

	String cuttingName;
	String imageName;
	String owner;

	/**
	 * An empty instance
	 */
	PuzzleInfo() {

	}

	/**
	 * An instance with image, cutting, rows, columns, width, height
	 * 
	 * @param imageName
	 * @param cuttingName
	 * @param rows
	 * @param columns
	 * @param width
	 * @param height
	 */
	public PuzzleInfo(String owner, String imageName, String cuttingName, int rows, int columns, double width,
			double height) {
		this.owner = owner;
		this.imageName = imageName;
		this.cuttingName = cuttingName;
		this.rows = rows;
		this.columns = columns;
		this.width = width;
		this.height = height;
	}

	public PuzzleInfo(String imageName, String cuttingName, int rows, int columns, double width, double height) {

		this.imageName = imageName;
		this.cuttingName = cuttingName;
		this.rows = rows;
		this.columns = columns;
		this.width = width;
		this.height = height;
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public String getCuttingName() {
		return cuttingName;
	}

	public void setCuttingName(String cuttingName) {
		this.cuttingName = cuttingName;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
}
