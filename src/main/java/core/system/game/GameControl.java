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
import core.system.setting.Setting;
import core.system.network.GameClient;
import core.system.network.GameServer;
import core.util.Util;

public class GameControl {

   private static float deltaTime;
   private static int width;
   private static int height;

   private static Map<Integer, Bomber> bomberEntities = new ConcurrentHashMap<>();
   private static List<StaticEntity> staticEntities = new CopyOnWriteArrayList<>();
   private static List<EnemyEntity> enemyEntities = new CopyOnWriteArrayList<>();
   private static List<BackgroundEntity> backgroundEntities = new CopyOnWriteArrayList<>();
   private static List<ItemEntity> itemEntities = new CopyOnWriteArrayList<>();
   private static boolean deathOverlayShown = false;

   

   

   

   public static void stop() {
     
      if (Setting.GAME_MODE == Setting.SERVER_MODE) {
         GameServer.stopServer();
      }
      if (Setting.GAME_MODE == Setting.CLIENT_MODE) {
         GameClient.disconnect();
      }
      Setting.GAME_MODE = Setting.SINGLE_MODE;
      clearEntities();
      System.gc();
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
      if (Bomber.isGameOver() && !deathOverlayShown) {
         Sound.stopMusic();
         Sound.playEffect("game_over");
         Util.showGameOverOverlay("/textures/game_over.png", BombermanGame.getGameRoot(), () -> {
         reset();
      });
      deathOverlayShown = true;
   }
      
      
   }

   

     
   private static String getCommandFromInput(int bomberType) {
      if (BombermanGame.input.contains(Bomber.BOMBER_KEY_CONTROLS[bomberType][Bomber.UP_MOVING])) {
         return Bomber.MOVE_UP;
      } else if (BombermanGame.input.contains(Bomber.BOMBER_KEY_CONTROLS[bomberType][Bomber.DOWN_MOVING])) {
         return Bomber.MOVE_DOWN;
      } else if (BombermanGame.input.contains(Bomber.BOMBER_KEY_CONTROLS[bomberType][Bomber.LEFT_MOVING])) {
         return Bomber.MOVE_LEFT;
      } else if (BombermanGame.input.contains(Bomber.BOMBER_KEY_CONTROLS[bomberType][Bomber.RIGHT_MOVING])) {
         return Bomber.MOVE_RIGHT;
      } else if (BombermanGame.input.contains(Bomber.BOMBER_KEY_CONTROLS[bomberType][Bomber.BOMB_PLACE])) {
         return Bomber.PLACE_BOMB;
      }
      return "NULL";
   }

   public static void handleInput(float deltaTime) {
      String command = "NULL";
      if (Setting.GAME_MODE == Setting.CLIENT_MODE) {
         command = getCommandFromInput(Bomber.BOMBER1);
         GameClient.sendControl(Setting.ID, command);

      } else if (Setting.GAME_MODE == Setting.SERVER_MODE) {
         command = getCommandFromInput(Bomber.BOMBER1);
         bomberEntities.get(Setting.ID).control(command, deltaTime);
      } else if (Setting.GAME_MODE == Setting.SINGLE_MODE) {
         command = getCommandFromInput(Bomber.BOMBER1);
         bomberEntities.get(Setting.ID).control(command, deltaTime);
      } else if (Setting.GAME_MODE == Setting.MULTI_MODE) {
         command = getCommandFromInput(Bomber.BOMBER1);
         bomberEntities.get(Setting.ID).control(command, deltaTime);
         command = getCommandFromInput(Bomber.BOMBER2);
         bomberEntities.get(Setting.ID + 1).control(command, deltaTime);
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
         Util.showOverlayWithButton("/textures/win2.png", BombermanGame.getGameRoot(), "Play Again", () -> {
           
            reset();
         });
         return;
      }

      Util.showOverlayWithButton("/textures/level_complete.jpg", BombermanGame.getGameRoot(), "Next Level", () -> {
         Setting.MAP_LEVEl++;
         reset();

      });
   }

   public static void reset() {
      Sound.stopMusic();
      Sound.playMusic("start_game", true);

      if (Setting.GAME_MODE == Setting.SERVER_MODE) {
         resetEntities();
      } else if (Setting.GAME_MODE == Setting.SINGLE_MODE || Setting.GAME_MODE == Setting.MULTI_MODE) {
         clearEntities();
      }
      loadMap(Setting.MAP_NAME);
      if (Setting.GAME_MODE == Setting.SERVER_MODE) {
         GameServer.broadcastMapDimensions();

      }
      }
      
   public static void clearEntities() {
      bomberEntities.clear();
      deathOverlayShown = false;
      staticEntities.clear();
      enemyEntities.clear();
      itemEntities.clear();
      backgroundEntities.clear();
   }
   public static void resetEntities(){
      bomberEntities.forEach(((_,b) -> b.resetBomber()));
      deathOverlayShown = false;
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
