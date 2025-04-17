package core.entity.dynamic_entity.mobile_entity.enemy_entity;

import core.entity.dynamic_entity.mobile_entity.MobileEntity;
import core.system.game.GameControl;
import core.util.Util;
import core.entity.dynamic_entity.mobile_entity.Bomber;

public class EnemyEntity extends MobileEntity {

    protected transient int speed = 10; // Increased default speed
    protected transient float moveTimer = 0;
    protected transient float directionChangeTimer = 0;
    protected transient float movementFrequencyTime = 0.01f;

    public EnemyEntity(int x, int y, int imageId) {
        super(x, y, imageId);

        // Initialize with random direction
        direction = Util.randomDirection();
    }

    @Override
    public void update(float deltaTime) {
        defaultMove(deltaTime);
        updateAnimation(deltaTime);
    }

    protected void defaultMove(float deltaTime) {
        // Update movement timer
        moveTimer += deltaTime;
        directionChangeTimer += deltaTime;

        // Time to change direction randomly
        if (directionChangeTimer >= 2.0) { // Change direction every 2 seconds
            direction = Util.randomDirection();
            directionChangeTimer = 0;
        }

        // Move at consistent intervals
        if (moveTimer >= movementFrequencyTime) {
            moveTimer = 0;

            // Try to move in the current direction
            if (!move(direction, speed, deltaTime)) {
                // If movement is blocked, pick a new direction immediately
                direction = Util.randomDirection();
            }
        }
    }

    protected boolean EnemyCollision() {
        for (Bomber bomber : GameControl.getBomberEntities()) {
            if (bomber.getXTile() == this.getXTile() && bomber.getYTile() == this.getYTile()) {
                if (!bomber.isInvincible()) {
                    bomber.decreaseHealth();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void remove() {
        GameControl.removeEntity(this);
    }
}
