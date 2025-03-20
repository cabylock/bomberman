package core.entity.dynamic_entity.static_entity;

import javafx.scene.image.Image;
import core.graphics.Sprite;

public class Brick extends StaticEntity {
    public Brick(int x, int y, Image image) {
        super(x, y, Sprite.brick.getFxImage());
    }

    @Override
    public void update() {
    }

    @Override
    protected void updateAnimation() {
    }

}
