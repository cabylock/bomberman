package core.entity.dynamic_entity;

import core.entity.Entity;

public abstract class DynamicEntity extends Entity {

   protected int animationStep = 0;
   protected int animationDelay = 0;

   // Image arrays for animation
   protected int[][] imageIds;

   public DynamicEntity(int x, int y, int imageId) {
      super(x, y, imageId);
   }

   @Override
   public void update(double deltaTime) {
   }

   // Keep the old update method for backward compatibility
  

   protected abstract void updateAnimation(double deltaTime);

   @Override
   public void remove() {

   }

}
