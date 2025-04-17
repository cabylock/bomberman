package core.entity.dynamic_entity.mobile_entity.enemy_entity;

import core.graphics.Sprite;
import core.system.setting.Setting;

public class Oneal extends EnemyEntity {

      private transient int speed = 20;

      public Oneal(int x, int y, int imageId) {
            super(x, y, imageId);

            imageIds = new int[4][3];
            imageIds[Setting.UP_MOVING][0] = Sprite.ONEAL_LEFT_0;
            imageIds[Setting.UP_MOVING][1] = Sprite.ONEAL_LEFT_1;
            imageIds[Setting.UP_MOVING][2] = Sprite.ONEAL_LEFT_2;
            imageIds[Setting.DOWN_MOVING][0] = Sprite.ONEAL_RIGHT_0;
            imageIds[Setting.DOWN_MOVING][1] = Sprite.ONEAL_RIGHT_1;
            imageIds[Setting.DOWN_MOVING][2] = Sprite.ONEAL_RIGHT_2;
            imageIds[Setting.LEFT_MOVING][0] = Sprite.ONEAL_LEFT_0;
            imageIds[Setting.LEFT_MOVING][1] = Sprite.ONEAL_LEFT_1;
            imageIds[Setting.LEFT_MOVING][2] = Sprite.ONEAL_LEFT_2;
            imageIds[Setting.RIGHT_MOVING][0] = Sprite.ONEAL_RIGHT_0;
            imageIds[Setting.RIGHT_MOVING][1] = Sprite.ONEAL_RIGHT_1;
            imageIds[Setting.RIGHT_MOVING][2] = Sprite.ONEAL_RIGHT_2;

      }

      @Override
      public void update(float deltaTime) {
            defaultMove(deltaTime);
            EnemyCollision();
            updateAnimation(deltaTime);
      }
}
