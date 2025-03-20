package core.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapGenerator {
   private static final Random random = new Random();

   /**
    * Generates a random Bomberman map based on level and dimensions
    * 
    * @param level       The level number (affects difficulty)
    * @param height      Map height
    * @param width       Map width
    * @param name        Map file name
    * @param playerCount Number of players (1 or 2)
    */
   public static void generateMap(int level, int height, int width, String name, int playerCount) {
      // Ensure dimensions are odd
      height = height % 2 == 0 ? height + 1 : height;
      width = width % 2 == 0 ? width + 1 : width;

      // Validate player count
      playerCount = Math.min(Math.max(playerCount, 1), 2);

      // Calculate densities based on level
      double enemyDensity = Math.min(0.4 + (level * 0.5), 2.0);
      double brickDensity = Math.min(0.3 + (level * 0.01), 0.6);
      double itemDensity = Math.max(0.7 - (level * 0.02), 0.3);

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

      // Create potential spawn points for players
      List<int[]> spawnPoints = generatePlayerSpawnPoints(height, width, playerCount);

      // Place players at spawn points
      for (int i = 0; i < playerCount && i < spawnPoints.size(); i++) {
         int[] spawn = spawnPoints.get(i);
         char playerChar = (i == 0) ? 'p' : 'q'; // 'p' for player 1, 'q' for player 2
         map[spawn[0]][spawn[1]] = playerChar;
      }

      // Define safe zones around players (no bricks or enemies)
      boolean[][] safeZone = new boolean[height][width];
      int safeRadius = 2;

      for (int[] spawn : spawnPoints) {
         for (int y = Math.max(1, spawn[0] - safeRadius); y <= Math.min(height - 2, spawn[0] + safeRadius); y++) {
            for (int x = Math.max(1, spawn[1] - safeRadius); x <= Math.min(width - 2, spawn[1] + safeRadius); x++) {
               safeZone[y][x] = true;
            }
         }
      }

      // Add bricks
      for (int y = 1; y < height - 1; y++) {
         for (int x = 1; x < width - 1; x++) {
            // Skip walls, players, and safe zones
            if (map[y][x] != ' ' || safeZone[y][x]) {
               continue;
            }

            if (random.nextDouble() < brickDensity) {
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
      String[] items = { "b", "f", "s" };
      int totalBricks = countCharacter(map, '*');
      int itemCount = (int) (totalBricks * itemDensity);
      int itemsPlaced = 0;

      while (itemsPlaced < itemCount) {
         int x = random.nextInt(width - 2) + 1;
         int y = random.nextInt(height - 2) + 1;

         if (map[y][x] == '*') {
            String item = items[random.nextInt(items.length)];
            map[y][x] = item.charAt(0);
            itemsPlaced++;
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
            // Higher levels get more Oneals (type 2)
            double onealsRatio = Math.min(0.3 + (level * 0.05), 0.8);
            char enemyType = random.nextDouble() < onealsRatio ? '2' : '1';
            map[y][x] = enemyType;
            enemiesPlaced++;
         }
      }

      // Save the map to file
      saveMap(level, height, width, map, name);
   }

   /**
    * Generate spawn points for players
    */
   private static List<int[]> generatePlayerSpawnPoints(int height, int width, int playerCount) {
      List<int[]> spawnPoints = new ArrayList<>();

      int[][] corners = {
            { 1, 1 }, // Top-left
            { 1, width - 2 }, // Top-right
            { height - 2, 1 }, // Bottom-left
            { height - 2, width - 2 } // Bottom-right
      };

      //randomly generate spawn points for players
      for (int i = 0; i < playerCount; i++) {
         

         spawnPoints.add(corners[i]);
      }
      return spawnPoints;
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
