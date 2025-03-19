package core.system;

import core.entity.*;
import core.entity.map_handle.MapEntity;
import core.graphics.*;
import core.util.Util;
import javafx.scene.input.KeyCode;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;

public class BombermanGame {

    public static final int DEFAULT = 0;
    public static final int CUSTOM = 1;
    public static int WIDTH = 30;
    public static int HEIGHT = 20;

    public static String filePath;
    private GraphicsContext gc;
    private Canvas canvas;
    public static final Set<KeyCode> input = new HashSet<>();
    private AnimationTimer gameLoop;
    private Stage stage;



    //for random map
    public BombermanGame(int level, String mapName) {
        Util.generateRandomMap(level, mapName);

        try {
            Thread.sleep(1000);
            MapEntity.loadMap(mapName, CUSTOM);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public BombermanGame(int level, int width, int height, String mapName) {
        Util.generateCustomMap(level, height, width, mapName);

        try {
            Thread.sleep(1000);
            MapEntity.loadMap(mapName, CUSTOM);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    //for default or custom map
    public BombermanGame(String mapName ,int mapType) {
      
        if(mapType== DEFAULT)
        {
            int level = mapName.charAt(mapName.length() - 5) - '0';
            MapEntity.loadMap(level);
        }
        else if(mapType== CUSTOM)
        {
            MapEntity.loadMap(mapName, CUSTOM);
        }
    }

   

    public static void main(String[] args) {
        Application.launch(args);
    }

    public void createGameScene(Stage stage) {
        this.stage = stage;

   
        // Create Canvas
        canvas = new Canvas(Sprite.SCALED_SIZE * MapEntity.getWidth(), Sprite.SCALED_SIZE * MapEntity.getHeight());
        gc = canvas.getGraphicsContext2D();

        // Create root container
        Group root = new Group();
        root.getChildren().add(canvas);

        // Create scene
        Scene scene = new Scene(root);

        scene.setOnKeyPressed(e -> {
            input.add(e.getCode());

            // Check for ESC key to return to menu
            if (e.getCode() == KeyCode.ESCAPE) {
                returnToMenu();
            }
        });

        scene.setOnKeyReleased(e -> {
            input.remove(e.getCode());
        });

        // Add scene to stage
        stage.setScene(scene);
        stage.show();

        // Store the animation timer so we can stop it later
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update();
                render();
            }
        };
        gameLoop.start();
    }

    /**
     * Return to main menu
     */
    private void returnToMenu() {
        // Stop the game loop
        if (gameLoop != null) {
            gameLoop.stop();
            gameLoop = null; 
        }
        MapEntity.clear();
        // Reset the game state if needed
        input.clear();

        // Create new menu and show it
        Main main = new Main();
        main.start(stage);
    }

 
    public void update() {
        MapEntity.update();
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        MapEntity.getBackgroundEntities().forEach(entity -> entity.render(gc));
        MapEntity.getItemEntities().forEach(entity -> entity.render(gc));
        for (Entity entity : MapEntity.getDynamicEntities()) {
            entity.render(gc);
        }
    }
}
