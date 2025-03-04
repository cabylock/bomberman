package core.entities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Map {
   private int width;
   private int height;
   private int level;
   private char[][] map;

   public Map(String filePath) {
      readMap(filePath);
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
      return map;
   }

   public void readMap(String filePath) {
      
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
         map = new char[height][width];
         
         // Rest of your code remains the same
         for (int i = 0; i < height; i++) {
            String line = br.readLine();
               
            
            for (int j = 0; j < width; j++) {
               map[i][j] = line.charAt(j);
            }
         }
         br.close();
         System.out.println("Map loaded successfully - Level: " + level + ", Size: " + width + "x" + height);
      } catch (java.io.IOException e) {
         e.printStackTrace();
      }
   }





   public static void main(String[] args) {
      // Try without leading slash first
      Map map = new Map("/levels/Level1.txt");
      for(int i =0 ; i<map.height; i++){
         for(int j = 0; j<map.width; j++){
            System.out.print(map.map[i][j]);
         }
         System.out.println();
      }
   }
}
