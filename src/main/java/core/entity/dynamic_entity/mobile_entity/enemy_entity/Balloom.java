package core.entity.dynamic_entity.mobile_entity.enemy_entity;

import core.graphics.Sprite;
import core.system.Setting;
import core.util.Util;
import javafx.scene.image.Image;


public class Balloom extends EnemyEntity {
    
    protected int speed = 1;



    public Balloom(int x, int y, Image image) {
        super(x, y, image);

        images = new Image[4][3];
        images[Setting.UP_MOVING][0] = Sprite.balloom_left1.getFxImage();
        images[Setting.UP_MOVING][1] = Sprite.balloom_left2.getFxImage();
        images[Setting.UP_MOVING][2] = Sprite.balloom_left3.getFxImage();

        images[Setting.DOWN_MOVING][0] = Sprite.balloom_right1.getFxImage();
        images[Setting.DOWN_MOVING][1] = Sprite.balloom_right2.getFxImage();
        images[Setting.DOWN_MOVING][2] = Sprite.balloom_right3.getFxImage();

        images[Setting.LEFT_MOVING][0] = Sprite.balloom_left1.getFxImage();
        images[Setting.LEFT_MOVING][1] = Sprite.balloom_left2.getFxImage();
        images[Setting.LEFT_MOVING][2] = Sprite.balloom_left3.getFxImage();

        images[Setting.RIGHT_MOVING][0] = Sprite.balloom_right1.getFxImage();
        images[Setting.RIGHT_MOVING][1] = Sprite.balloom_right2.getFxImage();
        images[Setting.RIGHT_MOVING][2] = Sprite.balloom_right3.getFxImage();
        
    }

    @Override
    public void update() {

        defaultMove();


        updateAnimation();

    }

    
    

}
