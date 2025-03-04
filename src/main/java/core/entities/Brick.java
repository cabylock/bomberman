package core.entities;

import javafx.scene.image.Image;
import core.graphics.Sprite;
public class Brick extends Entity {
    public Brick(int x, int y,Image image) {
        super(x, y, Sprite.brick.getFxImage());
    }

    @Override
    public void update() {
    }
   
}
