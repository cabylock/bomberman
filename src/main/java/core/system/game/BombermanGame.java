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
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

import java.util.Set;
import java.util.HashSet;

public class BombermanGame {

    private int frameCounter = 0;
    private long lastFpsUpdateTime = 0;
    private int fps = 0;

    private GraphicsContext gc;
    private Canvas canvas;
    public static final Set<KeyCode> input = new HashSet<>();
    private Stage stage;

    // UI elements for status bar
    private Text healthText;
    private Text bombsText;
    private Text enemiesText;
    private Text scoreText;
    private Text fpsText;

    // Add these fields to your BombermanGame class
    private Timeline gameLoop;
    private StackPane gameRoot;
    private boolean isPaused = false;

    // for custom map
    public BombermanGame(int level, String mapName) {
        Util.generateRandomMap(level, mapName, Setting.PLAYER_NUM);

        try {
            Thread.sleep(2000);

            GameControl.loadMap(mapName, Setting.CUSTOM_MAP);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // for more custom map
    public BombermanGame(int level, int width, int height, String mapName) {
        Util.generateCustomMap(level, height, width, mapName, Setting.PLAYER_NUM);

        try {
            Thread.sleep(2000);

            GameControl.loadMap(mapName, Setting.CUSTOM_MAP);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

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

        // Create the game loop
        gameLoop = new Timeline(
                new KeyFrame(Duration.seconds(1.0 / Setting.FPS_MAX), _ -> {
                    update();
                    render();
                    updateStatusBar();

                    // Count frames here instead
                    frameCounter++;
                    long now = System.nanoTime();
                    if (now - lastFpsUpdateTime >= 1_000_000_000) {
                        fps = frameCounter;
                        frameCounter = 0;
                        lastFpsUpdateTime = now;
                        fpsText.setText("FPS: " + fps);
                    }
                }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
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
        fpsText = new Text("FPS: 0");
        fpsText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        fpsText.setFill(Color.WHITE);

        // Add to status bar
        statusBar.getChildren().addAll(healthText, bombsText, enemiesText, scoreText, fpsText);

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

    /**
     * Show the pause menu overlay
     */
    private void showPauseMenu() {
        // Pause the game
        isPaused = true;
        gameLoop.pause();

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
        gameLoop.play();
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
        gameLoop.play();
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
        gameLoop.play();
    }

    // Make returnToMenu method public so the controller can access it
    public void returnToMenu() {
        // Stop the game loop
        if (gameLoop != null) {
            gameLoop.stop();
            gameLoop = null;
        }
        GameControl.clear();
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

    public void update() {
        GameControl.update();
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
