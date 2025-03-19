package core.system.controller;

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/system/fxml/MainMenu.fxml"));
            // System.out.println("location: " + getClass().getResource("/core/system/fxml/Main.fxml"));
            Parent root = loader.load();

            // Get the controller
            MainMenuController controller = loader.getController();
            controller.setStage(stage);

            // Set the scene
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
