package core.entity.dynamic_entity.mobile_entity.enemy_entity;


import core.graphics.Sprite;
import core.system.setting.Setting;
import javafx.scene.image.Image;

public class Oneal extends EnemyEntity {
      public Oneal(int x, int y, Image image) {

            super(x, y, image);

            speed = 2; 

            images = new Image[4][3];
            images[Setting.UP_MOVING][0] = Sprite.oneal_left1.getFxImage();
            images[Setting.UP_MOVING][1] = Sprite.oneal_left2.getFxImage();
            images[Setting.UP_MOVING][2] = Sprite.oneal_left3.getFxImage();
            images[Setting.DOWN_MOVING][0] = Sprite.oneal_right1.getFxImage();
            images[Setting.DOWN_MOVING][1] = Sprite.oneal_right2.getFxImage();
            images[Setting.DOWN_MOVING][2] = Sprite.oneal_right3.getFxImage();
            images[Setting.LEFT_MOVING][0] = Sprite.oneal_left1.getFxImage();
            images[Setting.LEFT_MOVING][1] = Sprite.oneal_left2.getFxImage();
            images[Setting.LEFT_MOVING][2] = Sprite.oneal_left3.getFxImage();
            images[Setting.RIGHT_MOVING][0] = Sprite.oneal_right1.getFxImage();
            images[Setting.RIGHT_MOVING][1] = Sprite.oneal_right2.getFxImage();
            images[Setting.RIGHT_MOVING][2] = Sprite.oneal_right3.getFxImage();


      }

      @Override
      public void update() {

            defaultMove();
            EnemyCollision();
            updateAnimation();
      }

      

     

   
}
