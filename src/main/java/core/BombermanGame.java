package core;

import core.entity.*;
import core.entity.map_handle.MapEntity;
import core.graphics.*;

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

public class BombermanGame extends Application {

    public static int WIDTH = 30;
    public static int HEIGHT = 20;

    private GraphicsContext gc;
    private Canvas canvas;
    public static int level = 1;
    public static final Set<KeyCode> input = new HashSet<>();

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {

        MapEntity.loadMap(level);

        // Tao Canvas
        canvas = new Canvas(Sprite.SCALED_SIZE * MapEntity.getWidth(), Sprite.SCALED_SIZE * MapEntity.getHeight());
        gc = canvas.getGraphicsContext2D();

        // Tao root container
        Group root = new Group();
        root.getChildren().add(canvas);

        // Tao scene
        Scene scene = new Scene(root);

        scene.setOnKeyPressed(e -> {
            input.add(e.getCode());
        });

        scene.setOnKeyReleased(e -> {
            input.remove(e.getCode());
        });

        // Them scene vao stage
        stage.setScene(scene);
        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update();
                render();
            }
        };
        timer.start();

    }

    public void update() {
        MapEntity.update();
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        MapEntity.getBackgroundEntities().forEach(entity -> entity.render(gc));
        for (Entity entity : MapEntity.getDynamicEntities()) {
            entity.render(gc);

        }
        MapEntity.getItemEntities().forEach(entity -> entity.render(gc));
    }
}
