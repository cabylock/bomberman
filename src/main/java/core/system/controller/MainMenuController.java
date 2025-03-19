package core.system.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.text.Text;

public class MainMenuController {
   @FXML
   private Text titleText;
   private Stage stage;

   public void setStage(Stage stage) {
      this.stage = stage;
   }

   @FXML
   private void showMapSelection() {
      try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/system/fxml/MapSelection.fxml"));
         Parent root = loader.load();

         MapSelectionController controller = loader.getController();
         controller.setStage(stage);
         controller.loadMaps();

         Scene scene = new Scene(root, 800, 600);
         stage.setScene(scene);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   @FXML
   private void showRandomMap() {
      try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/system/fxml/RandomMap.fxml"));
         Parent root = loader.load();

         RandomMapController controller = loader.getController();
         controller.setStage(stage);

         Stage dialogStage = new Stage();
         dialogStage.setTitle("Create Random Map");
         dialogStage.initModality(Modality.WINDOW_MODAL);
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
   private void exitGame() {
      stage.close();
   }
}
