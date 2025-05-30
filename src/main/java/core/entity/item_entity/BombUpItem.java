package core.entity.item_entity;

import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.system.game.GameControl;
import core.sound.Sound;

public class BombUpItem extends ItemEntity {
   public BombUpItem(int x, int y, int imageId) {
      super(x, y, imageId);
   }

   @Override
   public void update(float deltaTime) {
      for (Bomber bomber : GameControl.getBomberEntities()) {
         if (checkCollision(bomber.getX(), bomber.getY(), this.getX(),
               this.getY()) && !isBrickAtPosition()) {
            Sound.playEffect(Sound.GET_ITEM);
            bomber.setBombUp(true);
            GameControl.removeEntity(this);
            return;
         }
      }
   }

}
