package core.system.setting;

import javafx.scene.input.KeyCode;

public class Setting {

      public static final int SCREEN_HEIGHT = 600;
      public static final int SCREEN_WIDTH = 800;
      public static final int SEED_RANDOM = 123456789;

      public static final int DEFAULT_MAP = 0;
      public static final int MAX_LEVEL = 5;

      public static final int CUSTOM_MAP = 1;

      public static final int SERVER_MODE = 0;
      public static final int CLIENT_MODE = 1;
      

      public static int PLAYER_NUM = 1;
      public static String SERVER_ADDRESS = "localhost";
      public static int SERVER_PORT = 2005;
      
      public static String NETWORK_STATIC_ENTITIES = "STATIC";
      public static String NETWORK_ENEMY_ENTITIES = "ENEMY";
      public static String NETWORK_ITEM_ENTITIES = "ITEM";
      public static String NETWORK_BOMBER_ENTITIES = "BOMBER";
      public static String NETWORK_BACKGROUND_ENTITIES = "BACKGROUND";

      public static final int FPS_MAX = 100;
      public static final int FRAME_TIME_NS = 1_000_000_000 / FPS_MAX;

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
