package core.entity;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;

import core.graphics.*;
import core.system.setting.Setting;
import core.util.Util;
import java.io.Serializable;

public abstract class Entity implements Serializable {
   private static final long serialVersionUID = 1L;
   protected int id;

   protected double x;
   protected double y;
   protected int imageId;

   public Entity(int xUnit, int yUnit, int imageId) {
      this.x = xUnit * Sprite.SCALED_SIZE;
      this.y = yUnit * Sprite.SCALED_SIZE;
      this.imageId = imageId;
      this.id = Setting.ID;

   }

   public void render(GraphicsContext gc) {
      gc.drawImage(Sprite.getSprite(imageId).getFxImage(), x, y);
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public double getX() {
      return x;
   }

   public double getY() {
      return y;
   }

   public int getXTile() {
      return Util.toGrid(x);
   }

   public int getYTile() {
      return Util.toGrid(y);
   }

   public abstract void update(double deltaTime);

   public abstract void remove();

}
