package mpjp.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Simple Servlet for serving resources from a directory/package in the source
 * code. These files must be part of the WAR and fetched from the directory
 * where it is installed in the servlet container
 * <p>
 * The name of the package holding the resources can be managed using the static
 * methods {@link #getPackageName()} and {@link #setPackageName(String)}
 * <p>
 * Content types are to assigned resources based on their file extensions by the
 * {@link #getContentType(String)} method. Currently, only a couple of types are
 * recognized and default to {@code application/octet-stream}
 * 
 * This servlet must be configured in the {@code WEB-INF/web.xml} file of you
 * webapp using elements similar to the following. Notice the tailing asterisk
 * in the content of the {@code url-pattern} element.
 * 
 * <pre>
 * &lt;servlet&gt;
 *   &lt;servlet-name&gt;resourceServlet&lt;/servlet-name&gt;
 *   &lt;servlet-class&gt;mpjp.server.Resource&lt;/servlet-class&gt;
 * &lt;/servlet&gt;
 *
 * &lt;servlet-mapping&gt;
 *   &lt;servlet-name&gt;resourceServlet&lt;/servlet-name&gt;
 *   &lt;url-pattern&gt;/mpjp/resource/*&lt;/url-pattern&gt;
 * &lt;/servlet-mapping&gt;
 * </pre>
 * 
 * @author Jos&eacute; Paulo Leal {@code zp@dcc.fc.up.pt}
 *
 */
public class Resource extends HttpServlet {
	private static final int BUFFER_SIZE = 1024 * 1014;
	private static final long serialVersionUID = 1L;
	private static String BASE_IN_WAR = "WEB-INF/classes/";
	private static String packageName = "mpjp.resource";

	private File resourcesDir;

	/**
	 * Package name from where resources are currently being fetched
	 * 
	 * @return name of package
	 */
	public static String getPackageName() {
		return packageName;
	}

	/**
	 * Change name of the package/directory holding the resources It may be a binary
	 * (dot-separated) name
	 * 
	 * @param packageName new package name
	 */
	public static void setPackageName(String packageName) {
		Resource.packageName = packageName;
	}

	/**
	 * Sets the resource directory based on the WAR installation directory and the
	 * defined package name {@link #setPackageName(String)}
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		ServletContext context = getServletContext();
		File base = new File(context.getRealPath("/"));
		String packagePath = packageName.replace(".", "/");

		resourcesDir = new File(base, BASE_IN_WAR + packagePath);
	}

	/**
	 * Process HTTP requests to fetch resources
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();

		if (pathInfo == null) {
			error(resp, "No path info. Should be <code>/resources/<i>somefile</i></code");
			return;
		}

		String name = pathInfo.substring(1);
		File resource = new File(resourcesDir, name);

		if (!resource.exists()) {
			error(resp, "cannot file file for resource " + name);
			return;
		}

		if (!resource.canRead()) {
			error(resp, "no permission to read resource file for resource " + name);
			return;
		}

		resp.setContentType(getContentType(name));

		ServletOutputStream out = resp.getOutputStream();
		try (FileInputStream in = new FileInputStream(resource)) {
			byte buffer[] = new byte[BUFFER_SIZE];
			int len;
			while ((len = in.read(buffer)) > -1)
				out.write(buffer, 0, len);
		} catch (Exception e) {

		}
		out.close();
	}

	/**
	 * Return a content type based on the extension. Only a couple of types are
	 * currently supported. Defaults to {@code  application/octet-stream}
	 * 
	 * @param name of resource file
	 * @return content type
	 */
	String getContentType(String name) {
		int pos = name.lastIndexOf('.');
		String extension = (pos == -1 ? "" : name.substring(pos + 1));

		switch (extension) {
		case "png":
			return "image/png";
		case "jpeg":
		case "jpg":
			return "image/jpeg";
		case "wave":
		case "wav":
			return "video/wave";
		default:
			return "application/octet-stream";
		}
	}

	/**
	 * HTML format given error message in HTTP response.
	 * 
	 * @param resp
	 * @param message text
	 * @throws IOException raised by write operations
	 */
	private void error(HttpServletResponse resp, String message) throws IOException {
		PrintWriter out = resp.getWriter();
		resp.setContentType("text/HTML");
		out.println("<h2>Error in resource servlet</h2>");
		out.println("<p>" + message + "</p>");
	}

}
