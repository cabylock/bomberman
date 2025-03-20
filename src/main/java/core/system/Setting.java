package core.system;
import javafx.scene.input.KeyCode;

public  class Setting {
   
   public static final int DEFAULT_MAP = 0;
   public static final int CUSTOM_MAP = 1;
   public static  int PLAYER_NUM = 1; 


   public static  final int FPS_MAX = 100;
   public static  final int FRAME_TIME_NS = 1_000_000_000 / FPS_MAX;

   public static final int BOMBER1 = 1;
   public static final int BOMBER2 = 2;

   public static final KeyCode BOMBER_MOVE_UP_1 = KeyCode.UP;
   public static final KeyCode BOMBER_MOVE_DOWN_1 = KeyCode.DOWN;
   public static final KeyCode BOMBER_MOVE_LEFT_1 = KeyCode.LEFT;
   public static final KeyCode BOMBER_MOVE_RIGHT_1 = KeyCode.RIGHT;
   public static final KeyCode BOMBER_PLACE_BOMB_1 = KeyCode.NUMPAD1;

   
   public static final KeyCode BOMBER_MOVE_UP_2 = KeyCode.W;
   public static final KeyCode BOMBER_MOVE_DOWN_2 = KeyCode.S;
   public static final KeyCode BOMBER_MOVE_LEFT_2 = KeyCode.A;
   public static final KeyCode BOMBER_MOVE_RIGHT_2 = KeyCode.D;
   public static final KeyCode BOMBER_PLACE_BOMB_2 = KeyCode.J;

   public static final int RIGHT_MOVING = 0;
   public static final int LEFT_MOVING = 1;
   public static final int UP_MOVING = 2;
   public static final int DOWN_MOVING = 3;

}
