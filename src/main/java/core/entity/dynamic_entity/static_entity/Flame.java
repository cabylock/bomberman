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

   protected int length; // Length of the flame
   protected int timeAlive = 60; // 60 frames = 1 second

   protected final int CENTER = 0;
   protected final int HORIZONTAL = 1;
   protected final int VERTICAL = 2;
   protected final int LEFT_END = 3;
   protected final int RIGHT_END = 4;
   protected final int UP_END = 5;
   protected final int DOWN_END = 6;
   protected int flameType;

   public Flame(int x, int y, int imageId, int flameType) {
      super(x, y, imageId);
      imageIds = new int[7][3];
      // Center flame
      if (flameType == CENTER) {
         imageIds[CENTER][0] = Sprite.BOMB_EXPLODED;
         imageIds[CENTER][1] = Sprite.BOMB_EXPLODED1;
         imageIds[CENTER][2] = Sprite.BOMB_EXPLODED2;
      }
      // Horizontal flame
      if (flameType == HORIZONTAL) {
         imageIds[HORIZONTAL][0] = Sprite.EXPLOSION_HORIZONTAL;
         imageIds[HORIZONTAL][1] = Sprite.EXPLOSION_HORIZONTAL1;
         imageIds[HORIZONTAL][2] = Sprite.EXPLOSION_HORIZONTAL2;
      }
      // Vertical flame
      if (flameType == VERTICAL) {
         imageIds[VERTICAL][0] = Sprite.EXPLOSION_VERTICAL;
         imageIds[VERTICAL][1] = Sprite.EXPLOSION_VERTICAL1;
         imageIds[VERTICAL][2] = Sprite.EXPLOSION_VERTICAL2;
      }
      // Left end flame
      if (flameType == LEFT_END) {
         imageIds[LEFT_END][0] = Sprite.EXPLOSION_HORIZONTAL_LEFT_LAST;
         imageIds[LEFT_END][1] = Sprite.EXPLOSION_HORIZONTAL_LEFT_LAST1;
         imageIds[LEFT_END][2] = Sprite.EXPLOSION_HORIZONTAL_LEFT_LAST2;
      }
      // Right end flame
      if (flameType == RIGHT_END) {
         imageIds[RIGHT_END][0] = Sprite.EXPLOSION_HORIZONTAL_RIGHT_LAST;
         imageIds[RIGHT_END][1] = Sprite.EXPLOSION_HORIZONTAL_RIGHT_LAST1;
         imageIds[RIGHT_END][2] = Sprite.EXPLOSION_HORIZONTAL_RIGHT_LAST2;
      }
      // Up end flame
      if (flameType == UP_END) {
         imageIds[UP_END][0] = Sprite.EXPLOSION_VERTICAL_TOP_LAST;
         imageIds[UP_END][1] = Sprite.EXPLOSION_VERTICAL_TOP_LAST1;
         imageIds[UP_END][2] = Sprite.EXPLOSION_VERTICAL_TOP_LAST2;
      }
      // Down end flame
      if (flameType == DOWN_END) {
         imageIds[DOWN_END][0] = Sprite.EXPLOSION_VERTICAL_DOWN_LAST;
         imageIds[DOWN_END][1] = Sprite.EXPLOSION_VERTICAL_DOWN_LAST1;
         imageIds[DOWN_END][2] = Sprite.EXPLOSION_VERTICAL_DOWN_LAST2;
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
            entity.remove();
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
   public void update() {
      timeAlive--;
      if (timeAlive == 0) {
         remove();
      }
      flamecollision();
      updateAnimation();

   }

   @Override
   public void updateAnimation() {

      if (animationDelay == 0) {
         animationStep = (animationStep + 1) % 3;
         animationDelay = 20;
      } else {
         animationDelay--;
      }

      imageId = imageIds[flameType][animationStep];
   }

   public void remove() {
      GameControl.removeEntity(this);
   }

}
