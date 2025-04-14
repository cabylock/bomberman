package core.system.network;

import java.io.*;
import java.net.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import core.entity.dynamic_entity.mobile_entity.Bomber;

import core.system.game.GameControl;
import core.system.setting.Setting;
import core.util.Util;

public class GameServer extends Thread {

   private ServerSocket serverSocket;
   private static List<ClientHandler> clients = new CopyOnWriteArrayList<>();
   public boolean isRunning = true;

   // Add this constant for maximum clients
   private static final int MAX_CLIENTS = 4;

   public boolean startServer(int port) {
     

      // Add shutdown hook to ensure clean shutdown
      Runtime.getRuntime().addShutdownHook(new Thread(this::stopServer));

      try {
         // Create the server socket synchronously to catch binding issues immediately
         this.serverSocket = new ServerSocket(port);
         this.isRunning = true;
         this.start(); // Start the thread after successful socket creation
         System.out.println("Server started on port " + port);
         return true;
      } catch (IOException e) {
         System.err.println("Failed to start server: " + e.getMessage());
         this.isRunning = false;
         Util.showNotificationWindow("Cannot start server: " + e.getMessage());
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
               System.out.println("Connection rejected: Server full (" + MAX_CLIENTS + "/" + MAX_CLIENTS + ")");
            } else {
               // Accept the connection
               ClientHandler clientHandler = new ClientHandler(clientSocket);
               clientHandler.start();
               clients.add(clientHandler);

               System.out.println("Client connected: " + clientHandler.clientName +
                     " (" + clients.size() + "/" + MAX_CLIENTS + ")");
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
      System.out.println("Shutting down server...");

      try {
         // First notify all clients
         for (ClientHandler client : clients) {
            try {
               client.sendMessage("Server is shutting down.");
               client.disconnect();
            } catch (Exception e) {
               // Just log and continue with next client
               System.err.println("Error disconnecting client: " + e.getMessage());
            }
         }

         // Clear client list
         clients.clear();

         // Finally close server socket
         if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
            System.out.println("Server socket closed.");
         }

         System.out.println("Server stopped successfully.");
      } catch (IOException e) {
         System.err.println("Error stopping server: " + e.getMessage());
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

      public ClientHandler(Socket socket) {
         this.clientSocket = socket;
         this.clientAddress = socket.getInetAddress().getHostAddress();
         this.clientPort = socket.getPort();
         this.clientName = clientAddress + ":" + clientPort;
         try {
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
            this.in = new ObjectInputStream(clientSocket.getInputStream());
         } catch (IOException e) {
            System.err.println("Error initializing streams for client: " + e.getMessage());
            disconnect();
         }
      }

      @Override
      public void run() {
         id = Util.uuid();
         sendCommand("ID", id);
         Bomber clientBomber = new Bomber(1, 1, Setting.BOMBER1, Setting.BOMBER1, clientName);
         clientBomber.setId(id);
         GameControl.addEntity(clientBomber);

         sendData(GameControl.getBackgroundEntities(), Setting.NETWORK_BACKGROUND_ENTITIES);
         while (isRunning) {
            try {
               if (clientSocket == null || clientSocket.isClosed()) {
                  disconnect();
                  break;
               }
               receiveGameState();
            } catch (Exception e) {
               System.err.println("Error receiving data from client: " + e.getMessage());

            }

         }

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
            GameControl.removeEntity(GameControl.getBomberEntities().get(id));
            if (clientSocket != null && !clientSocket.isClosed()) {
               clientSocket.close();
               System.out.println("Client " + clientName + " disconnected.");
            }
         } catch (IOException e) {
            System.err.println("Error disconnecting client: " + e.getMessage());
         } finally {
            // Always remove from client list
            clients.remove(this);
         }
      }

      public void receiveGameState() {
         try {

            String message = in.readUTF();
            if ("STRING".equals(message)) {
               message = in.readUTF();
               System.out.println("Received message from " + clientName + ": " + message);

            } else if (Setting.NETWORK_BOMBER_ENTITIES.equals(message)) {
               float deltaTime = in.readFloat();
               String command = in.readUTF();
               int id = in.readInt();
               GameControl.getBomberEntitiesMap().get(id).control(command, deltaTime);

            }
            else if("PLAYER_NAME".equals(message)) {
               String name = in.readUTF();
               int id = in.readInt();
               GameControl.getBomberEntitiesMap().get(id).setName(name);
            }
            

         } catch (IOException e) {
            System.err.println("Error receive " + clientName + ": " + e.getMessage());
            receiveRetries--;
            if (receiveRetries <= 0) {
               System.err.println("Max retries reached. Disconnecting " + clientName + "...");
               disconnect();
            }
         }
      }

      public void sendMessage(String message) {
         try {
            out.reset(); // Reset the stream to avoid memory issues
            out.writeUTF("STRING"); // Indicate the type of message
            out.writeUTF(message); // Send the message
            out.flush(); // Ensure the data is sent immediately
         } catch (IOException e) {
            System.err.println("Error sending message: " + e.getMessage());

         }
      }

      public void sendCommand(String command, int id) {
         try {
            out.reset(); // Reset the stream to avoid memory issues
            out.writeUTF(command); // Send the command
            out.writeInt(id); // Send the ID
            out.flush(); // Ensure the data is sent immediately
         } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
         }
      }

      public synchronized <T> void sendData(List<T> data, String type) {
         try {
            out.reset(); // Reset the stream to avoid memory issues
            out.writeUTF(type); // Indicate the type of message
            out.writeObject(data); // Serialize and send the object
            out.flush(); // Ensure the data is sent immediately
         } catch (IOException e) {
            System.err.println("Error sending object: " + e.getMessage());
            disconnect();
         }
      }

   }

   // public static void main(String[] args) {
   // GameServer server = new GameServer();
   // server.startServer(8080);

   // Scanner scanner = new Scanner(System.in);
   // while (true) {
   // System.out.println("Enter 'stop' to stop the server:");
   // String command = scanner.nextLine();
   // if (command.equalsIgnoreCase("stop")) {
   // server.stopServer();
   // scanner.close();
   // break;
   // }
   // server.broadcastMessage(command);
   // }
   // }
}
