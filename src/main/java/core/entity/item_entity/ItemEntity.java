package core.entity.item_entity;

import javafx.scene.image.Image;
import core.entity.Entity;
import core.map_handle.MapEntity;
import core.graphics.Sprite;

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
    
    protected boolean checkCollision(int x1, int y1, int x2, int y2) {
        int size = Sprite.SCALED_SIZE;
        return (x1 + size > x2 && x1 < x2 + size
                && y1 + size > y2 && y1 < y2 + size);
    }
    
}
