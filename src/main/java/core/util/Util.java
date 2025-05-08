package core.util;

import core.graphics.Sprite;
import core.system.setting.Setting;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import java.util.Random;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.stage.Modality;

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
      int id = random.nextInt(10000);
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
   




   public static void showOverlayWithButton(String filePath,
         StackPane gameRoot,
         String buttonText,
         Runnable onClick) {
      // Kiểm tra nếu overlay đã tồn tại thì không thêm nữa
      for (javafx.scene.Node node : gameRoot.getChildren()) {
         if ("levelOverlay".equals(node.getId())) {
            return; // Đã có overlay → không làm gì thêm
         }
      }

      // Tạo ảnh overlay
      Image image = new Image(Util.class.getResourceAsStream(filePath));
      ImageView imageView = new ImageView(image);
      imageView.setPreserveRatio(true);
      imageView.setFitHeight(gameRoot.getHeight() * 0.5);

      // Tạo nút Next
      Button nextButton = new Button(buttonText);
      nextButton.setStyle("-fx-font-size:18px; -fx-background-color:#44c767; -fx-text-fill:white;");

      // Tạo overlay chứa ảnh và nút
      StackPane overlay = new StackPane(imageView, nextButton);
      overlay.setId("levelOverlay"); // đánh dấu để tránh trùng lặp
      overlay.setStyle("-fx-background-color: rgba(0,0,0,0.7);");
      overlay.setPrefSize(gameRoot.getWidth(), gameRoot.getHeight());
      StackPane.setAlignment(nextButton, Pos.BOTTOM_CENTER);

      // Hành động khi ấn nút
      nextButton.setOnAction(e -> {
         gameRoot.getChildren().remove(overlay);
         if (onClick != null)
            onClick.run();
      });

      // Thêm overlay vào gameRoot
      gameRoot.getChildren().add(overlay);
   }
   
   public static void showGameOverOverlay(String filePath,
         StackPane gameRoot,
         Runnable onPlayAgain) {
      // Tránh tạo nhiều overlay trùng
      for (javafx.scene.Node node : gameRoot.getChildren()) {
         if ("gameOverOverlay".equals(node.getId())) {
            return;
         }
      }

      // Tạo ảnh Game Over
      Image image = new Image(Util.class.getResourceAsStream(filePath));
      ImageView imageView = new ImageView(image);
      imageView.setPreserveRatio(true);
      imageView.setFitHeight(gameRoot.getHeight() * 0.5);

      // Nút Play Again
      Button playAgainButton = new Button("Play Again");
      playAgainButton.setStyle("-fx-font-size:18px; -fx-background-color:#e74c3c; -fx-text-fill:white;");

      // Overlay chứa ảnh và nút
      StackPane overlay = new StackPane(imageView, playAgainButton);
      overlay.setId("gameOverOverlay");
      overlay.setStyle("-fx-background-color: rgba(0,0,0,0.7);");
      overlay.setPrefSize(gameRoot.getWidth(), gameRoot.getHeight());
      StackPane.setAlignment(playAgainButton, Pos.BOTTOM_CENTER);

      // Hành động khi click
      playAgainButton.setOnAction(e -> {
         gameRoot.getChildren().remove(overlay);
         if (onPlayAgain != null)
            onPlayAgain.run();
      });

      // Thêm overlay vào game
      gameRoot.getChildren().add(overlay);
   }


   
}
