package core.entity.item_entity;

import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.system.game.GameControl;
import core.sound.Sound;

public class FlameUpItem extends ItemEntity {
    public FlameUpItem(int x, int y, int imageId) {
        super(x, y, imageId);
    }

    @Override
    public void update(float deltaTime) {
        for (Bomber bomber : GameControl.getBomberEntities()) {
            if (checkCollision(bomber.getX(), bomber.getY(), this.getX(),
                    this.getY()) && !isBrickAtPosition()) {
                Sound.playEffect(Sound.GET_ITEM);
                bomber.setFlameUp(true);
                GameControl.removeEntity(this);
                return;
            }
        }
    }

}
