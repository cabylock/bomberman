package core.entity.item_entity;

import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.map_handle.MapEntity;
import javafx.scene.image.Image;

public class BombItem extends ItemEntity {
   public BombItem(int x, int y, Image image) {
      super(x, y, image);
   }


   @Override
   public void update() {
      for(Bomber bomber : MapEntity.getBomberEntities()){
         if(checkCollision(bomber.getX(), bomber.getY(), this.getX(), this.getY())){
            bomber.increaseBomb();
            MapEntity.removeItem(this);
            return;
         }
      }
   }
   

}
