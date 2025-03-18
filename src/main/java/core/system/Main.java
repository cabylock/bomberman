package core.system;

import core.system.controller.MenuBoardController;

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

      MenuBoardController menuboard = new MenuBoardController(primaryStage);
      menuboard.createMenuScene();
      

      primaryStage.show();
    }
   
}
