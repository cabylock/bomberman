package core.util;

import core.graphics.Sprite;
import core.system.setting.Setting;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import java.util.Random;

public class Util {

   public static int toGrid(float x) {
      return (int) ((x + Sprite.DEFAULT_SIZE / 2) / Sprite.DEFAULT_SIZE);
   }

   public static int randomDirection() {
      Random random = new Random();
      return random.nextInt(4);

   }

   public static int uuid() {
      Random random = new Random();
      int id = random.nextInt(100000);
      return id;
   }

   public static void sleep(int seconds) {
      try {
         Thread.sleep(seconds * 1000);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }

   public static int randomRange(int min, int max) {
      Random random = new Random();
      return random.nextInt(max - min + 1) + min;
   }

   /**
    * Generate a random map with custom dimensions
    * 
    * @param level  Level number
    * @param height Map height
    * @param width  Map width
    */
   public static void generateCustomMap(int level, int height, int width, String name) {
      MapGenerator.generateMap(level, height, width, name);
   }

   /**
    * Logs a message to the console for developers
    * 
    * @param message The message to log
    */
   public static void logInfo(String message) {
      System.out.println("[INFO] " + message);
   }

   /**
    * Logs an error message to the console for developers
    * 
    * @param message The error message to log
    */
   public static void logError(String message) {
      System.err.println("[ERROR] " + message);
   }

   public static void showImage(String filePath, StackPane stackPane) {
      // Create a new stage (window)

      // Load the image

      Image image = new Image(Util.class.getResourceAsStream(filePath));

      // Create an image view
      ImageView imageView = new ImageView(image);

      // Create layout and add the image view
      if (stackPane == null) {
         stackPane = new StackPane();
         stackPane.getChildren().add(imageView);

         Stage imageStage = new Stage();
         imageStage.setTitle(null);
         javafx.application.Platform.runLater(() -> {
            imageStage.setWidth(Setting.SCREEN_WIDTH);
            imageStage.setHeight(Setting.SCREEN_HEIGHT);
         });

         Scene scene = new Scene(stackPane, Setting.SCREEN_WIDTH, Setting.SCREEN_HEIGHT);
         // Create scene with appropriate size
         imageStage.setScene(scene);
         imageStage.centerOnScreen();

         // Show the window
         imageStage.show();

      } else {

         stackPane.getChildren().clear();
         stackPane.getChildren().add(imageView);
      }

      stackPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

   }

}
