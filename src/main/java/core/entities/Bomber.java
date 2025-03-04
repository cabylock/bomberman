package core.entities;
import javafx.scene.image.Image;


public class Bomber extends Entity {
   

   private int speed = 1;
   private int direction = 0;
   private boolean moving = false;
   



   public Bomber(int x, int y, Image img) {
      super(x, y, img);
   }

   @Override
   public void update() {
      
   }
   
}
