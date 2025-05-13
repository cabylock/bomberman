package core.sound;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {
    private static MediaPlayer bgPlayer;
    private static boolean isMuted = false;
    private static String currentMusic = "";
    private static boolean wasLooping = false;

    public static void playMusic(String sound, boolean loop) {
        if (isMuted) {
            currentMusic = sound;
            wasLooping = loop;
            return;
        }
        stopMusic();
        String url = Sound.class.getResource("/sounds/" + sound + ".mp3").toExternalForm();
        bgPlayer = new MediaPlayer(new Media(url));
        bgPlayer.setCycleCount(loop ? MediaPlayer.INDEFINITE : 1);
        bgPlayer.play();
        currentMusic = sound;
        wasLooping = loop;
    }

    public static void stopMusic() {
        if (bgPlayer != null) {
            bgPlayer.stop();
            bgPlayer.dispose();
            bgPlayer = null;
        }
    }

    public static void playEffect(String sound) {
        if (isMuted)
            return;
        String url = Sound.class.getResource("/sounds/" + sound + ".mp3").toExternalForm();
        MediaPlayer fx = new MediaPlayer(new Media(url));
        fx.setCycleCount(1);
        fx.play();
    }

    public static void toggleMute() {
        isMuted = !isMuted;
        if (isMuted) {
            stopMusic();
        } else {
            // Resume music if there was music playing before
            if (!currentMusic.isEmpty()) {
                playMusic(currentMusic, wasLooping);
            }
        }
    }

    public static boolean isMuted() {
        return isMuted;
    }
}
