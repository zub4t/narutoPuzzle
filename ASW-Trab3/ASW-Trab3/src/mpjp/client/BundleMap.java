package mpjp.client;

import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.dom.client.ImageElement;

import mpjp.shared.PuzzleLayout;
import mpjp.shared.PuzzleSelectInfo;
import mpjp.shared.PuzzleView;

public class BundleMap {
	Map<String, Canvas> mapCanvas = null;
	Map<String, PuzzleLayout> mapPuzzleLayout = null;
	Map<String, PuzzleView> mapPuzzleView = null;
	Map<String, ImageElement> mapImageElement = null;
	Map<String, PuzzleSelectInfo> mapPuzzleSelectInfo = null;

	public BundleMap() {
		super();
		this.mapCanvas = new TreeMap<String, Canvas>();
		this.mapPuzzleLayout = new TreeMap<String, PuzzleLayout>();
		this.mapPuzzleView = new TreeMap<String, PuzzleView>();
		this.mapImageElement = new TreeMap<String, ImageElement>();
		this.mapPuzzleSelectInfo = new TreeMap<String, PuzzleSelectInfo>();
	}

	/**
	 * @return the mapPuzzleSelectInfo
	 */
	public Map<String, PuzzleSelectInfo> getMapPuzzleSelectInfo() {
		return mapPuzzleSelectInfo;
	}

	/**
	 * @param mapPuzzleSelectInfo the mapPuzzleSelectInfo to set
	 */
	public void setMapPuzzleSelectInfo(Map<String, PuzzleSelectInfo> mapPuzzleSelectInfo) {
		this.mapPuzzleSelectInfo = mapPuzzleSelectInfo;
	}

	/**
	 * @return the mapCanvas
	 */
	public Map<String, Canvas> getMapCanvas() {
		return mapCanvas;
	}

	/**
	 * @param mapCanvas the mapCanvas to set
	 */
	public void setMapCanvas(Map<String, Canvas> mapCanvas) {
		this.mapCanvas = mapCanvas;
	}

	/**
	 * @return the mapPuzzleLayout
	 */
	public Map<String, PuzzleLayout> getMapPuzzleLayout() {
		return mapPuzzleLayout;
	}

	/**
	 * @param mapPuzzleLayout the mapPuzzleLayout to set
	 */
	public void setMapPuzzleLayout(Map<String, PuzzleLayout> mapPuzzleLayout) {
		this.mapPuzzleLayout = mapPuzzleLayout;
	}

	/**
	 * @return the mapPuzzleView
	 */
	public Map<String, PuzzleView> getMapPuzzleView() {
		return mapPuzzleView;
	}

	/**
	 * @param mapPuzzleView the mapPuzzleView to set
	 */
	public void setMapPuzzleView(Map<String, PuzzleView> mapPuzzleView) {
		this.mapPuzzleView = mapPuzzleView;
	}

	/**
	 * @return the mapImageElement
	 */
	public Map<String, ImageElement> getMapImageElement() {
		return mapImageElement;
	}

	/**
	 * @param mapImageElement the mapImageElement to set
	 */
	public void setMapImageElement(Map<String, ImageElement> mapImageElement) {
		this.mapImageElement = mapImageElement;
	}

}
