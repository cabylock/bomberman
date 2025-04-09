package core.entity.dynamic_entity.static_entity;



public class Brick extends StaticEntity {
    public Brick(int x, int y, int imageId) {
        super(x, y, imageId);
    }

    @Override
    public void update(double deltaTime) {
        // No specific update logic for Brick
    }

    @Override
    protected void updateAnimation(double deltaTime) {
        // No animation for Brick
    }

}
