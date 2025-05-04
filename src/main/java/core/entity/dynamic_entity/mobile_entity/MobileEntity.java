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
import core.sound.Sound;

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

   // Số mạng (health)
   protected transient int health = 1;

   // Còn bao nhiêu giây miễn thương
   protected transient float invincibleRemaining = 0f;
   protected transient boolean isInvincible = false;

   // Bộ đếm cho blink animation khi miễn thương
   protected transient float blinkTimer = 0f;
   protected transient final float BLINK_INTERVAL = 0.1f; // nháy mỗi 0.1s

   protected transient int direction = Setting.DOWN_MOVING;

   protected transient final int ALIGN_TOLERANCE = 16;

   protected transient float deadAnimationTimer = 0;
   protected transient final float ANIMATION_TIME = 0.05f;
   protected transient final float DEAD_ANIMATION_TIME = 0.3f;

   public MobileEntity(int x, int y, int imageId) {
      super(x, y, imageId);
   }

   @Override
   public void update(float deltaTime) {
      updateInvincible(deltaTime);
      updateAnimation(deltaTime);
   }

   protected boolean move(int direction, int baseSpeed, float deltaTime) {
      if (health <= 0) {
         return false;
      }

      this.direction = direction;
      moving = true;

      float speed = baseSpeed * deltaTime * 10;
      float deltaX = 0, deltaY = 0;

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
      }

      float nextX = x + deltaX, nextY = y + deltaY;

      if (moveCollision(nextX, nextY)) {
         if (Math.abs(deltaX) > 0) {
            float alignedY = Math.round(y / Sprite.DEFAULT_SIZE) * Sprite.DEFAULT_SIZE;
            if (!moveCollision(nextX, alignedY)) {
               x = nextX;
               y = alignedY;
               return true;
            }
         } else if (Math.abs(deltaY) > 0) {
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
            if (checkCollision(this.x, this.y, entity.getX(), entity.getY()))
               continue;
            if (checkCollision(nextX, nextY, entity.getX(), entity.getY()) && !bombPass) {
               return true;
            }
         } else if (entity instanceof Brick) {
            if (checkCollision(nextX, nextY, entity.getX(), entity.getY()) && !wallPass) {
               return true;
            }
         }
      }
      for (Entity bg : GameControl.getBackgroundEntities()) {
         if (bg instanceof Grass)
            continue;
         if (checkCollision(nextX, nextY, bg.getX(), bg.getY()) && !wallPass) {
            return true;
         }
      }
      return false;
   }

   protected boolean checkCollision(float x1, float y1, float x2, float y2) {
      int size = Sprite.DEFAULT_SIZE;
      return x1 + size > x2 && x1 < x2 + size
            && y1 + size > y2 && y1 < y2 + size;
   }

   /** Giảm health, và vào trạng thái miễn thương nếu còn sống */
   public void decreaseHealth() {
      if (dying || isInvincible)
         return;

      health--;
      if (health <= 0) {
         dead();
      } else {
         isInvincible = true;
         invincibleRemaining = 3.0f;
         blinkTimer = 0f;
         moving = false;
         animationStep = 0; 
         direction = Setting.ANIMATION_NULL;

      }
   }

   /** Đếm ngược miễn thương */
   public void updateInvincible(float deltaTime) {
      if (!isInvincible)
         return;
      invincibleRemaining -= deltaTime;
      if (invincibleRemaining <= 0f) {
         isInvincible = false;
         invincibleRemaining = 0f;
         blinkTimer = 0f;
      }
   }

   @Override
   protected void updateAnimation(float deltaTime) {
      // 1) DEAD animation
      if (dying) {
         deadAnimationTimer += deltaTime;
         if (deadAnimationTimer >= DEAD_ANIMATION_TIME) {
            int max = imageIds[Setting.DEAD].length - 1;
            if (animationStep < max) {
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

      // 2) INVINCIBLE blink animation
      if (isInvincible) {
         blinkTimer += deltaTime;
         if ((int) (blinkTimer / BLINK_INTERVAL) % 2 == 0) {
            int frames = imageIds[direction].length;
            imageId = imageIds[direction][animationStep % frames];
         } else {
            int frames = imageIds[Setting.ANIMATION_NULL].length;
            imageId = imageIds[Setting.ANIMATION_NULL][animationStep % frames];
         }
         return;
      }

      // 3) NORMAL move vs idle
      int frames = imageIds[direction].length;
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
