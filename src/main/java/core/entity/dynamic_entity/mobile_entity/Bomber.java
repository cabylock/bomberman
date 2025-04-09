package core.entity.dynamic_entity.mobile_entity;

import core.graphics.Sprite;
import core.system.game.BombermanGame;
import core.system.game.GameControl;
import core.system.setting.Setting;
import core.entity.dynamic_entity.static_entity.Bomb;

public class Bomber extends MobileEntity {

   private int speed = 25;
   protected int flameSize = 1;
   protected int typePlayer;

   protected int bombCountMax = 1;

   protected static final int ITEM_DURATION = 1200; // 10 giây * 60 frames/giây

   public Bomber(int x, int y, int imageId, int typePlayer) {
      super(x, y, imageId);
      // Image arrays for animation
      this.typePlayer = typePlayer;
      if (typePlayer == Setting.BOMBER2) {
         id++;
      }
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
      imageIds[Setting.ANIMATION_NULL][0] = Sprite.ANIMATION_NULL;
      imageIds[Setting.ANIMATION_NULL][1] = Sprite.PLAYER_DOWN;
      imageIds[Setting.ANIMATION_NULL][2] = Sprite.ANIMATION_NULL;
   }

   @Override
   public void update(double deltaTime) {
      updateAnimation(deltaTime);
      updateItem();
      updateInvincible();
   }

   public void control(String command, double deltaTime) {
      if (command.equals(Setting.MOVE_DOWN)) {
         move(Setting.DOWN_MOVING, speed, deltaTime);
      } else if (command.equals(Setting.MOVE_UP)) {
         move(Setting.UP_MOVING, speed, deltaTime);
      } else if (command.equals(Setting.MOVE_LEFT)) {
         move(Setting.LEFT_MOVING, speed, deltaTime);
      } else if (command.equals(Setting.MOVE_RIGHT)) {
         move(Setting.RIGHT_MOVING, speed, deltaTime);
      } else if (command.equals(Setting.PLACE_BOMB)) {
         placeBomb();
      } else {
         moving = false;
      }
   }

   private void placeBomb() {

      BombermanGame.input.remove(Setting.BOMBER_KEY_CONTROLS[typePlayer][Setting.BOMB_PLACE]);
      if (bombCountMax == 0) {

         return;
      }

      int bombX = this.getXTile();
      int bombY = this.getYTile();

      Bomb newBomb = new Bomb(bombX, bombY, Sprite.BOMB, flameSize, id);
      GameControl.addEntity(newBomb);
      bombCountMax--;

   }

   public void updateItem() {
      if (flamePassTime > 0) {
         flamePassTime--;
         if (flamePassTime <= 0) {
            flamePass = false;
         }
      }
      if (bombPassTime > 0) {
         bombPassTime--;
         if (bombPassTime <= 0) {
            bombPass = false;
         }
      }
      if (speedUpTime > 0) {
         speedUpTime--;
         if (speedUpTime <= 0) {
            speedUp = false;
            speed = 25;
         }
      }
      if (flameUpTime > 0) {
         flameUpTime--;
         if (flameUpTime <= 0) {
            flameUp = false;
            flameSize = 1;
         }
      }
      if (bombUpTime > 0) {
         bombUpTime--;
         if (bombUpTime <= 0) {
            bombUp = false;
            bombCountMax = 1;
         }
      }
      if (wallPassTime > 0) {
         wallPassTime--;
         if (wallPassTime <= 0) {
            wallPass = false;
         }
      }

   }

   public void setFlamePass(boolean flamePass) {
      this.flamePass = flamePass;
      if (flamePass) {
         flamePassTime = ITEM_DURATION;
      }
   }

   public void setBombPass(boolean bombPass) {
      this.bombPass = bombPass;
      if (bombPass) {
         bombPassTime = ITEM_DURATION;
      }
   }

   public void setSpeedUp(boolean speedUp) {
      this.speedUp = speedUp;
      if (speedUp) {
         speed = 40;
      }
      speedUpTime = ITEM_DURATION;
   }

   public void setFlameUp(boolean flameUp) {
      this.flameUp = flameUp;
      if (flameUp) {
         flameSize = 2;
         flameUpTime = ITEM_DURATION;
      }
   }

   public void setBombUp(boolean bombUp) {
      this.bombUp = bombUp;
      if (bombUp) {
         bombCountMax = 2;
         bombUpTime = ITEM_DURATION;
      }
   }

   public void setWallPass(boolean wallPass) {
      this.wallPass = wallPass;
      if (wallPass) {
         wallPassTime = ITEM_DURATION;
      }
   }

   public void bombExplode() {
      if (bombCountMax < 2) {
         bombCountMax++;
      }
   }

   public void increaseHealth() {
      health++;
   }

   public boolean isFlamePass() {
      return flamePass;
   }

   public boolean isBombPass() {
      return bombPass;
   }

   @Override
   public void remove() {
      GameControl.removeEntity(this);
   }

}
