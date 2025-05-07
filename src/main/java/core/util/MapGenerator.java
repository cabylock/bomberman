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
      // Ensure dimensions are odd
      height = height % 2 == 0 ? height + 1 : height;
      width = width % 2 == 0 ? width + 1 : width;

      // Validate player count

      // Calculate densities based on level
      float enemyDensity = (float) Math.min(0.5 + (level * 1), 3.0);
      float brickDensity = (float) Math.min(0.5 + (level * 0.01), 0.6);
      float itemDensity = (float) Math.max(0.5 - (level * 0.02), 0.3);

      char[][] map = new char[height][width];

      // Fill map with grass (spaces)
      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            map[y][x] = ' ';
         }
      }

      // Add border walls
      for (int y = 0; y < height; y++) {
         map[y][0] = '#';
         map[y][width - 1] = '#';
      }
      for (int x = 0; x < width; x++) {
         map[0][x] = '#';
         map[height - 1][x] = '#';
      }

      // Create grid pattern walls
      for (int y = 2; y < height - 1; y += Util.randomRange(2, 4)) {
         for (int x = 2; x < width - 1; x += Util.randomRange(2, 4)) {
            map[y][x] = '#';
         }
      }
      int playerCount = 2;

      // Define safe zones: 4 corners (each as a 3x3 area)
      boolean[][] safeZone = new boolean[height][width];
      int safeRadius = 1; // 3x3 area

      int[][] corners = {
            { 1, 1 }, // Top-left
            { 1, width - 2 }, // Top-right
            { height - 2, 1 }, // Bottom-left
            { height - 2, width - 2 } // Bottom-right
      };

      // Mark 3x3 safe zones at each corner
      for (int[] corner : corners) {
         for (int y = Math.max(1, corner[0] - safeRadius); y <= Math.min(height - 2, corner[0] + safeRadius); y++) {
            for (int x = Math.max(1, corner[1] - safeRadius); x <= Math.min(width - 2, corner[1] + safeRadius); x++) {
               safeZone[y][x] = true;
            }
         }
      }

      // Collect all possible safe zone positions (excluding walls)
      List<int[]> safePositions = new ArrayList<>();
      for (int y = 1; y < height - 1; y++) {
         for (int x = 1; x < width - 1; x++) {
            if (safeZone[y][x]) {
               safePositions.add(new int[] { y, x });
            }
         }
      }
      java.util.Collections.shuffle(safePositions, random);

      // Place players at random safe zone positions
      for (int i = 0; i < playerCount && i < safePositions.size(); i++) {
         int[] spawn = safePositions.get(i);
         char playerChar = (i == 0) ? 'p' : 'q'; // 'p' for player 1, 'q' for player 2
         map[spawn[0]][spawn[1]] = playerChar;
      }

      // Add bricks
      for (int y = 1; y < height - 1; y++) {
         for (int x = 1; x < width - 1; x++) {
            // Skip walls, players, and safe zones
            if (map[y][x] != ' ' || safeZone[y][x]) {
               continue;
            }

            if (random.nextFloat() < brickDensity) {
               map[y][x] = '*';
            }
         }
      }

      // Add portal under a brick (not near the start position)
      boolean portalPlaced = false;
      while (!portalPlaced) {
         int x = random.nextInt(width - 4) + 3;
         int y = random.nextInt(height - 2) + 1;

         if (map[y][x] == '*') {
            map[y][x] = 'x';
            portalPlaced = true;
         }
      }

      // Add items under bricks
      String[] items = { "b", "f", "s", "m", "h", "o", "w", "x" };
      int totalBricks = countCharacter(map, '*');
      int itemCount = (int) (totalBricks * itemDensity);
      int itemsPlaced = 0;

      // Tính số lượng mỗi item cần đặt
      int itemsPerType = itemCount / items.length;

      while (itemsPlaced < itemCount) {
         int x = random.nextInt(width - 2) + 1;
         int y = random.nextInt(height - 2) + 1;

         if (map[y][x] == '*') {
            String item = items[random.nextInt(items.length)];

            // Đảm bảo mỗi item được phân bổ đều
            int countForItem = 0;
            while (countForItem < itemsPerType && map[y][x] == '*') {
               map[y][x] = item.charAt(0);
               itemsPlaced++;
               countForItem++;
            }
         }
      }

      // Add enemies
      int totalSpace = countCharacter(map, ' ');
      int enemyCount = Math.min((int) (totalSpace * enemyDensity), level + 5);
      int enemiesPlaced = 0;

      while (enemiesPlaced < enemyCount) {
         int x = random.nextInt(width - 2) + 1;
         int y = random.nextInt(height - 2) + 1;

         if (map[y][x] == ' ' && !safeZone[y][x]) {
            // Restrict enemy types by level (max 5 types)
            int maxEnemyType = Math.min(level, 5);
            float[] enemyRatios = new float[maxEnemyType];
            for (int i = 0; i < maxEnemyType; i++) {
               enemyRatios[i] = 1.0f / maxEnemyType;
            }
            float totalRatio = 1.0f;

            // Chọn một quái vật ngẫu nhiên theo tỷ lệ phân phối
            float randomChoice = random.nextFloat() * totalRatio;
            char enemyLevel = '1'; // Mặc định là loại 1
            float cumulativeRatio = 0;

            for (int i = 0; i < enemyRatios.length; i++) {
               cumulativeRatio += enemyRatios[i];
               if (randomChoice < cumulativeRatio) {
                  enemyLevel = (char) ('1' + i); // '1', '2', ..., up to maxEnemyType
                  break;
               }
            }

            map[y][x] = enemyLevel;
            enemiesPlaced++;
         }
      }

      // Save the map to file
      saveMap(level, height, width, map, name);
   }

   private static int countCharacter(char[][] map, char target) {
      int count = 0;
      for (char[] row : map) {
         for (char cell : row) {
            if (cell == target)
               count++;
         }
      }
      return count;
   }

   private static void saveMap(int level, int height, int width, char[][] map, String name) {
      String filePath = "src/main/resources/custom_levels/" + name;

      try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
         // Write header with level, rows, columns
         writer.println(level + " " + height + " " + width);

         // Write map content
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
