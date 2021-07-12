package mpjp.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import mpjp.game.cuttings.Cutting;
import mpjp.game.cuttings.CuttingFactoryImplementation;
import mpjp.quad.PointOutOfBoundException;
import mpjp.quad.PointQuadtree;
import mpjp.shared.HasPoint;
import mpjp.shared.MPJPException;
import mpjp.shared.PieceStatus;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleLayout;
import mpjp.shared.PuzzleSelectInfo;
import mpjp.shared.PuzzleView;
import mpjp.shared.geom.Point;

/**
 * An instance of Workspace is where puzzles are solved. It defines a
 * rectangular area that is large enough to have all puzzles pieces scattered at
 * the beginning without overlapping each other.
 * 
 * The ratio between the workspace and puzzle dimensions is controlled by the
 * static properties widthFactor and heightFactor. These properties can be
 * managed with the corresponding static getters and setters. There is also
 * static property radius that controls how close to pieces must be to their
 * expected relative positions in order to connect.
 *
 */
public class Workspace implements Serializable {
	private static final long serialVersionUID = 1L;
	PuzzleInfo info;
	Date start;
	PuzzleLayout puzzleLayout;
	PuzzleStructure puzzleStructure;
	PuzzleView puzzleView;
	public String workspaceId = null;
	PuzzleSelectInfo puzzleSelectInfo = null;
	transient static double heightFactor, widthFactor, radius;
	transient PointQuadtree<Tuplo> quadTree;
	Map<Integer, List<Integer>> blocks = null;
	Map<Integer, PieceStatus> pieces = null;

	/**
	 * @param puzzleInfo
	 */
	public Workspace() throws MPJPException {

	}

	public Workspace(PuzzleInfo puzzleInfo) throws MPJPException {
		this.info = puzzleInfo;
		this.start = new Date();
		widthFactor = 2;
		heightFactor = 2;
		scatter();
		radius = getSelectRadius() * 2;
	}

	/**
	 * The current piece positions and blocks formed by connected pieces
	 */
	public PuzzleLayout getCurrentLayout() {
		return puzzleLayout;
	}

	/**
	 * Information on this puzzle that may be used by a person to decide to help
	 * solving it.
	 */
	public PuzzleSelectInfo getPuzzleSelectInfo() {
		if (puzzleSelectInfo == null)
			return new PuzzleSelectInfo(info, start, getPercentageSolved());
		else
			return puzzleSelectInfo;
	}

	void setPuzzleSelectInfo(PuzzleSelectInfo puzzleSelectInfo) {
		this.puzzleSelectInfo = puzzleSelectInfo;
	}

	/**
	 * Puzzle structure used in this workspace's puzzle.
	 */
	public PuzzleStructure getPuzzleStructure() {
		return puzzleStructure;
	}

	/**
	 * The visual part of the puzzle, sent to the client when the user starts
	 * solving the puzzle.
	 */
	public PuzzleView getPuzzleView() {
		return puzzleView;
	}

	/**
	 * Current radius for matching
	 */
	public static double getRadius() {
		return radius;
	}

	/**
	 * Current proportion between board width and puzzle width.
	 */
	static double getWidthFactor() {
		return widthFactor;
	}

	/**
	 * The radius from the "center" of the piece where it can be selected.
	 */
	double getSelectRadius() {
		return Math.sqrt((puzzleStructure.getPieceWidth() * puzzleStructure.getPieceWidth())
				+ (puzzleStructure.getPieceHeight() * puzzleStructure.getPieceHeight())) / 2;
	}

	/**
	 * Creates a string ID for this solution by combining some of its features
	 */
	public String getId() {
		if (workspaceId == null) {
			workspaceId = Workspace.generateID();
		}
		return workspaceId;
	}

	/**
	 * Percentage in which puzzle is solved.
	 */
	int getPercentageSolved() {
		return puzzleLayout.getPercentageSolved();
	}

	/**
	 * Current proportion between board height and puzzle height.
	 */
	public static double getHeightFactor() {
		return heightFactor;

	}

	private void init() throws MPJPException {
		puzzleLayout = new PuzzleLayout();
		puzzleStructure = new PuzzleStructure(info);
		quadTree = new PointQuadtree<>(50000, 5000);

		CuttingFactoryImplementation cuttingFactoryImplementation = new CuttingFactoryImplementation();
		Cutting cutting = cuttingFactoryImplementation.createCutting​(info.getCuttingName());
		double workspaceWidth = puzzleStructure.getWidth() * widthFactor;
		double workspaceHeight = puzzleStructure.getHeight() * heightFactor;

		puzzleView = new PuzzleView(start, workspaceWidth, workspaceHeight, puzzleStructure.getWidth(),
				puzzleStructure.getHeight(), puzzleStructure.getPieceWidth(), puzzleStructure.getPieceHeight(),
				info.getImageName(), cutting.getShapes​(puzzleStructure), puzzleStructure.getStandardLocations());

		blocks = new TreeMap<Integer, List<Integer>>();
		pieces = new TreeMap<Integer, PieceStatus>();

	}

