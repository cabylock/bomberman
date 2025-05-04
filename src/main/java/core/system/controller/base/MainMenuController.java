package core.system.controller.base;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import core.sound.Sound;

public class MainMenuController {
  
   private Stage stage;

   public void setStage(Stage stage) {
      this.stage = stage;
   }

   @FXML
   public void initialize() {
      Sound.playMusic("start_menu",false);
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
   private void exit() {
      
      stage.close();
   }

}
