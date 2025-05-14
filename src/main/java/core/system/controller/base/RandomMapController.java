package core.system.controller.base;

import core.util.Util;
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

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void initialize() {
        if (levelComboBox != null) {
            levelComboBox.getItems().addAll(1, 2, 3, 4, 5);
            levelComboBox.setValue(1);
        }

        if (nameField != null) {
            String timestamp = String.valueOf(System.currentTimeMillis()).substring(8);
            nameField.setText("RandomMap_" + timestamp);
        }

        if (widthSlider != null && widthValueLabel != null) {
            widthSlider.valueProperty().addListener((_, _, newVal) -> {
                int value = newVal.intValue();
                widthValueLabel.setText(String.valueOf(value));
            });
        }

        if (heightSlider != null && heightValueLabel != null) {
            heightSlider.valueProperty().addListener((_, _, newVal) -> {
                int value = newVal.intValue();
                heightValueLabel.setText(String.valueOf(value));
            });
        }
    }

    @FXML
    private void handleCreate() {
        int level = levelComboBox.getValue();

        String mapName = "RandomMap_" + System.currentTimeMillis();
        if (nameField != null && nameField.getText() != null && !nameField.getText().trim().isEmpty()) {
            mapName = nameField.getText();
        }

        if (!mapName.endsWith(".txt")) {
            mapName += ".txt";
        }

        int width = 15;
        int height = 15;

        if (widthSlider != null) {
            width = (int) widthSlider.getValue();
        }

        if (heightSlider != null) {
            height = (int) heightSlider.getValue();
        }

        dialogStage.close();

        try {
            Util.generateCustomMap(level, height, width, mapName);
            Util.sleep(2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
