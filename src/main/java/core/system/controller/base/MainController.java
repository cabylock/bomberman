package core.system.controller.base;

import core.system.setting.Setting;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController {
    public static final int DEFAULT = 0;
    public static final int CUSTOM = 1;

    private Stage stage;

    public MainController(Stage stage) {
        this.stage = stage;
    }

    public void createMenuScene() {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/system/fxml/base/MainMenu.fxml"));
            // System.out.println("location: " +
            // getClass().getResource("/core/system/fxml/base/Main.fxml"));
            Parent root = loader.load();

            // Get the controller
            MainMenuController controller = loader.getController();
            controller.setStage(stage);

            // Set the scene
            Scene scene = new Scene(root, Setting.SCREEN_WIDTH, Setting.SCREEN_HEIGHT);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
