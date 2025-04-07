package core.system.game;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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

   private static GameServer server;
   private static GameClient client;

   private static List<Bomber> bomberEntities = new CopyOnWriteArrayList<Bomber>();
   private static List<StaticEntity> staticEntities = new CopyOnWriteArrayList<StaticEntity>();
   private static List<EnemyEntity> enemyEntities = new CopyOnWriteArrayList<EnemyEntity>();
   private static List<BackgroundEntity> backgroundEntities = new CopyOnWriteArrayList<BackgroundEntity>();
   private static List<ItemEntity> itemEntities = new CopyOnWriteArrayList<ItemEntity>();

   public static void InitializeServer() {
      server = new GameServer();
      server.startServer(Setting.SERVER_PORT);

   }

   public static void InitializeClient() {
      client = new GameClient(Setting.SERVER_ADDRESS, Setting.SERVER_PORT);
      client.connect();
   }

   public static void start(int gameMode) {
      GameControl.gameMode = gameMode;
      if(gameMode == Setting.SERVER_MODE)
      {
         server = new GameServer();
         server.startServer(Setting.SERVER_PORT);
         
      }
      else if(gameMode == Setting.CLIENT_MODE)
      {
         clear();
         client = new GameClient(Setting.SERVER_ADDRESS, Setting.SERVER_PORT);
         if(!client.connect())
         {
            Util.showNotificationWindow("Cannot connect to server");
            return;
         }
      }
      else 
      {  // test mode
         server = new GameServer();
         server.startServer(Setting.SERVER_PORT);
         client = new GameClient(Setting.SERVER_ADDRESS, Setting.SERVER_PORT);
         client.connect();
      }
      Thread runningThread = new Thread(() -> {
         while (true) {
            try {
               Thread.sleep(1_000 / Setting.FPS_MAX);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }

            update();
            if (gameMode == Setting.SERVER_MODE) {
               server.broadcastGameState();
            } 
            
         }
      });
      runningThread.setDaemon(true);
      runningThread.start();
   }

   public static void stop() {
      server.stopServer();
      client.disconnect();
   }

   public static void update() {

      for (Entity entity : bomberEntities) {
         entity.update();
      }

      if (gameMode == Setting.CLIENT_MODE) {
         client.sendData(bomberEntities, Setting.NETWORK_BOMBER_ENTITIES);
      }

      for (Entity entity : staticEntities) {
         entity.update();
      }
      if (gameMode == Setting.CLIENT_MODE) {
         client.sendData(staticEntities, Setting.NETWORK_STATIC_ENTITIES);
      }

   
      for (Entity entity : enemyEntities) {
         entity.update();
      }
      
      if( gameMode == Setting.CLIENT_MODE) {
         client.sendData(enemyEntities, Setting.NETWORK_ENEMY_ENTITIES);
      }

      for (Entity entity : itemEntities) {
         entity.update();
      }
      if (gameMode == Setting.CLIENT_MODE) {
         client.sendData(itemEntities, Setting.NETWORK_ITEM_ENTITIES);
      }
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
         bomberEntities.add((Bomber) entity);
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
      bomberEntities = entities;
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
         bomberEntities.remove(entity);
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

   public static List<Bomber> getBomberEntities() {
      return bomberEntities;
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
