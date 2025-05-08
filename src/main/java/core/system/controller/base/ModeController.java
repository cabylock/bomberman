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
   
    public void setStage(Stage stage) {
        this.stage = stage;
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
        


        GameControl.loadMap(Setting.MAP_NAME);
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
