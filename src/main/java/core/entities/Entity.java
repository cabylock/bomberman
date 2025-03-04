package core.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import core.graphics.*;


public abstract class Entity {
      protected int x;
      protected int y;
      protected Image image;

      public Entity(int xUnit, int yUnit, Image img) {
         this.x = xUnit * Sprite.SCALED_SIZE;
         this.y = yUnit * Sprite.SCALED_SIZE;
         this.image = img;
      }
      
      public void render(GraphicsContext gc) {
         gc.drawImage(image, x, y);
      }

      public abstract void update();



}
