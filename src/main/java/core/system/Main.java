package core.system;

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

      MenuBoard menuBoard = new MenuBoard(primaryStage);
         menuBoard.createMenuScene();
         primaryStage.show();
    }
   
}
