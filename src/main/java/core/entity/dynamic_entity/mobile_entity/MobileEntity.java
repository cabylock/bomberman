package core.entity.dynamic_entity.mobile_entity;

import core.entity.Entity;
import core.entity.background_entity.Grass;
import core.entity.dynamic_entity.DynamicEntity;
import core.entity.dynamic_entity.static_entity.StaticEntity;
import core.entity.dynamic_entity.static_entity.Bomb;
import core.entity.dynamic_entity.static_entity.Brick;
import core.graphics.Sprite;
import core.system.game.GameControl;
import core.system.setting.Setting;


public class MobileEntity extends DynamicEntity {
   protected boolean moving = false;
   protected boolean bombpass = false;
   protected boolean flamepass = false;
   protected boolean isAlive = true;

   protected int direction = Setting.DOWN_MOVING;

   protected final int ALIGN_TOLERANCE = 16;
   protected final int ANIMATION_DELAY = 10;
   protected final int DEAD_ANIMATION_DELAY = 40;

   public MobileEntity(int x, int y, int imageId) {
      super(x, y, imageId);   
   }

   @Override
   public void update() {
   }

   protected boolean move(int direction, int delta) {
      // System.out.println("x: " + x + " y: " + y + " xTile: " + this.getXTile() + "
      // yTile: " + this.getYTile());
      if (!isAlive) {
         return false;
      }

      this.direction = direction; // Cập nhật hướng trước
      moving = true; // Đặt trạng thái di chuyển

      int deltaX = 0;
      int deltaY = 0;

      switch (direction) {
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
         return false;
      } else {
         x = nextX;
         y = nextY;
         return true;
      }
   }

   protected boolean moveCollision(int nextX, int nextY) {
      for (StaticEntity entity : GameControl.getStaticEntities()) {
         if (entity instanceof Bomb) {
            if (this.bombpass) {
               continue;
            }
            if (checkCollision(this.x, this.y, entity.getX(), entity.getY())) {
               continue;
            }
            if (checkCollision(nextX, nextY, entity.getX(), entity.getY())) {
               return true;
            }
         } else if (entity instanceof Brick) {
            if (checkCollision(nextX, nextY, entity.getX(), entity.getY())) {
               
               return true;
            }
         } 
      }

      for (Entity bg : GameControl.getBackgroundEntities()) {
         if (bg instanceof Grass) {
            continue;
         }
         if (checkCollision(nextX, nextY, bg.getX(), bg.getY())) {
            return true;
         }
      }
      return false;
   }

   protected boolean checkCollision(int x1, int y1, int x2, int y2) {
      int size = Sprite.SCALED_SIZE;
      return (x1 + size > x2 && x1 < x2 + size
            && y1 + size > y2 && y1 < y2 + size);

   }

   @Override
   protected void updateAnimation() {
      animationDelay++;

      if (!isAlive) {
         if (animationDelay >= DEAD_ANIMATION_DELAY) {
            if (animationStep < 2) {
               animationStep++;
            } else {
               this.remove();
               return;
            }
            animationDelay = 0;
         }
      } else if (moving) {
         if (animationDelay >= ANIMATION_DELAY) {
            animationStep = (animationStep + 1) % 3;
            animationDelay = 0;
         }
      } else {
         animationStep = 0;
         animationDelay = 0;
      }

      imageId = imageIds[direction][animationStep];
   }

}
