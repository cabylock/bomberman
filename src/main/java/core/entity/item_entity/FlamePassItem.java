package core.entity.item_entity;

import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.system.game.GameControl;

public class FlamePassItem extends ItemEntity {
    public FlamePassItem(int x, int y, int imageId) {
        super(x, y, imageId);
    }

    @Override
    public void update(double deltaTime) {
        for (Bomber bomber : GameControl.getBomberEntities()) {
            if (checkCollision(bomber.getX(), bomber.getY(), this.getX(), this.getY())) {
                bomber.setFlamePass(true);
                GameControl.removeEntity(this);
                return;
            }
        }
    }
}
