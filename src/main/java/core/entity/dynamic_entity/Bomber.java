package core.entity.dynamic_entity;


import core.BombermanGame;
import core.graphics.Sprite;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;


public class Bomber extends DynamicEntity {

   

   public Bomber(int x, int y, Image image) {
      super(x, y, image);
      // Image arrays for animation
         rightImages = new Image[] {
               Sprite.player_right.getFxImage(),
               Sprite.player_right_1.getFxImage(),
               Sprite.player_right_2.getFxImage()
         };

         leftImages = new Image[] {
               Sprite.player_left.getFxImage(),
               Sprite.player_left_1.getFxImage(),
               Sprite.player_left_2.getFxImage()
         };

         upImages = new Image[] {
               Sprite.player_up.getFxImage(),
               Sprite.player_up_1.getFxImage(),
               Sprite.player_up_2.getFxImage()
         };

         downImages = new Image[] {
               Sprite.player_down.getFxImage(),
               Sprite.player_down_1.getFxImage(),
               Sprite.player_down_2.getFxImage(),
         };
   }

   @Override
   public void update() {
   moving = false;
   int nextX = x;
   int nextY = y;

   moveDelay++;
   if (moveDelay >= moveDelayThreshold) {
      moveDelay = 0;

      // Handle horizontal movement
      if (BombermanGame.input.contains(KeyCode.RIGHT)) {
         nextX += speed;
         direction = 0;
         moving = true;
      } else if (BombermanGame.input.contains(KeyCode.LEFT)) {
         nextX -= speed;
         direction = 2;
         moving = true;
      }

      // Only update X if no collision
      if (!isColliding(nextX, y)) {
         x = nextX;
      }

      // Handle vertical movement
      if (BombermanGame.input.contains(KeyCode.DOWN)) {
         nextY += speed;
         direction = 1;
         moving = true;
      } else if (BombermanGame.input.contains(KeyCode.UP)) {
         nextY -= speed;
         direction = 3;
         moving = true;
      }
      // else if (BombermanGame.input.contains(KeyCode.SPACE)) {
      //    BombermanGame.getBombManager().addBomb(x, y, boomImage);
      // }

      // Only update Y if no collision
      if (!isColliding(x, nextY)) {
         y = nextY;
      }
   }

   updateAnimation();
}

   // private void addBomb( int x, int y) {
   //    BombermanGame.stillObjects.add(new Bomb(x, y, boomImage));
   // }

}
