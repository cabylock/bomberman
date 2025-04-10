package core.entity.dynamic_entity.mobile_entity;

import core.graphics.Sprite;
import core.system.game.BombermanGame;
import core.system.game.GameControl;
import core.system.setting.Setting;
import core.entity.dynamic_entity.static_entity.Bomb;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class Bomber extends MobileEntity {

   private int speed = 25;
   private int flameSize = 1;
   private int typePlayer;
   private String playerName; // Store player's name

   private int initialX;
   private int initialY;

   protected int bombCountMax = 1;

   protected boolean flamePass = false;

   // public Bomber(int x, int y, int imageId, int typePlayer) {
   //    this(x, y, imageId, typePlayer, "Player " + (typePlayer == Setting.BOMBER2 ? "2" : "1"));
   // }

   public Bomber(int x, int y, int imageId, int typePlayer, String playerName) {
      super(x, y, imageId);
      this.initialX = x;
      this.initialY = y;
      this.playerName = playerName;
      // Image arrays for animation
      this.typePlayer = typePlayer;
      if (typePlayer == Setting.BOMBER2) {
         id++;
      }
      imageIds = new int[6][3];
      imageIds[Setting.RIGHT_MOVING][0] = Sprite.PLAYER_RIGHT;
      imageIds[Setting.RIGHT_MOVING][1] = Sprite.PLAYER_RIGHT_1;
      imageIds[Setting.RIGHT_MOVING][2] = Sprite.PLAYER_RIGHT_2;
      imageIds[Setting.LEFT_MOVING][0] = Sprite.PLAYER_LEFT;
      imageIds[Setting.LEFT_MOVING][1] = Sprite.PLAYER_LEFT_1;
      imageIds[Setting.LEFT_MOVING][2] = Sprite.PLAYER_LEFT_2;
      imageIds[Setting.UP_MOVING][0] = Sprite.PLAYER_UP;
      imageIds[Setting.UP_MOVING][1] = Sprite.PLAYER_UP_1;
      imageIds[Setting.UP_MOVING][2] = Sprite.PLAYER_UP_2;
      imageIds[Setting.DOWN_MOVING][0] = Sprite.PLAYER_DOWN;
      imageIds[Setting.DOWN_MOVING][1] = Sprite.PLAYER_DOWN_1;
      imageIds[Setting.DOWN_MOVING][2] = Sprite.PLAYER_DOWN_2;
      imageIds[Setting.DEAD][0] = Sprite.PLAYER_DEAD1;
      imageIds[Setting.DEAD][1] = Sprite.PLAYER_DEAD2;
      imageIds[Setting.DEAD][2] = Sprite.PLAYER_DEAD3;
      imageIds[Setting.ANIMATION_NULL][0] = Sprite.PLAYER_RIGHT;
      imageIds[Setting.ANIMATION_NULL][1] = Sprite.ANIMATION_NULL;
      imageIds[Setting.ANIMATION_NULL][2] = Sprite.ANIMATION_NULL;
   }

   @Override
   public void update(double deltaTime) {
      updateAnimation(deltaTime);
   }

   @Override
   public void render(GraphicsContext gc) {
      super.render(gc);
      // Render player name above character
      if (playerName != null && !playerName.isEmpty()) {
         // Save current graphics context state
         gc.save();

         // Set text properties
         gc.setFill(Color.WHITE);
         gc.setStroke(Color.BLACK);
         gc.setLineWidth(1.5);
         gc.setFont(new Font("Varela Round", 14));
         gc.setTextAlign(TextAlignment.CENTER);

         // Position is centered above player
         double textX = x + Sprite.SCALED_SIZE / 2;
         double textY = y - 10; // 10 pixels above player

         // Draw text with outline for better visibility
         gc.strokeText(playerName, textX, textY);
         gc.fillText(playerName, textX, textY);

         // Restore graphics context
         gc.restore();
      }
   }

   public void control(String command, double deltaTime) {
      if (command.equals(Setting.MOVE_DOWN)) {
         move(Setting.DOWN_MOVING, speed, deltaTime);
      } else if (command.equals(Setting.MOVE_UP)) {
         move(Setting.UP_MOVING, speed, deltaTime);
      } else if (command.equals(Setting.MOVE_LEFT)) {
         move(Setting.LEFT_MOVING, speed, deltaTime);
      } else if (command.equals(Setting.MOVE_RIGHT)) {
         move(Setting.RIGHT_MOVING, speed, deltaTime);
      } else if (command.equals(Setting.PLACE_BOMB)) {
         placeBomb();
      } else {
         moving = false;
      }
   }

   private void placeBomb() {

      BombermanGame.input.remove(Setting.BOMBER_KEY_CONTROLS[typePlayer][4]);
      if (bombCountMax == 0) {

         return;
      }

      int bombX = this.getXTile();
      int bombY = this.getYTile();

      Bomb newBomb = new Bomb(bombX, bombY, Sprite.BOMB, flameSize, id);
      GameControl.addEntity(newBomb);
      bombCountMax--;

   }

   public void setFlamePass(boolean flamePass) {
      this.flamePass = flamePass;
   }

   public void increaseSpeed() {
      speed++;
   }

   public void increaseFlameSize() {
      flameSize++;
   }

   public void increaseBomb() {
      bombCountMax++;
   }

   public void bombExplode() {
      bombCountMax++;
   }

   public void increaseHealth() {
      health++;
   }

   public void setBombPass(boolean bombpass) {
      this.bombpass = bombpass;

   }

   public void resetBomber() {
      x = initialX;
      y = initialY;
      bombCountMax = 1;
      speed = 25;
      flameSize = 1;
      isInvincible = false;
      invincibleTime = 0;

   }

   public boolean isFlamePass() {
      return flamePass;
   }

   public void updateInvincible() {

      if (isInvincible) {
         invincibleTime--;
         if (invincibleTime <= 0) {
            isInvincible = false;
         }
      }
   }

   @Override
   public void remove() {
      GameControl.removeEntity(this);
   }

   public String getPlayerName() {
      return playerName;
   }

   public void setPlayerName(String playerName) {
      this.playerName = playerName;
   }

}
