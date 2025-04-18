package core.system.controller.base;

import core.system.game.BombermanGame;
import core.system.setting.Setting;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ModeController {
    private Stage stage;
    private String mapName;
    private int mapType;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void selectSinglePlayerMode() {
        // Set the game to 1 player mode
        Setting.GAME_MODE = Setting.SINGLE_MODE;
        Setting.PLAYER_NUM = 1;
        startGame();
    }

    @FXML
    private void selectMultiPlayerMode() {
        // Set the game to 2 player mode
        Setting.GAME_MODE = Setting.MULTI_MODE;
        Setting.PLAYER_NUM = 2;
        startGame();

    }
    
    @FXML
    private void selectOnlineMode() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/system/fxml/base/NetworkSetup.fxml"));
            Parent root = loader.load();

            NetworkSetupController controller = loader.getController();
            controller.setMap(mapName, mapType);
            controller.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setMap(String mapName, int mapType) {
        this.mapName = mapName;
        this.mapType = mapType;
    }
    

    private void startGame()
    {
        BombermanGame game = new BombermanGame(mapName, mapType);
        game.createGameScene(stage);
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
