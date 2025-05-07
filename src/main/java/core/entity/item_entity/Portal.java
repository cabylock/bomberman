package core.entity.item_entity;

import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.system.game.GameControl;
import core.system.setting.Setting;
import core.sound.Sound;
import core.util.Util;
import core.system.game.BombermanGame;
import javafx.scene.layout.StackPane;

public class Portal extends ItemEntity {
    private boolean levelPassed = false;

    public Portal(int x, int y, int imageId) {
        super(x, y, imageId);
    }

    @Override
    public void update(float deltaTime) {
        if (levelPassed)
            return;

        for (Bomber bomber : GameControl.getBomberEntities()) {
            if (checkCollision(bomber.getX(), bomber.getY(), getX(), getY())
                    && GameControl.getEnemyEntities().size()!=0) {
                bomber.setPermanentFreeze(true);
                this.remove();
                levelPassed = true; // ✅ Đặt trước để tránh chạy lại nhiều lần

                Sound.stopMusic();
                Sound.playEffect("win_game");

                StackPane root = BombermanGame.getGameRoot();
                Util.showOverlayWithButton(
                        "/textures/level_complete.jpg",
                        root,
                        "Next Level",
                        () -> {
                            Setting.MAP_LEVEl++;
                            GameControl.resetGame();
                        });

                break; // Dừng sau khi xử lý
            }
        }
    }

}
