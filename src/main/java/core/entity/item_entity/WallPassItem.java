package core.entity.item_entity;

import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.system.game.GameControl;

public class WallPassItem extends ItemEntity {
    public WallPassItem(int x, int y, int imageId) {
        super(x, y, imageId);
    }

    @Override
    public void update(float deltaTime) {
        for (Bomber bomber : GameControl.getBomberEntities()) {
            if (checkCollision(bomber.getX(), bomber.getY(), getX(), getY())) {
                bomber.setWallPass(true);
                remove();
            }
        }
    }

}
