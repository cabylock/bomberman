package core.entity.dynamic_entity.static_entity;

import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.graphics.Sprite;
import core.system.game.GameControl;


public class Bomb extends StaticEntity {

    protected int timeAlive = 300; // 120 frames = 2 seconds

    private final int DEFAULT_IMAGE = 0;
    private int flameSize;
    private Flame[][] flameSegments;
    // private int timeToExplode = 120;
    private int  ownerId;

    // Directional constants
    protected final int[] DX = { 0, -1, 1, 0, 0 };
    protected final int[] DY = { 0, 0, 0, -1, 1 };

    public Bomb(int x, int y, int imageId, int flameSize, int ownerId) {
        super(x, y, imageId);
        this.flameSize = flameSize;
        this.ownerId = ownerId;
        this.flameSegments = new Flame[5][flameSize + 1];
        imageIds = new int[1][3];

        imageIds[DEFAULT_IMAGE][0] = Sprite.BOMB;
        imageIds[DEFAULT_IMAGE][1] = Sprite.BOMB_1;
        imageIds[DEFAULT_IMAGE][2] = Sprite.BOMB_2;

    }

    @Override
    public void update() {
        timeAlive--;
        if (timeAlive == 0) {
            explode();

        }

        updateAnimation();

    }

    public void explode() {

        for (Bomber bomber : GameControl.getBomberEntities()) {
            if (bomber.getId() == ownerId) {
                
                bomber.bombExplode();
            }
        }
     
        this.remove();
        for (int i = 0; i < 5; i++) {
            for (int j = 1; j <= flameSize; j++) {

                int flameType = i == 0 ? 0 : j == flameSize ? i + 2 : (i + 1) / 2;

                flameSegments[i][j] = new Flame(getXTile() + DX[i] * j, getYTile() + DY[i] * j,
                        Sprite.EXPLOSION_HORIZONTAL, flameType);
                if (flameSegments[i][j].flamecollision()) {
                    break;
                }

                GameControl.addEntity(flameSegments[i][j]);

            }
        }

    }

    @Override
    public void updateAnimation() {

        if (animationDelay == 0) {
            animationStep++;
            if (animationStep == 3) {
                animationStep = 0;
            }
            animationDelay = 20;
        } else {
            animationDelay--;
        }

        imageId = imageIds[DEFAULT_IMAGE][animationStep];

    }

}
