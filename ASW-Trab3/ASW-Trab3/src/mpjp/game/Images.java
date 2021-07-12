package mpjp.game;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Static methods for accessing the names of the images that may be used in
 * jigsaw puzzles. The static properties imageDirectory and extensions may be
 * used to control what images are selected.
 *
 */
public class Images {
	static Set<String> extensions = new HashSet<>(Arrays.asList("jpg"));
	static File imageDirectory;

	public static Set<String> getExtensions() {
		return extensions;
	}

	public static void setExtensions(Set<String> extensions) {
		Images.extensions = extensions;
	}

	public static void setImageDirectory(File imageDirectory) {
		Images.imageDirectory = imageDirectory;
	}

	static Set<String> getAvailableImages() {
	
		Set<String> availableImages = new HashSet<>();
		for (String extension : extensions) {
			for (String file : imageDirectory.list()) {
				if (file.contains(extension)) {
					availableImages.add(file);
				}
			}
		}
		return availableImages;
	}

	public static File getImageDirectory() {
		return imageDirectory;
	}

	public static void addExtension​(String extension) {
		extensions.add(extension);
	}
}
