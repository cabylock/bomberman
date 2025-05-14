package core.util;

import core.graphics.Sprite;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import java.util.Random;
import javafx.scene.control.Button;
import javafx.geometry.Pos;

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

   public static void generateCustomMap(int level, int height, int width, String name) {
      MapGenerator.generateMap(level, height, width, name);
   }

   public static void logInfo(String message) {
      System.out.println("[INFO] " + message);
   }

   public static void logError(String message) {
      System.err.println("[ERROR] " + message);
   }

   public static void showOverlayWithButton(String filePath, StackPane gameRoot,
         String buttonText, Runnable onClick) {
      for (javafx.scene.Node node : gameRoot.getChildren()) {
         if ("levelOverlay".equals(node.getId())) {
            return;
         }
      }

      Image image = new Image(Util.class.getResourceAsStream(filePath));
      ImageView imageView = new ImageView(image);
      imageView.setPreserveRatio(true);
      imageView.setFitHeight(gameRoot.getHeight() * 0.5);

      Button nextButton = new Button(buttonText);
      nextButton.setStyle("-fx-font-size:18px; -fx-background-color:#44c767; -fx-text-fill:white;");

      StackPane overlay = new StackPane(imageView, nextButton);
      overlay.setId("levelOverlay");
      overlay.setStyle("-fx-background-color: rgba(0,0,0,0.7);");
      overlay.setPrefSize(gameRoot.getWidth(), gameRoot.getHeight());
      StackPane.setAlignment(nextButton, Pos.BOTTOM_CENTER);

      nextButton.setOnAction(_ -> {
         gameRoot.getChildren().remove(overlay);
         if (onClick != null)
            onClick.run();
      });

      gameRoot.getChildren().add(overlay);
   }

   public static void showGameOverOverlay(String filePath, StackPane gameRoot, Runnable onPlayAgain) {
      for (javafx.scene.Node node : gameRoot.getChildren()) {
         if ("gameOverOverlay".equals(node.getId())) {
            return;
         }
      }

      Image image = new Image(Util.class.getResourceAsStream(filePath));
      ImageView imageView = new ImageView(image);
      imageView.setPreserveRatio(true);
      imageView.setFitHeight(gameRoot.getHeight() * 0.5);

      Button playAgainButton = new Button("Play Again");
      playAgainButton.setStyle("-fx-font-size:18px; -fx-background-color:#e74c3c; -fx-text-fill:white;");

      StackPane overlay = new StackPane(imageView, playAgainButton);
      overlay.setId("gameOverOverlay");
      overlay.setStyle("-fx-background-color: rgba(0,0,0,0.7);");
      overlay.setPrefSize(gameRoot.getWidth(), gameRoot.getHeight());
      StackPane.setAlignment(playAgainButton, Pos.BOTTOM_CENTER);

      playAgainButton.setOnAction(_ -> {
         gameRoot.getChildren().remove(overlay);
         if (onPlayAgain != null)
            onPlayAgain.run();
      });

      gameRoot.getChildren().add(overlay);
   }

}
