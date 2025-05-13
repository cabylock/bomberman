package core.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SpriteManager {
    private static final Map<String, BufferedImage> sprites = new HashMap<>();
    private static final String SPRITE_PATH = "/sprites/";

    public static BufferedImage getImage(String filename) {
        if (!sprites.containsKey(filename)) {
            loadImage(filename);
        }
        return sprites.get(filename);
    }

    private static void loadImage(String filename) {
        try {
            URL url = SpriteManager.class.getResource(SPRITE_PATH + filename);
            if (url != null) {
                BufferedImage image = ImageIO.read(url);
                sprites.put(filename, image);
            } else {
                System.err.println("Could not find sprite: " + filename);
            }
        } catch (IOException e) {
            System.err.println("Error loading sprite: " + filename);
            e.printStackTrace();
        }
    }
}