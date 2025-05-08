package core.entity.item_entity;

import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.system.game.GameControl;
import core.sound.Sound;
public class BrickPassItem extends ItemEntity {
    public BrickPassItem(int x, int y, int imageId) {
        super(x, y, imageId);
    }

    @Override
    public void update(float deltaTime) {
        for (Bomber bomber : GameControl.getBomberEntities()) {
            if (checkCollision(bomber.getX(), bomber.getY(), getX(), getY())) {
                Sound.playEffect("get_item");
                bomber.setBrickPass(true);
                remove();
            }
        }
    }

}
