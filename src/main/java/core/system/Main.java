package core.system;

import core.system.controller.MainController;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

   public static void main(String[] args) {
      launch(args);
   }

   @Override
   public void start(Stage primaryStage) {

      primaryStage.setTitle("Bomberman");
      primaryStage.setResizable(true);

      MainController mainboard = new MainController(primaryStage);
      mainboard.createMenuScene();

      primaryStage.show();
   }

}
