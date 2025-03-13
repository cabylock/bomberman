package core.entity.static_entity;
import core.entity.Entity;

import javafx.scene.image.Image;

public class Wall extends StaticEntity {
   public Wall(int x, int y, Image img) {
      super(x, y, img);
   }
    
   @Override
   public void update() {
   }
   
}
