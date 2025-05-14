package core.system.controller.base;

import core.system.setting.Setting;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController {

    private Stage stage;

    public MainController(Stage stage) {
        this.stage = stage;
    }

    public void createMenuScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/system/fxml/base/MainMenu.fxml"));
            Parent root = loader.load();

            MainMenuController controller = loader.getController();
            controller.setStage(stage);

            Scene scene = new Scene(root, Setting.SCREEN_WIDTH, Setting.SCREEN_HEIGHT);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
