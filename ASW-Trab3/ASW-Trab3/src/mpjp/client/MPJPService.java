package mpjp.client;

import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mpjp.quad.PointOutOfBoundException;
import mpjp.shared.MPJPException;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleLayout;
import mpjp.shared.PuzzleSelectInfo;
import mpjp.shared.PuzzleView;
import mpjp.shared.geom.Point;

@RemoteServiceRelativePath("service")
public interface MPJPService extends RemoteService {

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
	PuzzleLayout connect(String workspaceId, int pieceId, Point point) throws MPJPException;

	/**
	 * Creates a workspace with given information and returns an ID to refer to it
	 * in methods
	 * 
	 * @param info
	 * @return
	 * @throws MPJPException
	 * @throws PointOutOfBoundException
	 */
	String createWorkspace(PuzzleInfo info) throws MPJPException;

	/**
	 * 
	 * A set of cutting names available to produce puzzle pieces
	 * 
	 * @return
	 */
	Set<String> getAvailableCuttings();

	/**
	 * 
	 * A set of images available for puzzle backgrounds
	 * 
	 * @return
	 */
	Set<String> getAvailableImages();

	/**
	 * A map of workspace IDs (int) to PuzzleSelectInfo.
	 * 
	 * @return
	 */
	Map<String, PuzzleSelectInfo> getAvailableWorkspaces();

	/**
	 * Current layout of the workspace with given ID
	 * 
	 * @param workspaceId
	 * @return
	 */
	PuzzleLayout getCurrentLayout(String workspaceId);

	/**
	 * Puzzle view of given workspace
	 * 
	 * @param workspaceId
	 * @return
	 */
	PuzzleView getPuzzleView(String workspaceId);

	/**
	 * Select a piece in the given workspace, with its "center" near the given point
	 * 
	 * @param workspaceId
	 * @param point
	 * @return
	 */
	Integer selectPiece(String workspaceId, Point point);

}