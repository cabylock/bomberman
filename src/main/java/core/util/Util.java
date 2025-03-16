package core.util;

import core.graphics.Sprite;
import java.util.Random;

public class Util {
   

   public static int toGrid(int x)
   {
      return Math.round((x+ Sprite.SCALED_SIZE/2)/ Sprite.SCALED_SIZE);
   }

   public static int   randomDirection()
   {
      Random random = new Random();
      return random.nextInt(4);

   }

   public static int randomRange(int min, int max)
   {
      Random random = new Random();
      return random.nextInt(max - min + 1) + min;
   }

   /**
    * Generate a random map for the specified level
    * 
    * @param level Level number
    */
   public static void generateRandomMap(int level) {
      int width = 31; // Standard width
      int height = 13; // Standard height
      MapGenerator.generateMap(level, height, width);
   }

   /**
    * Generate a random map with custom dimensions
    * 
    * @param level  Level number
    * @param height Map height
    * @param width  Map width
    */
   public static void generateCustomMap(int level, int height, int width) {
      MapGenerator.generateMap(level, height, width);
   }

}
