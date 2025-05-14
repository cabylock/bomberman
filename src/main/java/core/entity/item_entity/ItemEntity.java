package core.entity.item_entity;

import core.entity.Entity;
import core.system.game.GameControl;
import core.graphics.Sprite;
import core.entity.dynamic_entity.static_entity.Brick;

public class ItemEntity extends Entity {
    public ItemEntity(int x, int y, int imageId) {
        super(x, y, imageId);
    }

    @Override
    public void update(float deltaTime) {
        // Base implementation does nothing
    }

    @Override
    public void remove() {
        GameControl.removeEntity(this);
    }

    protected boolean checkCollision(float x1, float y1, float x2, float y2) {
        int size = Sprite.DEFAULT_SIZE;
        return (x1 + size > x2 && x1 < x2 + size
                && y1 + size > y2 && y1 < y2 + size);
    }

    protected boolean isBrickAtPosition() {
        for (Entity entity : GameControl.getStaticEntities()) {
            if (entity instanceof Brick && entity.getX() == this.getX()
                    && entity.getY() == this.getY()) {
                return true;
            }
        }
        return false;
    }
}
