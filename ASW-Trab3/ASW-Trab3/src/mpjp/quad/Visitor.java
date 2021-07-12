package mpjp.quad;

import mpjp.shared.HasPoint;

/**
 * The Visitor interface, part of the abstract layer of the design pattern with
 * the same name. In this case, the visitor is parameterized by HasPoint and 
 * defines visit method for each of the types in the composite, namely LeafTrie 
 * and NodeTrie
 */
public interface Visitor<T extends HasPoint> {
	public void visit​(LeafTrie<T> leaf);
	public void visit​(NodeTrie<T> node);
}
