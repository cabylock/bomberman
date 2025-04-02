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

 
   public static final KeyCode[][] BOMBER_KEY_CONTROLS = {
        
         { KeyCode.RIGHT, KeyCode.LEFT, KeyCode.UP, KeyCode.DOWN, KeyCode.NUMPAD1 },
         
         { KeyCode.D, KeyCode.A, KeyCode.W, KeyCode.S, KeyCode.J }
   };

   public static final int RIGHT_MOVING = 0;
   public static final int LEFT_MOVING = 1;
   public static final int UP_MOVING = 2;
   public static final int DOWN_MOVING = 3;
   public static final int DEAD = 4;

}
