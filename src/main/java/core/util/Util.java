package core.util;

import core.graphics.Sprite;

public class Util {
   

   public static int toGrid(int x)
   {
      return Math.round((x+ Sprite.SCALED_SIZE/2)/ Sprite.SCALED_SIZE);
   }

}
