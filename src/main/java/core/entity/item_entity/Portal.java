package core.entity.item_entity;

import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.system.game.GameControl;
import core.sound.Sound;
import core.graphics.Sprite;

public class Portal extends ItemEntity {
    private boolean levelPassed = false;

    public Portal(int x, int y, int imageId) {
        super(x, y, imageId);
    }

    @Override
    public void update(float deltaTime) {
        if (levelPassed)
            return;

        for (Bomber bomber : GameControl.getBomberEntities()) {
            if (checkCollision(bomber.getX() + Sprite.DEFAULT_SIZE / 2,
                    bomber.getY() + Sprite.DEFAULT_SIZE / 2, getX() + Sprite.DEFAULT_SIZE / 2,
                    getY() + Sprite.DEFAULT_SIZE / 2) && GameControl.getEnemyEntities().isEmpty()) {
                bomber.setPermanentFreeze(true);
                this.remove();
                levelPassed = true;
                Sound.stopMusic();
                Sound.playEffect("win_game");
                GameControl.nextLevel();
                break;
            }
        }
    }

}
