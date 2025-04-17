package core.system.controller.base;

import java.net.InetAddress;
import javafx.scene.control.Label;

import core.system.game.BombermanGame;
import core.system.setting.Setting;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

   private Stage stage;
   private String mapName;
   private int mapType;

   public void setStage(Stage stage) {
      this.stage = stage;
   }

   public void setMap(String mapName, int mapType) {
      this.mapName = mapName;
      this.mapType = mapType;
   }

   @FXML
   public void initialize() {
      try {
         String localIp = getLocalIpAddress();
         myIpAddressField.setText(localIp);
         
         playerNameField.setText(localIp);
      } catch (Exception e) {
         e.printStackTrace();
         showError("Unable to retrieve local IP address.");
      }
   }

   // Helper method to get the real LAN IP address (works on Linux and Windows)
   private String getLocalIpAddress() {
      try {
         java.util.Enumeration<java.net.NetworkInterface> interfaces = java.net.NetworkInterface.getNetworkInterfaces();
         while (interfaces.hasMoreElements()) {
            java.net.NetworkInterface iface = interfaces.nextElement();
            if (!iface.isUp() || iface.isLoopback() || iface.isVirtual()) continue;
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
         int port = Integer.parseInt(hostPortField.getText());
         Setting.SERVER_PORT = port;

         // Save player name
         String playerName = playerNameField.getText().trim();
         if (!playerName.isEmpty()) {
            Setting.PLAYER_NAME = playerName;
         }

         startNetworkGame(Setting.SERVER_MODE);
      } catch (NumberFormatException e) {
         showError("Invalid port number. Please enter a valid port.");
      }
   }

   @FXML
   private void joinGame() {
      try {
         String ipAddress = ipAddressField.getText();
         int port = Integer.parseInt(clientPortField.getText());

         Setting.SERVER_ADDRESS = ipAddress;
         Setting.SERVER_PORT = port;

         // Save player name
         String playerName = playerNameField.getText().trim();
         if (!playerName.isEmpty()) {
            Setting.PLAYER_NAME = playerName;
         }

         startNetworkGame(Setting.CLIENT_MODE);
      } catch (NumberFormatException e) {
         showError("Invalid port number. Please enter a valid port.");
      }
   }

   private void startNetworkGame(int networkMode) {
      // Create the game with network mode
      BombermanGame game = new BombermanGame(mapName, mapType);

      // Set the game mode before creating scene
      Setting.GAME_MODE = networkMode;
      game.createGameScene(stage);
      // Create game scene

   }

   @FXML
   private void goBack() {
      try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/system/fxml/base/Mode.fxml"));
         Parent root = loader.load();

         ModeController controller = loader.getController();
         controller.setMap(mapName, mapType);
         controller.setStage(stage);

         Scene scene = new Scene(root);
         stage.setScene(scene);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void showError(String message) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText(null);
      alert.setContentText(message);
      alert.showAndWait();
   }
}
