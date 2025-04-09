package core.entity.dynamic_entity.mobile_entity;

import core.graphics.Sprite;
import core.system.game.BombermanGame;
import core.system.game.GameControl;
import core.system.setting.Setting;
import core.entity.Entity;
import core.entity.dynamic_entity.static_entity.Bomb;

public class Bomber extends MobileEntity {

   protected int speed = 2;
   protected int flameSize = 1;
   protected int typePlayer;
   public int bombCountMax = 1;

   public Bomber(int x, int y, int imageId, int typePlayer) {
      super(x, y, imageId);
      // Image arrays for animation
      this.typePlayer = typePlayer;
      imageIds = new int[5][3];
      imageIds[Setting.RIGHT_MOVING][0] = Sprite.PLAYER_RIGHT;
      imageIds[Setting.RIGHT_MOVING][1] = Sprite.PLAYER_RIGHT_1;
      imageIds[Setting.RIGHT_MOVING][2] = Sprite.PLAYER_RIGHT_2;
      imageIds[Setting.LEFT_MOVING][0] = Sprite.PLAYER_LEFT;
      imageIds[Setting.LEFT_MOVING][1] = Sprite.PLAYER_LEFT_1;
      imageIds[Setting.LEFT_MOVING][2] = Sprite.PLAYER_LEFT_2;
      imageIds[Setting.UP_MOVING][0] = Sprite.PLAYER_UP;
      imageIds[Setting.UP_MOVING][1] = Sprite.PLAYER_UP_1;
      imageIds[Setting.UP_MOVING][2] = Sprite.PLAYER_UP_2;
      imageIds[Setting.DOWN_MOVING][0] = Sprite.PLAYER_DOWN;
      imageIds[Setting.DOWN_MOVING][1] = Sprite.PLAYER_DOWN_1;
      imageIds[Setting.DOWN_MOVING][2] = Sprite.PLAYER_DOWN_2;
      imageIds[Setting.DEAD][0] = Sprite.PLAYER_DEAD1;
      imageIds[Setting.DEAD][1] = Sprite.PLAYER_DEAD2;
      imageIds[Setting.DEAD][2] = Sprite.PLAYER_DEAD3;
   }

   @Override
   public void update() {

      int playerIndex = typePlayer - 1;

      updateMove(playerIndex);

      updateAnimation();
   }

   private void updateMove(int typePlayer) {

      

      if (BombermanGame.input.contains(Setting.BOMBER_KEY_CONTROLS[typePlayer][Setting.UP_MOVING])) {
         move(Setting.UP_MOVING, speed);
      } else if (BombermanGame.input.contains(Setting.BOMBER_KEY_CONTROLS[typePlayer][Setting.DOWN_MOVING])) {
         move(Setting.DOWN_MOVING, speed);
      } else if (BombermanGame.input.contains(Setting.BOMBER_KEY_CONTROLS[typePlayer][Setting.LEFT_MOVING])) {
         move(Setting.LEFT_MOVING, speed);
      } else if (BombermanGame.input.contains(Setting.BOMBER_KEY_CONTROLS[typePlayer][Setting.RIGHT_MOVING])) {
         move(Setting.RIGHT_MOVING, speed);
      } else if (BombermanGame.input.contains(Setting.BOMBER_KEY_CONTROLS[typePlayer][4])) { // Phím đặt bom
         BombermanGame.input.remove(Setting.BOMBER_KEY_CONTROLS[typePlayer][4]);
         placeBomb();
      } else {
         moving = false;
      }

   }

   private void placeBomb() {
      if (bombCountMax == 0) {

         return;
      }

      int bombX = this.getXTile();
      int bombY = this.getYTile();

      Bomb newBomb = new Bomb(bombX, bombY, Sprite.BOMB, flameSize, id);
      GameControl.addEntity(newBomb);
      bombCountMax--;

   }

   public void increaseSpeed() {
      speed++;
   }

   public void increaseFlameSize() {
      flameSize++;
   }

   public void increaseBomb() {
      bombCountMax++;

   }

   public void bombExplode() {
      bombCountMax++;
   }

   public void dead() {
      isAlive = false;
      direction = Setting.DEAD;
      moving = false;
   }

   @Override
   public void remove() {
      GameControl.removeEntity(this);
   }

}
