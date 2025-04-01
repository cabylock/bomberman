package core.entity.dynamic_entity.mobile_entity.enemy_entity;
import core.entity.dynamic_entity.mobile_entity.MobileEntity;
import core.util.Util;
import javafx.scene.image.Image;
import core.map_handle.MapEntity;

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
    
    protected  void defaultMove()
    {
        moveDelay--;
        if (moveDelay == 0)
        {
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

    @Override
    public void remove() {
        MapEntity.removeEnemyEntity(this);
    }
   

   
   
}
