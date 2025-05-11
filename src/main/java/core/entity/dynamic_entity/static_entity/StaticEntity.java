package core.entity.dynamic_entity.static_entity;

import core.entity.dynamic_entity.DynamicEntity;
import core.system.game.GameControl;

public class StaticEntity extends DynamicEntity {
    protected transient final int DEFAULT_IMAGE = 0;
    public StaticEntity(int x, int y, int imageId) {
        super(x, y, imageId);
    }
    @Override
    public void updateAnimation(float deltaTime) {
        animationTimer += deltaTime;

        // Change animation frame every 0.33 seconds (3 frames per second)
        if (animationTimer >= 0.33) {
            animationStep = (animationStep + 1) % 3;
            animationTimer = 0;
        }

        imageId = imageIds[DEFAULT_IMAGE][animationStep];
    }

    @Override
    public void remove() {
        GameControl.removeEntity(this);
    }

}
