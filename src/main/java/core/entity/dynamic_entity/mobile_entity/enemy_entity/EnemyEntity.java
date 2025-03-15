package core.entity.dynamic_entity.mobile_entity.enemy_entity;
import core.entity.dynamic_entity.DynamicEntity;
import core.entity.dynamic_entity.mobile_entity.MobileEntity;
import javafx.scene.image.Image;


public class EnemyEntity extends MobileEntity {
    public EnemyEntity(int x, int y, Image image) {
        super(x, y, image);
    }

    @Override
    public void update() {
    }

    @Override
    protected void updateAnimation() {
    }

    @Override
    public void remove() {
    }
   
}
