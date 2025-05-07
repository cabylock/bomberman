package core.map;

import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.entity.dynamic_entity.mobile_entity.enemy_entity.Balloom;
import core.entity.dynamic_entity.mobile_entity.enemy_entity.EnemyEntity;
import core.entity.dynamic_entity.mobile_entity.enemy_entity.Oneal;
import core.entity.dynamic_entity.mobile_entity.enemy_entity.Doll;
import core.entity.dynamic_entity.mobile_entity.enemy_entity.Minvo;
import core.entity.dynamic_entity.mobile_entity.enemy_entity.Ghost;
import core.entity.dynamic_entity.static_entity.Brick;
import core.entity.dynamic_entity.static_entity.StaticEntity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import core.entity.background_entity.*;
import core.entity.item_entity.*;
import core.graphics.Sprite;
import core.system.game.GameControl;
import core.system.setting.Setting;

public class MapEntity {

   private static int width;
   private static int height;
   private static int level;

   private static char[][] mapData;

 

  

   public static void readMap(String name) {

      String filePath = Setting.MAP_TYPE == Setting.DEFAULT_MAP ? "/default_levels/" + name : "/custom_levels/" + name;
      

      try {
         // Try to load the resource
         InputStream is = MapEntity.class.getResourceAsStream(filePath+".txt");

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
         Setting.MAP_LEVEl = level;
         height = Integer.parseInt(info[1]);
         GameControl.setHeight(height);
         width = Integer.parseInt(info[2]);
         GameControl.setWidth(width);
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

   public static void loadMap(String name) {

      
      readMap(name);

      for (int i = 0; i < height; i++) {
         for (int j = 0; j < width; j++) {
            char c = mapData[i][j];
            BackgroundEntity grass = new Grass(j, i, Sprite.GRASS);
            GameControl.addEntity(grass);
            if (c == '#') {
               BackgroundEntity wall = new Wall(j, i, Sprite.WALL);
               GameControl.addEntity(wall);
            } else if (c == '*') {
               StaticEntity brick = new Brick(j, i, Sprite.BRICK);
               GameControl.addEntity(brick);
            } else if (c == 'x') {
               ItemEntity portal = new Portal(j, i, Sprite.PORTAL);
               GameControl.addEntity(portal);
               StaticEntity brick = new Brick(j, i, Sprite.BRICK);
               GameControl.addEntity(brick);
            } else if (c == 'p') {
               Bomber player = new Bomber(j, i, Sprite.PLAYER1_RIGHT_0, Setting.BOMBER1, "Player 1");
               GameControl.addEntity(player);
            } else if (c == 'q' && Setting.GAME_MODE == Setting.MULTI_MODE) {
               Bomber player = new Bomber(j, i, Sprite.PLAYER2_RIGHT_0, Setting.BOMBER2, "Player 2");
               GameControl.addEntity(player);
            } else if (c == '1') {
               EnemyEntity balloon = new Balloom(j, i, Sprite.BALLOOM_LEFT_0);
               GameControl.addEntity(balloon);
            } else if (c == '2') {
               Oneal oneal = new Oneal(j, i, Sprite.ONEAL_LEFT_0);
               GameControl.addEntity(oneal);
            } else if (c == '3') {
               Doll doll = new Doll(j, i, Sprite.DOLL_LEFT_0);
               GameControl.addEntity(doll);
            } else if (c == '4') {
               Minvo minvo = new Minvo(j, i, Sprite.MINVO_LEFT_0);
               GameControl.addEntity(minvo);
            } else if (c == '5') {
               Ghost ghost = new Ghost(j, i, Sprite.GHOST_LEFT_0);
               GameControl.addEntity(ghost);
            } else if (c == 'b') {
               ItemEntity BombItem = new BombUpItem(j, i, Sprite.POWERUP_BOMBS);
               GameControl.addEntity(BombItem);
               StaticEntity brick = new Brick(j, i, Sprite.BRICK);
               GameControl.addEntity(brick);
            } else if (c == 'f') {
               ItemEntity FlameItem = new FlameUpItem(j, i, Sprite.POWERUP_FLAMES);
               GameControl.addEntity(FlameItem);
               StaticEntity brick = new Brick(j, i, Sprite.BRICK);
               GameControl.addEntity(brick);
            } else if (c == 's') {
               ItemEntity SpeedItem = new SpeedItem(j, i, Sprite.POWERUP_SPEED);
               GameControl.addEntity(SpeedItem);
               StaticEntity brick = new Brick(j, i, Sprite.BRICK);
               GameControl.addEntity(brick);
            } else if (c == 'o') {
               ItemEntity BombPassItem = new BombPassItem(j, i, Sprite.POWERUP_BOMB_PASS);
               GameControl.addEntity(BombPassItem);
               StaticEntity brick = new Brick(j, i, Sprite.BRICK);
               GameControl.addEntity(brick);

            } else if (c == 'm') {
               ItemEntity FlamePassItem = new FlamePassItem(j, i, Sprite.POWERUP_FLAME_PASS);
               GameControl.addEntity(FlamePassItem);
               StaticEntity brick = new Brick(j, i, Sprite.BRICK);
               GameControl.addEntity(brick);
            } else if (c == 'h') {
               ItemEntity HeartItem = new HealthUpItem(j, i, Sprite.POWERUP_HEALTH_UP);
               GameControl.addEntity(HeartItem);
               StaticEntity brick = new Brick(j, i, Sprite.BRICK);
               GameControl.addEntity(brick);
            } else if (c == 'w') {
               ItemEntity WallPassItem = new BrickPassItem(j, i, Sprite.POWERUP_WALL_PASS);
               GameControl.addEntity(WallPassItem);
               StaticEntity brick = new Brick(j, i, Sprite.BRICK);
               GameControl.addEntity(brick);
            }

            else {
               // Grass is already added at the beginning of the loop for every cell
            }
         }

      }

   }

   

}
