package core.entity.background_entity;

import core.entity.Entity;

public class BackgroundEntity extends Entity {

   public BackgroundEntity(int x, int y, int imageId) {
      super(x, y, imageId);
   }

   @Override
   public void update(float deltaTime) {
      // Background entities don't need updating
   }

   @Override
   public void remove() {
   }

}
