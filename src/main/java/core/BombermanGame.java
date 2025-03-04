package core;

import core.entities.*;
import core.graphics.Sprite;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class BombermanGame extends Application {

    public static  int WIDTH = 30;
    public static  int HEIGHT = 20;

    
    private static int INITAIL_POSITION_X = 1;
    private static int INITAIL_POSITION_Y = 1;

    private GraphicsContext gc;
    private Canvas canvas;
    private List<Entity> entities = new ArrayList<>();
    private List<Entity> stillObjects = new ArrayList<>();

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {

        createMap();
        // Tao Canvas
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
        gc = canvas.getGraphicsContext2D();

        // Tao root container
        Group root = new Group();
        root.getChildren().add(canvas);

        // Tao scene
        Scene scene = new Scene(root);

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

        

        Entity bomberman = new Bomber(INITAIL_POSITION_X, INITAIL_POSITION_Y, Sprite.player_right.getFxImage());
        entities.add(bomberman);
    }

    public void createMap() {

        Map currentMap = new Map("/levels/Level1.txt");
        WIDTH = currentMap.getWidth();
        HEIGHT = currentMap.getHeight();
        System.out.println(WIDTH + " " + HEIGHT);
        for(int i = 0; i < currentMap.getHeight(); i++) {
            for(int j = 0; j < currentMap.getWidth(); j++) {
                char c = currentMap.getMap()[i][j];
                if (c == '#') {
                    Entity wall = new Wall(j, i, Sprite.wall.getFxImage());
                    stillObjects.add(wall);
                }
                else if (c == '*') {
                    Entity brick = new Brick(j, i, Sprite.brick.getFxImage());
                    stillObjects.add(brick);
                }
                else if (c == 'x') {
                    Entity portal = new Portal(j, i, Sprite.portal.getFxImage());
                    stillObjects.add(portal);
                }
                else if (c == 'p') {
                    INITAIL_POSITION_X = j;
                    INITAIL_POSITION_Y = i;
                }
                else if (c=='1')
                {
                    Entity balloon = new Balloom(j, i, Sprite.balloom_left1.getFxImage());
                    entities.add(balloon);
                }
                else if (c=='2')
                {
                    Entity balloon = new Oneal(j, i, Sprite.oneal_left1.getFxImage());
                    entities.add(balloon);
                }
                else if  (c== 'b')
                {
                    Entity BombItem = new BombItem(j, i, Sprite.powerup_bombs.getFxImage());
                    stillObjects.add(BombItem);
                }
                else if (c == 'f')
                {
                    Entity FlameItem = new FlameItem(j, i, Sprite.powerup_flames.getFxImage());
                    stillObjects.add(FlameItem);
                }
                else if (c == 's')
                {
                    Entity SpeedItem = new SpeedItem(j, i, Sprite.powerup_speed.getFxImage());
                    stillObjects.add(SpeedItem);
                }
                else 
                {
                    Entity grass = new Grass(j, i, Sprite.grass.getFxImage());
                    stillObjects.add(grass);
                }
            }
        }

        
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
