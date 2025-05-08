package core.entity.dynamic_entity.static_entity;

import core.entity.background_entity.Wall;

import core.entity.background_entity.BackgroundEntity;
import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.entity.dynamic_entity.mobile_entity.enemy_entity.EnemyEntity;

import core.entity.item_entity.ItemEntity;
import core.graphics.Sprite;
import core.system.game.GameControl;

public class Flame extends StaticEntity {
   // Animation constants

  
   private transient float timeAlive = 1.0f; // 1 second lifetime
   

   private transient final int CENTER = 0;
   private transient final int HORIZONTAL = 1;
   private transient final int VERTICAL = 2;
   private transient final int LEFT_END = 3;
   private transient final int RIGHT_END = 4;
   private transient final int UP_END = 5;
   private transient final int DOWN_END = 6;
   private transient int flameType;

   public Flame(int x, int y, int imageId, int flameType) {
      super(x, y, imageId);
      imageIds = new int[7][3];
      // Center flame
      if (flameType == CENTER) {
         imageIds[CENTER][0] = Sprite.BOMB_EXPLODED_0;
         imageIds[CENTER][1] = Sprite.BOMB_EXPLODED_1;
         imageIds[CENTER][2] = Sprite.BOMB_EXPLODED_2;
      }
      // Horizontal flame
      if (flameType == HORIZONTAL) {
         imageIds[HORIZONTAL][0] = Sprite.EXPLOSION_HORIZONTAL_0;
         imageIds[HORIZONTAL][1] = Sprite.EXPLOSION_HORIZONTAL_1;
         imageIds[HORIZONTAL][2] = Sprite.EXPLOSION_HORIZONTAL_2;
      }
      // Vertical flame
      if (flameType == VERTICAL) {
         imageIds[VERTICAL][0] = Sprite.EXPLOSION_VERTICAL_0;
         imageIds[VERTICAL][1] = Sprite.EXPLOSION_VERTICAL_1;
         imageIds[VERTICAL][2] = Sprite.EXPLOSION_VERTICAL_2;
      }
      // Left end flame
      if (flameType == LEFT_END) {
         imageIds[LEFT_END][0] = Sprite.EXPLOSION_HORIZONTAL_LEFT_LAST_0;
         imageIds[LEFT_END][1] = Sprite.EXPLOSION_HORIZONTAL_LEFT_LAST_1;
         imageIds[LEFT_END][2] = Sprite.EXPLOSION_HORIZONTAL_LEFT_LAST_2;
      }
      // Right end flame
      if (flameType == RIGHT_END) {
         imageIds[RIGHT_END][0] = Sprite.EXPLOSION_HORIZONTAL_RIGHT_LAST_0;
         imageIds[RIGHT_END][1] = Sprite.EXPLOSION_HORIZONTAL_RIGHT_LAST_1;
         imageIds[RIGHT_END][2] = Sprite.EXPLOSION_HORIZONTAL_RIGHT_LAST_2;
      }
      // Up end flame
      if (flameType == UP_END) {
         imageIds[UP_END][0] = Sprite.EXPLOSION_VERTICAL_TOP_LAST_0;
         imageIds[UP_END][1] = Sprite.EXPLOSION_VERTICAL_TOP_LAST_1;
         imageIds[UP_END][2] = Sprite.EXPLOSION_VERTICAL_TOP_LAST_2;
      }
      // Down end flame
      if (flameType == DOWN_END) {
         imageIds[DOWN_END][0] = Sprite.EXPLOSION_VERTICAL_DOWN_LAST_0;
         imageIds[DOWN_END][1] = Sprite.EXPLOSION_VERTICAL_DOWN_LAST_1;
         imageIds[DOWN_END][2] = Sprite.EXPLOSION_VERTICAL_DOWN_LAST_2;
      }

      this.flameType = flameType;
      this.imageId = imageIds[flameType][0];

   }

   // true if the flame is blocked with a wall/brick
   protected boolean flamecollision() {
      for (StaticEntity entity : GameControl.getStaticEntities()) {
         if (entity instanceof Flame) {
            continue;
         }
         if (entity instanceof Bomb) {
            if (entity.getXTile() == this.getXTile() && entity.getYTile() == this.getYTile()) {
               ((Bomb) entity).explode();
               return false;
            }
         }
         if (entity instanceof Brick) {
            if (entity.getX() == x && entity.getY() == y) {
               entity.remove();
               return true;
            }
         }
      }
      for (Bomber entity : GameControl.getBomberEntities()) {
         if (entity.getXTile() == this.getXTile() && entity.getYTile() == this.getYTile()) {
            if (!entity.isFlamePass() && !entity.isInvincible()) {
               entity.decreaseHealth();
            }
            return false;
         }
      }
      for (EnemyEntity entity : GameControl.getEnemyEntities()) {
         if (entity.getXTile() == this.getXTile() && entity.getYTile() == this.getYTile()) {
            entity.decreaseHealth();;
            return false;
         }
      }

      for (BackgroundEntity entity : GameControl.getBackgroundEntities()) {
         if (entity instanceof Wall) {
            if (entity.getX() == x && entity.getY() == y) {
               remove();
               return true;
            }
         }
      }
      for (ItemEntity entity : GameControl.getItemEntities()) {
         if (entity.getXTile() == this.getXTile() && entity.getYTile() == this.getYTile()) {
            entity.remove();
            return false;
         }
      }

      return false;

   }

   @Override
   public void update(float deltaTime) {
      timeAlive -= deltaTime;
      if (timeAlive <= 0) {
         remove();
      }
      flamecollision();
      updateAnimation(deltaTime);
   }

   @Override
   public void updateAnimation(float deltaTime) {
      animationTimer += deltaTime;

      // Change animation frame every 0.33 seconds (3 frames per second)
      if (animationTimer >= 0.33) {
         animationStep = (animationStep + 1) % 3;
         animationTimer = 0;
      }

      imageId = imageIds[flameType][animationStep];
   }

   public void remove() {
      GameControl.removeEntity(this);
   }

}
