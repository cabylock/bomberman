package core.entity.dynamic_entity.mobile_entity;


import core.BombermanGame;
import core.graphics.Sprite;
import core.entity.dynamic_entity.static_entity.Bomb;
import core.entity.map_handle.MapEntity;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class Bomber extends MobileEntity {

   protected int speed = 2;
  


   public Bomber(int x, int y, Image image) {
      super(x, y, image);
      // Image arrays for animation
      images = new Image[4][3];
      images[RIGHT_MOVING][0] = Sprite.player_right.getFxImage();
      images[RIGHT_MOVING][1] = Sprite.player_right_1.getFxImage();
      images[RIGHT_MOVING][2] = Sprite.player_right_2.getFxImage();
      images[LEFT_MOVING][0] = Sprite.player_left.getFxImage();
      images[LEFT_MOVING][1] = Sprite.player_left_1.getFxImage();
      images[LEFT_MOVING][2] = Sprite.player_left_2.getFxImage();
      images[UP_MOVING][0] = Sprite.player_up.getFxImage();
      images[UP_MOVING][1] = Sprite.player_up_1.getFxImage();
      images[UP_MOVING][2] = Sprite.player_up_2.getFxImage();
      images[DOWN_MOVING][0] = Sprite.player_down.getFxImage();
      images[DOWN_MOVING][1] = Sprite.player_down_1.getFxImage();
      images[DOWN_MOVING][2] = Sprite.player_down_2.getFxImage();
   }

   @Override
   public void update() {

      
      if (BombermanGame.input.contains(KeyCode.UP)) {
         move(UP_MOVING,speed);
      } else if (BombermanGame.input.contains(KeyCode.DOWN)) {
         move(DOWN_MOVING, speed);
      } else if (BombermanGame.input.contains(KeyCode.LEFT)) {
         move(LEFT_MOVING, speed);
      } else if (BombermanGame.input.contains(KeyCode.RIGHT)) {
         move(RIGHT_MOVING, speed);
      } else if (BombermanGame.input.contains(KeyCode.SPACE)) {
         placeBomb();
      } else {
         moving = false;
      }

      updateAnimation();
   }

   
   private void placeBomb() {
      BombermanGame.input.remove(KeyCode.SPACE);
      int bombX = this.getXTile();
      int bombY = this.getYTile();

      System.out.println("Placing bomb at: " + bombX + " " + bombY);
      Bomb bomb = new Bomb(bombX , bombY , Sprite.bomb.getFxImage());
      MapEntity.addDynamicEntity(bomb);
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
