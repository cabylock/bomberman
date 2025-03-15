package core.entity.dynamic_entity.static_entity;


import java.util.Map;

import core.entity.dynamic_entity.DynamicEntity;
import core.entity.map_handle.MapEntity;
import core.graphics.Sprite;
import javafx.scene.image.Image;


public class Bomb extends StaticEntity {

    protected int timeToExplode = 500; // 120 frames = 2 seconds
    protected int timeAfterExplode = 200; // 20 frames = 1/3 second
    protected boolean exploded = false;
    private final int DEFAULT_IMAGE = 0;
    private final int EXPLODED_IMAGE = 1;
    private Flame flame;

    public Bomb(int x, int y, Image image) {
        super(x, y, image);

        images = new Image[2][3];

        images[DEFAULT_IMAGE][0] = Sprite.bomb.getFxImage();
        images[DEFAULT_IMAGE][1] = Sprite.bomb_1.getFxImage();
        images[DEFAULT_IMAGE][2] = Sprite.bomb_2.getFxImage();
        images[EXPLODED_IMAGE][0] = Sprite.bomb_exploded.getFxImage();
        images[EXPLODED_IMAGE][1] = Sprite.bomb_exploded1.getFxImage();
        images[EXPLODED_IMAGE][2] = Sprite.bomb_exploded2.getFxImage();

    }

    @Override
    public void update() {
        timeToExplode--;
        if (timeToExplode == 0) {
            exploded = true;
            
            
        }
        if (exploded) {
            timeAfterExplode--;
            if (timeAfterExplode == 0) {
                remove();
                
            }
        }
        updateAnimation();
       
    }

 
    @Override
    public void updateAnimation() {
        if (exploded) {
            if (animationDelay == 0) {
                animationStep++;
                if (animationStep == 3) {
                    animationStep = 0;
                }
                animationDelay = 20;
            } else {
                animationDelay--;
            }
        }
        image = images[exploded ? EXPLODED_IMAGE : DEFAULT_IMAGE][animationStep];
        
    }


   

}
