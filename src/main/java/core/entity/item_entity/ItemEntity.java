package core.entity.item_entity;

import javafx.scene.image.Image;
import core.entity.Entity;
import core.map_handle.MapEntity;

public class ItemEntity extends Entity {
    public ItemEntity(int x, int y, Image image) {
        super(x, y, image);
    }

    @Override
    public void update() {
    }

    @Override
    public void remove() {
        MapEntity.removeItem(this);
    }
    
}
