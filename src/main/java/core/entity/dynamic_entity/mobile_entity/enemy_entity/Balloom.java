package core.entity.dynamic_entity.mobile_entity.enemy_entity;

import core.graphics.Sprite;
import core.system.setting.Setting;

public class Balloom extends EnemyEntity {

    private transient int speed = 20;

    public Balloom(int x, int y, int imageId) {
        super(x, y, imageId);

        imageIds = new int[4][3];
        imageIds[Setting.UP_MOVING][0] = Sprite.BALLOOM_LEFT1;
        imageIds[Setting.UP_MOVING][1] = Sprite.BALLOOM_LEFT2;
        imageIds[Setting.UP_MOVING][2] = Sprite.BALLOOM_LEFT3;

        imageIds[Setting.DOWN_MOVING][0] = Sprite.BALLOOM_RIGHT1;
        imageIds[Setting.DOWN_MOVING][1] = Sprite.BALLOOM_RIGHT2;
        imageIds[Setting.DOWN_MOVING][2] = Sprite.BALLOOM_RIGHT3;

        imageIds[Setting.LEFT_MOVING][0] = Sprite.BALLOOM_LEFT1;
        imageIds[Setting.LEFT_MOVING][1] = Sprite.BALLOOM_LEFT2;
        imageIds[Setting.LEFT_MOVING][2] = Sprite.BALLOOM_LEFT3;

        imageIds[Setting.RIGHT_MOVING][0] = Sprite.BALLOOM_RIGHT1;
        imageIds[Setting.RIGHT_MOVING][1] = Sprite.BALLOOM_RIGHT2;
        imageIds[Setting.RIGHT_MOVING][2] = Sprite.BALLOOM_RIGHT3;
    }

    @Override
    public void update(float deltaTime) {
        defaultMove(deltaTime);
        EnemyCollision();
        updateAnimation(deltaTime);
    }
}
