package mpjp.game;

import java.util.Map;
import java.util.Set;

import mpjp.game.cuttings.CuttingFactoryImplementation;
import mpjp.shared.MPJPException;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleLayout;
import mpjp.shared.PuzzleSelectInfo;
import mpjp.shared.PuzzleView;
import mpjp.shared.geom.Point;

/**
 * An instance of this class is responsible for managing the workspaces and
 * solving jigsaw puzzles. The methods of this class are those needed by web
 * client thus it follows the Façade design pattern. It also follows the
 * Singleton design pattern to provide a single instance of this class to the
 * application.
 *
 */
public class Manager {
	public static Manager MANAGER = new Manager();
	WorkspacePool pool = new WorkspacePool();

	public PuzzleLayout connect​(java.lang.String workspaceId, int pieceId, Point point) throws MPJPException {
		PuzzleLayout pl = pool.getWorkspace​(workspaceId).connect​(pieceId, point);
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					pool.backup​(workspaceId, pool.getWorkspace​(workspaceId));
				} catch (MPJPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();

		return pl;
	}

	public String createWorkspace​(PuzzleInfo info) throws MPJPException {

		return pool.createWorkspace​(info);
	}

	public Set<String> getAvailableCuttings() {
		CuttingFactoryImplementation cuttingFactory = new CuttingFactoryImplementation();
		return cuttingFactory.getAvaliableCuttings();
	}

	public Set<String> getAvailableImages() {
		return Images.getAvailableImages();
	}

	public Map<String, PuzzleSelectInfo> getAvailableWorkspaces() {
		return pool.getAvailableWorkspaces();
	}

	public PuzzleLayout getCurrentLayout​(String workspaceId) {
		PuzzleLayout puzzleLayout = null;
		try {
			puzzleLayout = pool.getWorkspace​(workspaceId).getCurrentLayout();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return puzzleLayout;
	}

	public PuzzleView getPuzzleView​(String workspaceId) {

		PuzzleView puzzleView = null;
		try {
			puzzleView = pool.getWorkspace​(workspaceId).getPuzzleView();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return puzzleView;

	}

	public Workspace getWorkspace(String workspaceId) {
		try {
			return pool.getWorkspace​(workspaceId);
		} catch (MPJPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Integer selectPiece​(String workspaceId, Point point) {
		Integer integer = null;
		try {
			integer = pool.getWorkspace​(workspaceId).selectPiece(point);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return integer;

	}

	void reset() {
		pool.reset();
	}

	public static Manager getInstance() {
		return MANAGER;
	}
}
