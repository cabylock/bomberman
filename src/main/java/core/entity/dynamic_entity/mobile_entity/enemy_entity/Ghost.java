package core.entity.dynamic_entity.mobile_entity.enemy_entity;

import core.util.Util;

import core.graphics.Sprite;

public class Ghost extends EnemyEntity {
       
        public Ghost(int x, int y, int imageId) {
                super(x, y, imageId);

                directionChangeTimer = 2.0f;
                direction = Util.randomDirection();

                usePathfinding = true;
                brickPass = true;

                imageIds = new int[5][];

                imageIds[UP_MOVING] = new int[] {
                                Sprite.GHOST_LEFT_0, Sprite.GHOST_LEFT_1, Sprite.GHOST_LEFT_2,
                                Sprite.GHOST_LEFT_3
                };
                imageIds[DOWN_MOVING] = new int[] {
                                Sprite.GHOST_RIGHT_0, Sprite.GHOST_RIGHT_1, Sprite.GHOST_RIGHT_2,
                                Sprite.GHOST_RIGHT_3
                };
                imageIds[LEFT_MOVING] = new int[] {
                                Sprite.GHOST_LEFT_0, Sprite.GHOST_LEFT_1, Sprite.GHOST_LEFT_2,
                                Sprite.GHOST_LEFT_3
                };
                imageIds[RIGHT_MOVING] = new int[] {
                                Sprite.GHOST_RIGHT_0, Sprite.GHOST_RIGHT_1, Sprite.GHOST_RIGHT_2,
                                Sprite.GHOST_RIGHT_3
                };
                imageIds[DEAD] = new int[] {
                                Sprite.GHOST_DEAD_0, Sprite.GHOST_DEAD_1, Sprite.GHOST_DEAD_2,
                                Sprite.GHOST_DEAD_3,
                                Sprite.GHOST_DEAD_4
                };
        }

        @Override
        public void update(float deltaTime) {
                intelligentMove(deltaTime);
                updateAnimation(deltaTime);
        }
}
