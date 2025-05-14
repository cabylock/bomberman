package core.entity.dynamic_entity.mobile_entity.enemy_entity;

import core.util.Util;
import core.graphics.Sprite;

public class Minvo extends EnemyEntity {

    private transient static final int MIN_SPEED = 10;
    private transient static final int MAX_SPEED = 30;
    private transient float speedChangeTimer = 0;

    public Minvo(int x, int y, int imageId) {
        super(x, y, imageId);
        directionChangeTimer = 2.0f;
        direction = Util.randomDirection();

        usePathfinding = true;

        imageIds = new int[5][];

        imageIds[UP_MOVING] = new int[] {
                Sprite.MINVO_LEFT_0, Sprite.MINVO_LEFT_1, Sprite.MINVO_LEFT_2,
                Sprite.MINVO_LEFT_3
        };
        imageIds[DOWN_MOVING] = new int[] {
                Sprite.MINVO_RIGHT_0, Sprite.MINVO_RIGHT_1, Sprite.MINVO_RIGHT_2,
                Sprite.MINVO_RIGHT_3
        };
        imageIds[LEFT_MOVING] = new int[] {
                Sprite.MINVO_LEFT_0, Sprite.MINVO_LEFT_1, Sprite.MINVO_LEFT_2,
                Sprite.MINVO_LEFT_3
        };
        imageIds[RIGHT_MOVING] = new int[] {
                Sprite.MINVO_RIGHT_0, Sprite.MINVO_RIGHT_1, Sprite.MINVO_RIGHT_2,
                Sprite.MINVO_RIGHT_3
        };
        imageIds[DEAD] = new int[] {
                Sprite.MINVO_DEAD_0, Sprite.MINVO_DEAD_1, Sprite.MINVO_DEAD_2, Sprite.MINVO_DEAD_3,
                Sprite.MINVO_DEAD_4
        };

    }

    @Override
    public void update(float deltaTime) {
        speedChangeTimer += deltaTime;
        if (speedChangeTimer >= 3.0f) {
            if (Math.random() > 0.5) {
                speed = MIN_SPEED;
            } else {
                speed = MAX_SPEED;
            }
            speedChangeTimer = 0;
        }

        intelligentMove(deltaTime);

        updateAnimation(deltaTime);
    }

}
