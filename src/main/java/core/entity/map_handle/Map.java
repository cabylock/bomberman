package core.entity.map_handle;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import core.entity.Entity;
import core.entity.dynamic_entity.*;
import core.entity.item_entity.*;
import core.entity.static_entity.*;
import core.graphics.Sprite;

public class Map {
   private int width;
   private int height;
   private int level;
   private char[][] mapData;
   private  List<Entity> entities = new ArrayList<>();
   private  List<Entity> stillObjects = new ArrayList<>();

   public Map(List<Entity> entities, List<Entity> stillObjects) {
      this.entities = entities;
      this.stillObjects = stillObjects;

   }

   public int getWidth() {
      return width;
   }

   public int getLevel() {
      return level;
   }

   public int getHeight() {
      return height;
   }

   /**
    * @return the map
    */
   public char[][] getMap() {
      return mapData;
   }

   public void readMap(int level) {
      String filePath = "/levels/Level" + level + ".txt";
      try {
         // Try to load the resource
         InputStream is = getClass().getResourceAsStream(filePath);

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
         System.out.println("Map loaded successfully - Level: " + level + ", Size: " + width + "x" + height);
      } catch (java.io.IOException e) {
         e.printStackTrace();
      }
   }

   public void loadMap(int level) {
      readMap(level);

      for (int i = 0; i < this.height; i++) {
         for (int j = 0; j < this.width; j++) {
            char c = mapData[i][j];
            Entity grass = new Grass(j, i, Sprite.grass.getFxImage());
            stillObjects.add(grass);
            if (c == '#') {
               Entity wall = new Wall(j, i, Sprite.wall.getFxImage());
               stillObjects.add(wall);
            } else if (c == '*') {
               Entity brick = new Brick(j, i, Sprite.brick.getFxImage());
               stillObjects.add(brick);
            } else if (c == 'x') {
               Entity portal = new Portal(j, i, Sprite.portal.getFxImage());
               stillObjects.add(portal);
               Entity brick = new Brick(j, i, Sprite.brick.getFxImage());
               stillObjects.add(brick);
            } else if (c == 'p') {
               Entity player = new Bomber(j, i, Sprite.player_right.getFxImage());
               entities.add(player);
            } else if (c == '1') {
               Entity balloon = new Balloom(j, i, Sprite.balloom_left1.getFxImage());
               entities.add(balloon);
            } else if (c == '2') {
               Entity oneal = new Oneal(j, i, Sprite.oneal_left1.getFxImage());
               entities.add(oneal);
            } else if (c == 'b') {
               Entity BombItem = new BombItem(j, i, Sprite.powerup_bombs.getFxImage());
               stillObjects.add(BombItem);
               Entity brick = new Brick(j, i, Sprite.brick.getFxImage());
               stillObjects.add(brick);
            } else if (c == 'f') {
               Entity FlameItem = new FlameItem(j, i, Sprite.powerup_flames.getFxImage());
               stillObjects.add(FlameItem);
               Entity brick = new Brick(j, i, Sprite.brick.getFxImage());
               stillObjects.add(brick);
            } else if (c == 's') {
               Entity SpeedItem = new SpeedItem(j, i, Sprite.powerup_speed.getFxImage());
               stillObjects.add(SpeedItem);
               Entity brick = new Brick(j, i, Sprite.brick.getFxImage());
               stillObjects.add(brick);
            } 
            else {
               // Entity grass = new Grass(j, i, Sprite.grass.getFxImage());
               // stillObjects.add(grass);
            }
         }

      }
   }

   public void nextLevel() {
      level++;
      loadMap(level);
   }

   public void reset() {
      entities.clear();
      stillObjects.clear();
      loadMap(level);
   }

   public List<Entity> getEntities() {
      return entities;
   }

   public List<Entity> getStillObjects() {
      return stillObjects;
   }
}
