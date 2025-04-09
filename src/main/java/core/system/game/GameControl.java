package core.system.game;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Map;
import core.entity.Entity;
import core.entity.background_entity.BackgroundEntity;
import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.entity.dynamic_entity.mobile_entity.enemy_entity.EnemyEntity;
import core.entity.dynamic_entity.static_entity.StaticEntity;
import core.entity.item_entity.ItemEntity;
import core.map.MapEntity;
import core.system.network.GameClient;
import core.system.network.GameServer;
import core.system.setting.Setting;
import core.util.Util;

public class GameControl {

   private static String mapName;
   private static int mapType;
   private static int level;
   private static int gameMode;

   private static double deltaTime;

   private static GameServer server;
   private static GameClient client;

   private static Map<Integer, Bomber> bomberEntities = new ConcurrentHashMap<>();
   private static List<StaticEntity> staticEntities = new CopyOnWriteArrayList<StaticEntity>();
   private static List<EnemyEntity> enemyEntities = new CopyOnWriteArrayList<EnemyEntity>();
   private static List<BackgroundEntity> backgroundEntities = new CopyOnWriteArrayList<BackgroundEntity>();
   private static List<ItemEntity> itemEntities = new CopyOnWriteArrayList<ItemEntity>();

   public static void InitializeServer() {
      server = new GameServer();
      server.startServer(Setting.SERVER_PORT);

   }

   public static void InitializeClient() {
      clear();
      client = new GameClient(Setting.SERVER_ADDRESS, Setting.SERVER_PORT);
      if (!client.connect()) {
         Util.showNotificationWindow("Cannot connect to server");
         return;
      }

   }

   public static void start(int gameMode) {
      GameControl.gameMode = gameMode;

      if (gameMode == Setting.SERVER_MODE) {
         InitializeServer();

      } else if (gameMode == Setting.CLIENT_MODE) {
         InitializeClient();
      } else if (gameMode == Setting.SINGLE_MODE) {

      } else if (gameMode == Setting.MULTI_MODE) {

      }

   }

   public static void stop() {
      if (gameMode == Setting.SERVER_MODE) {
         server.stopServer();
      } else if (gameMode == Setting.CLIENT_MODE) {
         client.disconnect();
      }
      gameMode = Setting.SINGLE_MODE;

   }

   public static void update(double deltaTime) {
      GameControl.deltaTime = deltaTime;
      handleInput(deltaTime);

      if (gameMode == Setting.CLIENT_MODE) {
         return;
      }

      for (EnemyEntity entity : enemyEntities) {
         entity.update(deltaTime);
      }
      for (StaticEntity entity : staticEntities) {
         entity.update(deltaTime);
      }
      for (ItemEntity entity : itemEntities) {
         entity.update(deltaTime);
      }
      for (Bomber entity : bomberEntities.values()) {
         entity.update(deltaTime);
      }

      if (gameMode == Setting.SERVER_MODE) {
         server.broadcastGameState();
      }
   }

   public static void handleInput(double deltaTime) {
      // First player (always present)
      handlePlayerInput(Setting.BOMBER1, Setting.ID, deltaTime);

      // Second player (only in multiplayer modes)
      if (gameMode == Setting.MULTI_MODE) {
         handlePlayerInput(Setting.BOMBER2, Setting.ID + 1, deltaTime);
      }
   }

   private static void handlePlayerInput(int playerType, int playerId, double deltaTime) {
      // Check if player entity exists
      if (!bomberEntities.containsKey(playerId)) {
         return;
      }

      String command = Setting.STOP;

      // Check each possible input in order of priority
      if (BombermanGame.input.contains(Setting.BOMBER_KEY_CONTROLS[playerType][Setting.UP_MOVING])) {
         command = Setting.MOVE_UP;
      } else if (BombermanGame.input.contains(Setting.BOMBER_KEY_CONTROLS[playerType][Setting.DOWN_MOVING])) {
         command = Setting.MOVE_DOWN;
      } else if (BombermanGame.input.contains(Setting.BOMBER_KEY_CONTROLS[playerType][Setting.LEFT_MOVING])) {
         command = Setting.MOVE_LEFT;
      } else if (BombermanGame.input.contains(Setting.BOMBER_KEY_CONTROLS[playerType][Setting.RIGHT_MOVING])) {
         command = Setting.MOVE_RIGHT;
      } else if (BombermanGame.input.contains(Setting.BOMBER_KEY_CONTROLS[playerType][Setting.BOMB_PLACE])) {
         command = Setting.PLACE_BOMB;
      }

      // Execute command based on game mode
      if (gameMode != Setting.CLIENT_MODE) {
         // Direct control for local or server modes
         bomberEntities.get(playerId).control(command, deltaTime);
      } else {
         client.sendCommand(command, playerId);
      }
   }

