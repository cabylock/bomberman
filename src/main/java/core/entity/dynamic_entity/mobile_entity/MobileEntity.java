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
   protected boolean dying = false;

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
   protected transient final float DEAD_ANIMATION_TIME = 0.3f; // Dead animation frame time

   public MobileEntity(int x, int y, int imageId) {
      super(x, y, imageId);
   }

 
   protected boolean move(int direction, int baseSpeed, float deltaTime) {
      if (health <= 0) {
         return false;
      }

      this.direction = direction;
      moving = true;

      // Calculate actual movement distance
      float speed = baseSpeed * deltaTime * 10;

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

      // Enhanced collision check for pathfinding
      if (moveCollision(nextX, nextY)) {
         // If we have a collision, try to align with the grid for smoother navigation
         if (Math.abs(deltaX) > 0) {
            // When moving horizontally, try to align vertically
            float alignedY = Math.round(y / Sprite.DEFAULT_SIZE) * Sprite.DEFAULT_SIZE;
            if (!moveCollision(nextX, alignedY)) {
               x = nextX;
               y = alignedY;
               return true;
            }
         } else if (Math.abs(deltaY) > 0) {
            // When moving vertically, try to align horizontally
            float alignedX = Math.round(x / Sprite.DEFAULT_SIZE) * Sprite.DEFAULT_SIZE;
            if (!moveCollision(alignedX, nextY)) {
               x = alignedX;
               y = nextY;
               return true;
            }
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
      int size = Sprite.DEFAULT_SIZE;
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
      // 1) DEAD state
      if (dying) {
         deadAnimationTimer += deltaTime;
         if (deadAnimationTimer >= DEAD_ANIMATION_TIME) {
            int maxDead = imageIds[Setting.DEAD].length - 1;
            if (animationStep < maxDead) {
               animationStep++;
            } else {
               remove();
               return;
            }
            deadAnimationTimer = 0;
         }
         imageId = imageIds[Setting.DEAD][animationStep];
         return;
      }

      // 2) INVINCIBLE state (Bomber có, Enemy không)
      if (isInvincible) {
         invincibleTimer += deltaTime;
         if (invincibleTimer >= INVINCIBLE_ANIMATION_TIME) {
            int invFrames = imageIds[direction].length;
            animationStep = (animationStep + 1) % invFrames;
            invincibleTimer = 0;
         }
         imageId = imageIds[direction][animationStep];
         return;
      }

      // 3) ALIVE state: move vs idle
      int frames = imageIds[direction].length; // 3 dành cho Bomber, 4 cho Enemy
      if (moving) {
         animationTimer += deltaTime;
         if (animationTimer >= ANIMATION_TIME) {
            animationStep = (animationStep + 1) % frames;
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
      dying = true;
      direction = Setting.DEAD;
      moving = false;
      animationStep = 0; 
      deadAnimationTimer = 0; 
   }

   public boolean isInvincible() {
      return isInvincible;
   }
}
