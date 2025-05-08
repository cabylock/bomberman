package core.entity.dynamic_entity.mobile_entity.enemy_entity;

import core.util.Util;

import core.graphics.Sprite;

public class Ghost extends EnemyEntity {

        private static final float DIRECTION_CHANGE_TIME = 2.0f;
        private static final float SIGHT_RANGE = 5.0f;

        public Ghost(int x, int y, int imageId) {
                super(x, y, imageId);

                directionChangeTimer = DIRECTION_CHANGE_TIME;
                direction = Util.randomDirection();

                // Configure pathfinding for Oneal
                pathfindingRange = SIGHT_RANGE;
                pathUpdateFrequency = 0.5f; // Update path more frequently
                usePathfinding = true;
                brickPass = true;

                // Khởi tạo imageIds
                imageIds = new int[5][]; // UP, DOWN, LEFT, RIGHT, DEAD

                imageIds[UP_MOVING] = new int[] {
                                Sprite.GHOST_LEFT_0, Sprite.GHOST_LEFT_1, Sprite.GHOST_LEFT_2, Sprite.GHOST_LEFT_3
                };
                imageIds[DOWN_MOVING] = new int[] {
                                Sprite.GHOST_RIGHT_0, Sprite.GHOST_RIGHT_1, Sprite.GHOST_RIGHT_2, Sprite.GHOST_RIGHT_3
                };
                imageIds[LEFT_MOVING] = new int[] {
                                Sprite.GHOST_LEFT_0, Sprite.GHOST_LEFT_1, Sprite.GHOST_LEFT_2, Sprite.GHOST_LEFT_3
                };
                imageIds[RIGHT_MOVING] = new int[] {
                                Sprite.GHOST_RIGHT_0, Sprite.GHOST_RIGHT_1, Sprite.GHOST_RIGHT_2, Sprite.GHOST_RIGHT_3
                };
                imageIds[DEAD] = new int[] {
                                Sprite.GHOST_DEAD_0, Sprite.GHOST_DEAD_1, Sprite.GHOST_DEAD_2, Sprite.GHOST_DEAD_3,
                                Sprite.GHOST_DEAD_4
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
