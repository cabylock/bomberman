package core.system.controller.base;

import javafx.scene.control.Label;

import core.system.game.BombermanGame;
import core.system.setting.Setting;
import core.util.Util;
import core.system.network.GameClient;
import core.system.network.GameServer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.application.Platform;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import core.system.game.GameControl;
import java.util.concurrent.Executors;

public class NetworkSetupController {
   @FXML
   private TextField hostPortField;

   @FXML
   private TextField ipAddressField;

   @FXML
   private Label myIpAddressField;

   @FXML
   private TextField clientPortField;

   @FXML
   private TextField playerNameField;

   @FXML
   private Label statusLabel;

   @FXML
   private ProgressIndicator connectionProgress;

   @FXML
   private HBox statusContainer;

   private Stage stage;
   private String mapName;
   private int mapType;
   private ExecutorService executorService = Executors.newSingleThreadExecutor();
   private GameServer server; // Add this field

   public void setStage(Stage stage) {
      this.stage = stage;
   }

  

   @FXML
   public void initialize() {
      try {
         String localIp = getLocalIpAddress();
         myIpAddressField.setText(localIp);

         playerNameField.setText("Player_" + localIp.substring(localIp.lastIndexOf('.') + 1));

         updateConnectionStatus("Ready to connect", "green", false);
      } catch (Exception e) {
         e.printStackTrace();
         updateConnectionStatus("Error retrieving IP address", "red", false);
      }
   }

   // Helper method to get the real LAN IP address (works on Linux and Windows)
   private String getLocalIpAddress() {
      try {
         java.util.Enumeration<java.net.NetworkInterface> interfaces = java.net.NetworkInterface.getNetworkInterfaces();
         while (interfaces.hasMoreElements()) {
            java.net.NetworkInterface iface = interfaces.nextElement();
            if (!iface.isUp() || iface.isLoopback() || iface.isVirtual())
               continue;
            java.util.Enumeration<java.net.InetAddress> addresses = iface.getInetAddresses();
            while (addresses.hasMoreElements()) {
               java.net.InetAddress addr = addresses.nextElement();
               if (addr instanceof java.net.Inet4Address && !addr.isLoopbackAddress()) {
                  return addr.getHostAddress();
               }
            }
         }
         // fallback
         return java.net.InetAddress.getLocalHost().getHostAddress();
      } catch (Exception e) {
         return "127.0.0.1";
      }
   }

   @FXML
   private void hostGame() {
      try {
         // Reset any previous error styling
         hostPortField.setStyle("");

         int port = Integer.parseInt(hostPortField.getText());

         // Validate port range
         if (port < 1024 || port > 65535) {
            hostPortField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            updateConnectionStatus("Invalid port. You should range 1024-65535", "red", false);
            return;
         }

         Setting.SERVER_PORT = port;

         // Save player name
         String playerName = playerNameField.getText().trim();
         if (!playerName.isEmpty()) {
            GameControl.getBomberEntities().get(Setting.ID).setName(playerName);
         }

         // First check if the port is available
         if (!GameServer.isPortAvailable(port)) {
            hostPortField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            updateConnectionStatus("Port " + port + " already in use. Try a different port.", "red", false);
            hostPortField.setPromptText("Enter a different port");
            return;
         }

         // Stop any previous server if running
         if (server != null && server.isRunning) {
            server.stopServer();
            server = null;
         }

         // Show progress indicator before starting server
         updateConnectionStatus("Server starting...", "orange", true);

         // Start the server in background so UI can update
         CompletableFuture.runAsync(() -> {
            server = new GameServer();
            boolean started = server.startServer(Setting.SERVER_PORT);

            Platform.runLater(() -> {
               if (!started) {
                  updateConnectionStatus("Failed to start server: " + server.getLastError(), "red", false);
                  return;
               }
               // Set the server in GameControl so it can be used for broadcasting
               GameControl.setServer(server);
               // Only now start the game scene
               updateConnectionStatus("Server started!", "green", false);
               Setting.GAME_MODE = Setting.SERVER_MODE;
               startNetworkGame();
            });
         }, executorService);

      } catch (NumberFormatException e) {
         hostPortField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
         updateConnectionStatus("Invalid port. You should try using range 1024-65535", "red", false);
      }
   }

