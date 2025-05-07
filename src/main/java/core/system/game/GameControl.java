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
import core.sound.Sound;
import core.system.network.GameClient;
import core.system.network.GameServer;
import core.system.setting.Setting;
import core.util.Util;

public class GameControl {

   
  
   private static float deltaTime;
   private static int width;
   private static int height;

   private static GameServer server;
   private static GameClient client;

   private static Map<Integer, Bomber> bomberEntities = new ConcurrentHashMap<>();
   private static List<StaticEntity> staticEntities = new CopyOnWriteArrayList<>();
   private static List<EnemyEntity> enemyEntities = new CopyOnWriteArrayList<>();
   private static List<BackgroundEntity> backgroundEntities = new CopyOnWriteArrayList<>();
   private static List<ItemEntity> itemEntities = new CopyOnWriteArrayList<>();

   private static boolean deathHandled = false; // NEW

   public static boolean InitializeServer() {
      server = new GameServer();
      server.startServer(Setting.SERVER_PORT);
      return server.isRunning;
   }

   public static boolean InitializeClient() {
      clear();
      client = new GameClient(Setting.SERVER_ADDRESS, Setting.SERVER_PORT);
      return client.connect();
   }

   public static boolean start() {
      
      if (Setting.GAME_MODE == Setting.SERVER_MODE)
         return InitializeServer();
      if (Setting.GAME_MODE == Setting.CLIENT_MODE)
         return InitializeClient();
      return true;
   }

   public static void stop() {
      if (Setting.GAME_MODE == Setting.SERVER_MODE && server != null) {
         server.stopServer();
         server = null;
      } else if (Setting.GAME_MODE == Setting.CLIENT_MODE && client != null) {
         client.disconnect();
         client = null;
      }
      Setting.GAME_MODE = Setting.SINGLE_MODE;
      clear();
      System.gc();
   }

   // Add this setter
   public static void setServer(GameServer s) {
      server = s;
   }

   // Add this setter
   public static void setClient(GameClient c) {
      client = c;
   }

   public static void update(float deltaTime) {
      GameControl.deltaTime = deltaTime;
      handleInput(deltaTime);

      if (Setting.GAME_MODE == Setting.CLIENT_MODE)
         return;

      for (EnemyEntity entity : enemyEntities)
         entity.update(deltaTime);
      for (StaticEntity entity : staticEntities)
         entity.update(deltaTime);
      for (ItemEntity entity : itemEntities)
         entity.update(deltaTime);
      for (Bomber entity : bomberEntities.values())
         entity.update(deltaTime);

      // Check if all players are dead, then stop music and play death sound
      if (!deathHandled) {
         long total = bomberEntities.size();
         long deadCount = bomberEntities.values().stream()
               .filter(b -> b.getHealth() <= 0 || b.isDying())
               .count();

         if (total > 0 && deadCount == total) {
            Sound.stopMusic();
            Sound.playEffect("game_over");
            deathHandled = true;
         }
      }

      // Fix: Only call broadcastGameState if server is not null
      if (Setting.GAME_MODE == Setting.SERVER_MODE && server != null) {
         server.broadcastGameState();
      }
   }

   public static void handleInput(float deltaTime) {
      handlePlayerInput(Bomber.BOMBER1, Setting.ID, deltaTime);
      if (Setting.GAME_MODE == Setting.MULTI_MODE) {
         handlePlayerInput(Bomber.BOMBER2, Setting.ID + 1, deltaTime);
      }
   }

   private static void handlePlayerInput(int playerType, int playerId, float deltaTime) {
      if (!bomberEntities.containsKey(playerId))
         return;

      String command = "NULL";

      if (BombermanGame.input.contains(Bomber.BOMBER_KEY_CONTROLS[playerType][Bomber.UP_MOVING])) {
         command = Bomber.MOVE_UP;
      } else if (BombermanGame.input.contains(Bomber.BOMBER_KEY_CONTROLS[playerType][Bomber.DOWN_MOVING])) {
         command = Bomber.MOVE_DOWN;
      } else if (BombermanGame.input.contains(Bomber.BOMBER_KEY_CONTROLS[playerType][Bomber.LEFT_MOVING])) {
         command = Bomber.MOVE_LEFT;
      } else if (BombermanGame.input.contains(Bomber.BOMBER_KEY_CONTROLS[playerType][Bomber.RIGHT_MOVING])) {
         command = Bomber.MOVE_RIGHT;
      } else if (BombermanGame.input.contains(Bomber.BOMBER_KEY_CONTROLS[playerType][Bomber.BOMB_PLACE])) {
         command = Bomber.PLACE_BOMB;
      }

      if (Setting.GAME_MODE != Setting.CLIENT_MODE) {
         bomberEntities.get(playerId).control(command, deltaTime);
      } else {
         // Fix: Only send command if client is not null
         if (client != null) {
            client.sendCommand(command, playerId);
         }
      }
   }

   public static float getDeltaTime() {
      return deltaTime;
   }

  

   public static void loadMap(String name) {

      MapEntity.loadMap(name);
   }

   public static void nextLevel() {
      if (Setting.MAP_TYPE == Setting.CUSTOM_MAP) {
         Util.logInfo("Please select another map or move to default map");
         return;
      }
      if (Setting.MAP_LEVEl == Setting.MAX_LEVEL) {
         Util.showImage("/textures/win2.png", BombermanGame.getGameRoot());
         return;
      }
      Setting.MAP_LEVEl++;
      loadMap("Level" + Setting.MAP_LEVEl);
   }

   public static void resetGame() {
      clear();
      loadMap("Level" + Setting.MAP_LEVEl);
   }

   public static void clear() {
      deathHandled = false;
      bomberEntities.forEach((_, b) -> b.resetBomber());
      staticEntities.clear();
      enemyEntities.clear();
      itemEntities.clear();
      backgroundEntities.clear();
   }

   public static void addEntity(Entity entity) {
      if (entity instanceof Bomber b) {
         bomberEntities.put(b.getId(), b);
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
      bomberEntities.clear();
      for (Bomber b : entities) {
         bomberEntities.put(b.getId(), b);
      }
   }

   public static void setStaticEntities(List<StaticEntity> entities) {
      staticEntities = new CopyOnWriteArrayList<>(entities);
   }

   public static void setEnemyEntities(List<EnemyEntity> entities) {
      enemyEntities = new CopyOnWriteArrayList<>(entities);
   }

   public static void setBackgroundEntities(List<BackgroundEntity> entities) {
      backgroundEntities = new CopyOnWriteArrayList<>(entities);
   }

   public static void setItemEntities(List<ItemEntity> entities) {
      itemEntities = new CopyOnWriteArrayList<>(entities);
   }

   public static void setHeight(int height) {
      GameControl.height = height;
   }

   public static void setWidth(int width) {

      GameControl.width = width; 
   }
   public static int getHeight() {
      return height;
   }
   public static int getWidth() {
      return width;
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
