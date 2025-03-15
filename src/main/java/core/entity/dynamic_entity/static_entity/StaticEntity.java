package core.entity.dynamic_entity.static_entity;

import core.entity.dynamic_entity.DynamicEntity;
import javafx.scene.image.Image;

public class StaticEntity extends DynamicEntity {
    public StaticEntity(int x, int y,Image image) {
        super(x, y,image);
    }

    @Override
    public void update() {
    }

    @Override
    protected void updateAnimation() {
    }
   
}
