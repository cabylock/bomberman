package core.entity.dynamic_entity.mobile_entity.enemy_entity;

import core.graphics.Sprite;
import core.system.setting.Setting;
import javafx.scene.image.Image;

public class Oneal extends EnemyEntity {
      public Oneal(int x, int y, int imageId) {

            super(x, y, imageId);

            speed = 2;

            imageIds = new int[4][3];
            imageIds[Setting.UP_MOVING][0] = Sprite.ONEAL_LEFT1;
            imageIds[Setting.UP_MOVING][1] = Sprite.ONEAL_LEFT2;
            imageIds[Setting.UP_MOVING][2] = Sprite.ONEAL_LEFT3;
            imageIds[Setting.DOWN_MOVING][0] = Sprite.ONEAL_RIGHT1;
            imageIds[Setting.DOWN_MOVING][1] = Sprite.ONEAL_RIGHT2;
            imageIds[Setting.DOWN_MOVING][2] = Sprite.ONEAL_RIGHT3;
            imageIds[Setting.LEFT_MOVING][0] = Sprite.ONEAL_LEFT1;
            imageIds[Setting.LEFT_MOVING][1] = Sprite.ONEAL_LEFT2;
            imageIds[Setting.LEFT_MOVING][2] = Sprite.ONEAL_LEFT3;
            imageIds[Setting.RIGHT_MOVING][0] = Sprite.ONEAL_RIGHT1;
            imageIds[Setting.RIGHT_MOVING][1] = Sprite.ONEAL_RIGHT2;
            imageIds[Setting.RIGHT_MOVING][2] = Sprite.ONEAL_RIGHT3;

      }

      @Override
      public void update() {

            defaultMove();
            EnemyCollision();
            updateAnimation();
      }

}
