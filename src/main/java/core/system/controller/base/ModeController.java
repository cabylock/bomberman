package core.system.controller.base;

import core.system.Setting;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ModeController {
    private Stage stage;

    @FXML
    private Text playerModeText;

    @FXML
    private Text playerCountText;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        // Update the player mode text based on Setting.PLAYER_NUM
        if (Setting.PLAYER_NUM == 2) {
            playerModeText.setText("2 Player Mode");

        } else {
            playerModeText.setText("1 Player Mode");

        }
    }

    @FXML
    private void showAvailableMaps() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/system/fxml/base/MapSelection.fxml"));
            Parent root = loader.load();

            MapSelectionController controller = loader.getController();
            controller.setStage(stage);
            controller.loadMaps();

            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void createRandomMap() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/system/fxml/base/RandomMap.fxml"));
            Parent root = loader.load();

            RandomMapController controller = loader.getController();
            controller.setStage(stage);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Create Random Map");
            dialogStage.initOwner(stage);

            controller.setDialogStage(dialogStage);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/system/fxml/base/MainMenu.fxml"));
            Parent root = loader.load();

            MainMenuController controller = loader.getController();
            controller.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
