package core.entity.dynamic_entity.static_entity;

import core.entity.dynamic_entity.DynamicEntity;
import core.system.game.GameControl;


public class StaticEntity extends DynamicEntity {
    public StaticEntity(int x, int y, int imageId) {
        super(x, y, imageId);
    }

    @Override
    public void update() {
    }

    @Override
    protected void updateAnimation() {
    }

    @Override
    public void remove() {
        GameControl.removeEntity(this);
    }

}
