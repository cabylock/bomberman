package core.system;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

public class MenuBoard {

   public static final int DEFAULT = 0;
   public static final int CUSTOM = 1;
   private Stage stage;
   private Scene menuScene;

   public MenuBoard(Stage stage) {
      this.stage = stage;
   }

   public void createMenuScene() {
      // Create title text
      Text titleText = new Text("BOMBERMAN");
      titleText.setFont(Font.font("Arial", 48));

      // Create buttons
      Button showMapsBtn = new Button("Show Available Maps");
      showMapsBtn.setPrefSize(200, 40);
      showMapsBtn.setOnAction(e -> showMapSelectionScene());

      Button randomMapBtn = new Button("Random Map");
      randomMapBtn.setPrefSize(200, 40);
      randomMapBtn.setOnAction(e -> startRandomMap());

      Button exitBtn = new Button("Exit");
      exitBtn.setPrefSize(200, 40);
      exitBtn.setOnAction(e -> stage.close());
      // Add all elements to a vertical layout
      VBox root = new VBox(20);
      root.getChildren().addAll(titleText, showMapsBtn, randomMapBtn, exitBtn);
      root.setAlignment(Pos.CENTER);

      // Create scene with the layout
      menuScene = new Scene(root, 800, 600); // Store the scene to return to it later

      // Set the scene on stage
      stage.setScene(menuScene);
   }

