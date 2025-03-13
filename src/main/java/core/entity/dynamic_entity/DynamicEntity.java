package core.entity.dynamic_entity;

import core.BombermanGame;
import core.entity.Entity;
import core.entity.static_entity.Grass;
import core.graphics.Sprite;
import javafx.scene.image.Image;


public class DynamicEntity extends Entity {

   protected int speed = 1 * Sprite.SCALED_SIZE / Sprite.DEFAULT_SIZE;
   protected int direction = 0;
   protected boolean moving = false;
   protected boolean alive = true;
   protected boolean win = false;
   protected int animationStep = 0;
   protected int animationDelay = 0;
   protected int moveDelay = 0;
   protected int moveDelayThreshold = 5; // Higher = slower movement
   protected String type;

   // Image arrays for animation
   protected Image[] rightImages;
   protected Image[] leftImages;
   protected Image[] upImages;
   protected Image[] downImages;

   public DynamicEntity(int x, int y, Image image) {
      super(x, y, image);
      this.type = type;

      if (type == "bomber") {
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
               Sprite.player_down_2.getFxImage()
         };
      } else if (type == "oneal") {
         // Image arrays for animation
         rightImages = new Image[] {
               Sprite.oneal_right1.getFxImage(),
               Sprite.oneal_right2.getFxImage(),
               Sprite.oneal_right3.getFxImage()
         };

         leftImages = new Image[] {
               Sprite.oneal_left1.getFxImage(),
               Sprite.oneal_left2.getFxImage(),
               Sprite.oneal_left3.getFxImage()
         };

         upImages = new Image[] {
               Sprite.oneal_right1.getFxImage(),
               Sprite.oneal_right2.getFxImage(),
               Sprite.oneal_right3.getFxImage()
         };

         downImages = new Image[] {
               Sprite.oneal_right1.getFxImage(),
               Sprite.oneal_right2.getFxImage(),
               Sprite.oneal_right3.getFxImage()
         };
      } else if (type == "balloom") {
         // Image arrays for animation
         rightImages = new Image[] {
               Sprite.balloom_right1.getFxImage(),
               Sprite.balloom_right2.getFxImage(),
               Sprite.balloom_right3.getFxImage()
         };

         leftImages = new Image[] {
               Sprite.balloom_left1.getFxImage(),
               Sprite.balloom_left2.getFxImage(),
               Sprite.balloom_left3.getFxImage()
         };

         upImages = new Image[] {
               Sprite.balloom_right1.getFxImage(),
               Sprite.balloom_right2.getFxImage(),
               Sprite.balloom_right3.getFxImage()
         };

         downImages = new Image[] {
               Sprite.balloom_right1.getFxImage(),
               Sprite.balloom_right2.getFxImage(),
               Sprite.balloom_right3.getFxImage()
         };
      }
      
   }

   @Override
   public void update(){}

   protected boolean isColliding(int pixelX, int pixelY) {
      int size = Sprite.SCALED_SIZE - 1;
      int gridX1 = pixelX / Sprite.SCALED_SIZE;
      int gridY1 = pixelY / Sprite.SCALED_SIZE;
      int gridX2 = (pixelX + size) / Sprite.SCALED_SIZE;
      int gridY2 = (pixelY + size) / Sprite.SCALED_SIZE;

      for (Entity entity : BombermanGame.stillObjects) {
         if (entity instanceof Grass)
            continue;

         int entityGridX = entity.getX() / Sprite.SCALED_SIZE;
         int entityGridY = entity.getY() / Sprite.SCALED_SIZE;

         if ((entityGridX == gridX1 && entityGridY == gridY1) ||
               (entityGridX == gridX2 && entityGridY == gridY1) ||
               (entityGridX == gridX1 && entityGridY == gridY2) ||
               (entityGridX == gridX2 && entityGridY == gridY2)) {
            return true; // Collision detected
         }
      }

      return false;
   }

   protected void updateAnimation() {
      // Only animate if moving
      if (!moving) {
         // Set standing still image based on direction
         switch (direction) {
            case 0:
               image = rightImages[0];
               break;
            case 1:
               image = downImages[0];
               break;
            case 2:
               image = leftImages[0];
               break;
            case 3:
               image = upImages[0];
               break;
         }
         return;
      }

      // Animation delay to slow down animation
      animationDelay++;
      if (animationDelay >= 2) {
         animationDelay = 0;
         animationStep = (animationStep + 1) % 3;

         // Update the image based on direction and animation step
         switch (direction) {
            case 0:
               image = rightImages[animationStep];
               break;
            case 1:
               image = downImages[animationStep];
               break;
            case 2:
               image = leftImages[animationStep];
               break;
            case 3:
               image = upImages[animationStep];
               break;
         }
      }
   }


}
