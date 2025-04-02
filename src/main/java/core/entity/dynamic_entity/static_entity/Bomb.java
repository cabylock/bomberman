package core.entity.dynamic_entity.static_entity;

import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.graphics.Sprite;
import core.map_handle.MapEntity;
import javafx.scene.image.Image;

public class Bomb extends StaticEntity {

    protected int timeAlive = 300; // 120 frames = 2 seconds

    private final int DEFAULT_IMAGE = 0;
    private int flameSize;
    private Flame[][] flameSegments;
    // private int timeToExplode = 120;
    private Bomber owner;

    // Directional constants
    protected final int[] DX = { 0, -1, 1, 0, 0 };
    protected final int[] DY = { 0, 0, 0, -1, 1 };

    public Bomb(int x, int y, Image image, int flameSize,Bomber owner) {
        super(x, y, image);
        this.flameSize = flameSize;
        this.owner = owner;
        this.flameSegments = new Flame[5][flameSize + 1];
        images = new Image[1][3];

        images[DEFAULT_IMAGE][0] = Sprite.bomb.getFxImage();
        images[DEFAULT_IMAGE][1] = Sprite.bomb_1.getFxImage();
        images[DEFAULT_IMAGE][2] = Sprite.bomb_2.getFxImage();

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
        
        owner.bombExplode();
        this.remove();
        for (int i = 0; i < 5; i++) {
            for (int j = 1; j <= flameSize; j++) {

                int flameType = i == 0 ? 0 : j == flameSize ? i + 2 : (i + 1) / 2;

                flameSegments[i][j] = new Flame(getXTile() + DX[i] * j, getYTile() + DY[i] * j,
                        Sprite.explosion_horizontal.getFxImage(), flameType);
                if (flameSegments[i][j].flamecollision()) {
                    break;
                }

                MapEntity.addStaticEntity(flameSegments[i][j]);
                ;
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

        image = images[DEFAULT_IMAGE][animationStep];

    }

}
