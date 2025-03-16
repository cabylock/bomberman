package core.entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import core.graphics.*;
import core.util.Util;

public abstract class Entity {
   protected int x;
   protected int y;
   protected Image image;


   public Entity(int xUnit, int yUnit, Image image) {
      this.x = xUnit * Sprite.SCALED_SIZE;
      this.y = yUnit * Sprite.SCALED_SIZE;
      this.image = image;

   }

   public void render(GraphicsContext gc) {
      gc.drawImage(image, x, y);
   }

   public int getX() {
      return x;
   }

   public int getY() {
      return y;
   }

   public int getXTile() {
      return Util.toGrid(x);
   }

   public int getYTile() {
      return Util.toGrid(y);
   }

   


   public abstract void update();
   public abstract void remove();

}
