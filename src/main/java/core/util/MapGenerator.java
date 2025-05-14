package core.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class MapGenerator {
   private static final Random random = new Random();

   public static void generateMap(int level, int height, int width, String name) {

      char[][] map = new char[height][width];
      int[][] safeZone = new int[height][width];

      int area = height * width;
      int safeZoneArea = 2;
      int numberOfWalls = (int) (area * 0.1);
      int numberOfBricks = (int) (area * 0.4);
      int numberOfEnemy = (int) (area * 0.1) + level;
      int numberOfItem = (int) (area * 0.2);
      int numberOfPortals = (int) (area * 0.05) + level;

      for (int y = 0; y < height; y++) {
         map[y][0] = '#';
         map[y][width - 1] = '#';

      }
      for (int x = 0; x < width; x++) {
         map[0][x] = '#';
         map[height - 1][x] = '#';
      }
      safeZone[1][1] = 1;
      safeZone[height - 2][width - 2] = 1;
      safeZone[1][width - 2] = 1;
      safeZone[height - 2][1] = 1;

      int spawnX1 = random.nextInt(width - 2) + 1;
      int spawnY1 = random.nextInt(height - 2) + 1;
      int spawnX2 = random.nextInt(width - 2) + 1;
      int spawnY2 = random.nextInt(height - 2) + 1;
      map[spawnY1][spawnX1] = 'p';
      map[spawnY2][spawnX2] = 'q';

      safeZone = createSafeZoneArea(spawnX2, spawnY2, safeZoneArea, safeZone);
      safeZone = createSafeZoneArea(spawnX1, spawnY1, safeZoneArea, safeZone);
      safeZone = createSafeZoneArea(1, 1, safeZoneArea, safeZone);
      safeZone = createSafeZoneArea(width - 2, height - 2, safeZoneArea, safeZone);
      safeZone = createSafeZoneArea(1, width - 2, safeZoneArea, safeZone);
      safeZone = createSafeZoneArea(height - 2, 1, safeZoneArea, safeZone);

      for (int i = 0; i < numberOfWalls; i++) {
         int x = random.nextInt(width - 2) + 1;
         int y = random.nextInt(height - 2) + 1;

         if (map[y][x] == '\u0000' && safeZone[y][x] == 0) {
            map[y][x] = '#';
         }
      }

      for (int i = 0; i < numberOfBricks; i++) {
         int x = random.nextInt(width - 2) + 1;
         int y = random.nextInt(height - 2) + 1;

         if (map[y][x] == '\u0000' && safeZone[y][x] == 0) {
            map[y][x] = '*';
         }
      }

      for (int i = 0; i <= numberOfPortals; i++) {
         int x = random.nextInt(width - 2) + 1;
         int y = random.nextInt(height - 2) + 1;

         if (map[y][x] == '\u0000') {
            map[y][x] = 'x';
         }
      }

      char[] itemTypes = { 'b', 'f', 's', 'm', 'h', 'o', 'w' };
      for (int i = 0; i < numberOfItem; i++) {
         int x = random.nextInt(width - 2) + 1;
         int y = random.nextInt(height - 2) + 1;

         if (map[y][x] == '\u0000' && safeZone[y][x] == 0) {
            char item = itemTypes[random.nextInt(itemTypes.length)];
            map[y][x] = item;
         }
      }

      int maxEnemyLevel = Math.min(5, level + 1);
      int maxEnemyType[] = new int[5];
      maxEnemyType[0] = 99;
      maxEnemyType[1] = 5;
      maxEnemyType[2] = 6;
      maxEnemyType[3] = 6;
      maxEnemyType[4] = 2;
      for (int i = 0; i < numberOfEnemy; i++) {
         int x = random.nextInt(width - 2) + 1;
         int y = random.nextInt(height - 2) + 1;

         if (map[y][x] == '\u0000' && safeZone[y][x] == 0) {
            while (true) {
               int enemyType = random.nextInt(maxEnemyLevel);
               if (maxEnemyType[enemyType] > 0) {
                  maxEnemyType[enemyType]--;
                  map[y][x] = (char) ('1' + enemyType);
                  break;
               }
            }

         }
      }

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
      for (int i = -safeZoneArea; i <= safeZoneArea; i++) {
         for (int j = -safeZoneArea; j <= safeZoneArea; j++) {
            int newX = x + i;
            int newY = y + j;
            if (newX >= 0 && newX < safeZone[0].length && newY >= 0 && newY < safeZone.length) {
               safeZone[newY][newX] = 1;
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
