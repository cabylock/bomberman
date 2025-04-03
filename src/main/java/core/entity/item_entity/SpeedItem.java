package core.entity.item_entity;

import javafx.scene.image.Image;
import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.map_handle.MapEntity;

public class SpeedItem extends ItemEntity {
    public SpeedItem(int x, int y, Image image) {
        super(x, y, image);
    }

    @Override
    public void update() {
        for (Bomber bomber : MapEntity.getBomberEntities()) {
            if (checkCollision(bomber.getX(), bomber.getY(), this.getX(), this.getY())) {
                bomber.increaseSpeed();
                MapEntity.removeItem(this);
                return;
            }
        }
    }

   
}
