package core.entity.map_handle;
import core.entity.dynamic_entity.*;
import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.entity.dynamic_entity.mobile_entity.MobileEntity;
import core.entity.dynamic_entity.mobile_entity.enemy_entity.Balloom;
import core.entity.dynamic_entity.mobile_entity.enemy_entity.EnemyEntity;
import core.entity.dynamic_entity.mobile_entity.enemy_entity.Oneal;
import core.entity.dynamic_entity.static_entity.Brick;
import core.entity.dynamic_entity.static_entity.StaticEntity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import java.util.concurrent.CopyOnWriteArrayList;
import core.entity.Entity;
import core.entity.background_entity.*;
import core.entity.item_entity.*;
import core.graphics.Sprite;
import core.system.Setting;

public class MapEntity {

   private static int width;
   private static int height;
   private static int level;
   private static String name;
   private static int mapType;
   private static char[][] mapData;
   private static List<DynamicEntity> dynamicEntities = new CopyOnWriteArrayList<DynamicEntity>();
   private static List<BackgroundEntity> backgroundEntities = new CopyOnWriteArrayList<BackgroundEntity>();
   private static List<ItemEntity> itemEntities = new CopyOnWriteArrayList<ItemEntity>();


   public static int getWidth() {
      return width;
   }

   public static int getLevel() {
      return level;
   }

   public static int getHeight() {
      return height;
   }

   public char[][] getMap() {
      return mapData;
   }

   


   public static void readMap(int level, int type) {
      String filePath="";
      if (type == Setting.DEFAULT_MAP) {
         
          filePath = "/default_levels/Level" + level + ".txt";
      }
      else if (type == Setting.CUSTOM_MAP) {
          filePath = "/custom_levels/Level" + level + ".txt";
      }
      readMap(filePath);
   }

   public static void readMap(String filePath)
   {
      try {
         // Try to load the resource
         InputStream is = MapEntity.class.getResourceAsStream(filePath);

         // Check if resource was found
         if (is == null) {
            System.err.println("ERROR: Could not find resource at path: " + filePath);
            return;
         }

         BufferedReader br = new BufferedReader(new InputStreamReader(is));

         // Read the first line and check if it's not null
         String firstLine = br.readLine();

         String[] info = firstLine.split(" ");

         // Now parse the values
         level = Integer.parseInt(info[0]);
         height = Integer.parseInt(info[1]);
         width = Integer.parseInt(info[2]);
         mapData = new char[height][width];

         // Rest of your code remains the same
         for (int i = 0; i < height; i++) {
            String line = br.readLine();

            for (int j = 0; j < width; j++) {
               mapData[i][j] = line.charAt(j);
            }
         }
         br.close();
         System.out.println("MapEntity loaded successfully - Level: " + level + ", Size: " + width + "x" + height);
      } catch (java.io.IOException e) {
         e.printStackTrace();
      }
   }

   public static void loadMap(String name,int mapType) {
      String filePath = mapType == Setting.DEFAULT_MAP ? "/default_levels/" + name : "/custom_levels/" + name ;
      readMap(filePath);

      for (int i = 0; i < height; i++) {
         for (int j = 0; j < width; j++) {
            char c = mapData[i][j];
            BackgroundEntity grass = new Grass(j, i, Sprite.grass.getFxImage());
            backgroundEntities.add(grass);
            if (c == '#') {
               BackgroundEntity wall = new Wall(j, i, Sprite.wall.getFxImage());
               backgroundEntities.add(wall);
            } else if (c == '*') {
               StaticEntity brick = new Brick(j, i, Sprite.brick.getFxImage());
               dynamicEntities.add(brick);
            } else if (c == 'x') {
               ItemEntity portal = new Portal(j, i, Sprite.portal.getFxImage());
               itemEntities.add(portal);
               StaticEntity brick = new Brick(j, i, Sprite.brick.getFxImage());
               dynamicEntities.add(brick);
            } else if (c == 'p') {
               MobileEntity player = new Bomber(j, i, Sprite.player_right.getFxImage(), Setting.BOMBER1);
               dynamicEntities.add(player);
            }
            else if(c== 'q'&& Setting.PLAYER_NUM==2){
               MobileEntity player = new Bomber(j, i, Sprite.player_right.getFxImage(), Setting.BOMBER2);
               dynamicEntities.add(player);
            }
            else if (c == '1') {
               EnemyEntity balloon = new Balloom(j, i, Sprite.balloom_left1.getFxImage());
               dynamicEntities.add(balloon);
            } else if (c == '2') {
               EnemyEntity oneal = new Oneal(j, i, Sprite.oneal_left1.getFxImage());
               dynamicEntities.add(oneal);
            } else if (c == 'b') {
               ItemEntity BombItem = new BombItem(j, i, Sprite.powerup_bombs.getFxImage());
               itemEntities.add(BombItem);
               DynamicEntity brick = new Brick(j, i, Sprite.brick.getFxImage());
               dynamicEntities.add(brick);
            } else if (c == 'f') {
               ItemEntity FlameItem = new FlameItem(j, i, Sprite.powerup_flames.getFxImage());
               itemEntities.add(FlameItem);
               DynamicEntity brick = new Brick(j, i, Sprite.brick.getFxImage());
               dynamicEntities.add(brick);
            } else if (c == 's') {
               ItemEntity SpeedItem = new SpeedItem(j, i, Sprite.powerup_speed.getFxImage());
               itemEntities.add(SpeedItem);
               StaticEntity brick = new Brick(j, i, Sprite.brick.getFxImage());
               dynamicEntities.add(brick);
            } else {
               // Grass is already added at the beginning of the loop for every cell
            }
         }

      }
      MapEntity.mapType = mapType;
   }

   public static void loadMap(int level) {
      loadMap("Level" + level+".txt", Setting.DEFAULT_MAP);

   }


   public static void nextLevel() {
      level++;
      loadMap(level);
   }

   public static void update() {

     

      for (Entity entity : dynamicEntities) {
         entity.update();
      }
   }

   public static void reset() {
      clear();
      loadMap(name,mapType);
   }
   
   public static void clear() {
      dynamicEntities.clear();
      backgroundEntities.clear();
      itemEntities.clear();
      
   }

   public static void addDynamicEntity(DynamicEntity entity) {
      dynamicEntities.add(entity);
      
   }

   public static void removeDynamicEntity(Entity entity) {
      dynamicEntities.remove(entity);
   }


   public static List<DynamicEntity> getDynamicEntities() {
      return dynamicEntities;
   }

   public static void addItem(ItemEntity item) {
      itemEntities.add(item);
   }

   public static void removeItem(ItemEntity item) {
      itemEntities.remove(item);
   }
   
   public static List<ItemEntity> getItemEntities() {
      return itemEntities;
   }

   public static void addBackgroundEntity(BackgroundEntity entity) {
      backgroundEntities.add(entity);
   }

   public static List<BackgroundEntity> getBackgroundEntities() {
      return backgroundEntities;
   }
   
   public static void removeBackgroundEntity(BackgroundEntity entity) {
      backgroundEntities.remove(entity);
   }


}
