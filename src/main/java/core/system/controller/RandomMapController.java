package core.system.controller;

import core.system.BombermanGame;
import core.util.Util;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RandomMapController {
    @FXML private TextField nameField;
    @FXML private ComboBox<Integer> levelComboBox;
    
    private Stage stage;
    private Stage dialogStage;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    /**
     * Initialize the controller after FXML is loaded
     */
    public void initialize() {
        // Populate level combo box
        levelComboBox.getItems().clear();
        levelComboBox.getItems().addAll(1, 2, 3, 4, 5);
        levelComboBox.setValue(1); // Default level
        
        // Set default map name with timestamp to ensure uniqueness
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(8);
        nameField.setText("RandomMap_" + timestamp);
    }
    
    /**
     * Generate a random map with specified name and level
     */
    @FXML
    private void generateMap() {
        String mapName = nameField.getText();
        
        // Validate map name
        if (mapName == null || mapName.trim().isEmpty()) {
            mapName = "RandomMap_" + System.currentTimeMillis();
        }
        
        // Ensure it has .txt extension
        if (!mapName.endsWith(".txt")) {
            mapName += ".txt";
        }
        
        // Get selected level
        int level = levelComboBox.getValue();
        
        // Close the dialog
        dialogStage.close();
        
        try {
            // Generate the random map
            Util.generateRandomMap(level, mapName);
            
            // Wait a bit for the file to be written
            Thread.sleep(500); 
            
            // Start the game with the generated map
            BombermanGame game = new BombermanGame(mapName, BombermanGame.CUSTOM);
            game.createGameScene(stage);
            
        } catch (Exception e) {
            e.printStackTrace();
            // You could show an error dialog here
        }
    }
    
    /**
     * Cancel the operation and close the dialog
     */
    @FXML
    private void cancel() {
        dialogStage.close();
    }
}
