package core.entity.dynamic_entity.mobile_entity;

import core.graphics.Sprite;
import core.system.game.BombermanGame;
import core.system.game.GameControl;
import core.entity.dynamic_entity.static_entity.Bomb;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import core.sound.Sound;
import javafx.scene.input.KeyCode;
import java.util.Map;


public class Bomber extends MobileEntity {

   public static transient final int BOMBER1 = 0;
   public static transient final int BOMBER2 = 1;

   public static transient final String MOVE_UP = "UP";
   public static transient final String MOVE_DOWN = "DOWN";
   public static transient final String MOVE_LEFT = "LEFT";
   public static transient final String MOVE_RIGHT = "RIGHT";
   public static transient final String PLACE_BOMB = "BOMB";
   public static transient final int BOMB_PLACE = 4;

   public static transient final KeyCode[][] BOMBER_KEY_CONTROLS = {

         { KeyCode.RIGHT, KeyCode.LEFT, KeyCode.UP, KeyCode.DOWN, KeyCode.NUMPAD1 },

         { KeyCode.D, KeyCode.A, KeyCode.W, KeyCode.S, KeyCode.J }
   };

   private transient int speed = 25;
   private transient int flameSize = 1;
   private transient int typePlayer;
   private String playerName; // Store player's name
   private transient boolean permanentFreeze = false;
   

   private int initialX;
   private int initialY;

   protected transient int bombCountMax = 1;

   protected transient static final int ITEM_DURATION = 10; 
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
      if (typePlayer == BOMBER2) {
         id++;
      }
      imageIds = new int[6][]; // 0 → UP, 1 → DOWN, 2 → LEFT, 3 → RIGHT, 4 → DEAD, 5 → ANIMATION_NULL

      if (typePlayer == BOMBER1) {
         imageIds[RIGHT_MOVING] = new int[] {
               Sprite.PLAYER1_RIGHT_0, Sprite.PLAYER1_RIGHT_1, Sprite.PLAYER1_RIGHT_2
         };
         imageIds[LEFT_MOVING] = new int[] {
               Sprite.PLAYER1_LEFT_0, Sprite.PLAYER1_LEFT_1, Sprite.PLAYER1_LEFT_2
         };
         imageIds[UP_MOVING] = new int[] {
               Sprite.PLAYER1_UP_0, Sprite.PLAYER1_UP_1, Sprite.PLAYER1_UP_2
         };
         imageIds[DOWN_MOVING] = new int[] {
               Sprite.PLAYER1_DOWN_0, Sprite.PLAYER1_DOWN_1, Sprite.PLAYER1_DOWN_2
         };
         imageIds[DEAD] = new int[] {
               Sprite.PLAYER1_DEAD_0, Sprite.PLAYER1_DEAD_1, Sprite.PLAYER1_DEAD_2,
               Sprite.PLAYER1_DEAD_3, Sprite.PLAYER1_DEAD_4, Sprite.PLAYER1_DEAD_5,
               Sprite.PLAYER1_DEAD_6, Sprite.PLAYER1_DEAD_7
         };
      } else {
         imageIds[RIGHT_MOVING] = new int[] {
               Sprite.PLAYER2_RIGHT_0, Sprite.PLAYER2_RIGHT_1, Sprite.PLAYER2_RIGHT_2
         };
         imageIds[LEFT_MOVING] = new int[] {
               Sprite.PLAYER2_LEFT_0, Sprite.PLAYER2_LEFT_1, Sprite.PLAYER2_LEFT_2
         };
         imageIds[UP_MOVING] = new int[] {
               Sprite.PLAYER2_UP_0, Sprite.PLAYER2_UP_1, Sprite.PLAYER2_UP_2
         };
         imageIds[DOWN_MOVING] = new int[] {
               Sprite.PLAYER2_DOWN_0, Sprite.PLAYER2_DOWN_1, Sprite.PLAYER2_DOWN_2
         };
         imageIds[DEAD] = new int[] {
               Sprite.PLAYER2_DEAD_0, Sprite.PLAYER2_DEAD_1, Sprite.PLAYER2_DEAD_2,
               Sprite.PLAYER2_DEAD_3, Sprite.PLAYER2_DEAD_4, Sprite.PLAYER2_DEAD_5,
               Sprite.PLAYER2_DEAD_6, Sprite.PLAYER2_DEAD_7
         };
      }

      // ANIMATION_NULL (dùng để "ẩn" khi invincible)
      imageIds[ANIMATION_NULL] = new int[] {
            Sprite.ANIMATION_NULL, Sprite.ANIMATION_NULL, Sprite.ANIMATION_NULL
      };
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
      if (permanentFreeze){
         moving = false;
         return;
      }
      if (command.equals(MOVE_DOWN)) {
         move(DOWN_MOVING, speed, deltaTime);
      } else if (command.equals(MOVE_UP)) {
         move(UP_MOVING, speed, deltaTime);
      } else if (command.equals(MOVE_LEFT)) {
         move(LEFT_MOVING, speed, deltaTime);
      } else if (command.equals(MOVE_RIGHT)) {
         move(RIGHT_MOVING, speed, deltaTime);
      } else if (command.equals(PLACE_BOMB)) {
         placeBomb();
      } else {
         moving = false;
      }
   }

   private void placeBomb() {
      Sound.playEffect("bomb_set");
      
      BombermanGame.input.remove(BOMBER_KEY_CONTROLS[typePlayer][BOMB_PLACE]);
      if (bombCountMax == 0) {
         return;
      }

      int bombX = this.getXTile();
      int bombY = this.getYTile();

      // Sử dụng sprite bomb phù hợp với loại player
      int bombSprite = (typePlayer == BOMBER1) ? Sprite.PLAYER1_BOMB_0 : Sprite.PLAYER2_BOMB_0;
      Bomb newBomb = new Bomb(bombX, bombY, bombSprite, flameSize, id);
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
      if (brickPassTime > 0) {
         brickPassTime -= deltaTime;
         if (brickPassTime <= 0) {
            brickPass = false;
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

   public void setBrickPass(boolean brickPass) {
      this.brickPass = brickPass;
      if (brickPass) {
         brickPassTime = ITEM_DURATION;
      }
   }
   
   public void setPermanentFreeze(boolean freeze) {
      this.permanentFreeze = freeze;
   }

   public boolean isPermanentFreeze() {
      return permanentFreeze;
   }

   public int getHealth(){
      return health;
   }

   public void bombExplode() {
      Sound.playEffect("bomb_explode");
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
      Sound.stopMusic();
      Sound.playMusic("start_game", true);
      x = initialX;
      y = initialY;
      bombCountMax = 1;
      speed = 25;
      flameSize = 1;
      // không reset health ở đây!
      isInvincible = false;
      invincibleRemaining = 0f;
      blinkTimer = 0f;
   }

   public boolean isDying() {
      return this.dying;
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

   public boolean isAlive() {
      return health > 0;
   }

   public void setPlayerName(String playerName) {
      this.playerName = playerName;
   }
   public static boolean isGameOver() {
      Map<Integer,Bomber> map = GameControl.getBomberEntitiesMap();
      if (map.isEmpty()) return false;
      return map.values().stream()
                .allMatch(b -> b.getHealth() <= 0 || b.isDying());
   }

}
