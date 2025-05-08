package core.entity.dynamic_entity.mobile_entity.enemy_entity;

import core.graphics.Sprite;
import core.system.setting.Setting;


public class Doll extends EnemyEntity 
{
    private static final int SPEED = 30;
    public Doll(int x, int y, int imageId) {
        super(x, y, imageId);
        imageIds = new int[5][]; // UP, DOWN, LEFT, RIGHT, DEAD
        speed = SPEED;

        imageIds[Setting.UP_MOVING] = new int[] {
                Sprite.DOLL_LEFT_0, Sprite.DOLL_LEFT_1, Sprite.DOLL_LEFT_2, Sprite.DOLL_LEFT_3
        };
        imageIds[Setting.DOWN_MOVING] = new int[] {
                Sprite.DOLL_RIGHT_0, Sprite.DOLL_RIGHT_1, Sprite.DOLL_RIGHT_2, Sprite.DOLL_RIGHT_3
        };
        imageIds[Setting.LEFT_MOVING] = new int[] {
                Sprite.DOLL_LEFT_0, Sprite.DOLL_LEFT_1, Sprite.DOLL_LEFT_2, Sprite.DOLL_LEFT_3
        };
        imageIds[Setting.RIGHT_MOVING] = new int[] {
                Sprite.DOLL_RIGHT_0, Sprite.DOLL_RIGHT_1, Sprite.DOLL_RIGHT_2, Sprite.DOLL_RIGHT_3
        };
        imageIds[Setting.DEAD] = new int[] {
                Sprite.DOLL_DEAD_0, Sprite.DOLL_DEAD_1, Sprite.DOLL_DEAD_2, Sprite.DOLL_DEAD_3, Sprite.DOLL_DEAD_4
        };
        
    }

    @Override
    public void update(float deltaTime) {
        defaultMove(deltaTime);
        enemyCollision();
        updateAnimation(deltaTime);
    }
}