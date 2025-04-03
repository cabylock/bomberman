package core.entity.item_entity;

import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.system.game.GameControl;
import javafx.scene.image.Image;

public class FlameItem extends ItemEntity {
    public FlameItem(int x, int y, Image image) {
        super(x, y, image);

    }

    @Override
    public void update() {
        for (Bomber bomber : GameControl.getBomberEntities()) {
            if (checkCollision(bomber.getX(), bomber.getY(), this.getX(), this.getY())) {
                bomber.increaseFlameSize();
                GameControl.removeEntity(this);
                return;
            }
        }

    }

}
