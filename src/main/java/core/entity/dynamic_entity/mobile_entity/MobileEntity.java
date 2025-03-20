package core.entity.dynamic_entity.mobile_entity;

import core.entity.Entity;
import core.entity.background_entity.Grass;
import core.entity.dynamic_entity.DynamicEntity;
import core.entity.dynamic_entity.mobile_entity.enemy_entity.EnemyEntity;
import core.entity.dynamic_entity.static_entity.Bomb;
import core.entity.dynamic_entity.static_entity.Brick;
import core.entity.dynamic_entity.static_entity.Flame;
import core.entity.item_entity.ItemEntity;
import core.entity.map_handle.MapEntity;
import core.graphics.Sprite;

import core.system.Setting;
import javafx.scene.image.Image;

public class MobileEntity extends DynamicEntity {
   protected boolean moving = false;
   protected boolean bombpass = false;
   protected boolean flamepass = false;
   protected boolean isAlive = true;

   protected int direction = Setting.DOWN_MOVING;

   protected final int ALIGN_TOLERANCE = 16; // Example tolerance value
   
   

   public MobileEntity(int x, int y, Image image) {
      super(x, y, image);
   }

   @Override
   public void update() {
   }

   protected boolean move(int direction, int delta) {
      // System.out.println("x: " + x + " y: " + y + " xTile: " + this.getXTile() + " yTile: " + this.getYTile());
      moving = true;
      int deltaX = 0;
      int deltaY = 0;

      switch(direction)
      {
         case Setting.RIGHT_MOVING:
            deltaX = delta;
            break;
         case Setting.LEFT_MOVING:
            deltaX = -delta;
            break;
         case Setting.DOWN_MOVING:
            deltaY = delta;
            break;
         case Setting.UP_MOVING:
            deltaY = -delta;
            break;
         default:
            break;
      }

      int nextX = x + deltaX;
      int nextY = y + deltaY;

      if (moveCollision(nextX, nextY)) {
         if (Math.abs(deltaX) > 0 && Math.abs(nextY - this.getYTile() * Sprite.SCALED_SIZE) < ALIGN_TOLERANCE) {
            if (!moveCollision(nextX, this.getYTile() * Sprite.SCALED_SIZE)) {
               x = nextX;
               y = this.getYTile() * Sprite.SCALED_SIZE;
               return true;
            }
            return false;
         } else if (Math.abs(deltaY) > 0 && Math.abs(nextX - this.getXTile() * Sprite.SCALED_SIZE) < ALIGN_TOLERANCE) {
            if (!moveCollision(this.getXTile() * Sprite.SCALED_SIZE, nextY)) {
               x = this.getXTile() * Sprite.SCALED_SIZE;
               y = nextY;
               return true;
            }
            return false;
         }
      } else {
         x = nextX;
         y = nextY;
         return true;
      }
      return false;
   }

   protected boolean moveCollision(int nextX, int nextY) {

      for (DynamicEntity entity : MapEntity.getDynamicEntities()) {
         // collide to bomb
         if (entity instanceof Bomb ) {

            if (this.bombpass) {
               continue;
            }
            // Allow movement if the player is currently standing on the bomb
            if (checkCollision(this.x, this.y, entity.getX(), entity.getY()))
            {
               continue;
            }

            // Block movement if trying to step onto the bomb
            if (checkCollision(nextX, nextY, entity.getX(), entity.getY())) {
               return true;
            }

         
         }  

         // collide to enemy
         else if (this instanceof Bomber && entity instanceof EnemyEntity) {
            if (checkCollision(nextX, nextY, entity.getX(), entity.getY())) {

               System.out.println("Bomber collided with enemy");
               return false;
            }
         }
         else if(entity instanceof Flame)
         {
            if (checkCollision(nextX, nextY, entity.getX(), entity.getY())) {
               this.remove();
               return false;
            }
         }

         else if(entity instanceof Brick)
         {
            if (checkCollision(nextX, nextY, entity.getX(), entity.getY())) {
               return true;
            }
         }

         

       
      }

      for (Entity bg : MapEntity.getBackgroundEntities()) {
         if (bg instanceof Grass) {
            continue;
         }
         if (checkCollision(nextX, nextY, bg.getX(), bg.getY())) {
            return true;
         }
      }
      for (ItemEntity item : MapEntity.getItemEntities()) {
         if (checkCollision(nextX, nextY, item.getX(), item.getY())) {
            item.remove();
            return false;
         }
      }

      return false;
   }

   private boolean checkCollision(int x1, int y1, int x2, int y2) {
      int size = Sprite.SCALED_SIZE;
      return (x1 + size > x2 && x1 < x2 + size
            && y1 + size > y2 && y1 < y2 + size);

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
