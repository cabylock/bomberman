package core.system.game;

import core.entity.*;
import core.entity.dynamic_entity.mobile_entity.enemy_entity.*;
import core.graphics.*;
import core.system.controller.base.MainMenuController;
import core.system.controller.ingame.PauseMenuController;
import core.system.entry.Main;
import core.system.setting.Setting;
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
import javafx.scene.layout.StackPane;
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
    private Stage stage;

    private long lastUpdateTime = 0;
    private double deltaTime = 0.0; // Time between frames in seconds

    // UI elements for status bar
    private Text healthText;
    private Text bombsText;
    private Text enemiesText;
    private Text scoreText;

    // Add these fields to your BombermanGame class
    private AnimationTimer gameLoop;
    private static StackPane gameRoot;
    private boolean isPaused = false;





    // for default or custom map
    public BombermanGame(String mapName, int mapType) {
        if (mapType == Setting.DEFAULT_MAP) {
            int level = mapName.charAt(mapName.length() - 5) - '0';

            GameControl.loadMap(level);
        } else if (mapType == Setting.CUSTOM_MAP) {

            GameControl.loadMap(mapName, Setting.CUSTOM_MAP);
        }
    }

    public static void main(String[] args) {

        Application.launch(args);
    }

    // Then update your createGameScene method:
    public void createGameScene(Stage stage) {
        this.stage = stage;

        // Create Canvas
        canvas = new Canvas(Sprite.SCALED_SIZE * GameControl.getWidth(), Sprite.SCALED_SIZE * GameControl.getHeight());
        gc = canvas.getGraphicsContext2D();

        // Create status bar
        HBox statusBar = createStatusBar();

        // Create game content container
        VBox gameContent = new VBox(5);
        gameContent.getChildren().addAll(statusBar, canvas);

        // Create a StackPane for overlays
        gameRoot = new StackPane();
        gameRoot.getChildren().add(gameContent);

        // Create scene
        Scene scene = new Scene(gameRoot);

        scene.setOnKeyPressed(e -> {
            input.add(e.getCode());

            // Check for ESC key to show pause menu
            if (e.getCode() == KeyCode.ESCAPE) {
                if (!isPaused) {
                    showPauseMenu();
                }
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

        GameControl.start(Setting.GAME_MODE);

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Calculate delta time in seconds (from nanoseconds)
                if (lastUpdateTime > 0) {
                    deltaTime = (now - lastUpdateTime) / 1_000_000_000.0;
                    // Cap delta time to prevent huge jumps after lag
                    if (deltaTime > 0.08)
                        deltaTime = 0.08;
                }

                render();

                // Always update with delta time regardless of frame timing
                GameControl.update(deltaTime);
                updateStatusBar();

                lastUpdateTime = now;
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

        enemiesText = new Text("👻 Enemies: 0");
        enemiesText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        enemiesText.setFill(Color.WHITE);

        scoreText = new Text("🏆 Score: 0");
        scoreText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        scoreText.setFill(Color.WHITE);

        // In your createStatusBar method, add:

        // Add to status bar
        statusBar.getChildren().addAll(healthText, bombsText, enemiesText, scoreText);

        return statusBar;
    }

    /**
     * Update the status bar with current game information
     */
    private void updateStatusBar() {

        // Count enemies
        int enemyCount = 0;
        for (Entity entity : GameControl.getEnemyEntities()) {
            if (entity instanceof EnemyEntity) { // Assuming Enemy class exists
                enemyCount++;
            }
        }
        enemiesText.setText("👾 Enemies: " + enemyCount);

        // Update score (if applicable)
        int score = 0; // You might want to track score in Player class or elsewhere
        scoreText.setText("🏆 Score: " + score);
    }

    // Add these new methods to BombermanGame class:
    public static StackPane getGameRoot() {
        return gameRoot;
    }

    /**
     * Show the pause menu overlay
     */
    private void showPauseMenu() {
        // Pause the game
        isPaused = true;
        gameLoop.stop();

        try {
            // Load the pause menu FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/system/fxml/ingame/PauseMenu.fxml"));
            VBox pauseMenu = loader.load();

            // Get controller and set the game reference
            PauseMenuController controller = loader.getController();
            controller.setGame(this);

            // Create overlay container
            StackPane overlay = new StackPane();
            overlay.getChildren().add(pauseMenu);
            controller.setOverlay(overlay);

            // Add to game root
            gameRoot.getChildren().add(overlay);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading pause menu: " + e.getMessage());
            // If pause menu fails, just unpause
            resumeGame();
        }
    }

    /**
     * Resume the game from pause
     */
    public void resumeGame() {
        isPaused = false;
        gameLoop.start();
    }

    public void nextLevel() {
        // Resume game if paused
        isPaused = false;

        // Stop and restart the game loop
        if (gameLoop != null) {
            gameLoop.stop();
        }

        // Load the next level
        GameControl.nextLevel();
        gameLoop.start();
    }

    // Update your restartGame method to support the pause menu
    public void restartGame() {
        // Resume game if paused
        isPaused = false;

        // Stop and restart the game loop
        if (gameLoop != null) {
            gameLoop.stop();
        }

        // Reset the game
        GameControl.resetGame();
        gameLoop.start();
    }

    // Make returnToMenu method public so the controller can access it
    public void returnToMenu() {
        // Stop the game loop
        if (gameLoop != null) {
            gameLoop.stop();
            gameLoop = null;
        }
        GameControl.clear();
        GameControl.stop();
        // Reset the game state if needed
        input.clear();

        try {
            // Load the MapSelection screen instead of Main
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/system/fxml/base/MainMenu.fxml"));
            Parent root = loader.load();

            // Get the controller and set the stage
            MainMenuController controller = loader.getController();
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

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        GameControl.getBackgroundEntities().forEach(entity -> entity.render(gc));
        GameControl.getItemEntities().forEach(entity -> entity.render(gc));
        GameControl.getStaticEntities().forEach(entity -> entity.render(gc));
        GameControl.getBomberEntities().forEach(entity -> entity.render(gc));
        GameControl.getEnemyEntities().forEach(entity -> entity.render(gc));
    }
}
