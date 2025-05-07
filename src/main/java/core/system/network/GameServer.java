package core.system.network;

import java.io.*;
import java.net.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import core.entity.background_entity.BackgroundEntity;
import core.entity.dynamic_entity.mobile_entity.Bomber;

import core.system.game.GameControl;
import core.system.setting.Setting;
import core.util.Util;

public class GameServer extends Thread {

   private ServerSocket serverSocket;
   private static List<ClientHandler> clients = new CopyOnWriteArrayList<>();
   public boolean isRunning = true;
   private String lastError = null; // Add field to track the last error

   // Add this constant for maximum clients
   private static final int MAX_CLIENTS = 4;

   public String getLastError() {
      return lastError;
   }

   public boolean startServer(int port) {

      // Add shutdown hook to ensure clean shutdown
      Runtime.getRuntime().addShutdownHook(new Thread(this::stopServer));

      try {
         // Create the server socket synchronously to catch binding issues immediately
         this.serverSocket = new ServerSocket(port);
         this.isRunning = true;
         this.start(); // Start the thread after successful socket creation
         Util.logInfo("Server started on port " + port);
         // Important to notify user of server start
         return true;
      } catch (BindException e) {
         // Specific handling for address already in use
         lastError = "Address already in use: Port " + port + " is not available";
         Util.logError(lastError);
         this.isRunning = false;
         return false;
      } catch (IOException e) {
         lastError = e.getMessage();
         Util.logError("Failed to start server: " + lastError);
         this.isRunning = false;
         // Important to notify user of server failure

         return false;
      }
   }

   public void broadcastGameState() {

      // Broadcast entity data to all clients

      broadcastData(GameControl.getBomberEntities(), Setting.NETWORK_BOMBER_ENTITIES);
      broadcastData(GameControl.getEnemyEntities(), Setting.NETWORK_ENEMY_ENTITIES);
      broadcastData(GameControl.getStaticEntities(), Setting.NETWORK_STATIC_ENTITIES);
      broadcastData(GameControl.getItemEntities(), Setting.NETWORK_ITEM_ENTITIES);
   }

   @Override
   public void run() {
      try {
         // Server socket is already created in startServer
         while (isRunning) {
            Socket clientSocket = serverSocket.accept();

            // Check if maximum clients limit reached
            if (clients.size() >= MAX_CLIENTS) {
               // Send rejection message to client
               try (ObjectOutputStream tempOut = new ObjectOutputStream(clientSocket.getOutputStream())) {
                  tempOut.writeUTF("SERVER_FULL");
                  tempOut.writeUTF("Server has reached maximum capacity of " + MAX_CLIENTS + " players.");
                  tempOut.flush();
                  clientSocket.close();
               }
               Util.logInfo("Connection rejected: Server full (" + MAX_CLIENTS + "/" + MAX_CLIENTS + ")");
            } else {
               // Accept the connection
               ClientHandler clientHandler = new ClientHandler(clientSocket);
               clientHandler.start();
               clients.add(clientHandler);

               Util.logInfo("Client connected: " + clientHandler.clientName +
                     " (" + clients.size() + "/" + MAX_CLIENTS + ")");

               // Notify all clients about the new connection
               broadcastMessage("Player joined: " + clientHandler.clientName);
               // Important game event - notify host

            }
         }
      } catch (IOException e) {
         this.isRunning = false;
         interrupt();
      }
   }

