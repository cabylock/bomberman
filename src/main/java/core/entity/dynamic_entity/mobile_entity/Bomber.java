package core.entity.dynamic_entity.mobile_entity;


import core.graphics.Sprite;
import core.system.BombermanGame;
import core.system.Setting;
import core.entity.dynamic_entity.static_entity.Bomb;
import core.entity.map_handle.MapEntity;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class Bomber extends MobileEntity {

   protected int speed = 2;
   protected int typePlayer;


   public Bomber(int x, int y, Image image,int typePlayer) {
      super(x, y, image);
      // Image arrays for animation
      this.typePlayer = typePlayer;
      images = new Image[4][3];
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
   }

   @Override
   public void update() {

      
      if (typePlayer == 1) {
         if (BombermanGame.input.contains(Setting.BOMBER_MOVE_UP_1)) {
            move(Setting.UP_MOVING, speed);
            direction = Setting.UP_MOVING;
            moving = true;
         } else if (BombermanGame.input.contains(Setting.BOMBER_MOVE_DOWN_1)) {
            move(Setting.DOWN_MOVING, speed);
            direction = Setting.DOWN_MOVING;
            moving = true;
         } else if (BombermanGame.input.contains(Setting.BOMBER_MOVE_LEFT_1)) {
            move(Setting.LEFT_MOVING, speed);
            direction = Setting.LEFT_MOVING;
            moving = true;
         } else if (BombermanGame.input.contains(Setting.BOMBER_MOVE_RIGHT_1)) {
            move(Setting.RIGHT_MOVING, speed);
            direction = Setting.RIGHT_MOVING;
            moving = true;
         } else {
            moving = false;
         }
         if (BombermanGame.input.contains(Setting.BOMBER_PLACE_BOMB_1)) {
            BombermanGame.input.remove(Setting.BOMBER_PLACE_BOMB_1);
            placeBomb();
         }
      }
      
      else if (typePlayer == 2) {
         if (BombermanGame.input.contains(Setting.BOMBER_MOVE_UP_2)) {
            move(Setting.UP_MOVING, speed);
            direction = Setting.UP_MOVING;
            moving = true;
         } else if (BombermanGame.input.contains(Setting.BOMBER_MOVE_DOWN_2)) {
            move(Setting.DOWN_MOVING, speed);
            direction = Setting.DOWN_MOVING;
            moving = true;
         } else if (BombermanGame.input.contains(Setting.BOMBER_MOVE_LEFT_2)) {
            move(Setting.LEFT_MOVING, speed);
            direction = Setting.LEFT_MOVING;
            moving = true;
         } else if (BombermanGame.input.contains(Setting.BOMBER_MOVE_RIGHT_2)) {
            move(Setting.RIGHT_MOVING, speed);
            direction = Setting.RIGHT_MOVING;
            moving = true;
         } else {
            moving = false;
         }
         if (BombermanGame.input.contains(Setting.BOMBER_PLACE_BOMB_2)) {
            BombermanGame.input.remove(Setting.BOMBER_PLACE_BOMB_2);
            placeBomb();
         }
      }





      updateAnimation();
   }

   
   private void placeBomb() {
      BombermanGame.input.remove(KeyCode.SPACE);
      int bombX = this.getXTile();
      int bombY = this.getYTile();

      // System.out.println("Placing bomb at: " + bombX + " " + bombY);
      Bomb bomb = new Bomb(bombX , bombY , Sprite.bomb.getFxImage());
      MapEntity.addDynamicEntity(bomb);
   }

  
}
