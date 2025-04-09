
package core.system.network;

import java.net.Socket;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import core.entity.background_entity.BackgroundEntity;
import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.entity.dynamic_entity.mobile_entity.enemy_entity.EnemyEntity;
import core.entity.dynamic_entity.static_entity.StaticEntity;
import core.entity.item_entity.ItemEntity;
import core.system.game.GameControl;
import core.system.setting.Setting;

public class GameClient extends Thread {

   private String serverAddress;
   private int serverPort;
   private ObjectOutputStream out;
   private ObjectInputStream in;
   private Socket serverSocket;
   private final int MAX_RETRIES = 5;
   private final int RETRY_DELAY = 2000; // in milliseconds
   private int receiveRetries = 5;


   public GameClient(String serverAddress, int serverPort) {
      this.serverAddress = serverAddress;
      this.serverPort = serverPort;

   }

   public boolean connect() {
      int retries = 0;
      while (retries < MAX_RETRIES) {
         try {
            serverSocket = new Socket(serverAddress, serverPort);
            out = new ObjectOutputStream(serverSocket.getOutputStream());
            in = new ObjectInputStream(serverSocket.getInputStream());
            System.out.println("Connected to server at " + serverAddress + ":" + serverPort);
            receiveGameState();
            start();
            return true; // Exit the loop if connection is successful
         } catch (IOException e) {
            System.err.println("Error, Retry connect to server: " + retries + 1 + " time(s) " + e.getMessage());
            retries++;
            if (retries < MAX_RETRIES) {
               try {
                  Thread.sleep(RETRY_DELAY);
               } catch (InterruptedException ie) {
                  Thread.currentThread().interrupt();
               }
            } else {
               System.err.println("Out of time. Exiting...");
            }
         }
      }
      return false;

   }

   public void disconnect() {
      try {
         if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
         }
         System.out.println("Disconnected from server.");
      } catch (IOException e) {
         System.err.println("Error disconnecting from server: " + e.getMessage());
      }
   }

   @Override
   public void run() {

      while (isAlive()) {
         try {
            if (serverSocket == null || serverSocket.isClosed()) {
               System.out.println("Server socket is closed");
               break;
            }
            receiveGameState();
         } catch (Exception e) {
            System.err.println("Error receiving game state: " + e.getMessage());
            break;
         }
      }
   }

   // In GameClient.java
public void sendGameState() {
    // Only send entities owned by this client
    sendData(GameControl.getBomberEntities(), Setting.NETWORK_BOMBER_ENTITIES);
    
   // Send minimal data for other types
    // Only sending bombers and static entities is often sufficient
}

 

   public void receiveGameState() {
      try {
         String message = in.readUTF();
         if ("STRING".equals(message)) {
            message = in.readUTF();
            System.out.println("Server: " + message);

         } else if (Setting.NETWORK_BOMBER_ENTITIES.equals(message)) {
            Object obj = in.readObject();
            List<Bomber> bombers = (List<Bomber>) obj;
            GameControl.setBomberEntities(bombers);

         } else if (Setting.NETWORK_ENEMY_ENTITIES.equals(message)) {
            Object obj = in.readObject();
            List<EnemyEntity> enemies = (List<EnemyEntity>) obj;
            GameControl.setEnemyEntities(enemies);
         } else if(Setting.NETWORK_STATIC_ENTITIES.equals(message)) {
            Object obj = in.readObject();
            List<StaticEntity> staticEntities = (List<StaticEntity>) obj;
            GameControl.setStaticEntities(staticEntities);
         } else if (Setting.NETWORK_ITEM_ENTITIES.equals(message)) {
            Object obj = in.readObject();
            List<ItemEntity> items = (List<ItemEntity>) obj;
            GameControl.setItemEntities(items);
         } else if (Setting.NETWORK_BACKGROUND_ENTITIES.equals(message)) {
            Object obj = in.readObject();
            List<BackgroundEntity> backgroundEntities = (List<BackgroundEntity>) obj;
            GameControl.setBackgroundEntities(backgroundEntities);
         }


      } catch (IOException e) {
         System.err.println("Error receiving object: " + e.getMessage());
         receiveRetries--;
         if (receiveRetries <= 0) {
            System.err.println("Max retries reached. Disconnecting...");
            disconnect();
         }
      } catch (ClassNotFoundException e) {
         System.err.println("Error deserializing object: " + e.getMessage());
      } catch (Exception e) {
         System.err.println("Unexpected error: " + e.getMessage());
      }

   }

   public void sendMessage(String message) {
      try {
         out.reset(); // Reset the stream to avoid memory issues
         out.writeUTF("STRING"); // Indicate the type of data being sent
         out.writeUTF(message); // Send the message
         out.flush(); // Ensure the data is sent immediately
      } catch (IOException e) {
         System.err.println("Error sending message: " + e.getMessage());
      }
   }

   public <T> void sendData(List<T> data, String type) {
      try {
         out.reset();
         out.writeUTF(type); // Indicate the type of data being sent
         out.writeObject(data); // Serialize and send the object
         out.flush(); // Ensure the data is sent immediately
      } catch (IOException e) {
         System.err.println("Error sending object: " + e.getMessage());
      }
   }

   // public static void main(String[] args) {

   // String serverAddress = "localhost"; // Replace with the server's IP address
   // int serverPort = 8080;

   // GameClient client = new GameClient(serverAddress, serverPort);
   // client.connect();

   // Bomber bomber = new Bomber(0, 3, 0, 1);
   // client.sendData(List.of(bomber),"BOMBER");
   // // client.start();

   // }

}
