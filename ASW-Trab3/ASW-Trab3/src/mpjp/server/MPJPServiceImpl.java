package mpjp.server;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import mpjp.client.MPJPService;
import mpjp.game.Images;
import mpjp.game.Manager;
import mpjp.game.Workspace;
import mpjp.game.WorkspacePool;
import mpjp.shared.MPJPException;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleLayout;
import mpjp.shared.PuzzleSelectInfo;
import mpjp.shared.PuzzleView;
import mpjp.shared.geom.Point;

public class MPJPServiceImpl extends RemoteServiceServlet implements MPJPService {
	private static final long serialVersionUID = 1L;

	public MPJPServiceImpl() {
	}

	@Override
	public void init() throws ServletException {
		super.init();
		ServletContext context = getServletContext();
		File base = new File(context.getRealPath("/"));

		File imagesdir = null;

		imagesdir = new File(base.getParent(), "/src/mpjp/resource/puzzles/");

		System.out.println("Parent directory : " + imagesdir);

		File poolDir = new File(base, "WEB-INF/pool");
		if (!poolDir.exists())
			poolDir.mkdir();

		Images.setImageDirectory(imagesdir);
		WorkspacePool.setPoolDiretory​(poolDir);
	}

	@Override
	public PuzzleLayout connect(java.lang.String workspaceId, int pieceId, Point point) throws MPJPException {
		return Manager.getInstance().connect​(workspaceId, pieceId, point);
	}

	@Override
	public String createWorkspace(PuzzleInfo info) throws MPJPException {
		return Manager.getInstance().createWorkspace​(info);
	}

	@Override

	public Set<String> getAvailableCuttings() {
		return Manager.getInstance().getAvailableCuttings();
	}

	@Override
	public Set<String> getAvailableImages() {
		return Manager.getInstance().getAvailableImages();
	}

	@Override
	public Map<String, PuzzleSelectInfo> getAvailableWorkspaces() {
		return Manager.getInstance().getAvailableWorkspaces();
	}

	@Override
	public PuzzleLayout getCurrentLayout(String workspaceId) {
		return Manager.getInstance().getCurrentLayout​(workspaceId);

	}

	@Override
	public PuzzleView getPuzzleView(String workspaceId) {
		return Manager.getInstance().getPuzzleView​(workspaceId);
	}

	@Override
	public Integer selectPiece(String workspaceId, Point point) {
		return Manager.getInstance().selectPiece​(workspaceId, point);

	}

}
