package core.entity.dynamic_entity.mobile_entity.enemy_entity;
import core.system.setting.Setting;
import core.util.Util;
import core.graphics.Sprite;    


public class Minvo extends EnemyEntity {

    private static final float MOVEMENT_FREQUENCY_TIME = 0.01f;
    private static final float DIRECTION_CHANGE_TIME = 2.0f;
    private static final float SIGHT_RANGE = 10.0f;
    private static final int MIN_SPEED = 10;
    private static final int MAX_SPEED = 30;

    private float speedChangeTimer = 0;

    public Minvo(int x, int y, int imageId) {
        super(x, y, imageId);
        movementFrequencyTime = MOVEMENT_FREQUENCY_TIME;
        directionChangeTimer = DIRECTION_CHANGE_TIME;
        direction = Util.randomDirection();

        // Cấu hình pathfinding cho Minvo
        pathfindingRange = SIGHT_RANGE;
        pathUpdateFrequency = 0.5f;
        usePathfinding = true;

        // Khởi tạo ảnh chuyển động
        imageIds = new int[5][]; // UP, DOWN, LEFT, RIGHT, DEAD

        imageIds[Setting.UP_MOVING] = new int[] {
                Sprite.MINVO_LEFT_0, Sprite.MINVO_LEFT_1, Sprite.MINVO_LEFT_2, Sprite.MINVO_LEFT_3
        };
        imageIds[Setting.DOWN_MOVING] = new int[] {
                Sprite.MINVO_RIGHT_0, Sprite.MINVO_RIGHT_1, Sprite.MINVO_RIGHT_2, Sprite.MINVO_RIGHT_3
        };
        imageIds[Setting.LEFT_MOVING] = new int[] {
                Sprite.MINVO_LEFT_0, Sprite.MINVO_LEFT_1, Sprite.MINVO_LEFT_2, Sprite.MINVO_LEFT_3
        };
        imageIds[Setting.RIGHT_MOVING] = new int[] {
                Sprite.MINVO_RIGHT_0, Sprite.MINVO_RIGHT_1, Sprite.MINVO_RIGHT_2, Sprite.MINVO_RIGHT_3
        };
        imageIds[Setting.DEAD] = new int[] {
                Sprite.MINVO_DEAD_0, Sprite.MINVO_DEAD_1, Sprite.MINVO_DEAD_2, Sprite.MINVO_DEAD_3, Sprite.MINVO_DEAD_4
        };

    }
    @Override
    public void update(float deltaTime) {
        // Cập nhật tốc độ mỗi 1.5 giây, chọn giữa min và max
        speedChangeTimer += deltaTime;
        if (speedChangeTimer >= 3.0f) {
            // Random chọn min hoặc max
            if (Math.random() > 0.5) {
                speed = MIN_SPEED; // Chọn tốc độ min
            } else {
                speed = MAX_SPEED; // Chọn tốc độ max
            }
            speedChangeTimer = 0; // Reset bộ đếm thời gian
        }

        // Gọi logic di chuyển thông minh
        intelligentMove(deltaTime); // Di chuyển với tốc độ mới

        // Cập nhật animation
        updateAnimation(deltaTime);
    }

}

