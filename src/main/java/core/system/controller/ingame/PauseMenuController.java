package core.system.controller.ingame;

import core.system.game.BombermanGame;
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
   private Button menuButton;

   @FXML
   private Button exitButton;

   private BombermanGame game;
   private StackPane overlay;

   public void setGame(BombermanGame game) {
      this.game = game;
   }

   public void setOverlay(StackPane overlay) {
      this.overlay = overlay;
   }

   @FXML
   private void handleResume() {
      if (overlay != null) {
         overlay.getChildren().clear();
         game.resumeGame();
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
   private void handleRestart() {
      if (overlay != null) {
         overlay.getChildren().clear();
         game.restartGame();
      }
   }

   @FXML
   private void handleReturnToMenu() {
      if (game != null) {
         game.returnToMenu();
      }
   }

   @FXML
   private void handleExit() {
      Stage stage = (Stage) pauseMenuRoot.getScene().getWindow();
      stage.close();
   }
}
