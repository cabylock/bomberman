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

   private transient int speed = 25;
   private transient int flameSize = 1;
   private transient int typePlayer;
   private String playerName; // Store player's name

   private int initialX;
   private int initialY;

   protected transient int bombCountMax = 1;

   protected transient static final int ITEM_DURATION = 5; // 10 giây * 60 frames/giây

   // public Bomber(int x, int y, int imageId, int typePlayer) {
   // this(x, y, imageId, typePlayer, "Player " + (typePlayer == Setting.BOMBER2 ?
   // "2" : "1"));
   // }

   public Bomber(int x, int y, int imageId, int typePlayer, String playerName) {
      super(x, y, imageId);
      this.initialX = x*Sprite.DEFAULT_SIZE;
      this.initialY = y*Sprite.DEFAULT_SIZE;
      this.playerName = playerName;
      this.typePlayer = typePlayer;
      if (typePlayer == Setting.BOMBER2) {
         id++;
      }
      imageIds = new int[6][8];
      if (typePlayer == Setting.BOMBER1) {
         imageIds[Setting.RIGHT_MOVING][0] = Sprite.PLAYER1_RIGHT_0;
         imageIds[Setting.RIGHT_MOVING][1] = Sprite.PLAYER1_RIGHT_1;
         imageIds[Setting.RIGHT_MOVING][2] = Sprite.PLAYER1_RIGHT_2;
         imageIds[Setting.LEFT_MOVING][0] = Sprite.PLAYER1_LEFT_0;
         imageIds[Setting.LEFT_MOVING][1] = Sprite.PLAYER1_LEFT_1;
         imageIds[Setting.LEFT_MOVING][2] = Sprite.PLAYER1_LEFT_2;
         imageIds[Setting.UP_MOVING][0] = Sprite.PLAYER1_UP_0;
         imageIds[Setting.UP_MOVING][1] = Sprite.PLAYER1_UP_1;
         imageIds[Setting.UP_MOVING][2] = Sprite.PLAYER1_UP_2;
         imageIds[Setting.DOWN_MOVING][0] = Sprite.PLAYER1_DOWN_0;
         imageIds[Setting.DOWN_MOVING][1] = Sprite.PLAYER1_DOWN_1;
         imageIds[Setting.DOWN_MOVING][2] = Sprite.PLAYER1_DOWN_2;
         imageIds[Setting.DEAD][0] = Sprite.PLAYER1_DEAD_0;
         imageIds[Setting.DEAD][1] = Sprite.PLAYER1_DEAD_1;
         imageIds[Setting.DEAD][2] = Sprite.PLAYER1_DEAD_2;
         imageIds[Setting.DEAD][3] = Sprite.PLAYER1_DEAD_3;
         imageIds[Setting.DEAD][4] = Sprite.PLAYER1_DEAD_4;
         imageIds[Setting.DEAD][5] = Sprite.PLAYER1_DEAD_5;
         imageIds[Setting.DEAD][6] = Sprite.PLAYER1_DEAD_6;
         imageIds[Setting.DEAD][7] = Sprite.PLAYER1_DEAD_7;
      } else {
         imageIds[Setting.RIGHT_MOVING][0] = Sprite.PLAYER2_RIGHT_0;
         imageIds[Setting.RIGHT_MOVING][1] = Sprite.PLAYER2_RIGHT_1;
         imageIds[Setting.RIGHT_MOVING][2] = Sprite.PLAYER2_RIGHT_2;
         imageIds[Setting.LEFT_MOVING][0] = Sprite.PLAYER2_LEFT_0;
         imageIds[Setting.LEFT_MOVING][1] = Sprite.PLAYER2_LEFT_1;
         imageIds[Setting.LEFT_MOVING][2] = Sprite.PLAYER2_LEFT_2;
         imageIds[Setting.UP_MOVING][0] = Sprite.PLAYER2_UP_0;
         imageIds[Setting.UP_MOVING][1] = Sprite.PLAYER2_UP_1;
         imageIds[Setting.UP_MOVING][2] = Sprite.PLAYER2_UP_2;
         imageIds[Setting.DOWN_MOVING][0] = Sprite.PLAYER2_DOWN_0;
         imageIds[Setting.DOWN_MOVING][1] = Sprite.PLAYER2_DOWN_1;
         imageIds[Setting.DOWN_MOVING][2] = Sprite.PLAYER2_DOWN_2;
         imageIds[Setting.DEAD][0] = Sprite.PLAYER2_DEAD_0;
         imageIds[Setting.DEAD][1] = Sprite.PLAYER2_DEAD_1;
         imageIds[Setting.DEAD][2] = Sprite.PLAYER2_DEAD_2;
         imageIds[Setting.DEAD][3] = Sprite.PLAYER2_DEAD_3;
         imageIds[Setting.DEAD][4] = Sprite.PLAYER2_DEAD_4;
         imageIds[Setting.DEAD][5] = Sprite.PLAYER2_DEAD_5;
         imageIds[Setting.DEAD][6] = Sprite.PLAYER2_DEAD_6;
         imageIds[Setting.DEAD][7] = Sprite.PLAYER2_DEAD_7;

      }
      imageIds[Setting.ANIMATION_NULL][0] = Sprite.ANIMATION_NULL;
      imageIds[Setting.ANIMATION_NULL][1] = Sprite.ANIMATION_NULL;
      imageIds[Setting.ANIMATION_NULL][2] = Sprite.ANIMATION_NULL;
   }

   @Override
   public void update(float deltaTime) {
      updateAnimation(deltaTime);
      updateItem(deltaTime);
      updateInvincible(deltaTime);
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
         float textX = x + Sprite.DEFAULT_SIZE / 2;
         float textY = y - 10; // 10 pixels above player

         // Draw text with outline for better visibility
         gc.strokeText(playerName, textX, textY);
         gc.fillText(playerName, textX, textY);

         // Restore graphics context
         gc.restore();
      }
   }

   public void control(String command, float deltaTime) {
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

      BombermanGame.input.remove(Setting.BOMBER_KEY_CONTROLS[typePlayer][Setting.BOMB_PLACE]);
      if (bombCountMax == 0) {

         return;
      }

      int bombX = this.getXTile();
      int bombY = this.getYTile();

      Bomb newBomb = new Bomb(bombX, bombY, Sprite.BOMB_0, flameSize, id);
      GameControl.addEntity(newBomb);
      bombCountMax--;

   }

   public void updateItem(float deltaTime) {
      if (flamePassTime > 0) {
         flamePassTime -= deltaTime;
         if (flamePassTime <= 0) {
            flamePass = false;
         }
      }
      if (bombPassTime > 0) {
         bombPassTime -= deltaTime;
         if (bombPassTime <= 0) {
            bombPass = false;
         }
      }
      if (speedUpTime > 0) {
         speedUpTime -= deltaTime;
         if (speedUpTime <= 0) {
            speed = 25;
            speedUp = false;
         }
      }
      if (flameUpTime > 0) {
         flameUpTime -= deltaTime;
         if (flameUpTime <= 0) {
            flameSize = 1;
            flameUp = false;
         }
      }
      if (bombUpTime > 0) {
         bombUpTime -= deltaTime;
         if (bombUpTime <= 0) {
            bombCountMax = 1;
            bombUp = false;
         }
      }
      if (wallPassTime > 0) {
         wallPassTime -= deltaTime;
         if (wallPassTime <= 0) {
            wallPass = false;
         }
      }

   }

   public void setFlamePass(boolean flamePass) {
      this.flamePass = flamePass;
      if (flamePass) {
         flamePassTime = ITEM_DURATION;
      }
   }

   public void setBombPass(boolean bombPass) {
      this.bombPass = bombPass;
      if (bombPass) {
         bombPassTime = ITEM_DURATION;
      }
   }

   public void setSpeedUp(boolean speedUp) {
      this.speedUp = speedUp;
      if (speedUp) {
         speed = 30;
      }
      speedUpTime = ITEM_DURATION;
   }

   public void setFlameUp(boolean flameUp) {
      this.flameUp = flameUp;
      if (flameUp) {
         flameSize = 2;
         flameUpTime = ITEM_DURATION;
      }
   }

   public void setBombUp(boolean bombUp) {
      this.bombUp = bombUp;
      if (bombUp) {
         bombCountMax = 2;
         bombUpTime = ITEM_DURATION;
      }
   }

   public void setWallPass(boolean wallPass) {
      this.wallPass = wallPass;
      if (wallPass) {
         wallPassTime = ITEM_DURATION;
      }
   }

   public void bombExplode() {
      if (bombCountMax < 2) {
         bombCountMax++;
      }
   }

   public void increaseHealth() {
      health++;
   }

   public void setName(String name) {
      this.playerName = name;
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
