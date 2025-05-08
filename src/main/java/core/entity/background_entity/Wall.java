package core.entity.background_entity;

public class Wall extends BackgroundEntity {
   public Wall(int x, int y, int imageId) {
      super(x, y, imageId);
   }

   @Override
   public void update(float deltaTime) {
      // No specific update logic for Wall
   }

}
