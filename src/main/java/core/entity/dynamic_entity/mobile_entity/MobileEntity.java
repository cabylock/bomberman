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
   
   protected transient boolean moving = false;
   protected transient boolean flamePass = false;
   protected transient boolean bombPass = false;
   protected transient boolean speedUp = false;
   protected transient boolean flameUp = false;
   protected transient boolean bombUp = false;
   protected transient boolean wallPass = false;

   protected transient float flamePassTime = 0;
   protected transient float bombPassTime = 0;
   protected transient float speedUpTime = 0;
   protected transient float flameUpTime = 0;
   protected transient float bombUpTime = 0;
   protected transient float wallPassTime = 0;

   protected transient int health = 1;
   protected transient boolean isInvincible = false;
   protected transient int invincibleTime = 180; // 180 frames = 1 giây

   protected transient int direction = Setting.DOWN_MOVING;

   protected transient final int ALIGN_TOLERANCE = 16;

   protected transient float invincibleTimer = 0;
   protected transient float deadAnimationTimer = 0;
   protected transient final float INVINCIBLE_ANIMATION_TIME = 0.1f; // Invincible animation frame time
   protected transient final float ANIMATION_TIME = 0.05f; // Animation frame time in seconds
   protected transient final float DEAD_ANIMATION_TIME = 0.5f; // Dead animation frame time

   public MobileEntity(int x, int y, int imageId) {
      super(x, y, imageId);
   }

   @Override
   public void update(float deltaTime) {

   }

   protected boolean move(int direction, int baseSpeed, float deltaTime) {
      if (health <= 0) {
         return false;
      }

      this.direction = direction;
      moving = true;

      // Calculate actual movement distance based on delta time
      float speed = baseSpeed * deltaTime * 10; // Remove the (int) cast to allow fractional movement

      float deltaX = 0;
      float deltaY = 0;

      switch (direction) {
         case Setting.RIGHT_MOVING:
            deltaX = speed;
            break;
         case Setting.LEFT_MOVING:
            deltaX = -speed;
            break;
         case Setting.DOWN_MOVING:
            deltaY = speed;
            break;
         case Setting.UP_MOVING:
            deltaY = -speed;
            break;
         default:
            break;
      }

      float nextX = x + deltaX;
      float nextY = y + deltaY;

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

   protected boolean moveCollision(float nextX, float nextY) {
      for (StaticEntity entity : GameControl.getStaticEntities()) {
         if (entity instanceof Bomb) {
            if (checkCollision(this.x, this.y, entity.getX(), entity.getY())) {
               continue;
            }

            if (checkCollision(nextX, nextY, entity.getX(), entity.getY())) {
               if (this.bombPass) {
                  return false;
               }
               return true;
            }
         } else if (entity instanceof Brick) {
            if (checkCollision(nextX, nextY, entity.getX(), entity.getY())) {
               if (this.wallPass) {
                  return false;
               }
               return true;
            }
         }
      }

      for (Entity bg : GameControl.getBackgroundEntities()) {
         if (bg instanceof Grass) {
            continue;
         }
         if (checkCollision(nextX, nextY, bg.getX(), bg.getY())) {
            if (this.wallPass) {
               return false;
            }
            return true;
         }
      }
      return false;
   }

   protected boolean checkCollision(float x1, float y1, float x2, float y2) {
      int size = Sprite.SCALED_SIZE;
      return (x1 + size > x2 && x1 < x2 + size
            && y1 + size > y2 && y1 < y2 + size);

   }

   public void updateInvincible(float deltaTime) {
      if (isInvincible) {
         invincibleTimer += deltaTime;
         if (invincibleTimer >= invincibleTime) {
            isInvincible = false;
            invincibleTimer = 0;
         }
      }

   }

   @Override
   protected void updateAnimation(float deltaTime) {
      if (health <= 0) {
         deadAnimationTimer += deltaTime;
         if (deadAnimationTimer >= DEAD_ANIMATION_TIME) {
            if (animationStep < 2) {
               animationStep++;
            } else {
               this.remove();
               return;
            }
            deadAnimationTimer = 0;
         }

      } else if (isInvincible) {
         invincibleTimer += deltaTime;
         if (invincibleTimer >= INVINCIBLE_ANIMATION_TIME) {
            animationStep = (animationStep + 1) % 3;
            invincibleTimer = 0;
         }

      } else if (moving) {
         animationTimer += deltaTime;
         if (animationTimer >= ANIMATION_TIME) {
            animationStep = (animationStep + 1) % 3;
            animationTimer = 0;
         }
      } else {
         animationStep = 0;
         animationTimer = 0;
      }
      imageId = imageIds[direction][animationStep];
   }

   public void decreaseHealth() {
      if (!isInvincible) {
         if (health > 0) {

            this.health--;
         }
         if (this.health <= 0) {
            this.dead();
         } else {

            isInvincible = true;
            invincibleTime = 180;
            direction = Setting.ANIMATION_NULL;
         }

      }
   }

   public void dead() {

      direction = Setting.DEAD;
      moving = false;
   }

   public boolean isInvincible() {
      return isInvincible;
   }
}
