package core.system.controller.ingame;

import core.system.game.BombermanGame;
import core.system.setting.Setting;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PauseMenuController {
   @FXML
   private VBox pauseMenuRoot;

   @FXML
   private Button resumeButton;

   @FXML
   private Button restartButton;

   @FXML
   private Button nextLevelButton;

   @FXML
   private Button menuButton;

   @FXML
   private Button exitButton;

   
   private StackPane overlay;

   public void setGame() {

      // Hide restart and next level buttons in client mode
      if (Setting.GAME_MODE == Setting.CLIENT_MODE) {
         if (restartButton != null) {
            restartButton.setVisible(false);
            restartButton.setManaged(false); // Remove from layout
         }

         if (nextLevelButton != null) {
            nextLevelButton.setVisible(false);
            nextLevelButton.setManaged(false); // Remove from layout
         }
      }
   }

   public void setOverlay(StackPane overlay) {
      this.overlay = overlay;
   }

   @FXML
   private void handleResume() {
      if (overlay != null) {
         overlay.getChildren().clear();
         BombermanGame.resumeGame();
      }
   }

   @FXML
   private void handleKeyPress(KeyEvent event) {
      if (event.getCode() == KeyCode.ESCAPE) {
         handleResume();
         event.consume();
      }
   }

   @FXML
   private void handleNextLevel() {
      if (overlay != null) {
         overlay.getChildren().clear();
         BombermanGame.nextLevel();
      }
   }

   @FXML
   private void handleRestart() {
      if (overlay != null) {
         overlay.getChildren().clear();
         BombermanGame.restartGame();
      }
   }

   @FXML
   private void handleReturnToMenu() {
      BombermanGame.returnToMenu();
   }

   @FXML
   private void handleExit() {
      Stage stage = (Stage) pauseMenuRoot.getScene().getWindow();
      stage.close();
   }
}
