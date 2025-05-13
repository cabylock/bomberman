package core.system.game;

import core.graphics.*;
import core.system.controller.base.MainMenuController;
import core.system.controller.ingame.PauseMenuController;
import core.system.entry.Main;
import core.system.setting.Setting;
import core.util.Util;
import javafx.scene.input.KeyCode;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import java.util.Set;
import java.util.HashSet;
import javafx.scene.image.Image;
import core.sound.Sound;

public class BombermanGame {

    private static GraphicsContext gc;
    private static Canvas canvas;
    public static final Set<KeyCode> input = new HashSet<>();
    private static Stage stage;

    private static long lastUpdateTime = 0;
    private static float deltaTime = 0;

    private static AnimationTimer gameLoop;
    private static StackPane gameRoot;
    private static boolean isPaused = false;

    public static void main(String[] args) {
        Application.launch(args);
    }

    public static void createGameScene(Stage primaryStage) {
        Sound.stopMusic();
        Sound.playMusic("start_game", true);
        stage = primaryStage;

        stage.setOnCloseRequest(_ -> {
            if (gameLoop != null) {
                gameLoop.stop();
                gameLoop = null;
            }
            Sound.stopMusic();
            GameControl.stop();
            input.clear();

            Platform.exit();
        });

        canvas = new Canvas(Sprite.DEFAULT_SIZE * GameControl.getWidth(),
                Sprite.DEFAULT_SIZE * GameControl.getHeight());

        gc = canvas.getGraphicsContext2D();

        VBox gameContent = new VBox(5);
        gameContent.getChildren().addAll(canvas);

        gameRoot = new StackPane();
        gameRoot.getChildren().add(gameContent);

        Scene scene = new Scene(gameRoot);

        scene.setOnKeyPressed(e -> {
            input.add(e.getCode());

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

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Bomberman Game");
        stage.getIcons().add(new Image("/textures/icon.jpg"));
        stage.setResizable(false);
        stage.show();

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastUpdateTime > 0) {
                    deltaTime = (float) ((now - lastUpdateTime) / 1_000_000_000.0);

                    if (deltaTime > 0.08f)
                        deltaTime = 0.08f;
                    if (deltaTime < Setting.FRAME_TIME_NS) {
                        return;
                    }
                }

                render();

                GameControl.update(deltaTime);

                lastUpdateTime = now;
            }
        };
        gameLoop.start();
    }

    public static StackPane getGameRoot() {
        return gameRoot;
    }

    private static void showPauseMenu() {
        isPaused = true;
        gameLoop.stop();

        try {
            FXMLLoader loader = new FXMLLoader(
                    BombermanGame.class.getResource("/core/system/fxml/ingame/PauseMenu.fxml"));
            VBox pauseMenu = loader.load();

            PauseMenuController controller = loader.getController();
            controller.setGame();

            StackPane overlay = new StackPane();
            overlay.getChildren().add(pauseMenu);
            controller.setOverlay(overlay);

            gameRoot.getChildren().add(overlay);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading pause menu: " + e.getMessage());
            resumeGame();
        }
    }

    public static void resumeGame() {
        isPaused = false;
        gameLoop.start();
    }

    public static void nextLevel() {
        if (Setting.GAME_MODE == Setting.CLIENT_MODE) {
            Util.logInfo("You don't have permission to change level in online mode");
            return;
        }
        isPaused = false;

        if (gameLoop != null) {
            gameLoop.stop();
        }

        GameControl.nextLevel();
        gameLoop.start();
    }

    public static void restartGame() {
        if (Setting.GAME_MODE == Setting.CLIENT_MODE) {
            Util.logInfo("You can't restart the game in online mode");
            return;
        }
        isPaused = false;

        if (gameLoop != null) {
            gameLoop.stop();
        }

        GameControl.reset();
        gameLoop.start();
    }

    public static void returnToMenu() {
        if (gameLoop != null) {
            gameLoop.stop();
            gameLoop = null;
        }
        GameControl.stop();
        GameControl.clearEntities();
        input.clear();

        try {
            FXMLLoader loader = new FXMLLoader(BombermanGame.class.getResource("/core/system/fxml/base/MainMenu.fxml"));
            Parent root = loader.load();

            MainMenuController controller = loader.getController();
            controller.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Main main = new Main();
            main.start(stage);
        }
    }

    public static void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        GameControl.getBackgroundEntities().forEach(entity -> entity.render(gc));
        GameControl.getItemEntities().forEach(entity -> entity.render(gc));
        GameControl.getStaticEntities().forEach(entity -> entity.render(gc));
        GameControl.getBomberEntities().forEach(entity -> {
            if (entity.isAlive()) {
                entity.render(gc);
            }
        });
        GameControl.getEnemyEntities().forEach(entity -> entity.render(gc));
    }
}
