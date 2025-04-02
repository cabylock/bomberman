package core.entity.dynamic_entity.static_entity;

import javafx.scene.image.Image;
import core.entity.background_entity.Wall;



import core.entity.background_entity.BackgroundEntity;
import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.entity.dynamic_entity.mobile_entity.enemy_entity.EnemyEntity;


import core.entity.item_entity.ItemEntity;
import core.graphics.Sprite;
import core.map_handle.MapEntity;


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

     
 
      public Flame(int x, int y, Image image, int flameType) {
         super(x, y, Sprite.bomb_exploded.getFxImage());
         images = new Image[7][3];
         // Center flame
         if (flameType == CENTER) {
            images[CENTER][0] = Sprite.bomb_exploded.getFxImage();
            images[CENTER][1] = Sprite.bomb_exploded1.getFxImage();
            images[CENTER][2] = Sprite.bomb_exploded2.getFxImage();
         }
         // Horizontal flame
         if (flameType == HORIZONTAL) {
            images[HORIZONTAL][0] = Sprite.explosion_horizontal.getFxImage();
            images[HORIZONTAL][1] = Sprite.explosion_horizontal1.getFxImage();
            images[HORIZONTAL][2] = Sprite.explosion_horizontal2.getFxImage();
         }
         // Vertical flame
         if (flameType == VERTICAL) {
            images[VERTICAL][0] = Sprite.explosion_vertical.getFxImage();
            images[VERTICAL][1] = Sprite.explosion_vertical1.getFxImage();
            images[VERTICAL][2] = Sprite.explosion_vertical2.getFxImage();
         }
         // Left end flame
         if (flameType == LEFT_END) {
            images[LEFT_END][0] = Sprite.explosion_horizontal_left_last.getFxImage();
            images[LEFT_END][1] = Sprite.explosion_horizontal_left_last1.getFxImage();
            images[LEFT_END][2] = Sprite.explosion_horizontal_left_last2.getFxImage();
         }
         // Right end flame
         if (flameType == RIGHT_END) {
            images[RIGHT_END][0] = Sprite.explosion_horizontal_right_last.getFxImage();
            images[RIGHT_END][1] = Sprite.explosion_horizontal_right_last1.getFxImage();
            images[RIGHT_END][2] = Sprite.explosion_horizontal_right_last2.getFxImage();
         }
         // Up end flame
         if (flameType == UP_END) {
            images[UP_END][0] = Sprite.explosion_vertical_top_last.getFxImage();
            images[UP_END][1] = Sprite.explosion_vertical_top_last1.getFxImage();
            images[UP_END][2] = Sprite.explosion_vertical_top_last2.getFxImage();
         }
         // Down end flame
         if (flameType == DOWN_END) {
            images[DOWN_END][0] = Sprite.explosion_vertical_down_last.getFxImage();
            images[DOWN_END][1] = Sprite.explosion_vertical_down_last1.getFxImage();
            images[DOWN_END][2] = Sprite.explosion_vertical_down_last2.getFxImage();
         }

         this.flameType = flameType;
         this.image = images[flameType][0];

      }

      //true if the flame is blocked with a wall/brick
      protected boolean flamecollision() {
         for (StaticEntity entity : MapEntity.getStaticEntities()) {
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
         for (Bomber entity : MapEntity.getBomberEntities()) {
            if (entity.getXTile() == this.getXTile() && entity.getYTile() == this.getYTile()) {
                  entity.dead();
                  return false; 
                  
               }
            }
            for (EnemyEntity entity : MapEntity.getEnemyEntities()) {
               if (entity.getXTile() == this.getXTile() && entity.getYTile() == this.getYTile()) {
                  entity.remove();
                  return false;
               }
            }

         
         for (BackgroundEntity entity : MapEntity.getBackgroundEntities()) {
            if (entity instanceof Wall) {
               if (entity.getX() == x && entity.getY() == y) {
                  remove();
                  return true; 
               }
            }
         }
         for(ItemEntity entity : MapEntity.getItemEntities())
         {
            if(entity.getXTile() == this.getXTile() && entity.getYTile() == this.getYTile())
            {
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
         


         image = images[flameType][animationStep];
      }

      public void remove() {
         MapEntity.removeStaticEntity(this);
      }
      

}
