package core.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapGenerator {
   private static final Random random = new Random();

   public static void generateMap(int level, int height, int width, String name) {

      char[][] map = new char[height][width];
      int[][] safeZone = new int[height][width];

      int area = height * width;
      int safeZoneArea = 2;
      int numberOfWalls = (int) (area * 0.2);
      int numberOfBricks = (int) (area * 0.3);
      int numberOfEnemy = (int) (area * 0.1);
      int numberOfItem = (int) (area * 0.2);
      int numberOfPortals = Math.min(4, level + 1);

      // border walls
      for (int y = 0; y < height; y++) {
         map[y][0] = '#';
         map[y][width - 1] = '#';

      }
      for (int x = 0; x < width; x++) {
         map[0][x] = '#';
         map[height - 1][x] = '#';
      }
      // safe zone 4 corners points
      safeZone[1][1] = 1;
      safeZone[height - 2][width - 2] = 1;
      safeZone[1][width - 2] = 1;
      safeZone[height - 2][1] = 1;

      // random safezone points for players
      int spawnX1 = random.nextInt(width - 2) + 1;
      int spawnY1 = random.nextInt(height - 2) + 1;
      int spawnX2 = random.nextInt(width - 2) + 1;
      int spawnY2 = random.nextInt(height - 2) + 1;
      map[spawnY1][spawnX1] = 'p';
      map[spawnY2][spawnX2] = 'q';

      // safe zone area
      safeZone = createSafeZoneArea(spawnX2, spawnY2, safeZoneArea, safeZone);
      safeZone = createSafeZoneArea(spawnX1, spawnY1, safeZoneArea, safeZone);
      safeZone = createSafeZoneArea(1, 1, safeZoneArea, safeZone);
      safeZone = createSafeZoneArea(width - 2, height - 2, safeZoneArea, safeZone);
      safeZone = createSafeZoneArea(1, width - 2, safeZoneArea, safeZone);
      safeZone = createSafeZoneArea(height - 2, 1, safeZoneArea, safeZone);

      // random walls
      for (int i = 0; i < numberOfWalls; i++) {
         int x = random.nextInt(width - 2) + 1;
         int y = random.nextInt(height - 2) + 1;

         if (map[y][x] == '\u0000' && safeZone[y][x] == 0) {
            map[y][x] = '#';
         }
      }
      // random bricks
      for (int i = 0; i < numberOfBricks; i++) {
         int x = random.nextInt(width - 2) + 1;
         int y = random.nextInt(height - 2) + 1;

         if (map[y][x] == '\u0000' && safeZone[y][x] == 0) {
            map[y][x] = '*';
         }
      }

      // random portals
      for (int i = 0; i < numberOfPortals; i++) {
         int x = random.nextInt(width - 2) + 1;
         int y = random.nextInt(height - 2) + 1;

         if (map[y][x] == '\u0000' && safeZone[y][x] == 0) {
            map[y][x] = 'x';
         }
      }



      // Place items under bricks
      char[] itemTypes = { 'b', 'f', 's', 'm', 'h', 'o', 'w' }; // bomb, flame, speed, etc.
      for (int i = 0; i < numberOfItem; i++) {
         int x = random.nextInt(width - 2) + 1;
         int y = random.nextInt(height - 2) + 1;

         if (map[y][x] == '\u0000' && safeZone[y][x] == 0) {
            char item = itemTypes[random.nextInt(itemTypes.length)];
            map[y][x] = item;
         }
      }

      // Add enemies to empty spaces
      int maxEnemyLevel = Math.min(5, level+1); // Enemy levels from 1 to 5 based on current level
      for (int i = 0; i < numberOfEnemy; i++) {
         int x = random.nextInt(width - 2) + 1;
         int y = random.nextInt(height - 2) + 1;

         if (map[y][x] == '\u0000' && safeZone[y][x] == 0) {
            int enemyType = random.nextInt(maxEnemyLevel) + 1;
            map[y][x] = (char) ('0' + enemyType);
         }
      }

      // Fill remaining empty spaces with grass (space character)
      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            if (map[y][x] == '\u0000') {
               map[y][x] = ' ';
            }
         }
      }

      saveMap(level, height, width, map, name);
   }



   private static int[][] createSafeZoneArea(int x, int y, int safeZoneArea, int[][] safeZone) {
      for (int i = 0; i < safeZoneArea; i++) {
         for (int j = 0; j < safeZoneArea; j++) {
            if(i + y < safeZone.length && j + x < safeZone[0].length) {
               safeZone[i + y][j + x] = 1;
            }
         }
      }
      return safeZone;
   }

   private static void saveMap(int level, int height, int width, char[][] map, String name) {
      String filePath = "src/main/resources/custom_levels/" + name;

      try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
         writer.println(level + " " + height + " " + width);
         for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
               writer.print(map[y][x]);
            }
            writer.println();
         }
         System.out.println("Map successfully generated for Level " + level);
      } catch (IOException e) {
         System.err.println("Error saving map: " + e.getMessage());
      }
   }
}
