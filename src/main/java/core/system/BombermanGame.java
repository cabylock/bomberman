package core.system;

import core.entity.*;
import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.entity.map_handle.MapEntity;
import core.entity.dynamic_entity.mobile_entity.enemy_entity.*;
import core.graphics.*;
import core.system.controller.ModeController;
import core.util.Util;
import javafx.scene.input.KeyCode;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.util.Set;
import java.util.HashSet;



public class BombermanGame {





    private GraphicsContext gc;
    private Canvas canvas;
    public static final Set<KeyCode> input = new HashSet<>();
    private AnimationTimer gameLoop;
    private Stage stage;

    // UI elements for status bar
    private Text healthText;
    private Text bombsText;
    private Text enemiesText;
    private Text scoreText;

    // for custom map
    public BombermanGame(int level, String mapName) {
        Util.generateRandomMap(level, mapName,Setting.PLAYER_NUM);

        try {
            Thread.sleep(1000);
          
            MapEntity.loadMap(mapName, Setting.CUSTOM_MAP);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    // for more custom map
    public BombermanGame(int level, int width, int height, String mapName) {
        Util.generateCustomMap(level, height, width, mapName, Setting.PLAYER_NUM);

        try {
            Thread.sleep(1000);
          
            MapEntity.loadMap(mapName, Setting.CUSTOM_MAP);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // for default or custom map
    public BombermanGame(String mapName, int mapType) {
        if (mapType == Setting.DEFAULT_MAP) {
            int level = mapName.charAt(mapName.length() - 5) - '0';
          
            MapEntity.loadMap(level);
        } else if (mapType == Setting.CUSTOM_MAP) {
          
            MapEntity.loadMap(mapName, Setting.CUSTOM_MAP);
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

        // Create status bar
        HBox statusBar = createStatusBar();

        // Create root container with VBox to stack game canvas and status bar
        VBox root = new VBox(5);
        root.getChildren().addAll(statusBar, canvas);

        // Create scene
        Scene scene = new Scene(root);

        scene.setOnKeyPressed(e -> {
            input.add(e.getCode());

            // Check for ESC key to return to menu
            if (e.getCode() == KeyCode.ESCAPE) {
                returnToMenu();
            }
            if (e.getCode() == KeyCode.R && e.isControlDown()) {
                restartGame();
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
                updateStatusBar();
            }
        };
        gameLoop.start();
    }

    /**
     * Create a status bar with game information
     */
    private HBox createStatusBar() {
        HBox statusBar = new HBox(20);
        statusBar.setPadding(new Insets(10));
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        // Create text elements for game stats
        healthText = new Text("❤ Health: 3");
        healthText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        healthText.setFill(Color.WHITE);

        bombsText = new Text("💣 Bombs: 1");
        bombsText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        bombsText.setFill(Color.WHITE);

        enemiesText = new Text("👾 Enemies: 0");
        enemiesText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        enemiesText.setFill(Color.WHITE);

        scoreText = new Text("🏆 Score: 0");
        scoreText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        scoreText.setFill(Color.WHITE);

        // Add text elements to status bar
        statusBar.getChildren().addAll(healthText, bombsText, enemiesText, scoreText);

        return statusBar;
    }

    /**
     * Update the status bar with current game information
     */
    private void updateStatusBar() {
        // Get player entity to access health and bombs
        Entity playerEntity = null;
        for (Entity entity : MapEntity.getDynamicEntities()) {
            if (entity instanceof Bomber) {
                playerEntity = entity;
                break;
            }
        }

        if (playerEntity instanceof Bomber) {
            Bomber player = (Bomber) playerEntity;
            // Update health display
            // int health = player.getLives(); // Assuming Player class has getLives() method
            int health = 1;
            healthText.setText("❤ Health: " + health);

            // Update bombs display
            // int bombs = player.getBombCount(); // Assuming Player class has getBombCount() method
            int bombs = 1;
            bombsText.setText("💣 Bombs: " + bombs);
        }

        // Count enemies
        int enemyCount = 0;
        for (Entity entity : MapEntity.getDynamicEntities()) {
            if (entity instanceof EnemyEntity) { // Assuming Enemy class exists
                enemyCount++;
            }
        }
        enemiesText.setText("👾 Enemies: " + enemyCount);

        // Update score (if applicable)
        int score = 0; // You might want to track score in Player class or elsewhere
        scoreText.setText("🏆 Score: " + score);
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

        try {
            // Load the MapSelection screen instead of Main
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/system/fxml/Mode.fxml"));
            Parent root = loader.load();

            // Get the controller and set the stage
            ModeController controller = loader.getController();
            controller.setStage(stage);

            // Set the scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();

            // Fallback to main menu if loading map selection fails
            Main main = new Main();
            main.start(stage);
        }

    }

    
    private void restartGame()
    {
       
        
        MapEntity.reset();
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
