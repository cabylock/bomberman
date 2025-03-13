package core;

import core.entity.*;
import core.entity.dynamic_entity.*;
import core.entity.item_entity.BombItem;
import core.entity.item_entity.FlameItem;
import core.entity.item_entity.Portal;
import core.entity.item_entity.SpeedItem;
import core.entity.map_handle.Map;
import core.entity.static_entity.*;
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
import java.util.ArrayList;
import java.util.List;

public class BombermanGame extends Application {

    public static  int WIDTH = 30;
    public static  int HEIGHT = 20;

    

    private GraphicsContext gc;
    private Canvas canvas;
    private static List<Entity> entities = new ArrayList<>();
    public static List<Entity> stillObjects = new ArrayList<>();
    public static int level = 1;
    public static final Set<KeyCode> input = new HashSet<>();

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }



    @Override
    public void start(Stage stage) {

        Map map = new Map(entities, stillObjects);
        map.loadMap(level);

        // Tao Canvas
        canvas = new Canvas(Sprite.SCALED_SIZE * map.getWidth(), Sprite.SCALED_SIZE * map.getHeight());
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
                render();
                update();
            }
        };
        timer.start();

        


    }

  


    public void update() {
        entities.forEach(Entity::update);
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        entities.forEach(g -> g.render(gc));
    }
}
