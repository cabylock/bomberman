package core.entity.dynamic_entity.mobile_entity.enemy_entity;

import core.system.game.GameControl;
import core.system.setting.Setting;
import core.util.Util;
import core.entity.dynamic_entity.static_entity.StaticEntity;
import core.entity.dynamic_entity.static_entity.Bomb;
import core.entity.Entity;
import core.entity.background_entity.Grass;
import core.graphics.Sprite;

import core.entity.background_entity.BackgroundEntity;
import core.entity.background_entity.Wall;




public class Ghost extends EnemyEntity {
    private static final int SPEED = 25;
    
    private static final float MOVEMENT_FREQUENCY_TIME = 0.01f;
    private static final float DIRECTION_CHANGE_TIME = 2.0f;
    private static final float SIGHT_RANGE = 5.0f;

    public Ghost(int x, int y, int imageId) {
        super(x, y, imageId);
        speed = SPEED;
  

        movementFrequencyTime = MOVEMENT_FREQUENCY_TIME;
        directionChangeTimer = DIRECTION_CHANGE_TIME;
        direction = Util.randomDirection();

        // Configure pathfinding for Oneal
        pathfindingRange = SIGHT_RANGE;
        pathUpdateFrequency = 0.5f; // Update path more frequently
        usePathfinding = true;

        // Khởi tạo imageIds
        imageIds = new int[5][]; // UP, DOWN, LEFT, RIGHT, DEAD

        imageIds[Setting.UP_MOVING] = new int[] {
                Sprite.GHOST_LEFT_0, Sprite.GHOST_LEFT_1, Sprite.GHOST_LEFT_2, Sprite.GHOST_LEFT_3
        };
        imageIds[Setting.DOWN_MOVING] = new int[] {
                Sprite.GHOST_RIGHT_0, Sprite.GHOST_RIGHT_1, Sprite.GHOST_RIGHT_2, Sprite.GHOST_RIGHT_3
        };
        imageIds[Setting.LEFT_MOVING] = new int[] {
                Sprite.GHOST_LEFT_0, Sprite.GHOST_LEFT_1, Sprite.GHOST_LEFT_2, Sprite.GHOST_LEFT_3
        };
        imageIds[Setting.RIGHT_MOVING] = new int[] {
                Sprite.GHOST_RIGHT_0, Sprite.GHOST_RIGHT_1, Sprite.GHOST_RIGHT_2, Sprite.GHOST_RIGHT_3
        };
        imageIds[Setting.DEAD] = new int[] {
                Sprite.GHOST_DEAD_0, Sprite.GHOST_DEAD_1, Sprite.GHOST_DEAD_2, Sprite.GHOST_DEAD_3, Sprite.GHOST_DEAD_4
        };
    }
    
    @Override
    protected boolean isWalkable(int x, int y) {
        if (x < 0 || y < 0 || x >= GameControl.getWidth() || y >= GameControl.getHeight()) {
            return false;
        }

        for (StaticEntity entity : GameControl.getStaticEntities()) {
            if (entity instanceof Bomb && !bombPass && entity.getXTile() == x && entity.getYTile() == y) {
                return false;
            }
            // BỎ QUA Brick hoàn toàn
        }

        for (BackgroundEntity entity : GameControl.getBackgroundEntities()) {
            if (entity instanceof Wall && entity.getXTile() == x && entity.getYTile() == y) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected boolean moveCollision(float nextX, float nextY) {
        for (StaticEntity entity : GameControl.getStaticEntities()) {
            if (entity instanceof Bomb) {
                if (checkCollision(this.x, this.y, entity.getX(), entity.getY())) {
                    continue;
                }
                if (checkCollision(nextX, nextY, entity.getX(), entity.getY())) {
                    if (!this.bombPass)
                        return true;
                }
            }

            // BỎ QUA Brick hoàn toàn
        }

        for (Entity bg : GameControl.getBackgroundEntities()) {
            // Wall vẫn chặn bình thường
            if (!(bg instanceof Grass)) {
                if (checkCollision(nextX, nextY, bg.getX(), bg.getY())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void update(float deltaTime) {
        // Use intelligent movement with pathfinding
        intelligentMove(deltaTime);

        // Update animation
        updateAnimation(deltaTime);
    }
}