   public static double getDeltaTime()
   {
      return deltaTime;
   }

   public static int getWidth() {
      return MapEntity.getWidth();
   }

   public static int getLevel() {
      return MapEntity.getLevel();
   }

   public static int getHeight() {
      return MapEntity.getHeight();
   }

   public static void loadMap(int level) {
      clear();
      GameControl.level = level;
      GameControl.mapName = "Level" + level + ".txt";
      GameControl.mapType = Setting.DEFAULT_MAP;
      MapEntity.loadMap(level);

   }

   public static void loadMap(String mapName, int mapType) {
      clear();
      GameControl.mapName = mapName;
      GameControl.mapType = mapType;
      MapEntity.loadMap(mapName, mapType);
   }

   public static void nextLevel() {

      if (mapType == Setting.CUSTOM_MAP) {
         Util.showNotificationWindow("Please select another map or move to default map");
         return;
      }
      if (level == Setting.MAX_LEVEL) {
         Util.showImage("/textures/win2.png", BombermanGame.getGameRoot());
         return;
      }
      level++;
      loadMap(level);
   }

   public static void resetGame() {
      clear();
      MapEntity.loadMap(mapName, mapType);
   }

   public static void clear() {
      bomberEntities.clear();
      staticEntities.clear();
      enemyEntities.clear();
      itemEntities.clear();
      backgroundEntities.clear();

   }

   public static void addEntity(Entity entity) {

      if (entity instanceof Bomber) {
         Bomber bomber = (Bomber) entity;
         bomberEntities.put(bomber.getId(), bomber);
      } else if (entity instanceof StaticEntity) {
         staticEntities.add((StaticEntity) entity);
      } else if (entity instanceof EnemyEntity) {
         enemyEntities.add((EnemyEntity) entity);
      } else if (entity instanceof BackgroundEntity) {
         backgroundEntities.add((BackgroundEntity) entity);
      } else if (entity instanceof ItemEntity) {
         itemEntities.add((ItemEntity) entity);
      }
   }

   public static void setBomberEntities(List<Bomber> entities) {
      for (Bomber bomber : entities) {
         bomberEntities.put(bomber.getId(), bomber);
      }
   }

   public static void setStaticEntities(List<StaticEntity> entities) {
      staticEntities = entities;
   }

   public static void setEnemyEntities(List<EnemyEntity> entities) {
      enemyEntities = entities;
   }

   public static void setBackgroundEntities(List<BackgroundEntity> entities) {
      backgroundEntities = entities;
   }

   public static void setItemEntities(List<ItemEntity> entities) {
      itemEntities = entities;
   }

   public static void removeEntity(Entity entity) {
      if (entity instanceof Bomber) {
         bomberEntities.remove(entity.getId());
      } else if (entity instanceof StaticEntity) {
         staticEntities.remove(entity);
      } else if (entity instanceof EnemyEntity) {
         enemyEntities.remove(entity);
      } else if (entity instanceof BackgroundEntity) {
         backgroundEntities.remove(entity);
      } else if (entity instanceof ItemEntity) {
         itemEntities.remove(entity);
      }
   }

   public static Map<Integer, Bomber> getBomberEntitiesMap() {
      return bomberEntities;
   }

   public static List<Bomber> getBomberEntities() {
      return new CopyOnWriteArrayList<>(bomberEntities.values());
   }

   public static List<StaticEntity> getStaticEntities() {
      return staticEntities;
   }

   public static List<EnemyEntity> getEnemyEntities() {
      return enemyEntities;
   }

   public static List<BackgroundEntity> getBackgroundEntities() {
      return backgroundEntities;
   }

   public static List<ItemEntity> getItemEntities() {
      return itemEntities;
   }

}
