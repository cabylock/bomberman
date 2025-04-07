package core.system.network;

import java.io.*;
import java.net.*;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import core.entity.background_entity.BackgroundEntity;
import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.entity.dynamic_entity.mobile_entity.enemy_entity.EnemyEntity;
import core.entity.dynamic_entity.static_entity.StaticEntity;
import core.entity.item_entity.ItemEntity;
import core.system.game.GameControl;
import core.system.setting.Setting;

public class GameServer extends Thread {

   private ServerSocket serverSocket;
   private int port;
   private List<ClientHandler> clients = new CopyOnWriteArrayList<>();
   private  boolean isRunning = true;

   private static List<Bomber> bomberEntities = GameControl.getBomberEntities();
   private static List<StaticEntity> staticEntities = GameControl.getStaticEntities();
   private static List<EnemyEntity> enemyEntities = GameControl.getEnemyEntities();
   private static List<ItemEntity> itemEntities = GameControl.getItemEntities();
   private static List<BackgroundEntity> backgroundEntities = GameControl.getBackgroundEntities();

   private ScheduledExecutorService broadcastScheduler;

   public void startServer(int port) {
      this.port = port;
      this.start();
      System.out.println("Server started on port " + port);

      // Start auto-broadcasting
      
   }

   public  void broadcastGameState() {
      
         // Broadcast entity data to all clients
         
         broadcastData(bomberEntities, Setting.NETWORK_BOMBER_ENTITIES);
         // System.out.println("Broadcasting bomber entities: " + bomberEntities.get(0).getX() + " " + bomberEntities.get(0).getY());
         broadcastData(staticEntities, Setting.NETWORK_STATIC_ENTITIES);
         broadcastData(enemyEntities, Setting.NETWORK_ENEMY_ENTITIES);
         broadcastData(itemEntities, Setting.NETWORK_ITEM_ENTITIES);
         broadcastData(backgroundEntities, Setting.NETWORK_BACKGROUND_ENTITIES);
      
   }


   public void updateGameState() {
      
   }

   @Override
   public void run() {
      try {
         serverSocket = new ServerSocket(port);

         while (isRunning) {
            Socket clientSocket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            clientHandler.start();
            clients.add(clientHandler);
            System.out.println("Client connected: " + clientHandler.clientName);
         }
      } catch (IOException e) {
         System.err.println("Error starting server: " + e.getMessage());
      }

   }

   public void stopServer() {
      isRunning = false;

      // Stop auto-broadcasting
      if (broadcastScheduler != null && !broadcastScheduler.isShutdown()) {
         broadcastScheduler.shutdown();
      }

      try {
         for (ClientHandler client : clients) {
            client.sendMessage("Server is shutting down.");
            client.disconnect();
            client.interrupt();
            client.clientSocket.close();
         }

         serverSocket.close();
         clients.clear();
         System.out.println("Server stopped.");

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
            out = null;
            in = null;
         }
      }

      @Override
      public  void run() {
         try {

            while (isRunning) {

               // Example of receiving a message from the client
               String message = in.readUTF();
               if ("STRING".equals(message)) {
                  message = in.readUTF();
                  System.out.println("Client " + clientName + " sent: " + message);
               } else if (Setting.NETWORK_BOMBER_ENTITIES.equals(message)) {
                  Object obj = in.readObject();
                  bomberEntities = (List<Bomber>) obj;

               } else if (Setting.NETWORK_ENEMY_ENTITIES.equals(message)) {
                  Object obj = in.readObject();
                  enemyEntities = (List<EnemyEntity>) obj;

               } else if (Setting.NETWORK_STATIC_ENTITIES.equals(message)) {
                  Object obj = in.readObject();
                  staticEntities = (List<StaticEntity>) obj;

               } else if (Setting.NETWORK_ITEM_ENTITIES.equals(message)) {
                  Object obj = in.readObject();
                  itemEntities = (List<ItemEntity>) obj;

               } else if (Setting.NETWORK_BACKGROUND_ENTITIES.equals(message)) {
                  Object obj = in.readObject();
                  backgroundEntities = (List<BackgroundEntity>) obj;

               }

            }

         } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error handling client: " + e.getMessage());
         }

         finally {
            disconnect();
         }
      }

      public void disconnect() {
         isRunning = false;
         try {
            if (clientSocket != null && !clientSocket.isClosed()) {
               clientSocket.close();
            }
         } catch (IOException e) {
            System.err.println("Error disconnecting client: " + e.getMessage());
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
            disconnect();
         }
      }

      public <T> void sendData(List<T> data, String type) {
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
