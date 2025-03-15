package core.entity.dynamic_entity;

import core.entity.Entity;

import javafx.scene.image.Image;
import core.entity.map_handle.*;

public abstract class DynamicEntity extends Entity {

   protected int animationStep = 0;
   protected int animationDelay = 0;

   protected final int ALIGN_TOLERANCE = 16; // Example tolerance value

   // Image arrays for animation
   protected Image[][] images;


   public DynamicEntity(int x, int y, Image image) {
      super(x, y, image);
   }

   @Override
   public void update() {
   }

   


   protected abstract void updateAnimation();

   protected void remove() {
      MapEntity.removeDynamicEntity(this);
   }

}
