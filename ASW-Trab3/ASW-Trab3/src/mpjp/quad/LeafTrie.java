package mpjp.quad;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import mpjp.shared.HasPoint;

/**
 * A Trie that has no descendants. This class corresponds to the Leaf in the
 * Composite design pattern.
 */
public class LeafTrie<T extends HasPoint> extends Trie<T> {
	List<T> points = new ArrayList<T>();

	/**
	 * Instance with corners
	 * 
	 * @param topLeftX
	 * @param topLeftY
	 * @param bottomRightX
	 * @param bottomRightY
	 */
	public LeafTrie(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY) {
		super(topLeftX, topLeftY, bottomRightX, bottomRightY);
	}

	/**
	 * Find a recorded point with the same coordinates of given point
	 * 
	 * @param point
	 */
	@Override
	public T find​(T point) {
		for (T n : points) {
			if (n.getX() == point.getX() && n.getY() == point.getY()) {
				return n;
			}
		}
		return null;
	}

	/**
	 * Delete given point
	 * 
	 * @param point
	 */
	@Override
	public void delete​(T point) {
		points.remove(this.find​(point));
	}

	/**
	 * Insert given point, replacing existing points in same location
	 * 
	 * @param point
	 */
	@Override
	public Trie<T> insertReplace​(T point) {
		points.remove(point);
		points.add(point);
		return this;
	}

	/**
	 * Insert given point
	 * 
	 * @param point
	 */
	@Override
	public Trie<T> insert​(T point) {
		if (!(points.size() > capacity)) {
			points.add(point);
			return this;
		}
		NodeTrie<T> nodeTrie = new NodeTrie<>(topLeftX, topLeftY, bottomRightX, bottomRightY);
		nodeTrie.insert​(point);
		for (T aux_point : points) {
			nodeTrie.insert​(aux_point);
		}
		return nodeTrie;
	}

	@Override
	protected Set<T> getAll() {
		Set<T> set = new TreeSet<T>();
		set.addAll(points);
		return set;
	}

	/**
	 * Accept a visitor to operate on a node of the composite structure
	 * 
	 * @param visitor
	 */
	@Override
	public void accept(Visitor<T> visitor) {
		visitor.visit​(this);
	}

	/**
	 * Collect all points in this node and its descendants in given set
	 * 
	 * @param set
	 */
	@Override
	public void collectAll​(Set<T> set) {
		set.addAll(points);
	}

	/**
	 * Collect points at a distance smaller or equal to radius from (x,y) and place
	 * them in given list
	 * 
	 * @param x
	 * @param y
	 * @param radius
	 * @param set
	 */
	@Override
	public void collectNear​(double x, double y, double radius, Set<T> set) {
		for (T point : points) {
			if (Trie.isInside(x, y, radius, point.getX(), point.getY())) {
				set.add(point);
			}
		}
	}

}
