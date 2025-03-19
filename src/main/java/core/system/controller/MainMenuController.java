package core.system.controller;

import core.system.Setting;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuController {
   private Stage stage;

   public void setStage(Stage stage) {
      this.stage = stage;
   }

   @FXML
   private void selectSinglePlayerMode() {
      // Set the game to 1 player mode
      Setting.PLAYER_NUM = 1;
      showMode(); // Go directly to map selection
   }

   @FXML
   private void selectMultiPlayerMode() {
      // Set the game to 2 player mode
      Setting.PLAYER_NUM = 2;
      showMode(); // Go directly to map selection
   }

   @FXML
   private void exit() {
      stage.close();
   }

   // This method is now private since it's only called internally
   private void showMode() {
      try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/system/fxml/Mode.fxml"));
         Parent root = loader.load();

         ModeController controller = loader.getController();
         controller.setStage(stage);
     

         Scene scene = new Scene(root, 800, 600);
         stage.setScene(scene);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
