
package core.system.network;
import java.net.Socket;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Scanner;

public class GameClient extends Thread {

   private String serverAddress;
   private int serverPort;
   private ObjectOutputStream out;
   private ObjectInputStream in;
   private Socket serverSocket;

   public GameClient(String serverAddress, int serverPort) {
      this.serverAddress = serverAddress;
      this.serverPort = serverPort;
     
   }

   public void connect() {
      try {
         serverSocket = new Socket(serverAddress, serverPort);
         System.out.println("Connected to server at " + serverAddress + ":" + serverPort);
         out = new ObjectOutputStream(serverSocket.getOutputStream());
         in = new ObjectInputStream(serverSocket.getInputStream());
         start();
      } catch (IOException e) {
         System.err.println("Error initializing client: " + e.getMessage());
         out = null;
         in = null;
      }
   }

   @Override
   public void run() {

      try {
         
         // Example of sending a message to the server
         sendMessage("Hello from client!");

         while(true) {
            // Example of receiving a message from the server
            String message = in.readUTF();
            if ("STRING".equals(message)) {
               message = in.readUTF();
               System.out.println("Received from server: " + message);
            } else if ("LIST".equals(message)) {
               List<?> data = (List<?>) in.readObject();
               System.out.println("Received list from server: " + data);
            }
         }

         // Example of receiving a message from the server
         

         

      } catch (IOException e) {
         System.err.println("Error connecting to server: " + e.getMessage());
      } catch (ClassNotFoundException e) {
         System.err.println("Error deserializing object: " + e.getMessage());
      } finally {
         try {
            if (serverSocket != null && !serverSocket.isClosed()) {
               serverSocket.close();
            }
         } catch (IOException e) {
            System.err.println("Error closing client socket: " + e.getMessage());
         }
      }

   }

    public void sendMessage(String message) {
       try {
            out.writeUTF("STRING"); // Indicate the type of data being sent
            out.writeUTF(message); // Send the message
            out.flush(); // Ensure the data is sent immediately
         } catch (IOException e) {
            System.err.println("Error sending message: " + e.getMessage());
         }
      }

      public <T> void sendData(List<T> data) {
         try {
            out.writeUTF("LIST"); // Indicate the type of data being sent
            out.writeObject(data); // Serialize and send the object
            out.flush(); // Ensure the data is sent immediately
         } catch (IOException e) {
            System.err.println("Error sending object: " + e.getMessage());
         }
      }

   public static void main(String[] args) {

      String serverAddress = "localhost"; // Replace with the server's IP address
      int serverPort = 8080;

      GameClient client = new GameClient(serverAddress, serverPort);
      client.connect();

      Scanner scanner = new Scanner(System.in);
      while (true) {
         System.out.print("Enter message to send to server: ");
         if(  client.serverSocket == null ||  client.serverSocket.isClosed()) {
            System.out.println("Connection closed. Exiting...");
            scanner.close();
            break;
         }
         String message = scanner.nextLine();
         client.sendMessage(message);
      }

   }

}
