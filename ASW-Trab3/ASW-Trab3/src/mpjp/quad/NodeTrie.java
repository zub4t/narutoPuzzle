package mpjp.quad;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import mpjp.shared.HasPoint;

/**
 * Trie with 4 sub tries with equal dimensions covering all its area. 
 * This class corresponds to the Container in the Composite design pattern. 
 */
public class NodeTrie<T extends HasPoint> extends Trie<T> {

	Map<Trie.Quadrant, Trie<T>> tries = new TreeMap<>();

    /**
     * Instance with corners
     * 
     * @param topLeftX
     * @param topLeftY
     * @param bottomRightX
     * @param bottomRightY
     */
	public NodeTrie(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY) {
		super(topLeftX, topLeftY, bottomRightX, bottomRightY);
		double width = bottomRightX - topLeftX;
		double height = topLeftY - bottomRightY;
		
		LeafTrie<T> SW = new LeafTrie<T>(topLeftX, topLeftY + (height / 2), topLeftX + (width / 2), bottomRightY);
		LeafTrie<T> SE = new LeafTrie<T>(topLeftX + (width / 2), topLeftY + (height / 2), bottomRightX, bottomRightY);
		LeafTrie<T> NW = new LeafTrie<T>(topLeftX, topLeftY, topLeftX + (width / 2), topLeftY + (height / 2));
		LeafTrie<T> NE = new LeafTrie<T>(topLeftX + (width / 2), topLeftY, bottomRightX, topLeftY + (height / 2));
		
		this.tries.put(Quadrant.SW, SW);
		this.tries.put(Quadrant.SE, SE);
		this.tries.put(Quadrant.NW, NW);
		this.tries.put(Quadrant.NE, NE);

	}

    /**
     * Find a recorded point with the same coordinates of given point
     * 
     * @param point
     */
	@Override
	public T find​(T point) {
		for (Map.Entry<Trie.Quadrant, Trie<T>> entry : tries.entrySet()) {
			if (entry.getValue().contains(point)) {
				return entry.getValue().find​(point);

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
		for (Map.Entry<Trie.Quadrant, Trie<T>> entry : tries.entrySet()) {
			if (entry.getValue().contains(point)) {
				entry.getValue().delete​(point);

			}
		}
	}

    /**
     * Insert given point, replacing existing points in same location
     * 
     * @param point
     */
	@Override
	public Trie<T> insertReplace​(T point) {
		for (Map.Entry<Trie.Quadrant, Trie<T>> entry : tries.entrySet()) {
			if (entry.getValue().contains(point)) {
				Trie<T> nt = entry.getValue().insertReplace​(point);
				tries.put(entry.getKey(), nt);
			}
		}
		return this;
	}
    
    /**
     * Insert given point
     * 
     * @param point
     */
	@Override
	public Trie<T> insert​(T point) {
		for (Map.Entry<Trie.Quadrant, Trie<T>> entry : tries.entrySet()) {
			if (entry.getValue().contains(point)) {
				Trie<T> nt = entry.getValue().insert​(point);
				tries.put(entry.getKey(), nt);
				break;
			}
		}
		return this;
	}

	@Override
	protected Set<T> getAll() {
		Set<T> set = new HashSet<>();
		for (Map.Entry<Trie.Quadrant, Trie<T>> entry : tries.entrySet()) {
			set.addAll(entry.getValue().getAll());

		}
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
     * @param points
     */
	@Override
	public void collectAll​(Set<T> points) {
		for (Map.Entry<Trie.Quadrant, Trie<T>> entry : tries.entrySet()) {
			points.addAll(entry.getValue().getAll());
		}
	}
    
    /**
     * Collect points at a distance smaller or equal to radius from (x,y) and 
     * place them in given list
     * 
     * @param x
     * @param y
     * @param radius
     * @param points
     */
	@Override
	public void collectNear​(double x, double y, double radius, Set<T> points) {
		for (Map.Entry<Trie.Quadrant, Trie<T>> entry : tries.entrySet()) {
			points.addAll(entry.getValue().getAll());
		}
	}

}
