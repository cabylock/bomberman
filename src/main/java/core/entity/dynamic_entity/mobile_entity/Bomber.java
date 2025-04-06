package core.entity.dynamic_entity.mobile_entity;

import core.graphics.Sprite;
import core.system.game.BombermanGame;
import core.system.game.GameControl;
import core.system.setting.Setting;
import core.entity.dynamic_entity.static_entity.Bomb;

public class Bomber extends MobileEntity {

   protected int speed = 2;
   protected int flameSize = 1;
   protected int typePlayer;
   protected int bombCountMax = 1;
   
   protected boolean flamePass = false;

   public Bomber(int x, int y, int imageId, int typePlayer) {
      super(x, y, imageId);
      // Image arrays for animation
      this.typePlayer = typePlayer;
      imageIds = new int[6][3];
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
      imageIds[Setting.ANIMATION_NULL][0] = Sprite.PLAYER_RIGHT;   
      imageIds[Setting.ANIMATION_NULL][1] = Sprite.ANIMATION_NULL;
      imageIds[Setting.ANIMATION_NULL][2] = Sprite.ANIMATION_NULL;
   }

   @Override
   public void update() {

      int playerIndex = typePlayer - 1;

      if (BombermanGame.input.contains(Setting.BOMBER_KEY_CONTROLS[playerIndex][Setting.UP_MOVING])) {
         move(Setting.UP_MOVING, speed);
      } else if (BombermanGame.input.contains(Setting.BOMBER_KEY_CONTROLS[playerIndex][Setting.DOWN_MOVING])) {
         move(Setting.DOWN_MOVING, speed);
      } else if (BombermanGame.input.contains(Setting.BOMBER_KEY_CONTROLS[playerIndex][Setting.LEFT_MOVING])) {
         move(Setting.LEFT_MOVING, speed);
      } else if (BombermanGame.input.contains(Setting.BOMBER_KEY_CONTROLS[playerIndex][Setting.RIGHT_MOVING])) {
         move(Setting.RIGHT_MOVING, speed);
      } else {
         moving = false;
      }

      if (BombermanGame.input.contains(Setting.BOMBER_KEY_CONTROLS[playerIndex][4])) { // Phím đặt bom
         BombermanGame.input.remove(Setting.BOMBER_KEY_CONTROLS[playerIndex][4]);
         placeBomb();
      }
      updateAnimation();
      updateInvincible();
   }
   

   private void placeBomb() {
      if (bombCountMax == 0) {

         return;
      }

      int bombX = this.getXTile();
      int bombY = this.getYTile();

      Bomb newBomb = new Bomb(bombX, bombY, Sprite.BOMB, flameSize, this);
      GameControl.addEntity(newBomb);
      bombCountMax--;

   }

   public void setFlamePass(boolean flamePass) {
      this.flamePass = flamePass;
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

   public void increaseHealth() {
      health++;
   }

   public void setBombPass(boolean bombpass) {
      this.bombpass = bombpass;

   }

   public boolean isFlamePass() {
      return flamePass;
   }
   public void updateInvincible() {
   
      if (isInvincible) {
         invincibleTime--;
         if (invincibleTime <= 0) {
            isInvincible = false;
         }
      }
   }

   @Override
   public void remove() {
      GameControl.removeEntity(this);
   }

}
