package core.entity.dynamic_entity.mobile_entity.enemy_entity;

import javafx.scene.image.Image;
import core.entity.Entity;
import core.entity.dynamic_entity.DynamicEntity;

public class Balloom extends EnemyEntity {
    public Balloom(int x, int y, Image image) {
        super(x, y, image);
    }

    @Override
    public void update() {
    }

    @Override
    protected void updateAnimation() {
    }

}
