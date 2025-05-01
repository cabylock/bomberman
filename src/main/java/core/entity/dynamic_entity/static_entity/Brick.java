package core.entity.dynamic_entity.static_entity;

import core.graphics.Sprite;
import core.system.game.GameControl;

public class Brick extends StaticEntity {
    private transient float timeAlive = 1f; // 0.5 giây trước khi biến mất
    private transient boolean isExploding = false;
    private transient final int DEFAULT_IMAGE = 0;

    public Brick(int x, int y, int imageId) {
        super(x, y, imageId);
        imageIds = new int[1][3];
        imageIds[DEFAULT_IMAGE][0] = Sprite.BRICK_EXPLODED_0;
        imageIds[DEFAULT_IMAGE][1] = Sprite.BRICK_EXPLODED_1;
        imageIds[DEFAULT_IMAGE][2] = Sprite.BRICK_EXPLODED_2;
    }

    @Override
    public void update(float deltaTime) {
        if (isExploding) {
            timeAlive -= deltaTime;
            if (timeAlive <= 0) {
                remove();
            }
            updateAnimation(deltaTime);
        }
    }

    @Override
    protected void updateAnimation(float deltaTime) {
        if (isExploding) {
            animationTimer += deltaTime;
            if (animationTimer >= 0.33) { // Thay đổi frame mỗi 0.2 giây
                animationStep = (animationStep + 1) % 3;
                animationTimer = 0;
            }
            imageId = imageIds[DEFAULT_IMAGE][animationStep];
        }
    }

    @Override
    public void remove() {
        if (!isExploding) {
            isExploding = true;
            timeAlive = 1f;
            animationStep = 0;
            animationTimer = 0;
        } else {
            GameControl.removeEntity(this);
        }
    }
}
