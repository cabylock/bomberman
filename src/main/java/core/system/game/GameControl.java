package core.system.game;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import core.entity.Entity;
import core.entity.background_entity.BackgroundEntity;
import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.entity.dynamic_entity.mobile_entity.enemy_entity.EnemyEntity;
import core.entity.dynamic_entity.static_entity.StaticEntity;
import core.entity.item_entity.ItemEntity;
import core.map_handle.MapEntity;
import core.system.setting.Setting;

public class GameControl {
   
   private static String mapName;
   private static int mapType;
   
   private static List<Bomber> bomberEntities = new CopyOnWriteArrayList<Bomber>();
   private static List<StaticEntity> staticEntities = new CopyOnWriteArrayList<StaticEntity>();
   private static List<EnemyEntity> enemyEntities = new CopyOnWriteArrayList<EnemyEntity>();
   private static List<BackgroundEntity> backgroundEntities = new CopyOnWriteArrayList<BackgroundEntity>();
   private static List<ItemEntity> itemEntities = new CopyOnWriteArrayList<ItemEntity>();





   public static void update() {

      for (Entity entity : bomberEntities) {
         entity.update();
      }
      for (Entity entity : staticEntities) {
         entity.update();
      }
      for (Entity entity : enemyEntities) {
         entity.update();
      }
      for (Entity entity : itemEntities) {
         entity.update();
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
      GameControl.mapName = "Level" + level+".txt";
      GameControl.mapType = Setting.DEFAULT_MAP;
      MapEntity.loadMap(level);

   }
   
   public static void loadMap(String mapName, int mapType) {
      clear();
      GameControl.mapName = mapName;
      GameControl.mapType = mapType;
      MapEntity.loadMap(mapName, mapType);
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
