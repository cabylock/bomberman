package core.system.controller.base;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LanController {

   @FXML
   private TextField hostPortField;
   @FXML
   private Button hostButton;
   @FXML
   private Label hostStatusLabel;

   @FXML
   private TextField ipAddressField;
   @FXML
   private TextField clientPortField;
   @FXML
   private Button connectButton;
   @FXML
   private Label clientStatusLabel;

   @FXML
   private TextArea messageDisplay;
   @FXML
   private TextField messageField;

   private ServerSocket serverSocket;
   private Socket clientSocket;
   private PrintWriter out;
   private BufferedReader in;
   private ExecutorService executor;
   private boolean isServerRunning = false;
   private boolean isClientConnected = false;
   private Stage stage;

   public void initialize() {
      executor = Executors.newCachedThreadPool();
      updateUI();
   }
   public void setStage(Stage stage) {
      this.stage = stage;
   }

   @FXML
   private void startServer() {
      if (!isServerRunning) {
         try {
            int port = Integer.parseInt(hostPortField.getText());
            executor.execute(() -> {
               try {
                  serverSocket = new ServerSocket(port);
                  isServerRunning = true;
                  Platform.runLater(() -> {
                     hostButton.setText("Stop Server");
                     hostPortField.setDisable(true);
                  });

                  // Log the server's IP address for convenience
                  String hostAddress = InetAddress.getLocalHost().getHostAddress();
                  Platform.runLater(
                        () -> appendToMessageDisplay("Server started. IP: " + hostAddress + ", Port: " + port));

                  // Wait for a client to connect
                  Socket socket = serverSocket.accept();
                  handleClientConnection(socket);

               } catch (IOException e) {
                  if (!serverSocket.isClosed()) {
                     System.out.println("Server error: " + e.getMessage());
                     Platform.runLater(() -> {
                        isServerRunning = false;
                        hostButton.setText("Start Server");
                        hostPortField.setDisable(false);
                     });
                  }
               }
            });
         } catch (NumberFormatException e) {
            appendToMessageDisplay("Invalid port number");
         }
      } else {
         stopServer();
      }
   }

   private void stopServer() {
      if (serverSocket != null && !serverSocket.isClosed()) {
         try {
            serverSocket.close();
            isServerRunning = false;
            System.out.println("Server stopped");
            Platform.runLater(() -> {
               hostButton.setText("Start Server");
               hostPortField.setDisable(false);
            });
         } catch (IOException e) {
            appendToMessageDisplay("Error stopping server: " + e.getMessage());
         }
      }
   }

   private void handleClientConnection(Socket socket) {
      try {
         Platform
               .runLater(() -> appendToMessageDisplay("Client connected: " + socket.getInetAddress().getHostAddress()));

         out = new PrintWriter(socket.getOutputStream(), true);
         in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

         String inputLine;
         while ((inputLine = in.readLine()) != null) {
            final String message = inputLine;
            Platform.runLater(() -> appendToMessageDisplay("Client: " + message));
         }
      } catch (IOException e) {
         if (!socket.isClosed()) {
            Platform.runLater(() -> appendToMessageDisplay("Error handling client connection: " + e.getMessage()));
         }
      } finally {
         try {
            socket.close();
         } catch (IOException e) {
            // Ignore
         }
      }
   }

   @FXML
   private void connectToServer() {
      if (!isClientConnected) {
         try {
            String ip = ipAddressField.getText();
            int port = Integer.parseInt(clientPortField.getText());

            executor.execute(() -> {
               try {
                  clientSocket = new Socket(ip, port);
                  out = new PrintWriter(clientSocket.getOutputStream(), true);
                  in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                  isClientConnected = true;
                  System.out.println("Connected to server at " + ip + ":" + port);
                  Platform.runLater(() -> {
                     connectButton.setText("Disconnect");
                     ipAddressField.setDisable(true);
                     clientPortField.setDisable(true);
                  });

                  String inputLine;
                  while ((inputLine = in.readLine()) != null) {
                     final String message = inputLine;
                     Platform.runLater(() -> appendToMessageDisplay("Server: " + message));
                  }
               } catch (IOException e) {
                  if (clientSocket == null || !clientSocket.isClosed()) {
                     updateClientStatus(false, "Connection failed");
                     appendToMessageDisplay("Connection failed: " + e.getMessage());
                  }
               } finally {
                  disconnectFromServer();
               }
            });
         } catch (NumberFormatException e) {
            appendToMessageDisplay("Invalid port number");
         }
      } else {
         disconnectFromServer();
      }
   }

   private void disconnectFromServer() {
      if (clientSocket != null && !clientSocket.isClosed()) {
         try {
            clientSocket.close();
            isClientConnected = false;
            System.out.println("Disconnected from server");
            Platform.runLater(() -> {
               connectButton.setText("Connect");
               ipAddressField.setDisable(false);
               clientPortField.setDisable(false);
            });
         } catch (IOException e) {
            appendToMessageDisplay("Error disconnecting: " + e.getMessage());
         }
      }
   }

   @FXML
   private void sendMessage() {
      String message = messageField.getText();
      if (message.isEmpty())
         return;

      if (out != null) {
         out.println(message);
         appendToMessageDisplay("You: " + message);
         messageField.clear();
      } else {
         appendToMessageDisplay("Not connected. Cannot send message.");
      }
   }



   private void updateClientStatus(boolean connected, String message) {
      isClientConnected = connected;
      Platform.runLater(() -> {
         clientStatusLabel.setText(message);
         clientStatusLabel.setStyle("-fx-text-fill: " + (connected ? "green" : "gray") + ";");
         connectButton.setText(connected ? "Disconnect" : "Connect");
         updateUI();
      });
   }

   private void appendToMessageDisplay(String message) {
      Platform.runLater(() -> {
         messageDisplay.appendText(message + "\n");
      });
   }

   private void updateUI() {
      hostPortField.setDisable(isServerRunning);
      ipAddressField.setDisable(isClientConnected);
      clientPortField.setDisable(isClientConnected);
   }

   @FXML
   private void backToMainMenu() {
      // Clean up resources before going back to the main menu
      if (isServerRunning) {
         stopServer();
      }
      if (isClientConnected) {
         disconnectFromServer();
      }
      if (executor != null) {
         executor.shutdownNow();
      }

   }
}
