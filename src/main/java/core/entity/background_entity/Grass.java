package core.entity.background_entity;

public class Grass extends BackgroundEntity {
   public Grass(int x, int y, int imageId) {
      super(x, y, imageId);
   }

   @Override
   public void update(double deltaTime) {
      // No specific update logic for Grass
   }

}