   private void showMapSelectionScene() {
      BorderPane root = new BorderPane();

      // Title
      Text title = new Text("Available Maps");
      title.setFont(Font.font("Arial", 32));
      VBox titleBox = new VBox(title);
      titleBox.setAlignment(Pos.CENTER);
      titleBox.setPadding(new Insets(20));
      root.setTop(titleBox);

      // Create tabs for default and custom maps
      VBox mapSelectionBox = new VBox(15);
      mapSelectionBox.setAlignment(Pos.CENTER);

      // Default maps section
      Text defaultMapsLabel = new Text("Default Maps");
      defaultMapsLabel.setFont(Font.font("Arial", 18));

      ListView<String> defaultMapList = new ListView<>();
      defaultMapList.setPrefHeight(200);

      // Custom maps section
      Text customMapsLabel = new Text("Custom Maps");
      customMapsLabel.setFont(Font.font("Arial", 18));

      ListView<String> customMapList = new ListView<>();
      customMapList.setPrefHeight(200);

      // Load default maps
      File defaultMapsDir = new File("src/main/resources/default_levels");
      if (defaultMapsDir.exists() && defaultMapsDir.isDirectory()) {
         File[] mapFiles = defaultMapsDir.listFiles((dir, name) -> name.endsWith(".txt"));
         if (mapFiles != null) {
            for (File mapFile : mapFiles) {
               defaultMapList.getItems().add(mapFile.getName());
            }
         }
      }

      // Load custom maps
      File customMapsDir = new File("src/main/resources/custom_levels");
      if (customMapsDir.exists() && customMapsDir.isDirectory()) {
         File[] mapFiles = customMapsDir.listFiles((dir, name) -> name.endsWith(".txt"));
         if (mapFiles != null) {
            for (File mapFile : mapFiles) {
               customMapList.getItems().add(mapFile.getName());
            }
         }
      }

      // Set selection listeners to deselect the other list when one is selected
      defaultMapList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
         if (newVal != null) {
            customMapList.getSelectionModel().clearSelection();
         }
      });

      customMapList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
         if (newVal != null) {
            defaultMapList.getSelectionModel().clearSelection();
         }
      });

      // Add key event handlers to the list views for Enter key
      defaultMapList.setOnKeyPressed(event -> {
         if (event.getCode() == KeyCode.ENTER) {
            String selectedMap = defaultMapList.getSelectionModel().getSelectedItem();
            if (selectedMap != null) {
               startGameWithMap(selectedMap, DEFAULT);
            }
         }
      });

      customMapList.setOnKeyPressed(event -> {
         if (event.getCode() == KeyCode.ENTER) {
            String selectedMap = customMapList.getSelectionModel().getSelectedItem();
            if (selectedMap != null) {
               startGameWithMap(selectedMap, CUSTOM);
            }
         }
      });

      // Add sections to the selection box
      mapSelectionBox.getChildren().addAll(
            defaultMapsLabel, defaultMapList,
            customMapsLabel, customMapList);

      root.setCenter(mapSelectionBox);

      // Button for playing the selected map
      Button playBtn = new Button("Play Selected Map");
      playBtn.setPrefSize(200, 40);
      playBtn.setOnAction(e -> {
         String selectedDefaultMap = defaultMapList.getSelectionModel().getSelectedItem();
         String selectedCustomMap = customMapList.getSelectionModel().getSelectedItem();

         if (selectedDefaultMap != null) {
            // Start game with selected default map
            startGameWithMap(selectedDefaultMap, DEFAULT);
         } else if (selectedCustomMap != null) {
            // Start game with selected custom map
            startGameWithMap(selectedCustomMap, CUSTOM);
         } else {
            // Show alert that no map is selected
            System.out.println("Please select a map first");
         }
      });

      Button backBtn = new Button("Back to Menu");
      backBtn.setPrefSize(200, 40);
      backBtn.setOnAction(e -> stage.setScene(menuScene));

      Button deleteBtn = new Button("Delete Selected Map");
      deleteBtn.setPrefSize(200, 40);

      deleteBtn.setOnAction(e -> {
         String selectedDefaultMap = defaultMapList.getSelectionModel().getSelectedItem();
         String selectedCustomMap = customMapList.getSelectionModel().getSelectedItem();

         if (selectedDefaultMap != null) {
            // Show warning for default maps
            showAlert("Cannot Delete Default Map",
                  "Default maps cannot be deleted.",
                  "Please select a custom map if you wish to delete it.");
         } else if (selectedCustomMap != null) {
            // Confirm before deleting custom map
            boolean confirmed = showConfirmation("Delete Map",
                  "Are you sure you want to delete " + selectedCustomMap + "?",
                  "This action cannot be undone.");
            if (confirmed) {
               deleteMap(selectedCustomMap, CUSTOM);
               // Refresh the map selection screen
               showMapSelectionScene();
            }
         } else {
            // No map selected
            showAlert("No Map Selected",
                  "Please select a map to delete.",
                  null);
         }
      });

      HBox buttonBox = new HBox(20, playBtn, backBtn, deleteBtn);
      buttonBox.setAlignment(Pos.CENTER);
      buttonBox.setPadding(new Insets(20));
      root.setBottom(buttonBox);

      Scene mapSelectionScene = new Scene(root, 800, 600);
      stage.setScene(mapSelectionScene);
   }

   private void startGameWithMap(String mapName, int mapType) {
      // Determine map type based on path

      BombermanGame game = new BombermanGame(mapName, mapType);

      game.createGameScene(stage);
   }

   private void startRandomMap() {
      // Create a dialog for random map settings
      Stage dialogStage = new Stage();
      dialogStage.setTitle("Random Map Settings");
      dialogStage.initModality(Modality.APPLICATION_MODAL);
      dialogStage.initOwner(stage);

      // Create form elements
      Label nameLabel = new Label("Map Name:");
      TextField nameField = new TextField("RandomMap");

      Label levelLabel = new Label("Level:");
      ComboBox<Integer> levelComboBox = new ComboBox<>();
      levelComboBox.getItems().addAll(1, 2, 3, 4, 5);
      levelComboBox.setValue(1); // Default level

      // Create buttons
      Button generateBtn = new Button("Generate Map");
      Button cancelBtn = new Button("Cancel");

      // Set button actions
      generateBtn.setOnAction(e -> {
         String mapName = nameField.getText();
         if (mapName.trim().isEmpty()) {
            mapName = "RandomMap";
         }

         // Make sure the name ends with .txt
         if (!mapName.endsWith(".txt")) {
            mapName += ".txt";
         }

         int level = levelComboBox.getValue();
         dialogStage.close();

         // Create the game with specified level and name
         BombermanGame game = new BombermanGame(level, mapName);

         // Create game scene
         game.createGameScene(stage);
      });

      cancelBtn.setOnAction(e -> dialogStage.close());

      // Create layout
      GridPane formGrid = new GridPane();
      formGrid.setHgap(10);
      formGrid.setVgap(10);
      formGrid.setPadding(new Insets(20));

      // Add form elements to grid
      formGrid.add(nameLabel, 0, 0);
      formGrid.add(nameField, 1, 0);
      formGrid.add(levelLabel, 0, 1);
      formGrid.add(levelComboBox, 1, 1);

      // Create button layout
      HBox buttonBox = new HBox(10);
      buttonBox.getChildren().addAll(generateBtn, cancelBtn);
      buttonBox.setAlignment(Pos.CENTER_RIGHT);

      // Create main layout
      VBox dialogLayout = new VBox(20);
      dialogLayout.getChildren().addAll(formGrid, buttonBox);
      dialogLayout.setPadding(new Insets(20));

      // Set scene
      Scene dialogScene = new Scene(dialogLayout);
      dialogStage.setScene(dialogScene);

      // Show dialog
      dialogStage.showAndWait();
   }

   private void deleteMap(String mapName, int mapType) {
      try {
         String dirPath = mapType == DEFAULT ? "src/main/resources/default_levels" : "src/main/resources/custom_levels";

         File mapFile = new File(dirPath, mapName);
         if (mapFile.exists()) {
            boolean deleted = mapFile.delete();
            if (deleted) {
               System.out.println("Successfully deleted map: " + mapName);
            } else {
               showAlert("Deletion Failed",
                     "Could not delete file: " + mapName,
                     "The file may be in use or protected.");
            }
         } else {
            showAlert("File Not Found",
                  "Could not find file: " + mapName,
                  "The file may have been moved or deleted already.");
         }
      } catch (Exception e) {
         e.printStackTrace();
         showAlert("Error",
               "An error occurred while deleting the map.",
               e.getMessage());
      }
   }

   private void showAlert(String title, String header, String content) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle(title);
      alert.setHeaderText(header);
      alert.setContentText(content);
      alert.showAndWait();
   }

   private boolean showConfirmation(String title, String header, String content) {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle(title);
      alert.setHeaderText(header);
      alert.setContentText(content);

      Optional<ButtonType> result = alert.showAndWait();
      return result.isPresent() && result.get() == ButtonType.OK;
   }

}
