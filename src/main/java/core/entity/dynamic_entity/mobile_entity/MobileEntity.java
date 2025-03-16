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
import javafx.scene.image.Image;

public class MobileEntity extends DynamicEntity {
   protected boolean moving = false;
   protected int direction = 0;// 0: up, 1: right, 2: down, 3: left
   protected boolean bombpass = false;
   protected boolean flamepass = false;
   protected boolean isAlive = true;
   protected final int ALIGN_TOLERANCE = 16; // Example tolerance value
   protected final int RIGHT_MOVING = 0;
   protected final int LEFT_MOVING = 1;
   protected final int UP_MOVING = 2;
   protected final int DOWN_MOVING = 3;
   

   public MobileEntity(int x, int y, Image image) {
      super(x, y, image);
   }

   @Override
   public void update() {
   }

   protected boolean move(int direction, int delta) {
      // System.out.println("x: " + x + " y: " + y + " xTile: " + this.getXTile() + " yTile: " + this.getYTile());
      moving = true;
      this.direction = direction;
      int deltaX = 0;
      int deltaY = 0;
      switch (direction) {
         case RIGHT_MOVING:
             deltaX = delta;
             deltaY = 0;
            break;
         case LEFT_MOVING:
            deltaX = -delta;
            deltaY = 0;
            break;
         case UP_MOVING:
            deltaX = 0;
            deltaY = -delta;
            break;
         case DOWN_MOVING:
            deltaX = 0;
            deltaY = delta;
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
   }

 

}
