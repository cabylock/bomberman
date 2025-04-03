package core.entity.dynamic_entity.mobile_entity.enemy_entity;

import core.entity.dynamic_entity.mobile_entity.MobileEntity;
import core.system.game.GameControl;
import core.util.Util;
import javafx.scene.image.Image;
import core.entity.dynamic_entity.mobile_entity.Bomber;

public class EnemyEntity extends MobileEntity {

    protected int constDirection;
    protected int speed = 1;
    protected int moveDelay = 5;

    public EnemyEntity(int x, int y, Image image) {
        super(x, y, image);
        constDirection = Util.randomRange(200, 500);

    }

    @Override
    public void update() {

    }

    protected void defaultMove() {
        moveDelay--;
        if (moveDelay == 0) {
            moveDelay = 5;
            if (constDirection == 0) {
                direction = Util.randomDirection();
                constDirection = Util.randomRange(200, 500);
            } else {
                constDirection--;
                if (!move(direction, speed)) {
                    direction = Util.randomDirection();
                }
            }
        }

    }

    protected boolean EnemyCollision() {
        for (Bomber bomber : GameControl.getBomberEntities()) {
            if (bomber.getXTile() == this.getXTile() && bomber.getYTile() == this.getYTile()) {
                bomber.dead();
                return true;
            }
        }
        return false;
    }

    @Override
    public void remove() {
        GameControl.removeEntity(this);
    }

}
