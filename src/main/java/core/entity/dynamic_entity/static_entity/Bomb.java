package core.entity.dynamic_entity.static_entity;

import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.graphics.Sprite;
import core.system.game.GameControl;
import core.system.setting.Setting;

public class Bomb extends StaticEntity {

    private transient float timeAlive = 3.0f; // 3 seconds before explosion
    private transient boolean hasExploded = false; // Thêm biến kiểm soát trạng thái nổ

   
    private transient int flameSize;
    private transient Flame[][] flameSegments;
    private transient int ownerId;

    // Directional constants
    protected final int[] DX = { 0, -1, 1, 0, 0 };
    protected final int[] DY = { 0, 0, 0, -1, 1 };

    public Bomb(int x, int y, int imageId, int flameSize, int ownerId) {
        super(x, y, imageId);
        this.flameSize = flameSize;
        this.ownerId = ownerId;
        this.flameSegments = new Flame[5][flameSize + 1];
        imageIds = new int[1][3];

        // Sử dụng sprite bomb phù hợp cho player 1 hoặc player 2
        if (ownerId == Setting.ID) {
            imageIds[DEFAULT_IMAGE][0] = Sprite.PLAYER1_BOMB_0;
            imageIds[DEFAULT_IMAGE][1] = Sprite.PLAYER1_BOMB_1;
            imageIds[DEFAULT_IMAGE][2] = Sprite.PLAYER1_BOMB_2;
        } else {
            imageIds[DEFAULT_IMAGE][0] = Sprite.PLAYER2_BOMB_0;
            imageIds[DEFAULT_IMAGE][1] = Sprite.PLAYER2_BOMB_1;
            imageIds[DEFAULT_IMAGE][2] = Sprite.PLAYER2_BOMB_2;
        }
    }

    @Override
    public void update(float deltaTime) {
        timeAlive -= deltaTime;
        if (timeAlive <= 0) {
            explode();
        }
        updateAnimation(deltaTime);
    }

    private void createFlames() {
        for (Bomber bomber : GameControl.getBomberEntities()) {
            if (bomber.getId() == ownerId) {
                bomber.bombExplode();
            }
        }

        for (int i = 0; i < 5; i++) {
            for (int j = 1; j <= flameSize; j++) {
                int flameType = i == 0 ? 0 : j == flameSize ? i + 2 : (i + 1) / 2;

                flameSegments[i][j] = new Flame(getXTile() + DX[i] * j, getYTile() + DY[i] * j,
                        Sprite.EXPLOSION_HORIZONTAL_0, flameType);
                if (flameSegments[i][j].flamecollision()) {
                    break;
                }

                GameControl.addEntity(flameSegments[i][j]);
            }
        }
    }

    public void explode() {
        if (hasExploded) {
            return;
        }

        hasExploded = true;

        // Phát âm thanh nổ bom

        // Tạo các ngọn lửa
        createFlames();

        // Xóa bom khỏi game
        GameControl.removeEntity(this);
    }

    

}
