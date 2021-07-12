package mpjp.client;

import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mpjp.quad.PointOutOfBoundException;
import mpjp.shared.MPJPException;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleLayout;
import mpjp.shared.PuzzleSelectInfo;
import mpjp.shared.PuzzleView;
import mpjp.shared.geom.Point;

public interface MPJPServiceAsync {

	/**
	 * In the workspace with the given ID, connect piece with given iD after moving
	 * its center to the given point.
	 * 
	 * @param workspaceId
	 * @param pieceId
	 * @param point
	 * @return
	 * @throws MPJPException
	 * @throws PointOutOfBoundException
	 */
	void connect(String workspaceId, int pieceId, Point point, AsyncCallback<PuzzleLayout> callback)
			throws MPJPException;

	/**
	 * Creates a workspace with given information and returns an ID to refer to it
	 * in methods
	 * 
	 * @param info
	 * @return
	 * @throws MPJPException
	 * @throws PointOutOfBoundException
	 */
	void createWorkspace(PuzzleInfo info, AsyncCallback<String> callback) throws MPJPException;

	/**
	 * 
	 * A set of cutting names available to produce puzzle pieces
	 * 
	 * @return
	 */
	void getAvailableCuttings(AsyncCallback<Set<String>> callback);

	/**
	 * 
	 * A set of images available for puzzle backgrounds
	 * 
	 * @return
	 */
	void getAvailableImages(AsyncCallback<Set<String>> callback);

	/**
	 * A map of workspace IDs (int) to PuzzleSelectInfo.
	 * 
	 * @return
	 */
	void getAvailableWorkspaces(AsyncCallback<Map<String, PuzzleSelectInfo>> callback);

	/**
	 * Current layout of the workspace with given ID
	 * 
	 * @param workspaceId
	 * @return
	 */
	void getCurrentLayout(String workspaceId, AsyncCallback<PuzzleLayout> callback);

	/**
	 * Puzzle view of given workspace
	 * 
	 * @param workspaceId
	 * @return
	 */
	void getPuzzleView(String workspaceId, AsyncCallback<PuzzleView> callback);

	/**
	 * Select a piece in the given workspace, with its "center" near the given point
	 * 
	 * @param workspaceId
	 * @param point
	 * @return
	 */
	void selectPiece(String workspaceId, Point point, AsyncCallback<Integer> callback);


}
