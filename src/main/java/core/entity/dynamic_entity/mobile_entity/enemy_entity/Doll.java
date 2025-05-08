package core.entity.dynamic_entity.mobile_entity.enemy_entity;

import core.graphics.Sprite;


public class Doll extends EnemyEntity 
{
    
    public Doll(int x, int y, int imageId) {
        super(x, y, imageId);
        imageIds = new int[5][]; // UP, DOWN, LEFT, RIGHT, DEAD
        speed = 30;

        imageIds[UP_MOVING] = new int[] {
                Sprite.DOLL_LEFT_0, Sprite.DOLL_LEFT_1, Sprite.DOLL_LEFT_2, Sprite.DOLL_LEFT_3
        };
        imageIds[DOWN_MOVING] = new int[] {
                Sprite.DOLL_RIGHT_0, Sprite.DOLL_RIGHT_1, Sprite.DOLL_RIGHT_2, Sprite.DOLL_RIGHT_3
        };
        imageIds[LEFT_MOVING] = new int[] {
                Sprite.DOLL_LEFT_0, Sprite.DOLL_LEFT_1, Sprite.DOLL_LEFT_2, Sprite.DOLL_LEFT_3
        };
        imageIds[RIGHT_MOVING] = new int[] {
                Sprite.DOLL_RIGHT_0, Sprite.DOLL_RIGHT_1, Sprite.DOLL_RIGHT_2, Sprite.DOLL_RIGHT_3
        };
        imageIds[DEAD] = new int[] {
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