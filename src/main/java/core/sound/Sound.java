package core.sound;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {
    private static MediaPlayer bgPlayer;

    public static void playMusic(String sound, boolean loop) {
        stopMusic();
        String url = Sound.class.getResource("/sounds/" + sound + ".mp3").toExternalForm();
        bgPlayer = new MediaPlayer(new Media(url));
        bgPlayer.setCycleCount(loop ? MediaPlayer.INDEFINITE : 1);
        bgPlayer.play();
    }

    public static void stopMusic() {
        if (bgPlayer != null) {
            bgPlayer.stop(); // dừng ngay lập tức
            bgPlayer.dispose(); // giải phóng tài nguyên
            bgPlayer = null;
        }
    }

    public static void playEffect(String sound) {

        String url = Sound.class.getResource("/sounds/" + sound + ".mp3").toExternalForm();

        MediaPlayer fx = new MediaPlayer(new Media(url));
        fx.setCycleCount(1);
        fx.play();
    }
    
}
