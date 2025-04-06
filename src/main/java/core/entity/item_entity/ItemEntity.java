package core.entity.item_entity;

import core.entity.Entity;
import core.system.game.GameControl;    
import core.graphics.Sprite;

public class ItemEntity extends Entity {
    public ItemEntity(int x, int y, int imageId) {
        super(x, y, imageId);
    }

    @Override
    public void update() {
    }

    @Override
    public void remove() {
        GameControl.removeEntity(this);
    }

    protected boolean checkCollision(int x1, int y1, int x2, int y2) {
        int size = Sprite.SCALED_SIZE;
        return (x1 + size > x2 && x1 < x2 + size
                && y1 + size > y2 && y1 < y2 + size);
    }

}
