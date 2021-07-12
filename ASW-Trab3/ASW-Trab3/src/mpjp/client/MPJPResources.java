package mpjp.client;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Access to resources -- images and audio -- stored in the server in a Java
 * source directory. By default, this directory is {@code /mpjp/resource/ } but
 * may be changed using {@link #setResourceDir(String)}.
 * 
 * All methods in this class are static. The {@link #loadAudio(String)} method
 * returns an audio object that may be played using it as context for the
 * {@code play()} method as in the following example.
 * 
 * <pre>
 * MPJPAudioResource clip = MPJPResources.loadAudio(soundName);
 * clip.play();
 * </pre>
 * 
 * The {@link #loadImageElement(String, Consumer)} method is void. It receives
 * as argument a consumer of a {@code ImageElement} that is invoked when the
 * image is available, as in the following example
 * 
 * <pre>
 * MPJPResources.loadImageElement(imageName, i -&gt; {
 * 	image = i;
 * 	paint();
 * });
 * </pre>
 * 
 * This {@code ImageElement} can be given as argument to the
 * {@code Context2d.drawImage()} method.
 * 
 * @author Jos&eacute; Paulo Leal {@code zp@dcc.fc.up.pt}
 */
public class MPJPResources {
	private static String resourceDir = "/mpjp/resource/";

	/**
	 * Pathname of the remote directory holding the resources fetch using HTTP. This
	 * pathname should correspond to the prefix configured in
	 * {@code WEB-INF/web.xml} with the {@code <url-pattern>} element, contained in
	 * the {@code <servlet-mapping>} element.
	 * 
	 * @return resource directory
	 */
	public static String getResourceDir() {
		return resourceDir;
	}

	/**
	 * Change the pathname of the remote directory holding the resources fetch using
	 * HTTP. This pathname should correspond to the prefix configured in
	 * {@code WEB-INF/web.xml} with the {@code <url-pattern>} element, contained in
	 * the {@code <servlet-mapping>} element.
	 * 
	 * @param resourceDir new name of resource dir
	 */
	public static void setResourceDir(String resourceDir) {
		MPJPResources.resourceDir = resourceDir;
	}

	/**
	 * An audio resource implemented as a native JavaScript Audio element GWT
	 * doesn't have audio support and needs to be implemented as native (JavaScript)
	 * method calls. Audio resources are played by invoking their {@code play()}
	 * method
	 */
	static public class MPJPAudioResource {
		JavaScriptObject audio;

		/**
		 * Create an audio resource loaded from the server
		 * 
		 * @param name
		 */
		public MPJPAudioResource(String name) {
			audio = loadSoundClip(name, resourceDir);
		}

		/**
		 * Play this audio resource
		 */
		public void play() {
			
			playSoundClip(audio);
		}

		/**
		 * Play this audio resource
		 */
		public void pause() {
			pauseSoundClip(audio);
		}

		private native JavaScriptObject loadSoundClip(String name, String dir) /*-{
			return new Audio(dir + name);
		}-*/;

		private native void playSoundClip(JavaScriptObject audio) /*-{
			audio.currentTime = 0;
			audio.volume = 0.01;
			audio.play();
		}-*/;

		private native void pauseSoundClip(JavaScriptObject audio) /*-{
			audio.pause();

		}-*/;
	}

	/**
	 * Load an audio resource. The returned instance can be used for playing the
	 * audio clip
	 * 
	 * <pre>
	 * MPJPResources.loadAudio(soundName).play();
	 * </pre>
	 * 
	 * @param audioName name of the audio clip (without directories)
	 * @return an audio clip
	 */
	static MPJPAudioResource loadAudio(String audioName) {
		if (audioName == null || "".equals(audioName))
			throw new RuntimeException("Invalid empty image name");
		else
			return new MPJPAudioResource(audioName);
	}

	private static Map<String, ImageElement> images = new HashMap<>();

	/**
	 * Load given image and execute consumer when loaded. Locally available images
	 * are reused, otherwise are loaded from the server. A typical use is
	 * 
	 * <pre>
	 * MPJPResources.loadImageElement(imageName, i -&gt; {
	 * 	image = i;
	 * 	paint();
	 * });
	 * </pre>
	 * 
	 * @param imageName name of image to load
	 * @param onLoad    method to consume image when loaded
	 */
	public static void loadImageElement(String imageName, Consumer<ImageElement> onLoad) {

		ImageElement image = images.get(imageName);

		if (image == null) {
			doLoadImageElement(imageName, i -> {
				images.put(imageName, i);
				onLoad.accept(i);

			});

		} else
			onLoad.accept(image);

	}

	private static void doLoadImageElement(String imageName, Consumer<ImageElement> onLoad) {

		if (imageName == null || "".equals(imageName))
			throw new RuntimeException("Invalid empty image name");
		else {
			final RootPanel rootPanel = RootPanel.get();
			final Image imageInstance = new Image();
			final Element element = imageInstance.getElement();

			imageInstance.addErrorHandler(e -> {
				consoleLog("error loading image " + imageName);
			});

			imageInstance.addLoadHandler(e -> {
				rootPanel.remove(imageInstance);
				onLoad.accept(ImageElement.as(element));
			});

			element.getStyle().setDisplay(Display.NONE);
			imageInstance.getElement().setId("img");
			rootPanel.add(imageInstance); // add image to page to force loading
			imageInstance.setUrl(resourceDir + imageName);
		}
	}

	/**
	 * Show object on the browser's console. Used mostly for reporting errors that
	 * cannot be thrown as exceptions.
	 * 
	 * @param object to show on console
	 */
	private static native void consoleLog(Object object) /*-{
		console.log(object.toString());
	}-*/;
}
