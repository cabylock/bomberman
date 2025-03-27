package core.system.controller.base;

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
   public void initialize() {

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

         // Create the scene first with explicit dimensions
         Scene scene = new Scene(root);
         dialogStage.setScene(scene);

         // Force minimum size on the stage
         dialogStage.setMinWidth(500);
         dialogStage.setMinHeight(400);

         controller.setDialogStage(dialogStage);

       

         dialogStage.showAndWait();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   @FXML
   private void exit() {
      stage.close();
   }

}
