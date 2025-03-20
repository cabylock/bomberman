package core.entity.dynamic_entity;

import core.entity.Entity;
import core.map_handle.*;
import javafx.scene.image.Image;

public abstract class DynamicEntity extends Entity {

   protected int animationStep = 0;
   protected int animationDelay = 0;

 

   // Image arrays for animation
   protected Image[][] images;


   public DynamicEntity(int x, int y, Image image) {
      super(x, y, image);
   }

   @Override
   public void update() {
   }

   


   protected abstract void updateAnimation();

   @Override
   public void remove() {
      MapEntity.removeDynamicEntity(this);
   }

}
