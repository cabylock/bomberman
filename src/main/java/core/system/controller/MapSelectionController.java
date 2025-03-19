package core.system.controller;

import java.io.File;
import java.util.Optional;
import core.system.BombermanGame;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class MapSelectionController {
    @FXML
    private ListView<String> defaultMapList;
    @FXML
    private ListView<String> customMapList;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void loadMaps() {

        defaultMapList.getItems().clear();
        customMapList.getItems().clear();
        // Load default maps
        File defaultMapsDir = new File("src/main/resources/default_levels");
        if (defaultMapsDir.exists() && defaultMapsDir.isDirectory()) {
            File[] mapFiles = defaultMapsDir.listFiles((dir, name) -> name.endsWith(".txt"));
            if (mapFiles != null) {
                for (File mapFile : mapFiles) {
                    defaultMapList.getItems().add(mapFile.getName().replace(".txt", ""));
                }
            }
        }

        // Load custom maps
        File customMapsDir = new File("src/main/resources/custom_levels");
        if (customMapsDir.exists() && customMapsDir.isDirectory()) {
            File[] mapFiles = customMapsDir.listFiles((dir, name) -> name.endsWith(".txt"));
            if (mapFiles != null) {
                for (File mapFile : mapFiles) {
                    customMapList.getItems().add(mapFile.getName().replace(".txt", ""));
                }
            }
        }

        // Set up selection listeners
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
    }

    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (event.getSource() == defaultMapList) {
                String selectedMap = defaultMapList.getSelectionModel().getSelectedItem();
                if (selectedMap != null) {
                    startGameWithMap(selectedMap, MainController.DEFAULT);
                }
            } else if (event.getSource() == customMapList) {
                String selectedMap = customMapList.getSelectionModel().getSelectedItem();
                if (selectedMap != null) {
                    startGameWithMap(selectedMap, MainController.CUSTOM);
                }
            }
        }
    }

    @FXML
    private void playSelectedMap() {
        String selectedDefaultMap = defaultMapList.getSelectionModel().getSelectedItem();
        String selectedCustomMap = customMapList.getSelectionModel().getSelectedItem();

        if (selectedDefaultMap != null) {
            startGameWithMap(selectedDefaultMap, MainController.DEFAULT);
        } else if (selectedCustomMap != null) {
            startGameWithMap(selectedCustomMap, MainController.CUSTOM);
        } else {
            showAlert("No Map Selected", "Please select a map to play.", null);
        }
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/system/fxml/Mode.fxml"));
            Parent root = loader.load();

            ModeController controller = loader.getController();
            controller.setStage(stage);

            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteSelectedMap() {
        String selectedDefaultMap = defaultMapList.getSelectionModel().getSelectedItem();
        String selectedCustomMap = customMapList.getSelectionModel().getSelectedItem();

        if (selectedDefaultMap != null) {
            showAlert("Cannot Delete Default Map",
                    "Default maps cannot be deleted.",
                    "Please select a custom map if you wish to delete it.");
        } else if (selectedCustomMap != null) {
            boolean confirmed = showConfirmation("Delete Map",
                    "Are you sure you want to delete " + selectedCustomMap + "?",
                    "This action cannot be undone.");
            if (confirmed) {
                deleteMap(selectedCustomMap, MainController.CUSTOM);
                loadMaps(); // Reload maps to refresh the list
            }
        } else {
            showAlert("No Map Selected", "Please select a map to delete.", null);
        }
    }

    private void startGameWithMap(String mapName, int mapType) {
        BombermanGame game = new BombermanGame(mapName+".txt", mapType);
        game.createGameScene(stage);
    }

    private void deleteMap(String mapName, int mapType) {
        try {
            String dirPath = mapType == MainController.DEFAULT ? "src/main/resources/default_levels"
                    : "src/main/resources/custom_levels";

            File mapFile = new File(dirPath, mapName);
            if (mapFile.exists()) {
                boolean deleted = mapFile.delete();
                if (!deleted) {
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
