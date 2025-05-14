package core.system.entry;

import core.system.controller.base.MainController;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

   public static void main(String[] args) {
      launch(args);
   }

   @Override
   public void start(Stage primaryStage) {

      primaryStage.setTitle("Bomberman");
      primaryStage.getIcons().add(new Image("/textures/icon_game.png"));
      primaryStage.setResizable(true);

      MainController mainboard = new MainController(primaryStage);
      mainboard.createMenuScene();

      primaryStage.show();
   }

}
