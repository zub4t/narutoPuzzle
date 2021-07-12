package mpjp.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import mpjp.shared.MPJPException;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleSelectInfo;

/**
 * A pool of workspaces where jigsaw puzzles are actually solved. At any given
 * moment, different puzzles may be solved in parallel. A pool indexes
 * workspaces by their ID (Workspace.getId()), stores them both in memory, and
 * persists them on the file system using serialization. The files with wokspace
 * serialization are kept in a directory specified by the property poolDirectory
 * that can be managed using static getters and setters.
 *
 */
public class WorkspacePool {
	static String SERIALIAZTION_SUFFIX = ".ser";
	static String poolDirectory;
	private Map<String, Workspace> availableWorkspaces;
	private Map<String, PuzzleSelectInfo> availablePuzzleSelectInfo;
	private List<File> workspaceFile = new ArrayList<>();

	public WorkspacePool() {
		availablePuzzleSelectInfo = new TreeMap<>();
		availableWorkspaces = new TreeMap<>();
		poolDirectory = System.getProperty("user.dir") + "/resources/";
		System.out.println("pool "+poolDirectory);
	}

	/**
	 * Change pool directory, the directory where workspace serializations are
	 * saved.
	 * 
	 * @param poolDirectory - File
	 */
	public static void setPoolDiretory​(File poolDirectory) {
		WorkspacePool.poolDirectory = poolDirectory.getPath().toString();
	}

	/**
	 * Convenience method for setting pool directory as a string
	 * 
	 * @param pathname
	 */
	static void setPoolDiretory​(java.lang.String pathname) {
		WorkspacePool.poolDirectory = pathname;
	}

	/**
	 * Serializes workspace
	 * 
	 * @param workspaceId - String
	 * @param workspace
	 */
	void backup​(String workspaceId, Workspace workspace) {
		try {
			FileOutputStream fileOut = new FileOutputStream(poolDirectory + "/" + workspaceId + SERIALIAZTION_SUFFIX);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOut);
			objectOutputStream.writeObject(workspace);
			if (objectOutputStream != null)
				objectOutputStream.close();
			if (fileOut != null)
				fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create a workspace that is stored using its id as key.
	 * 
	 * @param info - PuzzleInfo
	 */
	String createWorkspace​(PuzzleInfo info) throws MPJPException {
		Workspace workspace = null;
		FileOutputStream fileOut = null;
		ObjectOutputStream objectOutputStream = null;
		try {
			workspace = new Workspace(info);
			workspace.getId();
			fileOut = new FileOutputStream(poolDirectory + "/" + workspace.workspaceId + SERIALIAZTION_SUFFIX);
			objectOutputStream = new ObjectOutputStream(fileOut);
			objectOutputStream.writeObject(workspace);
			if (objectOutputStream != null)
				objectOutputStream.close();
			if (fileOut != null)
				fileOut.close();
			availableWorkspaces.put(workspace.workspaceId, workspace);
			availablePuzzleSelectInfo.put(workspace.workspaceId, workspace.getPuzzleSelectInfo());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return workspace.workspaceId;
	}

	/**
	 * A map of workspace IDs to PuzzleSelectInfo used for selecting an existing
	 * puzzle to solve.
	 */
	Map<String, PuzzleSelectInfo> getAvailableWorkspaces() {
		reset();
		Workspace wk = null;
		for (File fileEntry : getPoolDirectory().listFiles()) {
			if (!fileEntry.isDirectory()) {

				try {
					if (fileEntry.getName().contains(".ser"))
						wk = recover​(fileEntry.getName());

				} catch (MPJPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (wk != null) {
					if (wk.getPuzzleSelectInfo().getPercentageSolved() != 100) {
						availablePuzzleSelectInfo.put(fileEntry.getName().replaceAll(".ser", ""),
								wk.getPuzzleSelectInfo());
						availableWorkspaces.put(fileEntry.getName().replaceAll(".ser", ""), wk);
					} else {
						fileEntry.delete();
					}
				}
				wk = null;
			}
		}

		return availablePuzzleSelectInfo;
	}

	/**
	 * A File object for given ID.
	 * 
	 * @param workspaceId - String
	 */
	File getFile​(String workspaceId) {
		for (File file : workspaceFile) {
			if (file.getName().equals(workspaceId + SERIALIAZTION_SUFFIX)) {
				return file;
			}
		}
		File file = new File(poolDirectory + "/" + workspaceId + SERIALIAZTION_SUFFIX);
		workspaceFile.add(file);
		return file;
	}

	/**
	 * Current pool directory, the directory where workspace serializations are
	 * saved.
	 */
	public static File getPoolDirectory() {
		File file = new File(poolDirectory);
		return file;

	}

	/**
	 * Get the workspace with given id.
	 * 
	 * @param id
	 */
	Workspace getWorkspace​(String id) throws MPJPException {
		Workspace workspace = null;
		if (availableWorkspaces.get(id) != null) {
			workspace = availableWorkspaces.get(id);
		} else {
			WorkspacePool.recover​(id);
		}
		return workspace;
	}

	/**
	 * Recover a workspace form a serialization given its ID
	 * 
	 * @param workspaceId
	 */
	static Workspace recover​(String fnme) throws MPJPException {
		ObjectInputStream objectinputstream = null;
		Workspace workspace = null;
		try {
			FileInputStream streamIn = new FileInputStream(poolDirectory + "/" + fnme);
			objectinputstream = new ObjectInputStream(streamIn);
			workspace = (Workspace) objectinputstream.readObject();
			workspace.restore();
			if (objectinputstream != null) {
				objectinputstream.close();
			}
		} catch (Exception e) {
			throw new MPJPException(e);
		} finally {

		}
		return workspace;
	}

	void reset() {
		availableWorkspaces = new TreeMap<>();
		availablePuzzleSelectInfo = new TreeMap<>();
	}

}