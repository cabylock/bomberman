package core.system.controller.base;

import core.system.game.BombermanGame;
import core.system.game.GameControl;
import core.system.setting.Setting;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ModeController {
    private Stage stage;
    private String mapName; // Add field to store the map name

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Add setter for map name
    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    @FXML
    private void selectSinglePlayerMode() {
        // Set the game to 1 player mode
        Setting.GAME_MODE = Setting.SINGLE_MODE;
        startGame();
    }

    @FXML
    private void selectMultiPlayerMode() {
        // Set the game to 2 player mode
        Setting.GAME_MODE = Setting.MULTI_MODE;

        startGame();

    }

    @FXML
    private void selectOnlineMode() {
        try {
            Setting.GAME_MODE = Setting.SERVER_MODE;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/system/fxml/base/NetworkSetup.fxml"));
            Parent root = loader.load();

            NetworkSetupController controller = loader.getController();
            controller.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startGame() {

        BombermanGame.createGameScene(stage);

    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/system/fxml/base/MapSelection.fxml"));
            Parent root = loader.load();

            MapSelectionController controller = loader.getController();
            controller.loadMaps();
            controller.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
