package core.entity.dynamic_entity.mobile_entity.enemy_entity;
import core.util.Util;
import core.graphics.Sprite;

public class Oneal extends EnemyEntity {
    private static final int SPEED = 15;
    private static final float MOVEMENT_FREQUENCY_TIME = 0.01f;
    private static final float DIRECTION_CHANGE_TIME = 2.0f;
    private static final float SIGHT_RANGE = 5.0f;

    public Oneal(int x, int y, int imageId) {
        super(x, y, imageId);
        speed = SPEED;
        movementFrequencyTime = MOVEMENT_FREQUENCY_TIME;
        directionChangeTimer = DIRECTION_CHANGE_TIME;
        direction = Util.randomDirection();
        
        // Configure pathfinding for Oneal
        pathfindingRange = SIGHT_RANGE;
        pathUpdateFrequency = 1.0f; // Update path more frequently
        usePathfinding = true;

        // Khởi tạo imageIds
        imageIds = new int[5][];

        // UP, DOWN, LEFT, RIGHT each with 4 frames
        imageIds[UP_MOVING] = new int[] {
                Sprite.ONEAL_LEFT_0, Sprite.ONEAL_LEFT_1, Sprite.ONEAL_LEFT_2, Sprite.ONEAL_LEFT_3
        };
        imageIds[DOWN_MOVING] = new int[] {
                Sprite.ONEAL_RIGHT_0, Sprite.ONEAL_RIGHT_1, Sprite.ONEAL_RIGHT_2, Sprite.ONEAL_RIGHT_3
        };
        imageIds[LEFT_MOVING] = new int[] {
                Sprite.ONEAL_LEFT_0, Sprite.ONEAL_LEFT_1, Sprite.ONEAL_LEFT_2, Sprite.ONEAL_LEFT_3
        };
        imageIds[RIGHT_MOVING] = new int[] {
                Sprite.ONEAL_RIGHT_0, Sprite.ONEAL_RIGHT_1, Sprite.ONEAL_RIGHT_2, Sprite.ONEAL_RIGHT_3
        };

        // DEAD with 5 frames
        imageIds[DEAD] = new int[] {
                Sprite.ONEAL_DEAD_0, Sprite.ONEAL_DEAD_1, Sprite.ONEAL_DEAD_2, Sprite.ONEAL_DEAD_3, Sprite.ONEAL_DEAD_4
        };

    }

    @Override
    public void update(float deltaTime) {
        // Use intelligent movement with pathfinding
        intelligentMove(deltaTime);
        
        // Update animation
        updateAnimation(deltaTime);
    }
}
