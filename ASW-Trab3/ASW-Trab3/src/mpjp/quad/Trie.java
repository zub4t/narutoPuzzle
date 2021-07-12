package mpjp.quad;

import java.util.Set;

import mpjp.shared.HasPoint;

/**
 * Abstract class common to all classes implementing the trie structure. Defines
 * methods required by those classes and provides general methods for checking
 * overlaps and computing distances. This class corresponds to the Component in
 * the Composite design pattern.
 */
public abstract class Trie<T extends HasPoint> implements Element<T> {

	public enum Quadrant {
		NE, NW, SE, SW;

		static Quadrant valueOf​(java.lang.String name) {
			switch (name) {
			case "NE":
				return NE;
			case "NW":
				return NW;
			case "SE":
				return SE;
			case "SW":
				return SW;
			default:
				throw new IllegalArgumentException("Unexpected value: " + name);
			}

		}

	}

	static int capacity;
	protected double bottomRightX;
	protected double bottomRightY;
	protected double topLeftX;
	protected double topLeftY;

	protected Trie(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY) {
		super();
		Trie.capacity = 10;
		this.bottomRightX = bottomRightX;
		this.bottomRightY = bottomRightY;
		this.topLeftX = topLeftX;
		this.topLeftY = topLeftY;
	}

	public abstract void delete​(T point);

	public abstract T find​(T point);

	public abstract Trie<T> insert​(T point);

	protected abstract Set<T> getAll();

	public abstract void collectNear​(double x, double y, double radius, Set<T> points);

	public abstract void collectAll​(Set<T> points);

	public abstract Trie<T> insertReplace​(T point);

	public static int getCapacity() {
		return capacity;
	}

	boolean overlaps​(double x, double y, double radius) {
		return (topLeftX <= x - radius && bottomRightX > x - radius)
				&& (topLeftY >= y + radius && bottomRightY < y - radius);
	}

	static void setCapacity​(int capacity) {
		Trie.capacity = capacity;
	}

	@Override
	public String toString() {
		return "Trie [bottomRightX=" + bottomRightX + ", bottomRightY=" + bottomRightY + ", topLeftX=" + topLeftX
				+ ", topLeftY=" + topLeftY + "]";
	}

	boolean contains(T point) {
		return (topLeftX <= point.getX() && bottomRightX >= point.getX())
				&& (topLeftY >= point.getY() && bottomRightY <= point.getY());

	}

	static double getDistance​(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}

	static boolean isInside(double circle_x, double circle_y, double rad, double x, double y) {
		return ((x - circle_x) * (x - circle_x) + (y - circle_y) * (y - circle_y) <= rad * rad);

	}

}
