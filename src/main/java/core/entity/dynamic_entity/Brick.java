package core.entity.dynamic_entity;
import javafx.scene.image.Image;
import core.graphics.Sprite;


public class Brick extends DynamicEntity {
    public Brick(int x, int y,Image image) {
        super(x, y, Sprite.brick.getFxImage());
    }

    @Override
    public void update() {
    }
   
}
