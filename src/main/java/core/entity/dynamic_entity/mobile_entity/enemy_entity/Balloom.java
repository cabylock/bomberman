package core.entity.dynamic_entity.mobile_entity.enemy_entity;

import core.graphics.Sprite;
import core.util.Util;
import javafx.scene.image.Image;


public class Balloom extends EnemyEntity {
    
    protected int speed = 1;



    public Balloom(int x, int y, Image image) {
        super(x, y, image);

        images = new Image[4][3];
        images[UP_MOVING][0] = Sprite.balloom_left1.getFxImage();
        images[UP_MOVING][1] = Sprite.balloom_left2.getFxImage();
        images[UP_MOVING][2] = Sprite.balloom_left3.getFxImage();
        images[DOWN_MOVING][0] = Sprite.balloom_right1.getFxImage();
        images[DOWN_MOVING][1] = Sprite.balloom_right2.getFxImage();
        images[DOWN_MOVING][2] = Sprite.balloom_right3.getFxImage();
        images[LEFT_MOVING][0] = Sprite.balloom_left1.getFxImage();
        images[LEFT_MOVING][1] = Sprite.balloom_left2.getFxImage();
        images[LEFT_MOVING][2] = Sprite.balloom_left3.getFxImage();
        images[RIGHT_MOVING][0] = Sprite.balloom_right1.getFxImage();
        images[RIGHT_MOVING][1] = Sprite.balloom_right2.getFxImage();
        images[RIGHT_MOVING][2] = Sprite.balloom_right3.getFxImage();
        
    }

    @Override
    public void update() {

        defaultMove();


        updateAnimation();

    }

    @Override
    protected void updateAnimation() {
        if (moving) {
            animationDelay++;
            if (animationDelay >= 10) {
                animationStep = (animationStep + 1) % 3;
                animationDelay = 0;
            }
            image = images[direction][animationStep];
        } else {
            animationStep = 0;
            image = images[direction][0];
        }
    }

}
