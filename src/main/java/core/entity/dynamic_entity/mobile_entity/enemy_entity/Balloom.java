package core.entity.dynamic_entity.mobile_entity.enemy_entity;
import core.graphics.Sprite;


public class Balloom extends EnemyEntity {

    public Balloom(int x, int y, int imageId) {
        super(x, y, imageId);

        imageIds = new int[5][]; // UP, DOWN, LEFT, RIGHT, DEAD

        imageIds[UP_MOVING] = new int[] {
                Sprite.BALLOOM_LEFT_0, Sprite.BALLOOM_LEFT_1, Sprite.BALLOOM_LEFT_2, Sprite.BALLOOM_LEFT_3
        };
        imageIds[DOWN_MOVING] = new int[] {
                Sprite.BALLOOM_RIGHT_0, Sprite.BALLOOM_RIGHT_1, Sprite.BALLOOM_RIGHT_2, Sprite.BALLOOM_RIGHT_3
        };
        imageIds[LEFT_MOVING] = new int[] {
                Sprite.BALLOOM_LEFT_0, Sprite.BALLOOM_LEFT_1, Sprite.BALLOOM_LEFT_2, Sprite.BALLOOM_LEFT_3
        };
        imageIds[RIGHT_MOVING] = new int[] {
                Sprite.BALLOOM_RIGHT_0, Sprite.BALLOOM_RIGHT_1, Sprite.BALLOOM_RIGHT_2, Sprite.BALLOOM_RIGHT_3
        };
        imageIds[DEAD] = new int[] {
                Sprite.BALLOOM_DEAD_0, Sprite.BALLOOM_DEAD_1, Sprite.BALLOOM_DEAD_2, Sprite.BALLOOM_DEAD_3,
                Sprite.BALLOOM_DEAD_4
        };
    }

    @Override
    public void update(float deltaTime) {
        defaultMove(deltaTime);
        enemyCollision();
        enemyCollision();
        updateAnimation(deltaTime);
    }
}
