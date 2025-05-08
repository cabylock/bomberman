package core.system.network;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.ArrayList;
import core.util.Util;
import javafx.application.Platform;
import core.system.setting.Setting;
import core.system.game.BombermanGame;
import core.system.game.GameControl;
import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.entity.dynamic_entity.mobile_entity.enemy_entity.EnemyEntity;
import core.entity.dynamic_entity.static_entity.StaticEntity;
import core.entity.item_entity.ItemEntity;
import core.entity.background_entity.BackgroundEntity;
import core.entity.Entity;

public class GameClient {
   private static String serverAddress;
   private static int serverPort;
   private static ObjectOutputStream out;
   private static ObjectInputStream in;
   private static Socket socket;
   private static boolean isRunning = false;
   private static String errorMessage = null;
   private static Thread clientThread;
   private static int clientId;

   public static void initialize(String address, int port) {
      serverAddress = address;
      serverPort = port;
      errorMessage = null;
   }

   public static String getLastError() {
      return errorMessage;
   }

   public static boolean connect() {
      try {
         socket = new Socket(serverAddress, serverPort);

         // Initialize streams in correct order
         out = new ObjectOutputStream(socket.getOutputStream());
         out.flush(); // Flush the header
         in = new ObjectInputStream(socket.getInputStream());

         // Wait for initial data
         String messageType = in.readUTF();
         if (!messageType.equals(Setting.NETWORK_ID)) {
            throw new IOException("Expected ID message, got: " + messageType);
         }
         clientId = in.readInt();
         Setting.ID = clientId;
         Util.logInfo("Received client ID: " + clientId);

         // Receive map dimensions
         messageType = in.readUTF();
         if (!messageType.equals(Setting.NETWORK_MAP_DIMENSIONS)) {
            throw new IOException("Expected map dimensions, got: " + messageType);
         }
         int width = in.readInt();
         int height = in.readInt();
         GameControl.setWidth(width);
         GameControl.setHeight(height);


         messageType = in.readUTF();
         if (!messageType.equals(Setting.NETWORK_BACKGROUND_ENTITIES)) {
            throw new IOException("Expected background entities, got: " + messageType);
         }
         List<BackgroundEntity> backgroundEntities = (List<BackgroundEntity>) in.readObject();
         GameControl.setBackgroundEntities(backgroundEntities);


         isRunning = true;
         clientThread = new Thread(GameClient::run);
         clientThread.start();
         return true;
      } catch (Exception e) {
         errorMessage = e.getMessage();
         Util.logError("Connection error: " + e.getMessage());
         clearConnection();
         return false;
      }
   }

   private static void run() {
      try {
         while (isRunning) {
            if (socket == null || socket.isClosed()) {
               Util.logError("Socket closed unexpectedly");
               disconnect();
               break;
            }

            try {
               String messageType = in.readUTF();

               switch (messageType) {
                  case Setting.NETWORK_BOMBER_ENTITIES:
                     @SuppressWarnings("unchecked")
                     List<Bomber> bombers = (List<Bomber>) in.readObject();
                     
                     GameControl.setBomberEntities(bombers);
                     break;
                  case Setting.NETWORK_ENEMY_ENTITIES:
                     List<EnemyEntity> enemies = (List<EnemyEntity>) in.readObject();

                     GameControl.setEnemyEntities(enemies);
                     break;
                  case Setting.NETWORK_STATIC_ENTITIES:
                     List<StaticEntity> statics = (List<StaticEntity>) in.readObject();

                     GameControl.setStaticEntities(statics);
                     break;
                  case Setting.NETWORK_ITEM_ENTITIES:
                     List<ItemEntity> items = (List<ItemEntity>) in.readObject();

                     GameControl.setItemEntities(items);
                     break;
                  case Setting.NETWORK_BACKGROUND_ENTITIES:
                     List<BackgroundEntity> backgrounds = (List<BackgroundEntity>) in.readObject();
                     GameControl.setBackgroundEntities(backgrounds);
                     break;
                  case Setting.NETWORK_MAP_DIMENSIONS:
                     int width = in.readInt();
                     int height = in.readInt();
                     GameControl.setWidth(width);
                     GameControl.setHeight(height);
                     
                     break;
                  default:
                     Util.logError("Unknown message type: " + messageType);
               }

            } catch (ClassNotFoundException e) {
               Util.logError("Invalid data received: " + e.getMessage());
               disconnect();
               break;
            } catch (EOFException e) {
               Util.logError("Connection closed by server");
               disconnect();
               break;
            } catch (IOException e) {
               if (isRunning) {
                  Util.logError("IO error in game loop: " + e.getMessage());
                  disconnect();
               }
               break;
            }
         }
      } catch (Exception e) {
         Util.logError("Client error: " + e.getMessage());
         disconnect();
      }
   }

   public static void sendControl(int id, String control) {
      if (!isRunning || out == null || socket == null || socket.isClosed()) {
         return;
      }

      try {
         out.writeUTF(Setting.NETWORK_CONTROL);
         out.writeInt(id);
         out.writeUTF(control);
         out.flush();
      } catch (IOException e) {
         errorMessage = "Failed to send control: " + e.getMessage();
         Util.logError(errorMessage);
         disconnect();
      }
   }

   public static void disconnect() {
      isRunning = false;
      clearConnection();
      if (clientThread != null) {
         clientThread.interrupt();
      }
   }

   private static void clearConnection() {
      try {
         if (out != null) {
            out.close();
            out = null;
         }
         if (in != null) {
            in.close();
            in = null;
         }
         if (socket != null && !socket.isClosed()) {
            socket.close();
            socket = null;
         }
      } catch (IOException e) {
         Util.logError("Error cleaning up connection: " + e.getMessage());
      }
   }

}