	/**
	 * Restore transient fields that are not saved by serialization.
	 */
	public void restore() throws MPJPException {
		quadTree = new PointQuadtree<>(puzzleStructure.getWidth(), puzzleStructure.getHeight(), 2000);
		for (Entry<Integer, PieceStatus> piece : pieces.entrySet()) {
			try {
				quadTree.insert(new Tuplo(piece.getKey(), piece.getValue().getPosition()));
			} catch (PointOutOfBoundException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * Scatter the puzzle's pieces on the board before solving it.
	 * 
	 * @throws MPJPException
	 */
	void scatter() throws MPJPException {
		Random r = new Random();
		int count = 0;
		init();
		double margin = (puzzleStructure.getPieceHeight() + puzzleStructure.getPieceWidth() / 2);
		for (int i = 0; i < puzzleStructure.getPieceCount(); i++) {
			List<Integer> blockPieces = new ArrayList<Integer>();
			blockPieces.add(i);
			blocks.put(i, blockPieces);
			double randomX = (this.getPuzzleView().getWorkspaceWidth() - margin) * r.nextDouble();
			double randomY = (this.getPuzzleView().getWorkspaceHeight() - margin) * r.nextDouble();
			randomX = Math.abs(randomX);
			randomY = Math.abs(randomY);
			Point randomPoint = new Point(randomX, randomY);
			PieceStatus piece = new PieceStatus(i, i, randomPoint, 0);
			Set<Tuplo> aux = quadTree.findNear(piece.getX(), piece.getY(),
					(puzzleStructure.getPieceWidth() + puzzleStructure.getPieceHeight()) / 4);
			if (aux.isEmpty()) {
				pieces.put(i, piece);
				try {
					quadTree.insert(new Tuplo(i, randomPoint));
				} catch (PointOutOfBoundException e) {
					e.printStackTrace();
				}
			} else {
				if (count < 100)
					i--;
				count++;
			}

		}

		puzzleLayout.setBlocks(blocks);
		puzzleLayout.setPieces(pieces);
	}

	/**
	 * Select a piece given a pair of coordinates.
	 * 
	 * @param point
	 */
	public Integer selectPiece(Point point) {
		Set<Tuplo> nearPieces = quadTree.findNear(point.getX(), point.getY(), getSelectRadius());
		if (nearPieces.size() > 1) {
			int highest_block = -1;
			int highest_id = -1;
			for (HasPoint aux : nearPieces) {
				Tuplo tuplo = (Tuplo) aux;
				if (puzzleLayout.getPieces().get(tuplo.peiceId).getBlock() > highest_block) {
					highest_block = puzzleLayout.getPieces().get(tuplo.peiceId).getBlock();
					highest_id = tuplo.peiceId;
				}
			}
			return highest_id;
		} else if (nearPieces.size() == 1) {
			for (HasPoint aux : nearPieces) {
				Tuplo tuplo = (Tuplo) aux;
				return tuplo.peiceId;
			}
		}

		return null;
	}

	/**
	 * Verify if an id is valid and if the point is inbounds
	 * 
	 * @param id
	 * @param point
	 */
	private void verify(int id, Point point) throws MPJPException {
		if (id < 0 || id >= puzzleStructure.getPieceCount()) {
			throw new MPJPException("Invalid id " + id);
		}
		if (point.getX() < 0 || point.getX() >= puzzleView.getWorkspaceWidth() || point.getY() < 0
				|| point.getY() >= puzzleView.getWorkspaceHeight()) {
			throw new MPJPException("Point out of workspace " + point);
		}

	}

	/**
	 * Returns a set of ids thats possible to with the given id
	 * 
	 * @param id
	 */
	private Set<Integer> possibleIDs(int id) throws MPJPException {
		Set<Integer> possibleIDs = new HashSet<>();
		Integer E = puzzleStructure.getPieceFacing​(Direction.EAST, id);
		Integer S = puzzleStructure.getPieceFacing​(Direction.SOUTH, id);
		Integer W = puzzleStructure.getPieceFacing​(Direction.WEST, id);
		Integer N = puzzleStructure.getPieceFacing​(Direction.NORTH, id);
		if (E != null)
			possibleIDs.add(E);
		if (S != null)
			possibleIDs.add(S);
		if (W != null)
			possibleIDs.add(W);
		if (N != null)
			possibleIDs.add(N);
		return possibleIDs;
	}

	/**
	 * Updates the positions of a piece and its block
	 * 
	 * @param point
	 * @param id
	 * @param listAux - List<Integer>
	 */
	private void updatePiecesPosition(Point point, int id, List<Integer> listAux) throws MPJPException {
		try {
			Point vector = new Point(point.getX() - puzzleStructure.getPieceStandardCenter​(id).getX(),
					point.getY() - puzzleStructure.getPieceStandardCenter​(id).getY());
			for (int pieceID : listAux) {
				quadTree.delete(new Tuplo(0, pieces.get(pieceID).getPosition()));

				pieces.get(pieceID)
						.setPosition(new Point(puzzleStructure.getPieceStandardCenter​(pieceID).getX() + vector.getX(),
								puzzleStructure.getPieceStandardCenter​(pieceID).getY() + vector.getY()));
				try {
					quadTree.insert(new Tuplo(pieceID, pieces.get(pieceID).getPosition()));
					quadTree.delete(new Tuplo(0, pieces.get(id).getPosition()));
					quadTree.insert(new Tuplo(id, point));
					pieces.get(id).setPosition(point);
				} catch (PointOutOfBoundException e) {
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			throw new MPJPException(e);
		}
	}

	/**
	 * Returns a List of all pieces ids available for connection
	 * 
	 * @param currentBlock
	 * @param id
	 * @param nearestIDs   - Set<Tuplo>
	 * @param possibleIDs  - Set<Integer>
	 */
	private List<Integer> doConnectionAvailable(int currentBlock, int id, Set<Tuplo> nearestIDs,
			Set<Integer> possibleIDs) {

		Map<Integer, List<Integer>> blocks = puzzleLayout.getBlocks();
		Map<Integer, PieceStatus> pieces = puzzleLayout.getPieces();
		Set<Integer> blockIDToConnect = new HashSet<>();
		blockIDToConnect.add(pieces.get(id).getBlock());
		int lowestID = currentBlock;
		for (Tuplo tuplo : nearestIDs) {
			if (possibleIDs.contains(tuplo.peiceId)) {
				int nearestBlockID = pieces.get(tuplo.peiceId).getBlock();
				if (nearestBlockID < lowestID)
					lowestID = nearestBlockID;
				blockIDToConnect.add(nearestBlockID);
			}
		}

		List<Integer> listAux = new ArrayList<Integer>();
		for (int neighborBlockID : blockIDToConnect) {
			listAux.addAll(blocks.get(neighborBlockID));
			blocks.remove(neighborBlockID);
		}
		for (int pieceID : listAux) {
			pieces.get(pieceID).setBlock(lowestID);
		}
		blocks.put(lowestID, listAux);
		return listAux;

	}

	/**
	 * Move the piece with given id to given point and check if it connects with
	 * other pieces.
	 * 
	 * @param id
	 * @param point
	 */
	public PuzzleLayout connect​(int id, Point point) throws MPJPException {
		try {
			verify(id, point);
			int currentBlock = pieces.get(id).getBlock();
			/*
			 * Get the possibles IDs to connect to the current ID
			 */
			Set<Integer> possibleIDs = possibleIDs(id);
			/*
			 * Get all IDs near
			 */
			Set<Tuplo> nearestIDs = quadTree.findNear(point.getX(), point.getY(),
					(getSelectRadius() + puzzleStructure.getPieceWidth() + puzzleStructure.getPieceHeight()) / 2);
			/*
			 * set all pieces that can connect each other and apply the lowest block id to
			 * all of them then update its position
			 */
			updatePiecesPosition(point, id, doConnectionAvailable(currentBlock, id, nearestIDs, possibleIDs));
			puzzleLayout.setBlocks(blocks);
			puzzleLayout.setPieces(pieces);
			int piecesWithBlock = 0;
			for (Entry<Integer, PieceStatus> entry : pieces.entrySet()) {
				if (entry.getValue().getBlock() != 0) {
					piecesWithBlock++;
				}
			}
			PuzzleSelectInfo psi = getPuzzleSelectInfo();
			psi.setPercentageSolved(piecesWithBlock / pieces.size());
			return puzzleLayout;
		} catch (Exception e) {
			throw new MPJPException(e);
		}
	}

	/**
	 * Change proportion between board width and puzzle width.
	 * 
	 * @param heightFactor
	 */
	public static void setHeightFactor​(double heightFactor) {
		Workspace.heightFactor = heightFactor;
	}

	/**
	 * Set radius for matching pieces
	 * 
	 * @param radius
	 */
	public static void setRadius​(double radius) {
		Workspace.radius = radius;
	}

	/**
	 * Change proportion between board width and puzzle width.
	 * 
	 * @param widthFactor
	 */
	public static void setWidthFactor​(double widthFactor) {
		Workspace.widthFactor = widthFactor;
	}

	/**
	 * Random generates an ID
	 */
	private static String generateID() {
		String latters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
		String ID = "";
		for (int i = 0; i < 15; i++) {
			int r = (int) Math.floor(Math.random() * latters.length());
			ID += latters.substring(r, r + 1);
		}
		return ID;
	}

}