   public void stopServer() {
      if (!isRunning) {
         return; // Prevent multiple calls
      }

      isRunning = false;
      Util.logInfo("Shutting down server...");

      try {
         // First notify all clients
         for (ClientHandler client : clients) {
            try {
               client.sendMessage("Server is shutting down.");
               client.disconnect();
            } catch (Exception e) {
               // Just log and continue with next client
               Util.logError("Error disconnecting client: " + e.getMessage());
            }
         }

         // Clear client list
         clients.clear();

         // Finally close server socket
         if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
            Util.logInfo("Server socket closed.");
         }

         // Interrupt the server thread to unblock accept()
         this.interrupt();

         Util.logInfo("Server stopped successfully.");
      } catch (IOException e) {
         Util.logError("Error stopping server: " + e.getMessage());
      }
   }

   public void broadcastMessage(String message) {
      for (ClientHandler client : clients) {
         client.sendMessage(message);
      }
   }

   public void broadcastData(List<?> data, String type) {
      for (ClientHandler client : clients) {
         client.sendData(data, type);
      }
   }

   /**
    * Checks if a port is available without actually starting a server
    * 
    * @param port The port to check
    * @return True if port is available, false otherwise
    */
   public static boolean isPortAvailable(int port) {
      try (ServerSocket socket = new ServerSocket(port)) {
         // If we can open a server socket, the port is available
         socket.close();
         return true;
      } catch (IOException e) {
         // Port is in use or otherwise unavailable
         return false;
      }
   }

   private static class ClientHandler extends Thread {
      private Socket clientSocket;
      private ObjectInputStream in;
      private volatile boolean isRunning = true;
      private int receiveRetries = 5;

      private int id;
      private ObjectOutputStream out;
      private String clientName;
      private int clientPort;
      private String clientAddress;
      private final Object outLock = new Object(); // Add this lock for output stream synchronization

      public ClientHandler(Socket socket) {
         this.clientSocket = socket;
         this.clientAddress = socket.getInetAddress().getHostAddress();
         this.clientPort = socket.getPort();
         this.clientName = clientAddress + ":" + clientPort;
         try {
            // Critical: First create and flush the output stream before creating input
            // stream
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
            this.out.flush(); // This is crucial to avoid the "invalid type code: 00" error
            this.in = new ObjectInputStream(clientSocket.getInputStream());
         } catch (IOException e) {
            System.err.println("Error initializing streams for client: " + e.getMessage());
            disconnect();
         }
      }

      @Override
      public void run() {
         try {
            // First assign an ID
            id = Util.uuid();

            // Send ID to client - must be the first message
            
            synchronized (outLock) {
               out.reset();
               out.writeUTF("ID");
               out.writeInt(id);
               out.flush();
            }
            Util.logInfo("Sent ID " + id + " to client " + clientName);

            // Wait for client to send PLAYER_NAME message
            String nameType = in.readUTF();
            if (!"PLAYER_NAME".equals(nameType)) {
               Util.logError("Expected PLAYER_NAME from client, got: " + nameType);
               disconnect();
               return;
            }
            String playerName = in.readUTF();
            int playerId = in.readInt();
            Util.logInfo("Received player name from client: " + playerName + " (ID: " + playerId + ")");

            // Create a new bomber for this client with the correct player name
            Bomber clientBomber = new Bomber(1, 1, Bomber.BOMBER1, Bomber.BOMBER1, playerName);
            clientBomber.setId(id);
            GameControl.addEntity(clientBomber);

           

            // Notify other clients about new player
            for (ClientHandler client : clients) {
               if (client != this && client.isRunning) {
                  client.sendMessage("New player joined: " + playerName);
               }
            }

            // Send background entities - with careful error handling
            sendBackgroundData();

            while (isRunning) {
               if (clientSocket == null || clientSocket.isClosed()) {
                  disconnect();
                  break;
               }

               try {
                  receiveGameState();
                 
               } catch (Exception e) {
                  Util.logError("Client error (" + clientName + "): " + e.getMessage());
                  receiveRetries--;
                  if (receiveRetries <= 0) {
                     disconnect();
                     break;
                  }
               }
            }
         } catch (Exception e) {
            Util.logError("ClientHandler exception for " + clientName + ": " + e.getMessage());
            e.printStackTrace(); // Print stack trace for better debugging
            disconnect();
         }
      }

      /**
       * Send background data with careful error handling and retries
       */
      private void sendBackgroundData() {
         int retries = 3;
         while (retries > 0) {
            try {
               Util.logInfo("Sending background entities to client " + clientName);
               List<BackgroundEntity> bgEntities = GameControl.getBackgroundEntities();

             

               // Synchronize output stream writes
               synchronized (outLock) {
                  out.reset();
                  out.writeUTF(Setting.NETWORK_BACKGROUND_ENTITIES);
                  out.writeObject(bgEntities);
                  out.flush();
               }

               Util.logInfo("Sent " + bgEntities.size() + " background entities to " + clientName);

               // Wait to ensure client has time to process
               Thread.sleep(300);
               return; // Success
            } catch (Exception e) {
               Util.logError("Error sending background data to " + clientName + ": " + e.getMessage());
               e.printStackTrace();
               retries--;
               try {
                  Thread.sleep(500); // Wait before retrying
               } catch (InterruptedException ie) {
                  Thread.currentThread().interrupt();
               }
            }
         }
         Util.logError("Failed to send background data after multiple attempts");
      }

      public void disconnect() {
         isRunning = false;

         try {
            // First close streams
            if (out != null) {
               try {
                  out.close();
               } catch (Exception e) {
                  /* ignore */ }
            }

            if (in != null) {
               try {
                  in.close();
               } catch (Exception e) {
                  /* ignore */ }
            }

            // Then close socket
            Bomber clientBomber = GameControl.getBomberEntitiesMap().get(id);
            if (clientBomber != null) {
               GameControl.removeEntity(clientBomber);
            }
            if (clientSocket != null && !clientSocket.isClosed()) {
               clientSocket.close();
               Util.logInfo("Client " + clientName + " disconnected.");

               // Notify about disconnection
               for (ClientHandler client : clients) {
                  if (client != this && client.isRunning) {
                     client.sendMessage("Player left: " + clientName);
                  }
               }
               // Important game event - notify host

            }
         } catch (IOException e) {
            Util.logError("Error disconnecting client: " + e.getMessage());
         } finally {
            // Always remove from client list
            clients.remove(this);
         }
      }

      public void receiveGameState() {
         if (in == null || clientSocket == null || clientSocket.isClosed()) {
            return; // Don't try to read from closed streams
         }

         try {
            // Check if there's data available to read
            if (clientSocket.getInputStream().available() == 0) {
               // No data available yet
               return;
            }

            String message = in.readUTF();
            if (message == null) {
               Util.logError("Received null message from client " + clientName);
               return;
            }

            if ("STRING".equals(message)) {
               String clientMessage = in.readUTF();
               Util.logInfo("Received from " + clientName + ": " + clientMessage);
            } else if (Setting.NETWORK_BOMBER_ENTITIES.equals(message)) {
               try {
                  float deltaTime = in.readFloat();
                  String command = in.readUTF();
                  int id = in.readInt();

                  Util.logInfo("Command from client " + clientName + ": " + command + " for ID " + id);

                  // Safety check before processing command
                  if (GameControl.getBomberEntitiesMap().containsKey(id)) {
                     GameControl.getBomberEntitiesMap().get(id).control(command, deltaTime);
                  } else {
                     Util.logError("Client " + clientName + " tried to control non-existent bomber ID: " + id);
                  }
               } catch (Exception e) {
                  Util.logError("Error processing bomber command: " + e.getMessage());
               }
            } else if ("PLAYER_NAME".equals(message)) {
               try {
                  String name = in.readUTF();
                  int id = in.readInt();

                  Util.logInfo("Client " + clientName + " setting name: " + name + " for ID " + id);

                  // Safety check before setting name
                  if (GameControl.getBomberEntitiesMap().containsKey(id)) {
                     GameControl.getBomberEntitiesMap().get(id).setName(name);
                     Util.logInfo("Client " + clientName + " set name to: " + name);
                  } else {
                     Util.logError("Client " + clientName + " tried to set name for non-existent bomber ID: " + id);
                  }
               } catch (Exception e) {
                  Util.logError("Error processing name change: " + e.getMessage());
               }
            } else {
               Util.logError("Unknown message type from client " + clientName + ": " + message);
            }

            // Reset retry counter on successful receive
            receiveRetries = 5;

         } catch (IOException e) {
            String errorMsg = (e.getMessage() != null) ? e.getMessage() : "Connection error";
            Util.logError("Error receiving from " + clientName + ": " + errorMsg);
            receiveRetries--;

           

            if (receiveRetries <= 0) {
               Util.logError("Max retries reached. Disconnecting " + clientName);
               disconnect();
            }
         }
      }

      public void sendMessage(String message) {
         try {
            synchronized (outLock) {
               out.reset(); // Reset the stream to avoid memory issues
               out.writeUTF("STRING"); // Indicate the type of message
               out.writeUTF(message); // Send the message
               out.flush(); // Ensure the data is sent immediately
            }
         } catch (IOException e) {
            System.err.println("Error sending message: " + e.getMessage());

         }
      }


      public synchronized <T> void sendData(List<T> data, String type) {
         if (!isRunning || out == null || data == null || clientSocket == null || clientSocket.isClosed())
            return;

         // Don't send background entities through the standard broadcast
         if (Setting.NETWORK_BACKGROUND_ENTITIES.equals(type)) {
            return;
         }

         try {
            synchronized (outLock) {
               out.reset();
               out.writeUTF(type);
               out.writeObject(data);
               out.flush();
            }

            // Small delay to prevent overwhelming client - but not too long
            Thread.sleep(5);
         } catch (IOException e) {
            String errorMsg = (e.getMessage() != null) ? e.getMessage() : "Connection error";
            Util.logError("Error sending data to " + clientName + ": " + errorMsg);
            receiveRetries--;
            if (receiveRetries <= 0) {
               disconnect();
            }
         } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
         }
      }

   }

   
}
