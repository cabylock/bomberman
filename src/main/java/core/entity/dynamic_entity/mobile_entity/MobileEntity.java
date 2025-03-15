package core.entity.dynamic_entity.mobile_entity;

import core.entity.Entity;
import core.entity.background_entity.Grass;
import core.entity.dynamic_entity.DynamicEntity;
import core.entity.dynamic_entity.mobile_entity.enemy_entity.EnemyEntity;
import core.entity.dynamic_entity.static_entity.Bomb;
import core.entity.map_handle.MapEntity;
import core.graphics.Sprite;
import javafx.scene.image.Image;

public class MobileEntity extends DynamicEntity {
   protected boolean moving = false;
   protected int direction = 0;// 0: up, 1: right, 2: down, 3: left
   protected boolean bombpass = false;

   public MobileEntity(int x, int y, Image image) {
      super(x, y, image);
   }

   @Override
   public void update() {
   }

   protected void move(int direction, int deltaX, int deltaY) {
      System.out.println("x: " + x + " y: " + y + " xTile: " + this.getXTile() + " yTile: " + this.getYTile());
      moving = true;
      this.direction = direction;
      int nextX = x + deltaX;
      int nextY = y + deltaY;

      if (isColliding(nextX, nextY)) {
         if (Math.abs(deltaX) > 0 && Math.abs(nextY - this.getYTile() * Sprite.SCALED_SIZE) < ALIGN_TOLERANCE) {
            if (!isColliding(nextX, this.getYTile() * Sprite.SCALED_SIZE)) {
               x = nextX;
               y = this.getYTile() * Sprite.SCALED_SIZE;
            }
         } else if (Math.abs(deltaY) > 0 && Math.abs(nextX - this.getXTile() * Sprite.SCALED_SIZE) < ALIGN_TOLERANCE) {
            if (!isColliding(this.getXTile() * Sprite.SCALED_SIZE, nextY)) {
               x = this.getXTile() * Sprite.SCALED_SIZE;
               y = nextY;
            }
         }
      } else {
         x = nextX;
         y = nextY;
      }
   }

   protected boolean isColliding(int nextX, int nextY) {

      for (Entity entity : MapEntity.getDynamicEntities()) {
         // collide to bomb
         if (entity instanceof Bomb) {

            if (this.bombpass) {
               continue;
            }
            // Allow movement if the player is currently standing on the bomb
            if (checkCollision(this.x, this.y, entity.getX(), entity.getY()))
            {
               continue;
            }

            // Block movement if trying to step onto the bomb
            if ( checkCollision(nextX, nextY, entity.getX(), entity.getY())) {
                  return true;
               }
         }  

         else if (this instanceof Bomber && entity instanceof EnemyEntity) {
            if (checkCollision(nextX, nextY, entity.getX(), entity.getY())) {

               System.out.println("Bomber collided with enemy");
               return true;
            }
         }

         else if (entity != this && checkCollision(nextX, nextY, entity.getX(), entity.getY())) {
            return true;
         }
      }

      for (Entity entity : MapEntity.getBackgroundEntities()) {
         if (entity instanceof Grass) {
            continue;
         }
         if (checkCollision(nextX, nextY, entity.getX(), entity.getY())) {
            return true;
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

   @Override
   public void remove() {
   }

}
