package core.entity;

import javafx.scene.canvas.GraphicsContext;
import core.graphics.*;
import core.util.Util;
import java.io.Serializable;

public abstract class Entity implements Serializable {
   protected int x;
   protected int y;
   protected int imageId;

   public Entity(int xUnit, int yUnit, int imageId) {
      this.x = xUnit * Sprite.SCALED_SIZE;
      this.y = yUnit * Sprite.SCALED_SIZE;
      this.imageId = imageId;

   }

   public void render(GraphicsContext gc) {
      gc.drawImage(Sprite.sprites[imageId].getFxImage(), x, y);
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
