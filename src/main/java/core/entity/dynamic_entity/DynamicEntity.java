package core.entity.dynamic_entity;

import core.entity.Entity;

public abstract class DynamicEntity extends Entity {

   protected transient int animationStep = 0;
   protected transient float animationTimer = 0;

   // Image arrays for animation
   protected transient int[][] imageIds;

   public DynamicEntity(int x, int y, int imageId) {
      super(x, y, imageId);
   }

   @Override
   public void update(float deltaTime) {
   }

   protected abstract void updateAnimation(float deltaTime);

   @Override
   public void remove() {

   }

}
