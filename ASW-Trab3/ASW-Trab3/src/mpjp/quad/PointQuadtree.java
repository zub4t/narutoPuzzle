package mpjp.quad;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import mpjp.shared.HasPoint;

/**
 * @author marco. This class follows the Facade design pattern and presents a
 *         presents a single access point to manage quad trees. It provides
 *         methods for inserting, deleting and finding elements implementing
 *         HasPoint. This class corresponds to the Client in the Composite
 *         design pattern used in this package.
 * @param <T>
 */
public class PointQuadtree<T extends HasPoint> implements Iterable<T> {

	public Trie<T> top;
	public static Object padlock = new Object();

	/**
	 * Returns an iterator over the points stored in the quad tree
	 */
	@Override
	public Iterator<T> iterator() {
		PointIterator pointIterator = new PointIterator();
		synchronized (PointQuadtree.padlock) {
			try {
				PointQuadtree.padlock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return pointIterator;
	}

	/**
	 * Create a quad tree for a rectangle with given width and height.
	 * 
	 * @param width
	 * @param height
	 */
	public PointQuadtree(double width, double height) {
		top = new LeafTrie<T>(0, height, width, 0);

	}

	/**
	 * Create a quad tree for a rectangle with given dimensions and a margin.
	 * 
	 * @param width
	 * @param height
	 * @param margin
	 */
	public PointQuadtree(double width, double height, double margin) {
		top = new LeafTrie<T>(-margin, height + margin, width + margin, -margin);

	}

	/**
	 * Create a quad tree for points in a rectangle with given top left and bottom
	 * right corners.
	 * 
	 * @param topLeftX
	 * @param topLeftY
	 * @param bottomRightX
	 * @param bottomRightY
	 */
	public PointQuadtree(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY) {
		top = new LeafTrie<T>(topLeftX, topLeftY, bottomRightX, bottomRightY);

	}

	/**
	 * 
	 * Delete given point from QuadTree, if it exists there
	 * 
	 * @param point
	 */
	public void delete(T point) {
		top.delete​(point);
	}

	/**
	 * Find a recorded point with the same coordinates of given point
	 * 
	 * @param point
	 * @return
	 */
	public T find(T point) {
		return top.find​(point);
	}

	/**
	 * Returns a set of points at a distance smaller or equal to radius from point
	 * with given coordinates.
	 * 
	 * @param x
	 * @param y
	 * @param radius
	 * @return
	 */
	public Set<T> findNear(double x, double y, double radius) {
		Set<T> all = this.getAll();
		Set<T> filtered = new HashSet<>();
		for (T t : all) {
			if (Trie.isInside(x, y, radius, t.getX(), t.getY()))
				filtered.add(t);
		}
		return filtered;
	}

	/**
	 * A set with all points in the QuadTree
	 * 
	 * @return
	 */
	public Set<T> getAll() {
		return top.getAll();
	}

	/**
	 * Insert given point in the QuadTree
	 * 
	 * @param point
	 * @throws PointOutOfBoundException
	 */
	public void insert(T point) throws PointOutOfBoundException {
		if (!top.contains(point)) {
			throw new PointOutOfBoundException("Point out of bounds: " + point);
		}
		top = top.insert​(point);
	}

	public void insertReplace(T point) {
		top = top.insertReplace​(point);
	}

	/**
	 * @author marco. Iterator over points stored in the internal node structure It
	 *         traverses the tree depth first, using coroutine with threads, and
	 *         collects all points in no particular order.
	 * @param <T>
	 */

	private class PointIterator implements java.util.Iterator<T>, java.lang.Runnable, Visitor<T> {
		private T nextPoint;
		private Thread thread;
		private boolean terminated = false;

		PointIterator() {
			thread = new Thread(this, "Pointiterator");
			thread.start();
		}

		@Override
		public boolean hasNext() {
			return !terminated;
		}

		@Override
		public T next() {
			if (nextPoint == null) {
				synchronized (PointQuadtree.padlock) {
					handShake();
				}
			}
			T temp = nextPoint;
			synchronized (PointQuadtree.padlock) {
				handShake();
			}
			if (nextPoint == null)
				terminated = true;

			return temp;
		}

		@Override
		public void visit​(LeafTrie<T> leaf) {
			synchronized (PointQuadtree.padlock) {
				for (T point : leaf.points) {
					nextPoint = point;
					handShake();
				}
			}

		}

		@Override
		public void visit​(NodeTrie<T> node) {
			for (Map.Entry<Trie.Quadrant, Trie<T>> entry : node.tries.entrySet()) {
				if (entry.getValue() != null) {
					entry.getValue().accept(this);
				}
			}
			synchronized (PointQuadtree.padlock) {
				if (node.equals(top)) {
					nextPoint = null;
					PointQuadtree.padlock.notify();
				}
			}

		}

		@Override
		public void run() {
			top.accept(this);

		}

		private void handShake() {
			PointQuadtree.padlock.notify();
			try {
				PointQuadtree.padlock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
