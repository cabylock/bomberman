package core.system.network;

import java.net.Socket;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.List;
import core.entity.background_entity.BackgroundEntity;
import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.entity.dynamic_entity.mobile_entity.enemy_entity.EnemyEntity;
import core.entity.dynamic_entity.static_entity.StaticEntity;
import core.entity.item_entity.ItemEntity;
import core.system.game.GameControl;
import core.system.setting.Setting;
import core.util.Util;

public class GameClient extends Thread {

   private String serverAddress;
   private int serverPort;
   private ObjectOutputStream out;
   private ObjectInputStream in;
   private Socket serverSocket;
   private final int MAX_RETRIES = 5;
   private final int RETRY_DELAY = 2000; // in milliseconds
   private int receiveRetries = 5;
   private String lastError = null; // Add field to track the last error

   public GameClient(String serverAddress, int serverPort) {
      this.serverAddress = serverAddress;
      this.serverPort = serverPort;

      Runtime.getRuntime().addShutdownHook(new Thread(this::disconnect));
   }

   public String getLastError() {
      return lastError;
   }

   public boolean connect() {
      int retries = 0;
      while (retries < MAX_RETRIES) {
         try {
            // Connect to server
            serverSocket = new Socket(serverAddress, serverPort);
            serverSocket.setSoTimeout(10000);

            Util.logInfo("Socket connected to " + serverAddress + ":" + serverPort);

            // Initialize streams
            out = new ObjectOutputStream(serverSocket.getOutputStream());
            out.flush();

            in = new ObjectInputStream(serverSocket.getInputStream());

            // First message should be ID
            String message = in.readUTF();
            Util.logInfo("Received initial message: " + message);

            if ("ID".equals(message)) {
               int id = in.readInt();
               Setting.ID = id;
               Util.logInfo("Joined game as Player #" + id);

             

               // Wait a brief moment to ensure the server processes the player name
               Thread.sleep(100);

               // Use a separate method to handle the initial background data
               if (!receiveInitialData()) {
                  cleanupConnection();
                  lastError = "Failed to receive initial game data";
                  Util.logError(lastError);
                  return false;
               }

               // Only start client receive thread after successful initialization
               this.start();
               return true;
            } else if ("SERVER_FULL".equals(message)) {
               String reason = in.readUTF();
               lastError = reason;
               Util.logError("Cannot connect: " + reason);
               cleanupConnection();
               return false;
            } else {
               // Unexpected first message
               Util.logError("Unexpected first message from server: " + message);
               cleanupConnection();
               return false;
            }
         } catch (IOException e) {
            lastError = "Connection failed: " + e.getMessage();
            Util.logError(lastError + ". Retrying... (" + (retries + 1) + "/" + MAX_RETRIES + ")");
            cleanupConnection();

            retries++;
            if (retries < MAX_RETRIES) {
               try {
                  Thread.sleep(RETRY_DELAY);
               } catch (InterruptedException ie) {
                  Thread.currentThread().interrupt();
               }
            } else {
               lastError = "Failed to connect after " + MAX_RETRIES + " attempts";
               Util.logError("Out of retries. " + lastError);
               return false;
            }
         } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            lastError = "Connection interrupted";
            return false;
         }
      }
      return false;
   }

   /**
    * Handles the initial data received from the server (background entities)
    * This is separate from the regular message loop to ensure proper
    * initialization
    */
   private boolean receiveInitialData() {
      try {
         // Wait for background entities with timeout
         int waitTime = 0;
         int maxWaitTime = 5000; // 5 seconds max

         while (waitTime < maxWaitTime) {
            // Always try to read the type (blocking if necessary)
            String type = in.readUTF();
            Util.logInfo("Received initial data type: " + type);

            if (Setting.NETWORK_BACKGROUND_ENTITIES.equals(type)) {
               try {
                  Object bgData = in.readObject();
                  if (bgData == null) {
                     Util.logError("Received null background data");
                     // Wait and retry in case server is still preparing data
                     waitTime += 50; // Reduced from 200ms to 50ms
                     Thread.sleep(50);
                     continue;
                  }

                  // Log success but don't process until we're sure it worked
                  Util.logInfo("Received background entities, processing...");
                  GameControl.setBackgroundEntities((List<BackgroundEntity>) bgData);
                  Util.logInfo("Background entities processed successfully");
                  return true;
               } catch (ClassNotFoundException | ClassCastException e) {
                  Util.logError("Error processing background data: " + e.getMessage());
                  return false;
               }
            } else {
               // If we receive a string that is not the expected type, skip it and try again
               Util.logError("Expected background entities but received: " + type);
               // Try to skip any unexpected data
               try {
                  in.readObject();
               } catch (Exception skip) {
                  // Ignore, just try next
               }
               waitTime += 20; // Reduced from 100ms to 20ms
               Thread.sleep(20);
            }
         }

         Util.logError("Timeout waiting for background data from server");
         return false;
      } catch (Exception e) {
         Util.logError("Error receiving initial data: " + e.getMessage());
         return false;
      }
   }

   private void cleanupConnection() {
      // Helper method to clean up resources
      try {
         if (in != null) {
            in.close();
            in = null;
         }
         if (out != null) {
            out.close();
            out = null;
         }
         if (serverSocket != null) {
            serverSocket.close();
            serverSocket = null;
         }
      } catch (IOException e) {
         Util.logError("Error cleaning up connection: " + e.getMessage());
      }
   }

   public void disconnect() {
      try {
         if (serverSocket != null && !serverSocket.isClosed()) {
            // First close streams to avoid socket exceptions
            if (out != null) {
               out.close();
               out = null;
            }
            if (in != null) {
               in.close();
               in = null;
            }

            // Then close socket
            serverSocket.close();
            serverSocket = null;
            Util.logInfo("Disconnected from server");
         }
      } catch (IOException e) {
         Util.logError("Error disconnecting from server: " + e.getMessage());
      } finally {
         // Make sure thread is interrupted
         this.interrupt();
      }
   }

   @Override
   public void run() {
      receiveRetries = MAX_RETRIES; // Reset retry counter

      try {
         // Loop while socket is open and thread not interrupted
         while (!isInterrupted() && serverSocket != null && !serverSocket.isClosed()) {
            try {
               if (in != null) {
                  receiveGameState();
               }
               // Small delay to prevent CPU hogging
               Thread.sleep(5); // Reduced from 20ms to 5ms for faster polling
            } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
               break;
            } catch (Exception e) {
               String errorMsg = (e.getMessage() != null) ? e.getMessage() : e.getClass().getName();
               Util.logError("Error in client thread: " + errorMsg);
               receiveRetries--;
               if (receiveRetries <= 0) {
                  Util.logError("Max retries reached. Disconnecting...");
                  break;
               }
               // Wait before retrying

            }
         }
      } catch (Exception e) {
         String errorMsg = (e.getMessage() != null) ? e.getMessage() : e.getClass().getName();
         Util.logError("Unexpected client error: " + errorMsg);
      } finally {
         disconnect(); // Clean up resources
      }
   }

   public void sendCommand(String command, int id) {
      if (out == null || serverSocket == null || serverSocket.isClosed()) {
         return;
      }

      try {
         synchronized (out) {
            out.reset();
            // Fix: Use the correct constant as defined in Setting.java
            out.writeUTF(Setting.NETWORK_BOMBER_ENTITIES);
            out.writeFloat(GameControl.getDeltaTime());
            out.writeUTF(command);
            out.writeInt(id);
            out.flush();
         }
      } catch (IOException e) {
         // Only log if this isn't a normal disconnect
         if (serverSocket != null && !serverSocket.isClosed()) {
            Util.logError("Error sending command: " + e.getMessage());
            disconnect(); // Close connection on error
         }
      }
   }

   public void receiveGameState() {
      if (in == null || (serverSocket != null && serverSocket.isClosed())) {
         return; // Don't try to read from a closed stream
      }

      try {
         // First check if there's actually data available to read
         if (serverSocket.getInputStream().available() == 0) {
            // No data available yet, just return and try again later
            return;
         }

         String type = in.readUTF();
         if (type == null) {
            Util.logError("Received null message type from server");
            return;
         }

         switch (type) {
            case "STRING":
               String message = in.readUTF();
               Util.logInfo("Server: " + message);
               receiveRetries = MAX_RETRIES; // Reset retry counter on success
               break;

            case Setting.NETWORK_BOMBER_ENTITIES:
            case Setting.NETWORK_ENEMY_ENTITIES:
            case Setting.NETWORK_STATIC_ENTITIES:
            case Setting.NETWORK_ITEM_ENTITIES:
               try {
                  Object obj = in.readObject();
                  if (obj == null) {
                     Util.logError("Received null object data for type: " + type);
                     return;
                  }
                  handleEntityData(type, obj);
                  receiveRetries = MAX_RETRIES; // Reset retry counter on success
               } catch (ClassNotFoundException e) {
                  Util.logError("Error deserializing: " + e);
                  e.printStackTrace(); // Print full stack trace for debugging
               } catch (ClassCastException e) {
                  Util.logError("Invalid data format for " + type + ": " + e);
                  e.printStackTrace(); // Print full stack trace for debugging
               }
               break;

            default:
               Util.logError("Unknown message type: " + type);
         }
      } catch (IOException e) {
         // More specific error handling
         if (e.getMessage() == null) {
            Util.logError("Network IO error: Connection may have been reset");
         } else {
            Util.logError("Network error: " + e.getMessage());
         }
         receiveRetries--;
      } catch (Exception e) {
         Util.logError(
               "Unexpected error in receive: " + (e.getMessage() != null ? e.getMessage() : e.getClass().getName()));
         receiveRetries--;
      }
   }

   private void handleEntityData(String type, Object obj) {
      try {
         // Fix: Use the actual constants from Setting.java
         if (type.equals(Setting.NETWORK_BOMBER_ENTITIES)) {
            GameControl.setBomberEntities((List<Bomber>) obj);
         } else if (type.equals(Setting.NETWORK_ENEMY_ENTITIES)) {
            GameControl.setEnemyEntities((List<EnemyEntity>) obj);
         } else if (type.equals(Setting.NETWORK_STATIC_ENTITIES)) {
            GameControl.setStaticEntities((List<StaticEntity>) obj);
         } else if (type.equals(Setting.NETWORK_ITEM_ENTITIES)) {
            GameControl.setItemEntities((List<ItemEntity>) obj);
         } else if (type.equals(Setting.NETWORK_BACKGROUND_ENTITIES)) {
            GameControl.setBackgroundEntities((List<BackgroundEntity>) obj);
         }
      } catch (ClassCastException e) {
         Util.logError("Error processing entity data: " + e.getMessage());
      }
   }

}
