package core.entity.dynamic_entity.static_entity;

import core.entity.dynamic_entity.DynamicEntity;
import core.system.game.GameControl;

public class StaticEntity extends DynamicEntity {
    public StaticEntity(int x, int y, int imageId) {
        super(x, y, imageId);
    }

    @Override
    protected void updateAnimation(float deltaTime) {
    }

    @Override
    public void remove() {
        GameControl.removeEntity(this);
    }

}
