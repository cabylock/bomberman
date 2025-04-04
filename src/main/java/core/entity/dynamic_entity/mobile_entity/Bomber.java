package core.entity.dynamic_entity.mobile_entity;

import core.graphics.Sprite;
import core.system.game.BombermanGame;
import core.system.game.GameControl;
import core.system.setting.Setting;
import core.entity.dynamic_entity.static_entity.Bomb;
import javafx.scene.image.Image;

public class Bomber extends MobileEntity {

   protected int speed = 2;
   protected int flameSize = 1;
   protected int typePlayer;
   protected int bombCountMax = 1;

   public Bomber(int x, int y, Image image, int typePlayer) {
      super(x, y, image);
      // Image arrays for animation
      this.typePlayer = typePlayer;
      images = new Image[5][3];
      images[Setting.RIGHT_MOVING][0] = Sprite.player_right.getFxImage();
      images[Setting.RIGHT_MOVING][1] = Sprite.player_right_1.getFxImage();
      images[Setting.RIGHT_MOVING][2] = Sprite.player_right_2.getFxImage();
      images[Setting.LEFT_MOVING][0] = Sprite.player_left.getFxImage();
      images[Setting.LEFT_MOVING][1] = Sprite.player_left_1.getFxImage();
      images[Setting.LEFT_MOVING][2] = Sprite.player_left_2.getFxImage();
      images[Setting.UP_MOVING][0] = Sprite.player_up.getFxImage();
      images[Setting.UP_MOVING][1] = Sprite.player_up_1.getFxImage();
      images[Setting.UP_MOVING][2] = Sprite.player_up_2.getFxImage();
      images[Setting.DOWN_MOVING][0] = Sprite.player_down.getFxImage();
      images[Setting.DOWN_MOVING][1] = Sprite.player_down_1.getFxImage();
      images[Setting.DOWN_MOVING][2] = Sprite.player_down_2.getFxImage();
      images[Setting.DEAD][0] = Sprite.player_dead1.getFxImage();
      images[Setting.DEAD][1] = Sprite.player_dead2.getFxImage();
      images[Setting.DEAD][2] = Sprite.player_dead3.getFxImage();
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
   }

   private void placeBomb() {
      if (bombCountMax == 0) {

         return;
      }

      int bombX = this.getXTile();
      int bombY = this.getYTile();

      Bomb newBomb = new Bomb(bombX, bombY, Sprite.bomb.getFxImage(), flameSize, this);
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
