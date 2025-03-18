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
private void showRandomMapDialog() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("core/system/fxml/RandomMapDialog.fxml"));
        Parent root = loader.load();

        RandomMapController controller = loader.getController();
        controller.setStage(stage);
        
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Generate Random Map");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(stage);
        dialogStage.setScene(new Scene(root));
        
        // Set the dialog stage in the controller
        controller.setDialogStage(dialogStage);
        
        // Initialize after everything is set up
        controller.initialize();
        
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