   @FXML
   private void joinGame() {
      try {
         // Reset any previous error styling
         clientPortField.setStyle("");
         ipAddressField.setStyle("");

         String ipAddress = ipAddressField.getText();
         int port = Integer.parseInt(clientPortField.getText());

         // Basic validation
         if (ipAddress == null || ipAddress.trim().isEmpty()) {
            ipAddressField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            updateConnectionStatus("Please enter a valid IP address", "red", false);
            return;
         }

         if (port < 1024 || port > 65535) {
            clientPortField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            updateConnectionStatus("Invalid port. Use range 1024-65535", "red", false);
            return;
         }

         Setting.SERVER_ADDRESS = ipAddress;
         Setting.SERVER_PORT = port;

         // Save player name
         
         updateConnectionStatus("Connecting to server...", "orange", true);
         Util.logInfo("Attempting to connect to " + ipAddress + ":" + port);

         // Connect in background
         CompletableFuture.runAsync(() -> {
            GameClient client = new GameClient(ipAddress, port);
            boolean success = client.connect();

            Platform.runLater(() -> {
               if (success) {
                  // Set the client in GameControl so it can be used for sending commands
                  GameControl.setClient(client);

                  updateConnectionStatus("Connected to server!", "green", false);
                  Setting.GAME_MODE = Setting.CLIENT_MODE;
                  startNetworkGame();   
               

                  

               } else {
                  // Don't return to menu, just show the error
                  String errorMessage = client.getLastError();
                  if (errorMessage != null && !errorMessage.isEmpty()) {
                     updateConnectionStatus("Connection failed: " + errorMessage, "red", false);
                     // Also log for debugging
                     Util.logError("Connection failed: " + errorMessage);
                  } else {
                     updateConnectionStatus("Failed to connect to " + ipAddress + ":" + port, "red", false);
                  }

                  // Highlight the fields that might need correction
                  ipAddressField.setStyle("-fx-border-color: orange; -fx-border-width: 1px;");
                  clientPortField.setStyle("-fx-border-color: orange; -fx-border-width: 1px;");
               }
            });
         }, executorService);
      } catch (NumberFormatException e) {
         clientPortField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
         updateConnectionStatus("Invalid port. Use range 1024-65535", "red", false);
      }
   }

   /**
    * Updates the connection status UI elements
    * 
    * @param message      Status message to display
    * @param color        Color for the status text (green, red, orange)
    * @param showProgress Whether to show the progress indicator
    */
   public void updateConnectionStatus(String message, String color, boolean showProgress) {
      if (Platform.isFxApplicationThread()) {
         statusLabel.setText(message);
         statusLabel.setStyle("-fx-text-fill: " + color + ";");
         connectionProgress.setVisible(showProgress);
      } else {
         Platform.runLater(() -> {
            statusLabel.setText(message);
            statusLabel.setStyle("-fx-text-fill: " + color + ";");
            connectionProgress.setVisible(showProgress);
         });
      }
   }

   private void startNetworkGame() {
      // Clean up executor service
      executorService.shutdown();

      // Set the game mode before creating scene
      

      GameControl.start();

      // Create the game with network mode
      BombermanGame.createGameScene(stage);
   }

   @FXML
   private void goBack() {
      executorService.shutdown();

      // Stop server if running
      if (server != null && server.isRunning) {
         server.stopServer();
         server = null;
      }

      try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/system/fxml/base/Mode.fxml"));
         Parent root = loader.load();

         ModeController controller = loader.getController();
         
         controller.setStage(stage);

         Scene scene = new Scene(root);
         stage.setScene(scene);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

}
