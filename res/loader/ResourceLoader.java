package loader;
import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

public class ResourceLoader extends ClassLoader {

	public static ResourceLoader rL = new ResourceLoader();
	
	public static Image getImage(String filename) {
		InputStream input = ResourceLoader.class.getResourceAsStream("/img/" + filename);
		Image image = new Image(input);
		return image;
	}
	
	public static AudioClip getAudioClip(String filename) {
		String path = ResourceLoader.class.getResource("/filename").toString();
		AudioClip audioclip = new AudioClip(path);
		return audioclip;
	}
}
