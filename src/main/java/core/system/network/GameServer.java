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
   private static List<ClientHandler> clients = new CopyOnWriteArrayList<>();
   private boolean isRunning = true;
   private int  initialTime = 20;

   private ScheduledExecutorService broadcastScheduler;

   public void startServer(int port) {
      this.port = port;
      this.start();
      System.out.println("Server started on port " + port);

      // Start auto-broadcasting
      broadcastGameState();

   }

   public void broadcastGameState() {

      // Broadcast entity data to all clients

      broadcastData(GameControl.getBomberEntities(), Setting.NETWORK_BOMBER_ENTITIES);
      // System.out.println("Broadcasting bomber entities: " +
      // bomberEntities.get(0).getX() + " " + bomberEntities.get(0).getY());
      broadcastData(GameControl.getEnemyEntities(), Setting.NETWORK_ENEMY_ENTITIES);
      broadcastData(GameControl.getStaticEntities(), Setting.NETWORK_STATIC_ENTITIES);
      broadcastData(GameControl.getItemEntities(), Setting.NETWORK_ITEM_ENTITIES);
      if (initialTime >= 0) {
         broadcastData(GameControl.getBackgroundEntities(), Setting.NETWORK_BACKGROUND_ENTITIES);
         initialTime --; 
      }

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
            disconnect();
         }
      }

      @Override
      public  void run() {
         while (isRunning) {
            try {
               if(clientSocket == null || clientSocket.isClosed()) {
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
            if (clientSocket != null && !clientSocket.isClosed()) {
               clientSocket.close();
               in.close();
               out.close();
               
               System.out.println("Client " + clientName + " disconnected.");

            }
         } catch (IOException e) {
            System.err.println("Error disconnecting client: " + e.getMessage());
         }
         finally {
            clients.remove(this);
         }
      }

      public void receiveGameState() {
         try {

            String message = in.readUTF();
            if ("STRING".equals(message)) {
               message = in.readUTF();
               System.out.println("Client " + clientName + " sent: " + message);
            } else if (Setting.NETWORK_BOMBER_ENTITIES.equals(message)) {
               Object obj = in.readObject();
               GameControl.setBomberEntities((List<Bomber>) obj);
            } else if (Setting.NETWORK_ENEMY_ENTITIES.equals(message)) {
               Object obj = in.readObject();
               GameControl.setEnemyEntities((List<EnemyEntity>) obj);

            } else if (Setting.NETWORK_STATIC_ENTITIES.equals(message)) {
               Object obj = in.readObject();
               GameControl.setStaticEntities((List<StaticEntity>) obj);

            } else if (Setting.NETWORK_ITEM_ENTITIES.equals(message)) {
               Object obj = in.readObject();
               GameControl.setItemEntities((List<ItemEntity>) obj);

            } else if (Setting.NETWORK_BACKGROUND_ENTITIES.equals(message)) {
               Object obj = in.readObject();
               GameControl.setBackgroundEntities((List<BackgroundEntity>) obj);

            }

         }
         catch (IOException e) {
            System.err.println("Error receive " + clientName + ": " + e.getMessage());
         } catch (ClassNotFoundException e) {
            System.err.println("Error reading object from client: " + e.getMessage());
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
