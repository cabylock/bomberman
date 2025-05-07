package core.system.setting;

import core.util.Util;
import javafx.scene.input.KeyCode;

public class Setting {

      public static final int SCREEN_HEIGHT = 600;
      public static final int SCREEN_WIDTH = 800;
      public static final int SEED_RANDOM = 123456789;

      public static final int MAX_LEVEL = 5;

      public static int GAME_MODE = 0;
      public static int MAP_TYPE = 0;
      public static int MAP_LEVEl = 0;

      public static final int SINGLE_MODE = 0;
      public static final int MULTI_MODE = 1;
      public static final int SERVER_MODE = 2;
      public static final int CLIENT_MODE = 3;

      public static final int DEFAULT_MAP = 0;
      public static final int CUSTOM_MAP = 1;

      public static String SERVER_ADDRESS = "localhost";
      public static int SERVER_PORT = 2005;
      public static int ID = Util.uuid();

      public static final String NETWORK_STATIC_ENTITIES = "STATIC";
      public static final String NETWORK_ENEMY_ENTITIES = "ENEMY";
      public static final String NETWORK_ITEM_ENTITIES = "ITEM";
      public static final String NETWORK_BOMBER_ENTITIES = "BOMBER";
      public static final String NETWORK_BACKGROUND_ENTITIES = "BACKGROUND";

      public static final float FPS_MAX = 240;
      public static final float FRAME_TIME_NS = 1.0f / FPS_MAX;

      public static final int BOMBER1 = 0;
      public static final int BOMBER2 = 1;

      public static final String MOVE_UP = "UP";
      public static final String MOVE_DOWN = "DOWN";
      public static final String MOVE_LEFT = "LEFT";
      public static final String MOVE_RIGHT = "RIGHT";
      public static final String PLACE_BOMB = "BOMB";

      public static final KeyCode[][] BOMBER_KEY_CONTROLS = {

                  { KeyCode.RIGHT, KeyCode.LEFT, KeyCode.UP, KeyCode.DOWN, KeyCode.NUMPAD1 },

                  { KeyCode.D, KeyCode.A, KeyCode.W, KeyCode.S, KeyCode.J }
      };

      public static final int RIGHT_MOVING = 0;
      public static final int LEFT_MOVING = 1;
      public static final int UP_MOVING = 2;
      public static final int DOWN_MOVING = 3;
      public static final int BOMB_PLACE = 4;
      public static final int DEAD = 4;
      public static final int ANIMATION_NULL = 5;

}
