package core.system.network;

import java.io.*;
import java.net.*;
import java.util.concurrent.CopyOnWriteArrayList;
import core.system.setting.Setting;
import core.util.Util;
import core.system.game.GameControl;
import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.graphics.Sprite;

public class GameServer {
   private static ServerSocket serverSocket;
   private static CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();
   private static boolean isRunning = false;
   private static final int MAX_CLIENTS = 4;
   private static String errorMessage = null;
   private static Thread serverThread;

   public static boolean createServer(int port) {
      try {
         if (!isPortAvailable(port)) {
            errorMessage = "Port " + port + " is already in use.";
            Util.logError(errorMessage);
            return false;
         }

         serverSocket = new ServerSocket(port);
         serverSocket.setReuseAddress(true); // Add reuse address option
         isRunning = true;

         Bomber serverBomber = new Bomber(1, 1, Sprite.PLAYER1_RIGHT_0, Bomber.BOMBER1, "Server");
         GameControl.addEntity(serverBomber);

         serverThread = new Thread(GameServer::startServer);
         serverThread.start();
         Util.logInfo("Server started on port " + port);
         return true;
      } catch (IOException e) {
         errorMessage = "Failed to start server: " + e.getMessage();
         Util.logError(errorMessage);
         return false;
      }
   }

   public static String getErrorMessage() {
      return errorMessage;
   }

   private static void startServer() {
      while (isRunning) {
         try {
            if (serverSocket == null || serverSocket.isClosed()) {
               isRunning = false;
               break;
            }

            if (clients.size() < MAX_CLIENTS) {
               Socket clientSocket = serverSocket.accept();
               clientSocket.setSoTimeout(5000); // Set socket timeout to 5 seconds
               ClientHandler clientHandler = new ClientHandler(clientSocket, Util.uuid());
               clientHandler.start();
               clients.add(clientHandler);
               Util.logInfo("Client connected: " + clientSocket.getInetAddress());
            }
         } catch (SocketException e) {
            if (isRunning) {
               Util.logError("Socket error: " + e.getMessage());
            }
            isRunning = false;
         } catch (IOException e) {
            Util.logError("IO error: " + e.getMessage());
         }
      }
      stopServer();
   }

   public static void stopServer() {
      isRunning = false;
      for (ClientHandler client : clients) {
         client.disconnect();
      }
      clients.clear();

      try {
         if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
         }
      } catch (IOException e) {
         Util.logError("Error closing server socket: " + e.getMessage());
      }

      if (serverThread != null) {
         serverThread.interrupt();
      }
   }

   private static boolean isPortAvailable(int port) {
      try (ServerSocket socket = new ServerSocket(port)) {
         socket.setReuseAddress(true);
         return true;
      } catch (IOException e) {
         return false;
      }
   }

   public static void broadcastMapDimensions() {
      if (!isRunning)
         return;

      for (ClientHandler client : clients) {
         try {
            client.sendMapDimensions();
         } catch (IOException e) {
            Util.logError("Error sending map dimensions: " + e.getMessage());
         }
      }
   }

   private static class ClientHandler extends Thread {
      private Socket clientSocket;
      private ObjectInputStream in;
      private ObjectOutputStream out;
      private boolean isRunning = true;
      private int clientId;
      private Bomber clientBomber;

      // Add timestamp tracking for rate limiting
      private long lastGameStateSentTime = 0;
      private static final long GAME_STATE_SEND_INTERVAL = 50; // Send at most 20 updates per second

      public ClientHandler(Socket socket, int id) {
         try {
            clientSocket = socket;
            clientSocket.setTcpNoDelay(true); // Enable TCP_NODELAY for lower latency
            clientSocket.setSoTimeout(5000); // Set read timeout to 5 seconds
            clientId = id;

            clientBomber = new Bomber(1, 1, Sprite.PLAYER1_RIGHT_0, Bomber.BOMBER1, "Client" + clientId);
            clientBomber.setId(clientId);
            GameControl.addEntity(clientBomber);

            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(clientSocket.getInputStream());

            sendId(id);
            sendMapDimensions();
            sendObject(Setting.NETWORK_BACKGROUND_ENTITIES, GameControl.getBackgroundEntities());
            sendGameState();

            Util.logInfo("Client " + clientId + " initialized successfully");
         } catch (IOException e) {
            Util.logError("Error creating client handler: " + e.getMessage());
            disconnect();
         }
      }

      @Override
      public void run() {
         int errorCount = 0;
         try {
            while (isRunning) {
               if (clientSocket == null || clientSocket.isClosed()) {
                  disconnect();
                  break;
               }

               try {
                  String messageType = in.readUTF();
                  if (messageType.equals(Setting.NETWORK_CONTROL)) {
                     int id = in.readInt();
                     String control = in.readUTF();
                     GameControl.getBomberEntitiesMap().get(id).control(control, GameControl.getDeltaTime());

                     // Only send game state if enough time has passed since last update
                     long currentTime = System.currentTimeMillis();
                     if (currentTime - lastGameStateSentTime >= GAME_STATE_SEND_INTERVAL) {
                        sendGameState();
                        lastGameStateSentTime = currentTime;
                     }
                  }

               } catch (SocketTimeoutException e) {
                  // Socket timeout is normal, just continue the loop
                  continue;
               } catch (IOException e) {
                  Util.logError("Error reading command: " + e.getMessage());
                  errorCount++;
                  if (errorCount > 10) {
                     disconnect();
                  }
               }
            }
         } catch (Exception e) {
            Util.logError("Client handler error: " + e.getMessage());
            disconnect();
         }
      }

      public void sendObject(String type, Object object) throws IOException {
         if (out != null) {
            out.reset();
            out.writeUTF(type);
            out.writeObject(object);
            out.flush();
         }
      }

      public void sendId(int id) throws IOException {
         if (out != null) {
            out.writeUTF(Setting.NETWORK_ID);
            out.writeInt(id);
            out.flush();
         }
      }

      public void sendMapDimensions() throws IOException {
         if (out != null) {
            out.writeUTF(Setting.NETWORK_MAP_DIMENSIONS);
            out.writeInt(GameControl.getWidth());
            out.writeInt(GameControl.getHeight());
            out.flush();
         }
      }

      public void sendGameState() throws IOException {
         if (out != null) {

            sendObject(Setting.NETWORK_BOMBER_ENTITIES, GameControl.getBomberEntities());

            sendObject(Setting.NETWORK_ENEMY_ENTITIES, GameControl.getEnemyEntities());

            // These change less frequently, so we could send them at longer intervals
            // But for simplicity, we'll keep them in the same update
            sendObject(Setting.NETWORK_STATIC_ENTITIES, GameControl.getStaticEntities());
            sendObject(Setting.NETWORK_ITEM_ENTITIES, GameControl.getItemEntities());

          
         }
      }

      public void disconnect() {
         isRunning = false;

         try {
            if (out != null) {
               out.close();
               out = null;
            }
            if (in != null) {
               in.close();
               in = null;
            }
            if (clientSocket != null && !clientSocket.isClosed()) {
               clientSocket.close();
               clientSocket = null;
            }
         } catch (IOException e) {
            Util.logError("Error disconnecting client: " + e.getMessage());
         } finally {
            if (clientBomber != null) {
               GameControl.removeEntity(clientBomber);
            }
            clients.remove(this);
         }
      }
   }
}
