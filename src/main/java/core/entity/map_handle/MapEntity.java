package core.entity.map_handle;
import core.entity.dynamic_entity.*;
import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.entity.dynamic_entity.mobile_entity.enemy_entity.Balloom;
import core.entity.dynamic_entity.mobile_entity.enemy_entity.Oneal;
import core.entity.dynamic_entity.static_entity.Bomb;
import core.entity.dynamic_entity.static_entity.Brick;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import core.entity.Entity;
import core.entity.background_entity.*;
import core.entity.item_entity.*;
import core.graphics.Sprite;

public class MapEntity {
   private static int width;
   private static int height;
   private static int level;
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

   public static void readMap(int level) {
      String filePath = "/levels/Level" + level + ".txt";
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

   public static void loadMap(int level) {
      readMap(level);

      for (int i = 0; i < height; i++) {
         for (int j = 0; j < width; j++) {
            char c = mapData[i][j];
            BackgroundEntity grass = new Grass(j, i, Sprite.grass.getFxImage());
            backgroundEntities.add(grass);
            if (c == '#') {
               BackgroundEntity wall = new Wall(j, i, Sprite.wall.getFxImage());
               backgroundEntities.add(wall);
            } else if (c == '*') {
               DynamicEntity brick = new Brick(j, i, Sprite.brick.getFxImage());
               dynamicEntities.add(brick);
            } else if (c == 'x') {
               ItemEntity portal = new Portal(j, i, Sprite.portal.getFxImage());
               itemEntities.add(portal);
               DynamicEntity brick = new Brick(j, i, Sprite.brick.getFxImage());
               dynamicEntities.add(brick);
            } else if (c == 'p') {
               DynamicEntity player = new Bomber(j, i, Sprite.player_right.getFxImage());
               dynamicEntities.add(player);
            } else if (c == '1') {
               DynamicEntity balloon = new Balloom(j, i, Sprite.balloom_left1.getFxImage());
               dynamicEntities.add(balloon);
            } else if (c == '2') {
               DynamicEntity oneal = new Oneal(j, i, Sprite.oneal_left1.getFxImage());
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
               DynamicEntity brick = new Brick(j, i, Sprite.brick.getFxImage());
               dynamicEntities.add(brick);
            } else {
               // Entity grass = new Grass(j, i, Sprite.grass.getFxImage());
               // backgroundEntity.add(grass);
            }
         }

      }
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
      dynamicEntities.clear();
      backgroundEntities.clear();
      loadMap(level);
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

   public static List<BackgroundEntity> getBackgroundEntities() {
      return backgroundEntities;
   }
   public static List<ItemEntity> getItemEntities() {
      return itemEntities;
   }
}
