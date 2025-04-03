package core.system.controller.base;

import core.system.BombermanGame;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RandomMapController {
    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<Integer> levelComboBox;

    @FXML
    private Slider widthSlider;

    @FXML
    private Slider heightSlider;

    @FXML
    private Label widthValueLabel;

    @FXML
    private Label heightValueLabel;

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
        // Set up level combo box with options 1-5
        if (levelComboBox != null) {
            levelComboBox.getItems().addAll(1, 2, 3, 4, 5);
            levelComboBox.setValue(1); // Default to level 1
        }

        // Set default map name with timestamp
        if (nameField != null) {
            String timestamp = String.valueOf(System.currentTimeMillis()).substring(8);
            nameField.setText("RandomMap_" + timestamp);
        }

        // Set up slider value change listeners
        if (widthSlider != null && widthValueLabel != null) {
            // Update label when slider value changes
            widthSlider.valueProperty().addListener((_, _, newVal) -> {
                int value = newVal.intValue();
                widthValueLabel.setText(String.valueOf(value));
            });
        }

        if (heightSlider != null && heightValueLabel != null) {
            // Update label when slider value changes
            heightSlider.valueProperty().addListener((_, _, newVal) -> {
                int value = newVal.intValue();
                heightValueLabel.setText(String.valueOf(value));
            });
        }
    }

    /**
     * Handler for the Create button
     */
    @FXML
    private void handleCreate() {
        // Get the selected level
        int level = levelComboBox.getValue();

        // Generate map name with timestamp if not present
        String mapName = "RandomMap_" + System.currentTimeMillis();
        if (nameField != null && nameField.getText() != null && !nameField.getText().trim().isEmpty()) {
            mapName = nameField.getText();
        }

        // Ensure it has .txt extension
        if (!mapName.endsWith(".txt")) {
            mapName += ".txt";
        }

        // Get width and height from sliders
        int width = 31; // Default value
        int height = 15; // Default value

        if (widthSlider != null) {
            width = (int) widthSlider.getValue();
        }

        if (heightSlider != null) {
            height = (int) heightSlider.getValue();
        }

        // Close the dialog
        dialogStage.close();

        try {
            // Start the game with the generated map using the selected level and dimensions
            BombermanGame game = new BombermanGame(level, width, height, mapName);
            game.createGameScene(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handler for the Cancel button
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
