package core.system.controller.base;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import core.system.setting.Setting;
import core.system.network.GameServer;
import core.system.network.GameClient;
import core.system.game.GameControl;
import core.system.game.BombermanGame;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;


public class NetworkSetupController {
   @FXML
   private TextField hostPortField;

   @FXML
   private Label myIpAddressField;

   @FXML
   private VBox hostBox;
   @FXML
   private VBox clientBox;
   @FXML
   private TextField ipAddressField;
   @FXML
   private TextField clientPortField;

   private Stage stage;

   public void setStage(Stage stage) {
      this.stage = stage;
   }

   @FXML
   public void initialize() {
      try {
         String localIp = getLocalIpAddress();
         myIpAddressField.setText(localIp);

         if (Setting.GAME_MODE == Setting.CLIENT_MODE) {
            hostBox.setVisible(false);
            hostBox.setManaged(false);
            clientBox.setVisible(true);
            clientBox.setManaged(true);
         } else {
            hostBox.setVisible(true);
            hostBox.setManaged(true);
            clientBox.setVisible(false);
            clientBox.setManaged(false);
         }
      } catch (Exception e) {
         e.printStackTrace();
         showError("Error retrieving IP address", e.getMessage());
      }
   }

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
         return java.net.InetAddress.getLocalHost().getHostAddress();
      } catch (Exception e) {
         return "127.0.0.1";
      }
   }

   @FXML
   private void hostGame() {
      try {
         hostPortField.setStyle("");
         int port = Integer.parseInt(hostPortField.getText());

         if (port < 1024 || port > 65535) {
            hostPortField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            showError("Invalid Port", "Please use a port number between 1024 and 65535");
            return;
         }

         Setting.SERVER_PORT = port;
         Setting.GAME_MODE = Setting.SERVER_MODE;

         if (GameServer.createServer(port)) {
            startGame();
         } else {
            showError("Server Error", GameServer.getErrorMessage());
         }
      } catch (NumberFormatException e) {
         hostPortField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
         showError("Invalid Port", "Please enter a valid port number");
      }
   }

   @FXML
   private void joinGame() {
      try {
         clientPortField.setStyle("");
         ipAddressField.setStyle("");
         String ipAddress = ipAddressField.getText();
         int port = Integer.parseInt(clientPortField.getText());

         if (ipAddress == null || ipAddress.trim().isEmpty()) {
            ipAddressField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            showError("Invalid Address", "Please enter a valid IP address");
            return;
         }

         if (port < 1024 || port > 65535) {
            clientPortField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            showError("Invalid Port", "Please enter a port number between 1024 and 65535");
            return;
         }

         Setting.GAME_MODE = Setting.CLIENT_MODE;

         GameClient.initialize(ipAddress, port);
         if (GameClient.connect()) {
            startGame();
         } else {
            showError("Connection Error", GameClient.getLastError());
         }
      } catch (NumberFormatException e) {
         clientPortField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
         showError("Invalid Port", "Please enter a valid port number");
      }
   }

   private void startGame() {
      try {
         if (Setting.GAME_MODE == Setting.CLIENT_MODE) {

            BombermanGame.createGameScene(stage);
         } else {
            GameControl.loadMap(Setting.MAP_NAME);
            BombermanGame.createGameScene(stage);
         }
      } catch (Exception e) {
         e.printStackTrace();
         showError("Error starting game", e.getMessage());
      }
   }

   private void showError(String title, String message) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle(title);
      alert.setHeaderText(null);
      alert.setContentText(message);
      alert.showAndWait();
   }

   @FXML
   private void goBack() {
      try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/system/fxml/base/MapSelection.fxml"));
         Parent root = loader.load();
         MapSelectionController controller = loader.getController();
         controller.setStage(stage);
         controller.loadMaps();
         Scene scene = new Scene(root);
         stage.setScene(scene);
      } catch (Exception e) {
         e.printStackTrace();
}
}
}
